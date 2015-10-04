
package org.synergy.io.msgs;

import java.io.DataInputStream;
import java.io.IOException;

public class MouseWheelMessage {
	public static final MessageType MESSAGE_TYPE = MessageType.DMOUSEWHEEL;
	
	private short xDelta;
	private short yDelta;
	
	public MouseWheelMessage (DataInputStream din) throws IOException {
		xDelta = din.readShort();
		yDelta = din.readShort();
	}
	
	public int getXDelta() {
		return xDelta;
	}
	
	public int getYDelta() {
		return yDelta;
	}
	
	public String toString () {
		return "MouseWheelMessage:(" + xDelta + "," + yDelta + ")";
	}

}
