
package org.synergy.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Stream {

	public void close ();
	
	public boolean isReady ();

    public Object getEventTarget ();
    
    public InputStream getInputStream () throws IOException;
    
    public OutputStream getOutputStream () throws IOException;
}
