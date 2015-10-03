package org.synergy.phone;

import org.synergy.mouse.IRemoteService;
import org.synergy.phone.ptop.OSJumperPeerToPeerClient;
import org.synergy.phone.util.PCODE;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;

public class OSJumperPhoneManager 
{
	private Context m_objContext;
	private boolean m_bClickOnVoiceCallButton = false; // 음성 전화 기능 사용 정보
	private boolean m_bClickOnCallButton = false; // 음성 전화 연결 사용 정보

	private OSJumperPeerToPeerClient m_objOSJumperPeerToPeerClient;
	private Thread m_objPeerToPeerClientThread;
	
	public OSJumperPhoneManager(Context objContext)
	{
		this.m_objContext = objContext;
	}
	
	public void onClickVoiceCallEvent(IRemoteService objIRemoteService, String strIP)
	{
		if(false == m_bClickOnVoiceCallButton) setOnVoiceCall(objIRemoteService, strIP);
		else setOffVoiceCall(objIRemoteService);
	}
	
	public void onClickCallEvent(String strReceiverIP)
	{
		if(false == m_bClickOnCallButton) setOnCall(strReceiverIP);
		else setOffCall();
	}
	
	private void setOnVoiceCall(IRemoteService objIRemoteService, String strIP)
	{
		m_bClickOnVoiceCallButton = true;
		
		Bundle objBundle = new Bundle();
		objBundle.putString("OSJUMPER_IP", strIP);
		try {objIRemoteService.onRemoteService(PCODE.RECOGNITION_VOICE_CALL_ON, 0, 0, objBundle);}
		catch (RemoteException e){e.printStackTrace();}
	}
	
	private void setOffVoiceCall(IRemoteService objIRemoteService)
	{
		m_bClickOnVoiceCallButton = false;
		
		try {objIRemoteService.onRemoteService(PCODE.RECOGNITION_VOICE_CALL_OFF, 0, 0, null);}
		catch (RemoteException e){e.printStackTrace();}
	}
	
	private void setOnCall(String strReceiverIP)
	{
		m_objOSJumperPeerToPeerClient = new OSJumperPeerToPeerClient(m_objContext, strReceiverIP);
		m_objPeerToPeerClientThread = new Thread(m_objOSJumperPeerToPeerClient);
		m_objPeerToPeerClientThread.start();
		
		m_objOSJumperPeerToPeerClient.sendVoiceMessage(PCODE.RECOGNITION_REQUEST_CALL_S);
	}
	
	private void setOffCall()
	{
		m_objOSJumperPeerToPeerClient.sendVoiceMessage(PCODE.RECOGNITION_REQUEST_END_S);
		m_objOSJumperPeerToPeerClient.closeSocket();
	}
}
