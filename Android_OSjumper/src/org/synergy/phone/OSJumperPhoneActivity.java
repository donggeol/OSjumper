package org.synergy.phone;

import org.synergy.R;
import org.synergy.phone.service.IRemoteService;
import org.synergy.phone.service.IRemoteServiceCallback;
import org.synergy.phone.util.ECODE;
import org.synergy.phone.util.RCODE;
import org.synergy.phone.view.OSJumperPhoneView;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class OSJumperPhoneActivity extends Activity
{
	private ServiceConnection m_ServiceConnection;
	private IRemoteService m_Service = null;
	private IRemoteServiceCallback m_ServiceCallback;

	private OSJumperPhoneView mOSJumperPhoneView;
	
	private Button mStartBtn, mStopBtn;
	private boolean b_objButtonSelecter = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.mbiz_vtrs_layout);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
        init();
	}

	private void init()
	{
		initView();
		startServiceBinder();
	}
	
	private void initView()
	{
		ImageView mLeftVolume01 = (ImageView)findViewById(R.id.vtrs_volume_left_1);
		ImageView mLeftVolume02 = (ImageView)findViewById(R.id.vtrs_volume_left_2);
		ImageView mLeftVolume03 = (ImageView)findViewById(R.id.vtrs_volume_left_3);
		ImageView mLeftVolume04 = (ImageView)findViewById(R.id.vtrs_volume_left_4);
		ImageView mLeftVolume05 = (ImageView)findViewById(R.id.vtrs_volume_left_5);
		ImageView mLeftVolume06 = (ImageView)findViewById(R.id.vtrs_volume_left_6);
		ImageView mLeftVolume07 = (ImageView)findViewById(R.id.vtrs_volume_left_7);

		ImageView mRightVolume01 = (ImageView)findViewById(R.id.vtrs_volume_right_1);
		ImageView mRightVolume02 = (ImageView)findViewById(R.id.vtrs_volume_right_2);
		ImageView mRightVolume03 = (ImageView)findViewById(R.id.vtrs_volume_right_3);
		ImageView mRightVolume04 = (ImageView)findViewById(R.id.vtrs_volume_right_4);
		ImageView mRightVolume05 = (ImageView)findViewById(R.id.vtrs_volume_right_5);
		ImageView mRightVolume06 = (ImageView)findViewById(R.id.vtrs_volume_right_6);
		ImageView mRightVolume07 = (ImageView)findViewById(R.id.vtrs_volume_right_7);

		mStartBtn = (Button)findViewById(R.id.start_btn);
		mStartBtn.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				if(false == b_objButtonSelecter)
				{
					startCallService();
				}
			}
		});
		mStopBtn = (Button)findViewById(R.id.stop_btn);
		mStopBtn.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				if(true == b_objButtonSelecter)
				{
					stopCallService();
				}
			}
		});
		mOSJumperPhoneView = new OSJumperPhoneView(this);
		mOSJumperPhoneView.initVTTRLeftVolumeView(mLeftVolume01, mLeftVolume02, mLeftVolume03, mLeftVolume04, mLeftVolume05, mLeftVolume06, mLeftVolume07);
		mOSJumperPhoneView.initVTTRRightVolumeView(mRightVolume01, mRightVolume02, mRightVolume03, mRightVolume04, mRightVolume05, mRightVolume06, mRightVolume07);
	}
	
	private void startServiceBinder()
	{
		try
		{
			initServiceCallback();
			initServiceConnection();
			bindMBizRecognitionService();
		}
		catch (Exception e){e.printStackTrace();}
	}
	
	private void startCallService()
	{
		b_objButtonSelecter = true;
		mStartBtn.setTextColor(Color.parseColor("#FF0000"));
		mStopBtn.setTextColor(Color.parseColor("#FFFFFF"));
		mStartBtn.setEnabled(false);
		mStopBtn.setEnabled(true);
		handler.sendEmptyMessage(RCODE.RECOGNITION_SERVICE_START);
	}
	
	private void stopCallService()
	{
		try 
		{
			b_objButtonSelecter = false;
			mStartBtn.setTextColor(Color.parseColor("#FFFFFF"));
			mStopBtn.setTextColor(Color.parseColor("#FF0000"));
			mStartBtn.setEnabled(true);
			mStopBtn.setEnabled(false);
			handler.sendEmptyMessage(RCODE.RECOGNITION_SERVICE_STOP);
			unbindMBizRecognitionService();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		this.finish();
	}
	
	/**
	 * 콜백메소드 초기화
	 */
	private void initServiceCallback ()
	{
		m_ServiceCallback = new IRemoteServiceCallback.Stub()
		{
			@Override
			public void onRemoteServiceCallback(int what, Bundle objBundle) throws RemoteException 
			{
				switch (what)
				{
					case ECODE.RECOGNITION_NETWORK_ERROR:
					{
						Toast.makeText(OSJumperPhoneActivity.this, "네트워크 연결에 실패하였습니다.", Toast.LENGTH_SHORT).show();
					}
					break;
					case RCODE.RECOGNITION_SERVICE_SPEECH: 
					{
						Message mSpeechMessage = new Message();
						mSpeechMessage.setData(objBundle);
						mSpeechMessage.what = RCODE.RECOGNITION_SERVICE_SPEECH;
						handler.sendMessage(mSpeechMessage);
					}
					break;
					default: break;
				}
			}
		};
	}
	
	/**
	 * 서비스 연결 초기화
	 */
	private void initServiceConnection ()
	{
		m_ServiceConnection = new ServiceConnection()
		{
			@Override
			public void onServiceConnected (ComponentName name, IBinder service)
			{
				m_Service = IRemoteService.Stub.asInterface(service);
				try
				{
					m_Service.registerCallback(m_ServiceCallback);
				}
				catch (RemoteException re)
				{
					re.printStackTrace();
				}
			}

			@Override
			public void onServiceDisconnected (ComponentName name)
			{
				m_Service = null;
			}
		};
	}

	/**
	 * 서비스 연결
	 * @throws Exception
	 */
	private void bindMBizRecognitionService () throws Exception
	{
		Intent remoteService = new Intent(IRemoteService.class.getName());
		bindService(remoteService, m_ServiceConnection, Context.BIND_AUTO_CREATE);
	}
	
	/**
	 * 서비스 해제
	 * @throws Exception
	 */
	private void unbindMBizRecognitionService () throws Exception
	{
		if (m_ServiceConnection != null)
		{
			unbindService(m_ServiceConnection);
		}
	}
	
	/**
	 * 이벤트에 따른 핸들러 초기화
	 */
	private Handler handler = new Handler(new Handler.Callback() 
	{
		@Override
		public boolean handleMessage(Message msg) 
		{
			switch (msg.what) 
			{
				case RCODE.RECOGNITION_SERVICE_SPEECH:
				{
					int nStep = msg.getData().getInt("SERVICE_SPEECH"); // 음성의 데시벨 정보를 1~7단계로 구분하여 전달 받는다.
					mOSJumperPhoneView.setVolumeImg(nStep);
				}
				break;
				case RCODE.RECOGNITION_SERVICE_STOP:
				{
					try 
					{
						m_Service.onRemoteService(msg.what, null); // 음성인신 서비스에게 음성입력 중단 명령을 전달한다.
					}
					catch (RemoteException e)
					{
						e.printStackTrace();
					}
				}
				break;
				case RCODE.RECOGNITION_SERVICE_START:
				{
					try
					{
						Intent intent = getIntent();
						Bundle objBundle = new Bundle();
						objBundle.putString("OSJUMPER_RECEIVER_IP", intent.getStringExtra("OSJUMPER_RECEIVER_IP"));
						m_Service.onRemoteService(msg.what, objBundle); // 음성인신 서비스에게 음성입력을 시작하라는 명령을 전달한다.
					}
					catch (RemoteException e)
					{
						e.printStackTrace();
					}
				}
				break;
			}
			return false;
		}
	});
}
