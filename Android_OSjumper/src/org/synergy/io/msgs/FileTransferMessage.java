package org.synergy.io.msgs;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.synergy.io.MessageDataInputStream;

public class FileTransferMessage extends Message
{
	public static final MessageType MESSAGE_TYPE = MessageType.DFILETRANSFER;

	private ArrayList <Byte> arfile = new ArrayList <Byte> ();
	private int mark;
	private String data;
	
	public FileTransferMessage (MessageHeader header, DataInputStream din) throws IOException {
		super (header);
		
		MessageDataInputStream mdin = new MessageDataInputStream(din);
		mark = mdin.read();
		//data = mdin.readString();
		
		byte[] arByte = mdin.readByteArray();
		for(int i=0; i<arByte.length; i++)
		{
			arfile.add(arByte[i]);
		}
	}
	
	public int getMark()
	{
		return mark;
	}
	
	public ArrayList <Byte> getData()
	{
		return arfile;
	}
	
	/*public String getData()
	{
		return data;
	}*/
}
