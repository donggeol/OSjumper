
package org.synergy.net;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetworkAddress {

    private InetAddress address;
    private String hostname = "";
    private int port = 0;
    

    public NetworkAddress () {
    }

    public NetworkAddress (int port) {
    }

    public NetworkAddress (final String hostname, final int port) {
        this.hostname = hostname;
        this.port = port;
    }

    protected void finalize () throws Throwable {
        super.finalize ();

        // TODO close address
    }

    public void resolve () throws UnknownHostException {
        if (address != null) {
            // Discard previous address

            address = null;
        }

        if (hostname == null) {
        	System.out.println ("Hostname is empty");
        } else {
            address = InetAddress.getByName (hostname);
        }
    }

    public boolean isValid () {
    	return true;
    }

    public InetAddress getAddress () {
        return address;
    }

    public int getPort () {
    	return port;
    }

    public String getHostname () {
    	return hostname;
    }

    private void checkPort () {
    }

    boolean equalTo (NetworkAddress address) {
    	return false;
    }  
}
