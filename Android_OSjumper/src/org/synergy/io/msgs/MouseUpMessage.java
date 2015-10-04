
package org.synergy.io.msgs;

import java.io.DataInputStream;
import java.io.IOException;

public class MouseUpMessage extends Message {
    public static final MessageType MESSAGE_TYPE = MessageType.DMOUSEUP;

    byte buttonID = 0;

    public MouseUpMessage (DataInputStream din) throws IOException {
        super ();
        buttonID = din.readByte ();
    }

    public int getButtonID () {
        return (int) buttonID;
    }

    public String toString () {
        return "MouseUpMessage:" + buttonID;
    }

}
