
package org.synergy.io.msgs;

import java.io.IOException;

public class InfoMessage extends Message {
	private static final MessageType MESSAGE_TYPE = MessageType.DINFO;

	short screenX;
	short screenY;
	short screenWidth;
	short screenHeight;
	short unknown; // TODO: I haven't figured out what this is used for yet
	short cursorX;
	short cursorY;
	

    public InfoMessage (int screenX, int screenY, int screenWidth, int screenHeight,
    					 int cursorX, int cursorY) {
    	super (MESSAGE_TYPE);

    	this.screenX = (short) screenX;
    	this.screenY = (short) screenY;
    	this.screenWidth = (short) screenWidth;
    	this.screenHeight = (short) screenHeight;
    	this.unknown = 0; // TODO: see above
    	this.cursorX = (short) cursorX;
    	this.cursorY = (short) cursorY;
    }

    @Override
    protected final void writeData () throws IOException {
        dataStream.writeShort (screenX);
        dataStream.writeShort (screenY);
        dataStream.writeShort (screenWidth);
        dataStream.writeShort (screenHeight);
        dataStream.writeShort (unknown);
        dataStream.writeShort (cursorX);
        dataStream.writeShort (cursorY);
    }
    
    @Override
    public final String toString () {
    	return "InfoMessage:" +
                screenX + ":" + 
                screenY + ":" + 
                screenWidth + ":" + 
                screenHeight + ":" + 
                unknown + ":" + 
                cursorX + ":" + 
                cursorY;
    	
    }
}
