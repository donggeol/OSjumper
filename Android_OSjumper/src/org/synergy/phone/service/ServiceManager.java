package org.synergy.phone.service;

import java.util.List;

import org.synergy.phone.util.ECODE;
import org.synergy.phone.util.RCODE;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;

public class ServiceManager 
{
	public static final String FILE_NAME = "/logsound.pcm";
	
	private Handler mHandler;
	private Context mContext;
	
	public ServiceManager(Context mContext, Handler mHandler)
	{
		this.mContext = mContext;
		this.mHandler = mHandler;
	}
	
	/**
	 * 음성인식이 종료되는 EndPoint에 따른 처리를 위한 메소드
	 * @param 음성 유효성 여부, 입력된 음성정보 리스트
	 */
	public void onEndOfSpeech(boolean bValidVoice, List<byte[]> mSound)
	{
		if(true == bValidVoice)
		{
			openThreadEndOfSpeech(mSound);
		}
		else
		{
			mHandler.sendEmptyMessage(ECODE.RECOGNITION_SERVICE_ERROR);
		}
	}
	
	/**
	 * 음성파일 생성 및 구글서버와의 커넥트 동장은 시간이 오래 걸리는 동작으로 요청이 들어올 경우 다중 쓰레드를 이용하여 병렬적으로 동작시킴
	 * @param mSound
	 */
	protected void openThreadEndOfSpeech (final List<byte[]> mSound)
	{
		Thread thread = new Thread()
		{
			@Override
			public void run ()
			{
				try 
				{
					if(false == "TEST".contains("TEST"))
					{
						mHandler.sendEmptyMessage(RCODE.RECOGNITION_SERVICE_WORD);
					}
					else
					{
						mHandler.sendEmptyMessage(ECODE.RECOGNITION_NOSEARCH_ERROR);
					}
				} 
				catch (Exception e)
				{
					mHandler.sendEmptyMessage(ECODE.RECOGNITION_NOSEARCH_ERROR);
				}
			}
		};
		thread.start();
	}
	
	/**
	 * 음성인식으로 부터 입력된 byte 정보를 정장하는 메소드
	 * @param data, read, mSound
	 */
	public void copySound(byte[] data, int read, List<byte[]> mSound) 
	{
		if (read < 0) 
		{
			return;
		}
		byte[] cache = new byte[read];
		System.arraycopy(data, 0, cache, 0, read);
		mSound.add(cache);
	}
	
	/**
	 * 인자값에 따른 콜백 메소드 호출을 구분하는 필터 메소드
	 * @param 번들값, 요청코드, 서비스 연결 바인더
	 */
	public void sendCallBackFilter (Bundle objBundle, int nCode, ServiceBinder mBinder)
	{
		if(null == objBundle)
		{
			sendCallBack(nCode, mBinder);
		}
		else
		{
			sendCallBack(objBundle, nCode, mBinder);
		}
	}
	
	/**
	 * 전달해줄 번들값이 존재하는 경우 실행되는 콜백 메소드
	 * @param 번들값, 요청코드, 서비스 연결 바인더
	 */
	private void sendCallBack (Bundle objBundle, int nCode, ServiceBinder mBinder)
	{
		int N = mBinder.getServiceCallback().beginBroadcast();
		for(int i = 0; i < N; i++)
		{
			try 
			{
				mBinder.getServiceCallback().getBroadcastItem(i).onRemoteServiceCallback(nCode, objBundle);
			} 
			catch (RemoteException e) 
			{
				e.printStackTrace();
			}
		}
		mBinder.getServiceCallback().finishBroadcast();
	}
	
	/**
	 * 전달해줄 번들값이 존재하지 않는 경우 실행되는 콜백 메소드
	 * @param 요청코드, 서비스 연결 바인더
	 */
	private void sendCallBack (int nCode, ServiceBinder mBinder)
	{
		int N = mBinder.getServiceCallback().beginBroadcast();
		for(int i = 0; i < N; i++)
		{
			try 
			{
				mBinder.getServiceCallback().getBroadcastItem(i).onRemoteServiceCallback(nCode, null);
			} 
			catch (RemoteException e) 
			{
				e.printStackTrace();
			}
		}
		mBinder.getServiceCallback().finishBroadcast();
	}
}
