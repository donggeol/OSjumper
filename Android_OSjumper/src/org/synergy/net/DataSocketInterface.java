
package org.synergy.net;

import java.io.IOException;

import org.synergy.io.Stream;

public interface DataSocketInterface extends Stream, SocketInterface {

    public abstract void connect (NetworkAddress address) throws IOException;
    
    public abstract boolean isReady ();

}
