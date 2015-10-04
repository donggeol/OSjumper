
package org.synergy.io.msgs;

import java.io.DataInputStream;

public class NoOpMessage extends EmptyMessage {
	private static final MessageType MESSAGE_TYPE = MessageType.CNOOP;
	
	public NoOpMessage (MessageHeader header, DataInputStream din) {
		super (header);
	}
	
	public NoOpMessage () {
		super (MESSAGE_TYPE);
	}
}
