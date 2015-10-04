
package org.synergy.io.msgs;

import java.io.DataInputStream;

public class CloseMessage extends EmptyMessage {
	private static final MessageType MESSAGE_TYPE = MessageType.CCLOSE;
	
	public CloseMessage (MessageHeader header, DataInputStream din) {
		super (header);
	}
	
	public CloseMessage () {
		super (MESSAGE_TYPE);
	}
	
	
}
