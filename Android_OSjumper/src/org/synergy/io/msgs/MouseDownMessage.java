
package org.synergy.io.msgs;

import java.io.DataInputStream;
import java.io.IOException;

public class MouseDownMessage extends Message {
    public static final MessageType MESSAGE_TYPE = MessageType.DMOUSEDOWN;

    byte buttonID = 0;

    public MouseDownMessage (DataInputStream din) throws IOException {
        super ();
        buttonID = din.readByte ();
    }

    public int getButtonID () {
        return (int) buttonID;
    }

    public String toString () {
        return "MouseDownMessage:" + buttonID;
    }

}
