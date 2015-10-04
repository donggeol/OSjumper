
package org.synergy.io.msgs;

import java.io.DataInputStream;
import java.io.IOException;

import org.synergy.io.MessageDataInputStream;

public class ClipboardDataMessage extends Message {
	public static final MessageType MESSAGE_TYPE = MessageType.DCLIPBOARD;
	
	private byte id;
	private int sequenceNumber;
	private String data;
		
	public ClipboardDataMessage (MessageHeader header, DataInputStream din) throws IOException {
		super (header);
		
		MessageDataInputStream mdin = new MessageDataInputStream(din);
		
		id = mdin.readByte();
		sequenceNumber = mdin.readInt();
		data = mdin.readString ();
	}
	
	public String toString () {
		return data; //"ClipboardDataMessage:" + id + ":" + sequenceNumber + ":" + data;
	}
	
}
