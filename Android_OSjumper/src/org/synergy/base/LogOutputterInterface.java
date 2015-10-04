
package org.synergy.base;

public interface LogOutputterInterface {

    public void open (final String title);

    public void close ();

    public void show (final boolean showIfEmpty);

    public boolean write (Log.Level level, final String tag, final String message);

    public void flush ();
}
