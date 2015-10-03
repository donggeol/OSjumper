package org.synergy.phone.service;

import org.synergy.phone.service.IRemoteServiceCallback;

interface IRemoteService
{
	void registerCallback(IRemoteServiceCallback cb);
	void unregisterCallback(IRemoteServiceCallback cb);
	
	String onRemoteService(int what, in Bundle objBundle);
}