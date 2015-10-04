
package org.synergy.io.msgs;

import java.io.DataInputStream;
import java.io.IOException;

import java.util.ArrayList;

public class SetOptionsMessage extends Message {
	public static final MessageType MESSAGE_TYPE = MessageType.DSETOPTIONS;


    public SetOptionsMessage (MessageHeader header, DataInputStream din) throws IOException { 
        ArrayList <Integer> options = new ArrayList <Integer> ();

        // Read off a list of integers until all the data defined in the header has been read
        int dataLeft = header.getDataSize ();
        while (dataLeft > 0) {
            options.add (Integer.valueOf (din.readInt ()));

            dataLeft -= INT_SIZE;
        }
        if (dataLeft != 0) {
            throw new IOException ("Error reading SetOptionsMessage. dataLeft: " + dataLeft);
        }
    }

    public String toString () {
        return "SetOptionsMessage:";
    }
}
