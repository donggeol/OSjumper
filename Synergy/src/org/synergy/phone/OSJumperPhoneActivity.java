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
	 * �ݹ�޼ҵ� �ʱ�ȭ
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
						Toast.makeText(OSJumperPhoneActivity.this, "��Ʈ��ũ ���ῡ �����Ͽ����ϴ�.", Toast.LENGTH_SHORT).show();
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
	 * ���� ���� �ʱ�ȭ
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
	 * ���� ����
	 * @throws Exception
	 */
	private void bindMBizRecognitionService () throws Exception
	{
		Intent remoteService = new Intent(IRemoteService.class.getName());
		bindService(remoteService, m_ServiceConnection, Context.BIND_AUTO_CREATE);
	}
	
	/**
	 * ���� ����
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
	 * �̺�Ʈ�� ���� �ڵ鷯 �ʱ�ȭ
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
					int nStep = msg.getData().getInt("SERVICE_SPEECH"); // ������ ���ú� ������ 1~7�ܰ�� �����Ͽ� ���� �޴´�.
					mOSJumperPhoneView.setVolumeImg(nStep);
				}
				break;
				case RCODE.RECOGNITION_SERVICE_STOP:
				{
					try 
					{
						m_Service.onRemoteService(msg.what, null); // �����ν� ���񽺿��� �����Է� �ߴ� ����� �����Ѵ�.
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
						m_Service.onRemoteService(msg.what, objBundle); // �����ν� ���񽺿��� �����Է��� �����϶�� ����� �����Ѵ�.
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
