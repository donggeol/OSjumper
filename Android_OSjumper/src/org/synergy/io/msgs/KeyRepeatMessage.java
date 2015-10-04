
package org.synergy.io.msgs;

import java.io.DataInputStream;
import java.io.IOException;

public class KeyRepeatMessage extends Message {
	public static final MessageType MESSAGE_TYPE = MessageType.DKEYDOWN;
	
	private short id;
	private short mask;
	private short count;
	private short button;
	
	public KeyRepeatMessage (DataInputStream din) throws IOException {
		id = din.readShort ();
		mask = din.readShort ();
		count = din.readShort ();
		button = din.readShort ();
	}

	public int getID () {
		return (int) id;
	}
	
	public int getMask () {
		return (int) mask;
	}
	
	public int getCount () { 
		return (int) count;
	}
	
	public int getButton () {
		return (int) button;
	}

	
	
	public String toString () {
		return MESSAGE_TYPE + ":" + id + ":" + mask + ":" + button;
	}
}
