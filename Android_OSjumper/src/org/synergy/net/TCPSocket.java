
package org.synergy.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.synergy.base.Event;
import org.synergy.base.EventQueue;
import org.synergy.base.EventType;

public class TCPSocket implements DataSocketInterface {

	private static final int SOCKET_CONNECTION_TIMEOUT_IN_MILLIS = 1000;

    private Socket socket;

    private boolean connected = false;
    private boolean readable = false;
    private boolean writable = false;

    public TCPSocket () {
        try {
            socket = new Socket ();


        } catch (Exception e) {
            // TODO
            e.printStackTrace ();
        }

    }

    private void init () {
        // default state
        connected = false;
        readable = false;
        writable = false;

    }

    private void onConnected () {
        connected = true;
        readable = true;
        writable = true;
    }


    public void finalize () throws Throwable {
    }

    public void bind (final NetworkAddress address) {
    }

    public void close () {
    }
    
    public boolean isReady () {
    	return connected;
    }

    public InputStream getInputStream () throws IOException {
        return socket.getInputStream ();
    }
    public OutputStream getOutputStream () throws IOException {
    	return socket.getOutputStream ();
    }

    public void connect (final NetworkAddress address) 
    	throws IOException 
    {
        // TODO
        socket.connect (new InetSocketAddress (address.getAddress (), address.getPort ()),
                SOCKET_CONNECTION_TIMEOUT_IN_MILLIS);

        // Turn off Nagle's algorithm and set traffic type (RFC 1349) to minimize delay
        // to avoid mouse pointer "lagging"
        socket.setTcpNoDelay(true);
        socket.setTrafficClass(8);
        
        sendEvent (EventType.SOCKET_CONNECTED);
        onConnected ();
        sendEvent (EventType.STREAM_INPUT_READY);
    }
    
    public Object getEventTarget () {
    	return this;
    }
    
    private void sendEvent (EventType eventType) {
    	EventQueue.getInstance().addEvent(new Event (eventType, getEventTarget (), null));
    }
    
}
