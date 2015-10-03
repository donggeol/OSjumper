package org.synergy.client.file;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.os.Environment;

public class ClientFile 
{
	private String m_objStrFileName;
	
	public void setFileName(String m_objStrFileName) 
	{
		this.m_objStrFileName = m_objStrFileName;
	}

	public void saveFile(byte[] data)
	{
		File SDCardRoot = Environment.getExternalStorageDirectory();
		String strFilePath = SDCardRoot.getPath()+"/OSJumper";
		File filePath = new File(strFilePath);
		if(false == filePath.exists())
		{
			filePath.mkdirs();
			filePath.mkdir();
		}
		
		File file = new File(strFilePath, m_objStrFileName);
		FileOutputStream fileOutput = null;
		try
		{
			fileOutput = new FileOutputStream(file);
			fileOutput.write(data);
			fileOutput.close();
		}
		catch (FileNotFoundException e) {e.printStackTrace();} 
		catch (IOException e) {e.printStackTrace();}
	}
	
	public void saveFile(DataInputStream dataInputStream)
	{
		File SDCardRoot = Environment.getExternalStorageDirectory();
		//create a new file, specifying the path, and the filename
		//which we want to save the file as.
		File file = new File(SDCardRoot, m_objStrFileName);

		//this will be used to write the downloaded data into the file we created
		FileOutputStream fileOutput = null;
		try 
		{
			fileOutput = new FileOutputStream(file);
		
			//variable to store total downloaded bytes
			int downloadedSize = 0;
	
			//create a buffer...
			byte[] buffer = new byte[1024];
			int bufferLength = 0; //used to store a temporary size of the buffer
	
			//now, read through the input buffer and write the contents to the file
			while ( (bufferLength = dataInputStream.read(buffer)) > 0 ) 
			{
				//add the data in the buffer to the file in the file output stream (the file on the sd card
				fileOutput.write(buffer, 0, bufferLength);
				//add up the size so we know how much is downloaded
				downloadedSize += bufferLength;
			}
			//close the output stream when done
			fileOutput.close();
		
		} 
		catch (FileNotFoundException e) {e.printStackTrace();} 
		catch (IOException e) {e.printStackTrace();}
	}
}
