
package org.synergy.common.screens;

import java.util.ArrayList;

import android.graphics.Point;
import android.graphics.Rect;

public interface ScreenInterface {

	public Object getEventTarget ();

	public Rect getShape ();
	
	public Point getCursorPos ();
	
	public void enable ();
	
	public void disable ();
	
	public void enter (int toggleMask);
	
	public boolean leave ();
	
	public void keyDown (int keyEventID, int mask, int button);
	
	public void keyUp (int keyEventID, int mask, int button);
	
	public void keyRepeat (int keyEventID, int mask, int button);

	public void mouseDown (int buttonID);

	public void mouseUp (int buttonID);

	public void mouseMove (int x, int y);

	public void mouseRelativeMove (int x, int y);

	public void mouseWheel (int x, int y);
	
	public void clipBoard (String strMessage);
	
	public void fileTransfer (ArrayList<Byte> arfile);
	
	public void dragInfo (String strFileName);
	

}
