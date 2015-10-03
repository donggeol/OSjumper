package org.synergy.phone.service;

import org.synergy.phone.util.RCODE;

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
	public String onRemoteService(int what, Bundle objBundle) throws RemoteException 
	{
		switch (what)
		{
			case RCODE.RECOGNITION_SERVICE_START : 
			{
				Message objMessage = new Message();
				objMessage.setData(objBundle);
				objMessage.what = what;
				mHandler.sendMessage(objMessage); 
			}
			break;
			case RCODE.RECOGNITION_SERVICE_STOP : 
			{
				mHandler.sendEmptyMessage(what); 
			}
			break;
		}
		return null;
	}
}
