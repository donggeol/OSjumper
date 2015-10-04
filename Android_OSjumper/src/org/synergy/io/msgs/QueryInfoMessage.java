
package org.synergy.io.msgs;

import java.io.DataInputStream;

public class QueryInfoMessage extends EmptyMessage {
    public static final MessageType MESSAGE_TYPE = MessageType.QINFO;

    public QueryInfoMessage (DataInputStream din) {
        super ();
    }

    public QueryInfoMessage () {
        super (MESSAGE_TYPE);
    }
}
