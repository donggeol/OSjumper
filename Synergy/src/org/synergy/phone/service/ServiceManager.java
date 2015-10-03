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
	 * �����ν��� ����Ǵ� EndPoint�� ���� ó���� ���� �޼ҵ�
	 * @param ���� ��ȿ�� ����, �Էµ� �������� ����Ʈ
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
	 * �������� ���� �� ���ۼ������� Ŀ��Ʈ ������ �ð��� ���� �ɸ��� �������� ��û�� ���� ��� ���� �����带 �̿��Ͽ� ���������� ���۽�Ŵ
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
	 * �����ν����� ���� �Էµ� byte ������ �����ϴ� �޼ҵ�
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
	 * ���ڰ��� ���� �ݹ� �޼ҵ� ȣ���� �����ϴ� ���� �޼ҵ�
	 * @param ���鰪, ��û�ڵ�, ���� ���� ���δ�
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
	 * �������� ���鰪�� �����ϴ� ��� ����Ǵ� �ݹ� �޼ҵ�
	 * @param ���鰪, ��û�ڵ�, ���� ���� ���δ�
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
	 * �������� ���鰪�� �������� �ʴ� ��� ����Ǵ� �ݹ� �޼ҵ�
	 * @param ��û�ڵ�, ���� ���� ���δ�
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
