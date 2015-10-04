
package org.synergy.base;

public interface EventQueueBuffer {

    // get an event, wait for a period of time
    public EventData getEvent (double timeout) throws InterruptedException;
    
    // No timeout
    public EventData getEvent () throws InterruptedException;

    public void addEvent (Integer dataID) throws InterruptedException;

    public boolean isEmpty ();

    //public EventQueueTimer newTimer (double duration, boolean oneShot);

    //public void deleteTimer (EventQueueTimer timer);

}
