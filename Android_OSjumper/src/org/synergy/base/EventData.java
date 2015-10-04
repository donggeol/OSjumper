
package org.synergy.base;


public class EventData {

	public enum Type {
		NONE,
		SYSTEM,
		USER
	}
	
	private Type type;
	private Event event;
	private Integer dataID;
	
	// None
	public EventData () {
		this.type = Type.NONE;
		this.dataID = -1;
		this.event = null;
	}
	
	public EventData (Type type, Event event, Integer dataID) {
		this.type = type;
		this.event = event;
		this.dataID = dataID;
	}

	public Type getType() {
		return type;
	}

	public Integer getDataID() {
		return dataID;
	}

	public Event getEvent() {
		return event;
	}
	
}
