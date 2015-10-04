
package org.synergy.io.msgs;

import java.io.DataInputStream;
import java.io.IOException;

public class KeyDownMessage extends Message {
	public static final MessageType MESSAGE_TYPE = MessageType.DKEYDOWN;
	
	private int id;
	private int mask;
	private int button;
	
	public KeyDownMessage (DataInputStream din) throws IOException {
		id = din.readUnsignedShort ();
		mask = din.readUnsignedShort ();
		button = din.readUnsignedShort ();
	}
	
	public int getID () {
		return id;
	}
	
	public int getMask () {
		return mask;
	}
	
	public int getButton () {
		return button;
	}
	

	public String toString () {
		return MESSAGE_TYPE + ":" + id + ":" + mask + ":" + button;
	}
}
