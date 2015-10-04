
package org.synergy.base;

import org.synergy.base.exceptions.InvalidMessageException;

public interface EventQueueInterface {

	public void adoptBuffer (EventQueueBuffer eventQueueBuffer);
	
	public Event getEvent (final Event event, final double timeout) throws InvalidMessageException;
	
	public boolean dispatchEvent (final Event event);

	public void addEvent (final Event event);

    public boolean isEmpty ();
}
