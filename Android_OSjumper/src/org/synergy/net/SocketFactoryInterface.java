
package org.synergy.net;

public interface SocketFactoryInterface {

    public DataSocketInterface create ();

    public ListenSocketInterface createListen ();
}
