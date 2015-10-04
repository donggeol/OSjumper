
package org.synergy.io.msgs;

import java.io.DataInputStream;
import java.io.IOException;

public class MouseMoveMessage extends Message {
	public static final MessageType MESSAGE_TYPE = MessageType.DMOUSEMOVE;
	
	private short x;
	private short y;
	
	public MouseMoveMessage (DataInputStream din) throws IOException {
		x = din.readShort ();
		y = din.readShort ();
	}
	
	public int getX () {
		return (int) x;
	}
	
	public int getY () {
		return (int) y;
	}
	
	
	public String toString () {
		return "MouseMoveMessage:(" + x + "," + y + ")";
	}
	
}
