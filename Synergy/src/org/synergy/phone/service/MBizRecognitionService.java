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
	
	private ServiceBinder m_objBinder = null; // ���񽺸� ȣ���� �۰��� ���ε� Ŭ����
	private ServiceManager m_objServiceManager = null; // ���� ���۰� ���õ� ���ó���� ����ϴ� Ŭ����

	private ReadRunnable readRunnable; // ���� ���������� ���۵Ǵ� ���� ��Ʈ�� ������ �����ϴ� Ŭ����
	private OSJumperSocketClient m_objOSJumperSocketClient;
	
	private Thread m_objOSJumperSocketClientThread;
	private String m_strIP;
	
	/**
	 * ���񽺿� ���õ� ������ ó���ϴ� �ڵ鷯 ���� ����
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
					objBundle.putInt("SERVICE_SPEECH", nStep); // ������ ���ú� ������ 1~7�ܰ�� �����Ͽ� ���� �����Ѵ�.
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
		public void onRmsChanged(float rmsdB) // �ʱ� ���ú� 0�� �������� ���ú��� ���� �� ������ ȣ�� �Ǵ� �����ʷ� ��ȿ ������ üũ�ϱ� ���� ���ȴ�.
		{
			Message mMessage = new Message();
			mMessage.obj = (int)(rmsdB/7); // ���ú��� ��� �Ϲ������� 0~70���� �˻��ǹǷ� �ܰ踦 �� 7�ܰ�� ������ UI�� �����Ѵ�.
			mMessage.what = RCODE.RECOGNITION_SERVICE_SPEECH;
			mHandler.sendMessage(mMessage);
		}
		
		@Override 
		public void onEndOfSpeech() // ���Ϸ�Ʈ Ÿ���� �������� ���еǾ��� ���������� �Է��� �Ϸ� �Ǿ��� ��� ȣ�� �Ǵ� �����ʷ� ������ ���ۼ����� ������ ������ ó���Ѵ�.
		{}
		
		@Override
		public void onBufferReceived(byte[] buffer) // �����Է����� ������ ���۰��� �Էµɶ� ȣ�� �Ǵ� �����ʷ� ���������� byte �迭�� ����ϱ� ���� ���ȴ�. 
		{
			m_objOSJumperSocketClient.sendVoiceMessageStart(buffer);
		}
		
		@Override public void onReadyForSpeech(float paramFloat1, float paramFloat2) {}
		@Override public void onBeginningOfSpeech(){}
	};
}
