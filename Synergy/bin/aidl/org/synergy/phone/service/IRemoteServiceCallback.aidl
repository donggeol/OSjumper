package org.synergy.phone.service;

oneway interface IRemoteServiceCallback
{
	void onRemoteServiceCallback(int what, in Bundle objBundle);
}
