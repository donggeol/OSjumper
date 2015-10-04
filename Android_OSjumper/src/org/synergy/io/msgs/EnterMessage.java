
package org.synergy.io.msgs;

import java.io.DataInputStream;
import java.io.IOException;

public class EnterMessage extends Message {
	public static final MessageType MESSAGE_TYPE = MessageType.CENTER;
	
	private short x;
	private short y;
	private int sequenceNumber;
	private short mask;
	
	public EnterMessage (MessageHeader header, DataInputStream din) throws IOException {
		super (header);
		
		x = din.readShort ();
		y = din.readShort ();
		sequenceNumber = din.readInt ();
		mask = din.readShort ();
	}
	
	public short getX () {
		return x;
	}

	public short getY () {
		return y;
	}

	public int getSequenceNumber () {
		return sequenceNumber;
	}

	public short getMask () {
		return mask;
	}

	public String toString () {
		return "EnterMessage:(" + x + "," + y + "):" + sequenceNumber + ":" + mask;  
	}
	
	
}
