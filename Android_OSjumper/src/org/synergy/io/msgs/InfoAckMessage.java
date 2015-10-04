
package org.synergy.io.msgs;

import java.io.DataInputStream;

public class InfoAckMessage extends EmptyMessage {
    public static final MessageType MESSAGE_TYPE = MessageType.CINFOACK;

    public InfoAckMessage (DataInputStream din) {
        super ();
    }

    public InfoAckMessage () {
        super (MESSAGE_TYPE);
    }
}
