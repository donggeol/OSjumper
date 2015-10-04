
package org.synergy.io;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Data Output Stream for messages
 * 
 * This class was specifically designed for writing strings in messages. 
 * 
 * Strings are written as: String length (int) + String 
 *  * 
 * 
 */

public class MessageDataOutputStream extends DataOutputStream {
	
	public MessageDataOutputStream (OutputStream out) {
		super (out);
	}
	
	
	/**
	 * Writes the string length and the string. 
	 *   
	 * @param str to write
	 * @throws IOException
	 */
	public void writeString (String str) throws IOException {
		super.writeInt (str.length());
		super.write(str.getBytes ("UTF8"));
	}
	
	
	
	
}
