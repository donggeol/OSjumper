
package org.synergy.net;

public class TCPSocketFactory implements SocketFactoryInterface {

    public TCPSocketFactory () { }

    public DataSocketInterface create () {
        return new TCPSocket ();
    }

    public ListenSocketInterface createListen () {
        return new TCPListenSocket ();
    }

}

