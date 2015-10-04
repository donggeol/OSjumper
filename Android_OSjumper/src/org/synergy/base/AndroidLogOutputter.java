
package org.synergy.base;

public class AndroidLogOutputter implements LogOutputterInterface {

    public AndroidLogOutputter () {
    }

    public void open (final String title) {
        // Do nothing
    }

    public void close () {
        // Do nothing
    }

    public void show (final boolean showIfEmpty) {
        // Do nothing
    }

    public boolean write (Log.Level level, final String tag, final String message) {
    	switch (level) {
    	case PRINT:
    		android.util.Log.v (tag, message);
    		break;
    	case FATAL:
    	case ERROR:
    		android.util.Log.e (tag, message);
    		break;
    	case WARNING:
    		android.util.Log.w (tag, message);
    		break;
    	case NOTE:
    	case INFO:
    		android.util.Log.i (tag, message);
    		break;
    	case DEBUG:
    	case DEBUG1:
    	case DEBUG2:
    	case DEBUG3:
    	case DEBUG4:
    	case DEBUG5:
    		android.util.Log.d (tag, message);
    		break;
    	}
    	
    	return true; // wtf
    }
	
    public void flush () {
        System.out.flush ();
    }
	
}
