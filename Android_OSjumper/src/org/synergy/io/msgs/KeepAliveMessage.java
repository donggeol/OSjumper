
package org.synergy.io.msgs;

import java.io.DataInputStream;

public class KeepAliveMessage extends EmptyMessage {
	private static final MessageType MESSAGE_TYPE = MessageType.CKEEPALIVE;

	public KeepAliveMessage (MessageHeader header, DataInputStream din) {
		super (header);
	}
	
	public KeepAliveMessage () {
		super (MESSAGE_TYPE);
	}
	
	public String toString () {
		return "KeepAliveMessage";
	}
	
}
