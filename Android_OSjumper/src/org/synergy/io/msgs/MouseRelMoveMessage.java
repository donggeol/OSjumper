
package org.synergy.io.msgs;

import java.io.DataInputStream;
import java.io.IOException;

public class MouseRelMoveMessage {
	public static final MessageType MESSAGE_TYPE = MessageType.DMOUSERELMOVE;

	private short x;
	private short y;

	public MouseRelMoveMessage (DataInputStream din) throws IOException {
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
		return "MouseRelMoveMessage:(" + x + "," + y + ")";
	}
}
