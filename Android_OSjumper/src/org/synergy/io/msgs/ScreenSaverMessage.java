
package org.synergy.io.msgs;

import java.io.DataInputStream;
import java.io.IOException;

public class ScreenSaverMessage {
	public static final MessageType MESSAGE_TYPE = MessageType.DMOUSERELMOVE;

	public ScreenSaverMessage (DataInputStream din, byte screenSaverOnFlag) throws IOException {
	}
	
	public String toString () {
		return "ScreenSaverMessage: TODO";
	}
}
