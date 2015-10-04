
package org.synergy.io.msgs;

public class EmptyMessage extends Message {

	public EmptyMessage () {
		super ();
	}
	
	public EmptyMessage (MessageHeader header) {
		super (header);
	}
	public EmptyMessage (MessageType type) {
		super (type);
	}
	
	protected final void writeData () {
		// Do nothing. 
	}
}
