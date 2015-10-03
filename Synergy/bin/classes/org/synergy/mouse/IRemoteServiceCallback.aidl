package org.synergy.mouse;

oneway interface IRemoteServiceCallback
{
	void onRemoteServiceCallback(int what, in Bundle objBundle);
}
