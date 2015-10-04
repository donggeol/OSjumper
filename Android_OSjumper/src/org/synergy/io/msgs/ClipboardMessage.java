
package org.synergy.io.msgs;

import java.io.DataInputStream;
import java.io.IOException;

public class ClipboardMessage extends Message {
	public static final MessageType MESSAGE_TYPE = MessageType.DCLIPBOARD;
	public String m_strClipBoardMessage = "";
	
	public ClipboardMessage (DataInputStream din) throws IOException {
		m_strClipBoardMessage = din.readLine();   //문제? 부분 
	}
	
	public String toString () {
		return m_strClipBoardMessage;
	}
}
