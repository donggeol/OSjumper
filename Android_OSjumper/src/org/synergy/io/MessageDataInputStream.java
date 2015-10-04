
package org.synergy.io;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.util.Log;

public class MessageDataInputStream extends DataInputStream {

	public MessageDataInputStream (InputStream in) {
		super (in);
	}
	
	/**
	 * Read in a string.  First reads in the string length and then the string
	 */
	public String readString () throws IOException {
		int stringLength = readInt ();
        Log.d("::OSJumper stringLength::", stringLength+"");
		
		// Read in the bytes and convert to a string
		byte [] stringBytes = new byte [stringLength];
		read (stringBytes, 0, stringBytes.length);
		return new String (stringBytes);
	}
	
	public byte[] readByteArray () throws IOException {
		int stringLength = readInt ();
        Log.d("::OSJumper stringLength::", stringLength+"");
		
		// Read in the bytes and convert to a string
		byte [] stringBytes = new byte [stringLength];
		read (stringBytes, 0, stringBytes.length);
		return stringBytes;
	}
	
	public String readString (int stringLength) throws IOException {
		// Read in the bytes and convert to a string
		byte [] stringBytes = new byte [stringLength];
		read (stringBytes, 0, stringBytes.length);
		return new String (stringBytes);
	}
	
	/**
	 * Read an expected string from the stream
	 * @throws IOException if expected string is not read
	 */
	public void readExpectedString (String expectedString) throws IOException {
		byte [] stringBytes = new byte [expectedString.length()];
		
		// Read in the bytes and convert to a string
		read (stringBytes, 0, stringBytes.length);
		String readString = new String (stringBytes);
		
		if (readString.equals(expectedString) == false) {
			throw new IOException ("Expected string " + expectedString + " not found.  Found: " + readString);
		}
	}
	
	
}
