package org.synergy.mouse;

import org.synergy.R;
import org.synergy.phone.ptop.OSJumperPeerToPeerServer;
import org.synergy.phone.util.PCODE;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;

public class AlwaysOnTopService extends Service
{
	private ServiceBinder m_objBinder = null;
	// WindowManager
	private WindowManager mWindowManager;
	
	// AlwaysOnTop Views
	private ImageView mPhoneView; // Phone Icon
	private ImageView mMouseView; // Mouse Pointer
	
	// Parameters of Views
	private WindowManager.LayoutParams mPhoneParams; // Layout Parameter of Phone Icon
	private WindowManager.LayoutParams mMouseParams; // Layout Parameter of Mouse Pointer
	
	// Settings Variables
	public static SharedPreferences settings;
	int pointer_handed;										// Values RIGHT_HANDED(0), LEFT_HANDED(1) can be used for this
	int pointer_sensitivity;
	int pointer_img;

	// Variables_About Click Event
	private GestureDetector mGesDetector;	
	private SimpleOnGestureListener mGestureListener = new SimpleOnGestureListener() {};
	
	// Realtime Settings Switcher
	// used for "int settingToBeChanged" : realtimeSetting(int settingToBeChnaged)  
	private final int RELOAD				= 0;
	private final int POINTER_SENSITIVITY	= 3;
	private final int POINTER_IMAGE			= 4;

	private final int RIGHT_HANDED	= 0;
	
	private EventManager m_objEventManager;
	
	private OSJumperPeerToPeerServer m_objOSJumperPeerToPeerServer;
	private Thread m_objPeerToPeerServerThread;
	private boolean m_bImageChecker;
	private boolean m_bPhoneAlameStartChecker;
	
	private int m_nUpX, m_nUpY;
	private int m_nDownX, m_nDownY; 
	
	@Override
	public void onCreate() 
	{
		super.onCreate();
		
		// Load Previous Saved Settings
		settings = getSharedPreferences("settings", 0);

		// Create Objects
		mGesDetector = new GestureDetector(this, mGestureListener);
		
		mPhoneView = new ImageView(this);
		mPhoneView.setImageResource(R.drawable.phone_on);
		mPhoneView.setOnTouchListener(mViewTouchListener);
		mPhoneView.setVisibility(View.INVISIBLE);
		
		mMouseView = new ImageView(this);
		mMouseView.setImageResource(pointer_img);
		mMouseView.setVisibility(View.INVISIBLE);
		
		// Setting Touchpad Parameters
		mPhoneParams = new WindowManager.LayoutParams(
			WindowManager.LayoutParams.WRAP_CONTENT,
			WindowManager.LayoutParams.WRAP_CONTENT,
			WindowManager.LayoutParams.TYPE_PHONE,
			WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
			PixelFormat.TRANSLUCENT);
		mPhoneParams.windowAnimations = android.R.style.Animation_Translucent;
		mPhoneParams.gravity = Gravity.RIGHT | Gravity.TOP;
		mPhoneParams.x += 20;
		mPhoneParams.y += 20;
				
		// Setting Mousepointer Parameters
		mMouseParams = new WindowManager.LayoutParams(
			WindowManager.LayoutParams.WRAP_CONTENT,
			WindowManager.LayoutParams.WRAP_CONTENT,
			WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
			WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
			PixelFormat.TRANSLUCENT);
		mMouseParams.gravity = Gravity.LEFT | Gravity.TOP;
			
		// Load Window Manager
		mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

		// Add Views to WindowManager
		mWindowManager.addView(mPhoneView, mPhoneParams);
		mWindowManager.addView(mMouseView, mMouseParams);
		
		// reload
		realtimeSetting(RELOAD);
		m_objEventManager = new EventManager();
	}
		
	private Handler mHandler = new Handler(new Handler.Callback() 
	{
		@Override
		public boolean handleMessage(Message msg) 
		{
			switch (msg.what)
			{	
				case TYPECODE.MOUSE_POINT :
				{
					mMouseParams.x = (Integer)msg.arg1;
					mMouseParams.y = (Integer)msg.arg2;
		        	mWindowManager.updateViewLayout(mMouseView, mMouseParams);
		        	
		        	m_objEventManager.setMouseDown(false);
				}
		    	break;
				case TYPECODE.KEYBOARD_ID :
				{
					int nButtonType = (Integer)msg.arg1;
					m_objEventManager.onKeyBoard(nButtonType+"");
				}
		    	break;
				case TYPECODE.CLIPBOARD_MESSAGE :
				{
					Bundle objBundle = msg.getData();
					String strClipBoardMessage = objBundle.getString("CLIPBOARD");
					m_objEventManager.setClipBoardMessage(strClipBoardMessage);
				}
		    	break;
				case TYPECODE.MOUSE_DOWN : 
				{
					m_nDownX = mMouseParams.x;
					m_nDownY = mMouseParams.y;
					
		        	m_objEventManager.setMouseDown(true);
				}
				break;
				case TYPECODE.MOUSE_UP : 
				{
					m_nUpX = mMouseParams.x; 
					m_nUpY = mMouseParams.y;
					
					if(true == m_objEventManager.isMouseDown()) 
						m_objEventManager.onSingleTap(mMouseParams.x, mMouseParams.y);
					else
						m_objEventManager.onSwipScreen(m_nDownX, m_nDownY, m_nUpX, m_nUpY);
					
					m_objEventManager.setMouseDown(false); 
				}
				break;
				case TYPECODE.MOUSE_ENTER : mMouseView.setVisibility(View.VISIBLE); break;
				case TYPECODE.MOUSE_LEAVE : mMouseView.setVisibility(View.INVISIBLE); break;
				case PCODE.RECOGNITION_VOICE_CALL_ON : 
				{
					Bundle objBundle = msg.getData();
					String strIP = objBundle.getString("OSJUMPER_IP");
					startOnPeerToPeerServer(strIP); 
				}
				break;
				case PCODE.RECOGNITION_VOICE_CALL_OFF : stopOnPeerToPeerServer(); break;
				case PCODE.RECOGNITION_REQUEST_CALL : 
				{
					m_objOSJumperPeerToPeerServer.sendVoiceMessage("START");
					m_bPhoneAlameStartChecker = true; 
					mHandler.sendEmptyMessage(PCODE.RECOGNITION_REQUEST_ALARM);
				}
				break;
				case PCODE.RECOGNITION_CALL_START : m_objOSJumperPeerToPeerServer.setRunOnVoice(true); break;
				case PCODE.RECOGNITION_REQUEST_ALARM : alarmPhoneImage(); break;
				case PCODE.RECOGNITION_CALL_START_FROM_SERVER : callStart(); break;
				case PCODE.RECOGNITION_CALL_END : m_objOSJumperPeerToPeerServer.setRunOnVoice(false); setPhoneImage(); break;
				case PCODE.RECOGNITION_CALL_REJECT_FROM_SERVER : 
				{
					setPhoneImage();
					m_objOSJumperPeerToPeerServer.setRunOnVoice(false); 
					m_objOSJumperPeerToPeerServer.sendVoiceToMobileClient(PCODE.RECOGNITION_REJECT_CALL_S);
				}
				break;
			}
			return false;
		}
	});
	
	/**
	 * Touch Event - Use To Phone Icon
	 */
	private OnTouchListener mViewTouchListener = new OnTouchListener() 
	{
		@Override
		public boolean onTouch(View v, MotionEvent event) 
		{
			if (mGesDetector.onTouchEvent(event)) 
			{
				return true;
			}
			else 
			{
				switch (event.getAction()) 
				{
					case MotionEvent.ACTION_DOWN: callStart(); break;
				}
			}
			return true;
		}
	};
		
	@Override
	public IBinder onBind (Intent intent)
	{
		if (m_objBinder == null)
		{
			m_objBinder = new ServiceBinder(mHandler);
		}
		return m_objBinder;
	}
	
	/**
	 * Apply Setting Variable
	 */
	public void realtimeSetting(int settingToBeChnaged) {

		// Load values of preferences
		pointer_handed		= settings.getInt("pointer_handed", RIGHT_HANDED);
		pointer_sensitivity	= settings.getInt("pointer_sensitivity", 50);
		pointer_img			= settings.getInt("pointer_img", R.drawable.pointer_basic);
		
		switch(settingToBeChnaged) {
		case RELOAD:
			mMouseView.setImageResource(pointer_img);
			mWindowManager.updateViewLayout(mMouseView, mMouseParams);
			break;
		case POINTER_SENSITIVITY:		// Set pointer sensitivity
			break;
		case POINTER_IMAGE:				// Set pointer image
			mMouseView.setImageResource(pointer_img);
			mWindowManager.updateViewLayout(mMouseView, mMouseParams);
			break;
		default:
			break;
		}
		mMouseView.setVisibility(View.INVISIBLE);
	}
	
	@Override
	public void onDestroy() {
		if(mWindowManager != null) {		//서비스 종료시 뷰 제거. *중요 : 뷰를 꼭 제거 해야함.
			if(mMouseView != null) mWindowManager.removeView(mMouseView);
		}
		super.onDestroy();
	}

	
	/**
	 * In case, Rotate a Device
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) 
	{
		super.onConfigurationChanged(newConfig);
		
		// Resetting Mousepointer Position
		int nTemp = mMouseParams.x;
		mMouseParams.x = mMouseParams.y;
		mMouseParams.y = nTemp;
		
		// View Update
		mWindowManager.updateViewLayout(mMouseView, mMouseParams);
	}
	
	// Binder
	public class LocalBinder extends Binder {
		AlwaysOnTopService getService() {
			return AlwaysOnTopService.this;
		}
	}
	
	private void startOnPeerToPeerServer(String strIP)
	{
		mPhoneView.setVisibility(View.VISIBLE);
		
		m_objOSJumperPeerToPeerServer = new OSJumperPeerToPeerServer(mHandler, strIP);
		m_objPeerToPeerServerThread = new Thread(m_objOSJumperPeerToPeerServer);
		m_objPeerToPeerServerThread.start();
		
		m_objOSJumperPeerToPeerServer.sendVoiceMessage("START");
	}
	
	private void stopOnPeerToPeerServer()
	{
		mPhoneView.setImageResource(R.drawable.phone_on);
		mPhoneView.setVisibility(View.INVISIBLE);
		m_bPhoneAlameStartChecker = false;
		m_objOSJumperPeerToPeerServer.closeSocket();
	}
	
	private void alarmPhoneImage()
	{
		m_bImageChecker = !m_bImageChecker;
		
		if(true == m_bImageChecker)
			mPhoneView.setImageResource(R.drawable.phone_call);
		else
			mPhoneView.setImageResource(R.drawable.phone_on);
		
		mWindowManager.updateViewLayout(mPhoneView, mPhoneParams);
		
		if(true == m_bPhoneAlameStartChecker)
			mHandler.sendEmptyMessageDelayed(PCODE.RECOGNITION_REQUEST_ALARM, 500);
	}
	
	private void setPhoneImage()
	{
		mPhoneView.setImageResource(R.drawable.phone_on);
		mWindowManager.updateViewLayout(mPhoneView, mPhoneParams);
	}
	
	private void callStart()
	{
		if(true == m_bPhoneAlameStartChecker)
		{
			m_bPhoneAlameStartChecker = false;
			m_objOSJumperPeerToPeerServer.sendVoiceMessage(PCODE.RECOGNITION_RECEIVE_CALL_S);
			mHandler.sendEmptyMessage(PCODE.RECOGNITION_CALL_START);
		}
	}
}