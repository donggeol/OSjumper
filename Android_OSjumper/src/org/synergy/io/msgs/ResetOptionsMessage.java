
package org.synergy.io.msgs;

import java.io.DataInputStream;

public class ResetOptionsMessage extends EmptyMessage {
	public static final MessageType MESSAGE_TYPE = MessageType.CRESETOPTIONS;
	
	public ResetOptionsMessage (DataInputStream din) {
		
	}
	
	public ResetOptionsMessage (MessageHeader header) {
		super (header);
	}
	
	public ResetOptionsMessage () {
		super (MESSAGE_TYPE);
	}

	public String toString () {
		return "ResetOptionsMessage: TODO";
	}
}
