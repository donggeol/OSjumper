package org.synergy.mouse;

import org.synergy.base.Event;
import org.synergy.base.EventQueue;
import org.synergy.base.EventType;
import org.synergy.base.Log;
import org.synergy.client.Client;
import org.synergy.common.screens.BasicScreen;
import org.synergy.injection.Injection;
import org.synergy.mouse.util.PhoneManager;
import org.synergy.net.NetworkAddress;
import org.synergy.net.SocketFactoryInterface;
import org.synergy.net.TCPSocketFactory;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.Display;
import android.view.WindowManager;

public class ConnectManager 
{
	private Context m_objContext;
	private Thread mainLoopThread = null;
	private Client m_objClient;
	private BasicScreen m_objBasicScreen;

	private ServiceConnection m_ServiceConnection;
	private IRemoteService m_objService = null;
	private IRemoteServiceCallback m_ServiceCallback;
	
	public IRemoteService getIRemoteService()
	{
		return m_objService;
	}
	
	private class MainLoopThread extends Thread 
	{
		public void run() 
		{
			try 
			{
				Event event = new Event();
				event = EventQueue.getInstance().getEvent(event, -1.0);
				Log.note("Event grabbed");
				while (event.getType() != EventType.QUIT && mainLoopThread == Thread.currentThread()) 
				{
					EventQueue.getInstance().dispatchEvent(event);
					event = EventQueue.getInstance().getEvent(event, -1.0);
					Log.note("Event grabbed");
				}
				mainLoopThread = null;
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			} 
			finally 
			{
				Injection.stop();
			}
		}
	}
	
	public ConnectManager(Context objContext)
	{
		this.m_objContext = objContext;
		init();
	}
	
	private void init()
	{
		initServiceConnection ();
		initService();
	}

	public void connect (String strClientName, String strIpAddress, String strPortStr) 
    {
    	String deviceName = PhoneManager.getDeviceID(m_objContext);
    	int port = Integer.parseInt(strPortStr);
    	
        try 
        {
        	SocketFactoryInterface socketFactory = new TCPSocketFactory();
       	   	NetworkAddress serverAddress = new NetworkAddress (strIpAddress, port);
        	serverAddress.resolve ();

        	Injection.startInjection(deviceName);

        	m_objBasicScreen = new BasicScreen();
        	WindowManager wm = ((Activity)m_objContext).getWindowManager();
        	 
        	Display display = wm.getDefaultDisplay ();
        	m_objBasicScreen.setShape (display.getWidth (), display.getHeight ());
        	
            Log.debug ("Hostname: " + strClientName);
            
            m_objClient = new Client (m_objContext, strClientName, serverAddress, socketFactory, null, m_objBasicScreen);
            m_objClient.initService(m_objService);
            m_objClient.connect ();

			if (mainLoopThread == null) 
			{
				mainLoopThread = new MainLoopThread();
				mainLoopThread.start();
			}
        } 
        catch (Exception e) 
        {
        	e.printStackTrace();
        }
    }
    
	public void resize () 
    {
        try 
        {
        	WindowManager wm = ((Activity)m_objContext).getWindowManager();
        	Display display = wm.getDefaultDisplay ();
        	m_objBasicScreen.setShape (display.getWidth (), display.getHeight ());
    		m_objClient.handleShapeChanged();
        } 
        catch (Exception e) 
        {
        	e.printStackTrace();
        }
    }
	
	public void disconnect () 
    {
        try 
        {
        	Injection.stopInjection();
        	if(null != m_objClient) m_objClient.disconnect("disconnect");
			if(null != m_ServiceConnection) m_objContext.unbindService(m_ServiceConnection);
			if(null != m_ServiceConnection) m_ServiceConnection.onServiceDisconnected(null);
        }
        catch (Exception e) 
        {
        	e.printStackTrace();
        }
    }
	
	private void initService()
    {
		Intent remoteService = new Intent(IRemoteService.class.getName());
		m_objContext.bindService(remoteService, m_ServiceConnection, Context.BIND_AUTO_CREATE);
    }
	
	private void initServiceConnection ()
	{
		m_ServiceConnection = new ServiceConnection()
		{
			@Override
			public void onServiceConnected (ComponentName name, IBinder service)
			{
				m_objService = IRemoteService.Stub.asInterface(service);
				try
				{
					m_objService.registerCallback(m_ServiceCallback);
				}
				catch (RemoteException re)
				{
					re.printStackTrace();
				}
			}

			@Override
			public void onServiceDisconnected (ComponentName name)
			{
				m_objService = null;
			}
		};
	}
}
