package org.synergy.phone.service;

import java.io.IOException;

import org.synergy.phone.control.ReadRunnable;
import org.synergy.phone.socket.OSJumperSocketClient;
import org.synergy.phone.util.ECODE;
import org.synergy.phone.util.PCODE;
import org.synergy.phone.util.RCODE;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import com.google.android.voicesearch.endpointer.EndpointerInputStream;
import com.google.android.voicesearch.endpointer.EndpointerInputStream.Listener;

public class MBizRecognitionService extends Service
{
	private Context m_objContext;
	
	private ServiceBinder m_objBinder = null; // 서비스를 호출한 앱과의 바인딩 클래스
	private ServiceManager m_objServiceManager = null; // 서비스 동작과 관련된 기능처리를 담당하는 클래스

	private ReadRunnable readRunnable; // 실제 연속적으로 동작되는 음성 스트림 정보를 관리하는 클래스
	private OSJumperSocketClient m_objOSJumperSocketClient;
	
	private Thread m_objOSJumperSocketClientThread;
	private String m_strIP;
	
	/**
	 * 서비스와 관련된 동작을 처리하는 핸들러 변수 생성
	 */
	private Handler mHandler = new Handler(new Handler.Callback() 
	{
		@Override
		public boolean handleMessage(Message msg) 
		{
			switch (msg.what) 
			{
				case ECODE.RECOGNITION_NETWORK_ERROR:
				case ECODE.RECOGNITION_MIC_ERROR:   
				case ECODE.RECOGNITION_GOOGLE_SERVER_ERROR: 
				case ECODE.RECOGNITION_SERVICE_ERROR:   
				case ECODE.RECOGNITION_NOSEARCH_ERROR: 
				{
					m_objServiceManager.sendCallBackFilter(null, msg.what, m_objBinder);
				}
				break;
				case RCODE.RECOGNITION_SERVICE_SPEECH:
				{
					int nStep = (Integer)msg.obj;
					Bundle objBundle = new Bundle();
					objBundle.putInt("SERVICE_SPEECH", nStep); // 음성의 데시벨 정보를 1~7단계로 구분하여 값을 전달한다.
					m_objServiceManager.sendCallBackFilter(objBundle, RCODE.RECOGNITION_SERVICE_SPEECH, m_objBinder);
				}
				break;
				case RCODE.RECOGNITION_SERVICE_START : 
				{
					Bundle objBundle = msg.getData();
					m_strIP = objBundle.getString("OSJUMPER_RECEIVER_IP");
					start(m_strIP);
				}
				break;
				case RCODE.RECOGNITION_SERVICE_STOP : stop(); break;
				case RCODE.RECOGNITION_SERVICE_REJECT : 
					Toast.makeText(m_objContext, "Reject Call From "+m_strIP, Toast.LENGTH_SHORT).show();
				break;
			}
			return false;
		}
	});
	
	@Override
	public void onCreate ()
	{
		super.onCreate();
		this.m_objContext = this;
		m_objServiceManager = new ServiceManager(this, mHandler);
	}
	
	@Override
	public IBinder onBind (Intent intent)
	{
		if (m_objBinder == null)
		{
			m_objBinder = new ServiceBinder(mHandler);
		}
		return m_objBinder;
	}
	
	@Override
	public void onDestroy ()
	{
		super.onDestroy();
		
		if(null != readRunnable)
		{
			readRunnable.stopRun();
			readRunnable.stopRecord();
		}
	}
	
	private void start(String strIP)
	{
		try
		{
			m_objOSJumperSocketClient = new OSJumperSocketClient(strIP, mHandler);
			m_objOSJumperSocketClientThread = new Thread(m_objOSJumperSocketClient);
			m_objOSJumperSocketClientThread.start();
			
			try
			{
				readRunnable = new ReadRunnable(1000000, listener);
				readRunnable.startRecord();
				new Thread(readRunnable).start();
				m_objOSJumperSocketClient.onVoiceStart();
			}
			catch (IOException e)
			{
				mHandler.sendEmptyMessage(ECODE.RECOGNITION_MIC_ERROR);
			}
		}
		catch (Exception e)
		{
			mHandler.sendEmptyMessage(ECODE.RECOGNITION_SERVICE_ERROR);
		}
	}

	private void stop()
	{
		try
		{
			readRunnable.stopRecord();
			m_objOSJumperSocketClient.sendVoiceMessage(PCODE.RECOGNITION_CALL_END_S);
			m_objOSJumperSocketClient.closeSocket();
			this.onDestroy();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	//private int nBuffCount = 0;
	//private byte[] mBuf = new byte[2400];
	private final EndpointerInputStream.Listener listener = new Listener() 
	{
		@Override
		public void onRmsChanged(float rmsdB) // 초기 데시벨 0을 기준으로 데시벨이 변경 될 때마다 호출 되는 리스너로 유효 음성을 체크하기 위해 사용된다.
		{
			Message mMessage = new Message();
			mMessage.obj = (int)(rmsdB/7); // 데시벨을 경우 일반적으로 0~70까지 검색되므로 단계를 총 7단계로 나누어 UI에 전달한다.
			mMessage.what = RCODE.RECOGNITION_SERVICE_SPEECH;
			mHandler.sendMessage(mMessage);
		}
		
		@Override 
		public void onEndOfSpeech() // 사일런트 타임을 기준으로 구분되어진 음성정보의 입력이 완료 되었을 경우 호출 되는 리스너로 음성을 구글서버로 보내는 동작을 처리한다.
		{}
		
		@Override
		public void onBufferReceived(byte[] buffer) // 음성입력으로 생성된 버퍼값이 입력될때 호출 되는 리스너로 음성정보를 byte 배열에 등록하기 위해 사용된다. 
		{
			m_objOSJumperSocketClient.sendVoiceMessageStart(buffer);
		}
		
		@Override public void onReadyForSpeech(float paramFloat1, float paramFloat2) {}
		@Override public void onBeginningOfSpeech(){}
	};
}
