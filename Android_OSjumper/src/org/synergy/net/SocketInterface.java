
package org.synergy.net;


public interface SocketInterface {

    public void bind (final NetworkAddress address);

    public void close ();
    
    public Object getEventTarget ();
    

}
