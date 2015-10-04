

package org.synergy.base;



public class Stopwatch {
	
	private double mark;
	private boolean triggered;
	private boolean stopped;
	
	public Stopwatch (boolean triggered) {
	    this.mark = 0.0;
        this.triggered = triggered;
        this.stopped = triggered;

        if (!this.triggered) {
            mark = getClock ();
        }

	}

    private double getClock () {
        return (double) System.currentTimeMillis () / 1000.0;
    }


    public double reset () {
        if (this.stopped) {
            final double dt = this.mark;
            this.mark = 0.0;
            return dt;
        } else {
            final double t = getClock ();
            final double dt = t - this.mark;
            this.mark = dt;
            return dt;
        }
    }

    public void stop () {
        if (this.stopped) {
            return;
        }

        // save the elapsed time
        this.mark = getClock () - this.mark;
        this.stopped = true;
    }

    public void start () {
        this.triggered = false;
        if (!this.stopped) {
            return;
        }

        // set the mark such that it reports the time elapsed at stop ()
        this.mark = getClock () - this.mark;
        this.stopped = false;
    }

    public void setTrigger () {
        stop ();
        this.triggered = true;
    }

    public double getTime () {
        if (this.triggered) {
            final double dt = this.mark;
            start ();
            return dt;
        } else if (this.stopped) {
            return this.mark;
        } else {
            return getClock () - this.mark;
        }
    }

    public double getTimeNoStart () {
        if (this.stopped) {
            return this.mark;
        } else {
            return getClock () - this.mark;
        }
    }


	public boolean isStopped () {
        return this.stopped;
    }
}
