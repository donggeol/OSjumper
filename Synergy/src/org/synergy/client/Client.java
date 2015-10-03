/*
 * synergy -- mouse and keyboard sharing utility
 * Copyright (C) 2010 Shaun Patterson
 * Copyright (C) 2010 The Synergy Project
 * Copyright (C) 2009 The Synergy+ Project
 * Copyright (C) 2002 Chris Schoeneman
 * 
 * This package is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * found in the file COPYING that should have accompanied this file.
 * 
 * This package is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.synergy.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.synergy.base.Event;
import org.synergy.base.EventJobInterface;
import org.synergy.base.EventQueue;
import org.synergy.base.EventTarget;
import org.synergy.base.EventType;
import org.synergy.base.Log;
import org.synergy.client.file.ClientFile;
import org.synergy.client.file.FCODE;
import org.synergy.common.screens.ScreenInterface;
import org.synergy.io.Stream;
import org.synergy.io.StreamFilterFactoryInterface;
import org.synergy.io.msgs.EnterMessage;
import org.synergy.io.msgs.HelloBackMessage;
import org.synergy.io.msgs.HelloMessage;
import org.synergy.io.msgs.LeaveMessage;
import org.synergy.mouse.IRemoteService;
import org.synergy.mouse.TYPECODE;
import org.synergy.net.DataSocketInterface;
import org.synergy.net.NetworkAddress;
import org.synergy.net.SocketFactoryInterface;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.widget.Toast;

public class Client implements EventTarget {
	
	private final Context context;
	private String name;
	private NetworkAddress serverAddress;
	private Stream stream;
	private SocketFactoryInterface socketFactory;
	private StreamFilterFactoryInterface streamFilterFactory;
	private ScreenInterface screen;
	
	private int mouseX;
	private int mouseY;
	
	private ServerProxy server;
	private IRemoteService m_objService = null;
	private ClientFile m_objClientFile = null;
	private ArrayList<Byte> m_arByte = new ArrayList<Byte>();
	
	public Client (final Context context, final String name, final NetworkAddress serverAddress,
			SocketFactoryInterface socketFactory, StreamFilterFactoryInterface streamFilterFactory,
			ScreenInterface screen) {
		
		this.context = context;
		this.name = name;
		this.serverAddress = serverAddress;
		this.socketFactory = socketFactory;
		this.streamFilterFactory = streamFilterFactory;
		this.screen = screen;
        this.m_objClientFile = new ClientFile();
        
        assert (socketFactory != null);
        assert (screen != null);
	}
	
	public void finalize () throws Throwable {}
	
	public void initService(IRemoteService objService)
	{
		this.m_objService = objService;
	}
	
	public void connect () {
        if (stream != null) {
            Log.info ("stream != null");
            return;
        }

		try {
			serverAddress.resolve ();
			
			if (serverAddress.getAddress () != null) {
				Log.debug ("Connecting to: '" +
						serverAddress.getHostname () + "': " +
						serverAddress.getAddress () + ":" +
						serverAddress.getPort ());
			}
			
            // create the socket
	        DataSocketInterface socket = socketFactory.create ();
    
            // filter socket messages, including a packetizing filter
            stream = socket;
            if (streamFilterFactory != null) {
                // TODO stream = streamFilterFactory.create (stream, true);
            }

            // connect
            Log.debug ("connecting to server");

            setupConnecting ();
            setupTimer ();

            socket.connect (serverAddress);
            
            final Toast toast = Toast.makeText(context, "Connected to " + serverAddress.getHostname()
                    + ":" + serverAddress.getPort(), Toast.LENGTH_SHORT);
            toast.show();
		} catch (IOException e) {
			final String errorMessage = "Failed to connect to " + serverAddress.getHostname()
					+ ":" + serverAddress.getPort();
			Log.error(errorMessage);
			final Toast toast = Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT);
			toast.show();
		}
	}
	
	public void disconnect (String msg) {
		cleanupTimer ();
		cleanupScreen ();
		cleanupConnecting ();
		if (msg != null) {
			sendConnectionFailedEvent (msg);
		} else {
			sendEvent (EventType.CLIENT_DISCONNECTED, null);
		}
	}
		
    private void setupConnecting () {
        assert (stream != null);

        EventQueue.getInstance().adoptHandler(EventType.SOCKET_CONNECTED, stream.getEventTarget(),
        		new EventJobInterface () { 
        			public void run (Event event) {
        				handleConnected();
        			}});
        
        EventJobInterface job = EventQueue.getInstance().getHandler(EventType.SOCKET_CONNECTED, stream.getEventTarget());
        
        EventQueue.getInstance().adoptHandler(EventType.SOCKET_CONNECT_FAILED, stream.getEventTarget(),
        		new EventJobInterface () {
        			public void run (Event event) {
        				handleConnectionFailed();
        			}});
    }

    private void cleanupConnecting () {
        if (stream != null) {
            EventQueue.getInstance().removeHandler (EventType.SOCKET_CONNECTED, stream.getEventTarget ());
            EventQueue.getInstance().removeHandler (EventType.SOCKET_CONNECT_FAILED, stream.getEventTarget ());
        }
    }

    private void setupTimer () {
    }
    
    private void handleConnected () {
    	Log.debug1 ("connected; wait for hello");

        cleanupConnecting ();
        setupConnection ();
    }
    
    private void handleConnectionFailed () {
        // TODO
    }
    
    private void handleDisconnected () {
    	// TODO
    }

    private void handleHello () {
        Log.debug ("handling hello");

        try {
            // Read in the Hello Message
            DataInputStream din = new DataInputStream (stream.getInputStream ());
            HelloMessage helloMessage = new HelloMessage (din);

            Log.debug1 ("Read hello message: " + helloMessage);
            
            // TODO check versions
            
            // say hello back
            DataOutputStream dout = new DataOutputStream (stream.getOutputStream ());

            // Grab the hostname
            new HelloBackMessage (1, 3, name).write (dout);

            setupScreen ();
            cleanupTimer ();

            // make sure we process any remaining messages later. we won't 
            //  receive another event for already pending messages so we fake
            //  one
            if (stream.isReady ()) {
                // TODO, So far this event does nothing -- I think
                EventQueue.getInstance ().addEvent (new Event (EventType.STREAM_INPUT_READY, stream.getEventTarget ()));
            }

        } catch (Exception e) {
            e.printStackTrace ();
        }
    }
    
	private void setupConnection () {
        assert (stream != null);

        EventQueue.getInstance().adoptHandler(EventType.SOCKET_DISCONNECTED, stream.getEventTarget(),
        		new EventJobInterface () { public void run (Event event) {
        				handleDisconnected ();
        			}});

        EventQueue.getInstance().adoptHandler(EventType.STREAM_INPUT_READY, stream.getEventTarget(),
        		new EventJobInterface () { public void run (Event event) {
        				handleHello ();
        			}});

        EventQueue.getInstance().adoptHandler(EventType.STREAM_OUTPUT_ERROR, stream.getEventTarget(),
        		new EventJobInterface () { public void run (Event event) {
        				handleDisconnected ();
        			}});
        EventQueue.getInstance().adoptHandler(EventType.STREAM_INPUT_SHUTDOWN, stream.getEventTarget(),
        		new EventJobInterface () { public void run (Event event) {
        				handleDisconnected ();
        			}});
        EventQueue.getInstance().adoptHandler(EventType.STREAM_OUTPUT_SHUTDOWN, stream.getEventTarget(),
        		new EventJobInterface () { public void run (Event event) {
        				handleDisconnected ();
        			}});
    }

    private void setupScreen () {
        assert (server == null);
        assert (screen == null);

        server = new ServerProxy (this, stream);
        
        EventQueue.getInstance().adoptHandler(EventType.SHAPE_CHANGED, getEventTarget (),
        		new EventJobInterface () { public void run (Event event) {
        				handleShapeChanged ();
        			}});
    }
    
    private void cleanupTimer () {
        // TODO
    }
    
    private void cleanupScreen () {
    	// TODO/
    }

    public Object getEventTarget () {
        return screen.getEventTarget ();
    }

    public void handleShapeChanged () {
        Log.debug ("resolution changed");
        server.onInfoChanged ();
    }

    public Rect getShape () {
        return screen.getShape ();
    }

    public Point getCursorPos () {
        return screen.getCursorPos ();
    }
	

    public void handshakeComplete () {
        screen.enable ();
        sendEvent (EventType.CLIENT_CONNECTED, "");
    }

    private void sendEvent (EventType type, Object data) {
        EventQueue.getInstance ().addEvent (new Event (type, data));
    }

    private void sendConnectionFailedEvent (String msg) {}

    public void enter (EnterMessage enterMessage) {
    	mouseX = enterMessage.getX ();
    	mouseY = enterMessage.getY ();
        screen.mouseMove (mouseX, mouseY);
        screen.enter ((int)enterMessage.getMask ());

    	try {m_objService.onRemoteService(TYPECODE.MOUSE_ENTER, 0, 0, null);}
		catch (RemoteException e){e.printStackTrace();}
    }
    
    public void leave (LeaveMessage leaveMessage) {
    	// Since I don't know how to hide the cursor, tuck it away out of sight
    	screen.mouseMove (screen.getShape ().right, screen.getShape ().bottom);

    	try {m_objService.onRemoteService(TYPECODE.MOUSE_LEAVE, 0, 0, null);}
		catch (RemoteException e){e.printStackTrace();}
    }

    public void mouseMove (int x, int y) {
    	screen.mouseMove (x, y); 
    	
    	try {m_objService.onRemoteService(TYPECODE.MOUSE_POINT, x, y, null);}
		catch (RemoteException e){e.printStackTrace();}
    }

    public void mouseDown (int buttonID) {
        screen.mouseDown (buttonID);
        
        if(1 == buttonID)
        {
	    	try {m_objService.onRemoteService(TYPECODE.MOUSE_DOWN, 0, 0, null);}
			catch (RemoteException e){e.printStackTrace();}
        }
    }

    public void mouseUp (int buttonID) {
        screen.mouseUp (buttonID);
        
        if(1 == buttonID)
        {
	    	try {m_objService.onRemoteService(TYPECODE.MOUSE_UP, 0, 0, null);}
			catch (RemoteException e){e.printStackTrace();}
        }
    }
    
    public void relMouseMove (int x, int y) {
    	screen.mouseRelativeMove (x, y);
    }
    
    public void mouseWheel (int x, int y) {
    	screen.mouseWheel(x, y);
    }
    
    /**
     * @param keyEventID A VK_ defined in KeyEvent
     */
    public void keyDown (int keyEventID, int mask, int button) {
       screen.keyDown (keyEventID, mask, button);
       
       try {m_objService.onRemoteKeyBoardService(TYPECODE.KEYBOARD_ID, button, null);
	   } catch (RemoteException e) {e.printStackTrace();}
    }
    
    /**
     * @param keyEventID A VK_ defined in KeyEvent
     */
    public void keyRepeat (int keyEventID, int mask, int button) {
    	screen.keyDown (keyEventID, mask, button);
    }
    
    /**
     * @param keyEventID A VK_ defined in KeyEvent
     */
   public void keyUp (int keyEventID, int mask, int button) {
   		screen.keyUp (keyEventID, mask, button);
   }

	public void clipBoard(String strMessage) {
		screen.clipBoard(strMessage);
		
		Bundle objBundle = new Bundle();
		objBundle.putString("CLIPBOARD", strMessage);
		try {m_objService.onRemoteClipBoardService(TYPECODE.CLIPBOARD_MESSAGE, objBundle);
		} catch (RemoteException e) {e.printStackTrace();}
	}
	
	public void fileTransfer(int nMark, ArrayList<Byte> arByte) {
		screen.fileTransfer(null);
		
		switch(nMark)
		{
			case 1: 
			{
				mHandler.sendEmptyMessage(FCODE.FILE_TRANSFER_START);
				m_arByte.clear();
			}
			break;
			case 2:
			{
				mHandler.sendEmptyMessage(FCODE.FILE_TRANSFER_ING);
				m_arByte.addAll(arByte); 
			}
			break; 
			case 3: 
			{
				mHandler.sendEmptyMessage(FCODE.FILE_TRANSFER_END);
				byte[] arB = new byte[m_arByte.size()];
				for(int i=0; i<m_arByte.size(); i++)
				{
					arB[i] = m_arByte.get(i);
				}
				m_objClientFile.saveFile(arB); 
			}
			break;
		}
	}
	
	public void dragInfo(String strFilePaht) {
		screen.dragInfo(strFilePaht);
		
		if(null != m_objClientFile)
			m_objClientFile.setFileName(strFilePaht);
	}
	
	private Handler mHandler = new Handler(new Handler.Callback() 
	{
		@Override
		public boolean handleMessage(Message msg) 
		{
			switch (msg.what) 
			{
				case FCODE.FILE_TRANSFER_START:
				{
					Toast.makeText(context, "File Transfer Start", Toast.LENGTH_SHORT).show();
				}
				break;
				case FCODE.FILE_TRANSFER_ING:
				{
					Toast.makeText(context, "File Transfer ing....", Toast.LENGTH_SHORT).show();
				}
				break;
				case FCODE.FILE_TRANSFER_END : 
				{
					Toast.makeText(context, "File Transfer End", Toast.LENGTH_SHORT).show();
				}
				break;
			}
			return false;
		}
	});
}