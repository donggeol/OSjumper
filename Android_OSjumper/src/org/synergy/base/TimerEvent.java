
package org.synergy.base;

public class TimerEvent {

	// The timer
	private EventQueueTimer timer;
	
	// Number of repeats
	private Integer count;
	
	public TimerEvent (EventQueueTimer timer, Integer count) {
		this.timer = timer;
		this.count = count;
	}

	public EventQueueTimer getTimer() {
		return timer;
	}

	public void setTimer(EventQueueTimer timer) {
		this.timer = timer;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
}
