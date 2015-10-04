
package org.synergy.io.msgs;

import java.io.DataInputStream;
import java.io.IOException;

public class LeaveMessage extends EmptyMessage {
	public static final MessageType MESSAGE_TYPE = MessageType.CLEAVE;
	
	public LeaveMessage (DataInputStream din) throws IOException {
		super ();
	}
	
	public String toString () {
		return "LeaveMessage";
	}
}
