
package org.synergy.client;

import android.graphics.*;

public class ClientInfo {

	Rect screenPosition;
	Point cursorPos;

	public ClientInfo(Rect screenPosition, Point cursorPos) {
		this.screenPosition = screenPosition;
		this.cursorPos = cursorPos;
	}

	public Rect getScreenPosition() {
		return screenPosition;
	}

	public void setScreenPosition(Rect screenPosition) {
		this.screenPosition = screenPosition;
	}

	public Point getCursorPos() {
		return cursorPos;
	}

	public void setCursorPos(Point cursorPos) {
		this.cursorPos = cursorPos;
	}
}
