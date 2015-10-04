
package org.synergy.io.msgs;

import java.io.DataInputStream;
import java.io.IOException;

import org.synergy.io.MessageDataInputStream;

/**
  * This message does not have a header
  */
public class HelloMessage extends Message {
	
    private static final int HELLO_MESSAGE_SIZE = 11;

    private int majorVersion;
    private int minorVersion;

	public HelloMessage (int majorVersion, int minorVersion) {
		// This message does not have a standard header
		this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
	}

    public HelloMessage (DataInputStream din) throws InvalidMessageException {
        try {
        	MessageDataInputStream mdin = new MessageDataInputStream(din);
        	
	        int packetSize = mdin.readInt ();
	
	        if (packetSize != HELLO_MESSAGE_SIZE) {
	            throw new InvalidMessageException ("Hello message not the right size: " + packetSize);
	        }

        	// Read in "Synergy" string
        	mdin.readExpectedString("Synergy");
        	
        	// Read in the major and minor protocol versions
        	majorVersion = mdin.readShort ();
        	minorVersion = mdin.readShort ();
        } catch (IOException e) {
        	throw new InvalidMessageException (e.getMessage());
        }
    }
    
    /*@Override
	public void write(DataOutputStream dout) throws IOException {
		// TODO Auto-generated method stub
    	// Not needed for client
	}*/

	public int getMajorVersion() {
		return majorVersion;
	}

	public int getMinorVersion() {
		return minorVersion;
	}
	
	public String toString () {
		return "HelloMessage:" + majorVersion + ":"  + minorVersion;
	}
}
