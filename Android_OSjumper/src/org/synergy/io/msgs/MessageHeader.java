
package org.synergy.io.msgs;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import android.util.Log;

/**
 * Describes a message header
 * 
 * Size = 
 *  length of message type +
 *  message data size (see Message.write)
 */
public class MessageHeader {
	private final static int MESSAGE_TYPE_SIZE = 4;
	
	private Integer size;
	private Integer dataSize;
	private MessageType type;
	
	public MessageHeader (MessageType type) {
		this.type = type;
		this.size = type.getValue ().length ();
		this.dataSize = null;  // User must set
	}
	
	public MessageHeader (String type) {
		this.type = MessageType.fromString (type);
		this.size = this.type.getValue ().length ();
		this.dataSize = null;  // User must set
	}
	
	/**
	 * Read in a message header
	 * @param din Data input stream from socket
	 */
	public MessageHeader (DataInputStream din)
	{
		try
		{
			int messageSize = din.readInt ();
			
			byte messageTypeBytes [] = new byte [MESSAGE_TYPE_SIZE];
	        din.read (messageTypeBytes, 0, MESSAGE_TYPE_SIZE);
			
	        this.type = MessageType.fromString (new String (messageTypeBytes)); 
			this.size = MESSAGE_TYPE_SIZE;
	        this.dataSize = messageSize - this.size;
	        
	        Log.d("::OSJumper::", type.getValue());
		}
		catch(Exception e)
		{
			//e.printStackTrace();
			this.type = MessageType.CNOOP;
		}
	}
	
	/**
	 * Set the size of the DATA passed along with this message
	 * @param dataSize
	 */
	public void setDataSize (int dataSize) {
		this.dataSize = dataSize;
	}
	
	/**
	 * Get the size of the data in the message
	 */
	public int getDataSize () {
		return this.dataSize;
	}
	
	/**
	 * Get the message type for the message this header describes
	 * @return
	 */
	public MessageType getType () {
		return type;
	}
	
	public void write (DataOutputStream dout) throws IOException {
		if (dataSize == null) {
			throw new IOException ("Message header size is null");
		}
		
		dout.writeInt(size + dataSize);
		dout.write (type.getValue ().getBytes("UTF8"));
		
	}

	public String toString () {
		return "MessageHeader:" + size + ":" + dataSize + ":" + type;
	}
	
}
