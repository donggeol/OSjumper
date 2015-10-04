package org.synergy.mouse;

import org.synergy.mouse.IRemoteServiceCallback;

interface IRemoteService
{
	void registerCallback(IRemoteServiceCallback cb);
	void unregisterCallback(IRemoteServiceCallback cb);
	
	String onRemoteService(int type, int x, int y, in Bundle objBundle);
	String onRemoteClipBoardService(int type, in Bundle objBundle);
	String onRemoteKeyBoardService(int type, int buttonID, in Bundle objBundle);
}