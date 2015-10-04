
package org.synergy.common.keys;

public interface KeyStateInterface {
	
	public void updateKeyMap ();
	
	public void updateKeyState ();
	
	public void setHalfDuplexMask (Integer keyModifierMask);
	
	public void fakeKeyDown (int id, int mask, int button);
	
	public void fakeKeyUp (int id, int mask, int button);
	
	// public void fakeKeyRepeat
	
	public void fakeAllKeysUp ();
	
	public void fakeCtrlAltDel ();
	
	// TODO... the rest
}
