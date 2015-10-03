package org.synergy.mouse;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

public class ServiceBinder extends IRemoteService.Stub
{
	private RemoteCallbackList<IRemoteServiceCallback> m_Callbacks = new RemoteCallbackList<IRemoteServiceCallback>();
	private Handler mHandler;
	
	public ServiceBinder(Handler mHandler)
	{
		this.mHandler = mHandler;
	}
	
	public RemoteCallbackList<IRemoteServiceCallback> getServiceCallback ()
	{
		return m_Callbacks;
	}
	
	@Override
	public void registerCallback (IRemoteServiceCallback serviceCallback) throws RemoteException
	{
		if (serviceCallback != null)
		{
			m_Callbacks.register(serviceCallback);
		}
	}
	
	@Override
	public void unregisterCallback (IRemoteServiceCallback serviceCallback) throws RemoteException
	{
		if (serviceCallback != null)
		{
			m_Callbacks.unregister(serviceCallback);
		}
	}

	@Override
	public String onRemoteService(int type, int x, int y, Bundle objBundle) throws RemoteException 
	{
		Message mMessage = new Message();
		
		if(null != objBundle) 
			mMessage.setData(objBundle);
		
		mMessage.arg1 = x;
		mMessage.arg2 = y;
		mMessage.what = type;
		mHandler.sendMessage(mMessage);
		return null;
	}

	@Override
	public String onRemoteClipBoardService(int type, Bundle objBundle) throws RemoteException 
	{
		Message mMessage = new Message();
		mMessage.setData(objBundle);
		mMessage.what = type;
		mHandler.sendMessage(mMessage);
		return null;
	}

	@Override
	public String onRemoteKeyBoardService(int type, int buttonID, Bundle objBundle) throws RemoteException 
	{
		Message mMessage = new Message();
		mMessage.arg1 = buttonID;
		mMessage.what = type;
		mHandler.sendMessage(mMessage);
		return null;
	}
}
