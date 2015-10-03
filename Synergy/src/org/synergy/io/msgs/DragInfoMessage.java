package org.synergy.io.msgs;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.synergy.io.MessageDataInputStream;

import android.util.Log;

public class DragInfoMessage extends Message
{
	public static final MessageType MESSAGE_TYPE = MessageType.DDRAGINFO;
	
	private String fileName;
	private String fileSize;
	
	private int sequenceNumber;
	private String data;
	
	public DragInfoMessage (MessageHeader header, DataInputStream din) throws IOException {
		super (header);
		
		
		MessageDataInputStream mdin = new MessageDataInputStream(din);
		sequenceNumber = mdin.readUnsignedShort();
		data = mdin.readString();
		fileNameParser(data);
	}
	
	public String getFileName() {
		return fileName;
	}

	public String getFileSize() {
		return fileSize;
	}

	private void fileNameParser(String strPath) 
	{
		String[] parsing;
		strPath = strPath.trim();
		parsing = strPath.split("\\\\");

		strPath = parsing[parsing.length - 1];

		parsing = strPath.split(",");
		fileName = parsing[0];
		fileSize = parsing[1];
	}
}
