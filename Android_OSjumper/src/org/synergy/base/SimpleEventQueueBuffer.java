
package org.synergy.base;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.*;
import java.util.concurrent.TimeUnit;

public class SimpleEventQueueBuffer implements EventQueueBuffer {
	private BlockingQueue <Integer> queue;
	
	public SimpleEventQueueBuffer () {
		// TODO: NOTE: This WAS a LinkedBlockingDeque but Android does not support that
		// 
		// Need to reevaluate the workings there and make sure everything is going to work
		queue = new LinkedBlockingQueue <Integer> ();
	}
	
    public EventData getEvent () throws InterruptedException {
    	Integer dataID = queue.take ();
        return new EventData (EventData.Type.USER, null, dataID);
    }
    
    public EventData getEvent (double timeout) throws InterruptedException {
    	Integer dataID = queue.poll ((long)(timeout * 1000.0), TimeUnit.MILLISECONDS);
        return new EventData (EventData.Type.USER, null, dataID);
    }

	public void addEvent (Integer dataID) throws InterruptedException {
		queue.put (dataID);
	}

	public boolean isEmpty () {
		return queue.isEmpty ();
	}
}
