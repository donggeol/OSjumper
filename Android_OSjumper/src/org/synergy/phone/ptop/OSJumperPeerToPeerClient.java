package org.synergy.phone.ptop;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.synergy.phone.OSJumperPhoneActivity;
import org.synergy.phone.util.PCODE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class OSJumperPeerToPeerClient implements Runnable
{
	private final int m_nBufferSize = 1600;
	private Context m_objContext;
	private String m_strIP;
	private InetAddress m_objServerAddr; // IP 번호
	private DatagramSocket m_objDatagramSocket; // 연결소켓 
	private boolean m_bRunOnServer = false;
	
	public OSJumperPeerToPeerClient(Context objContext, String strIP)
	{
		try
		{
			this.m_objContext = objContext;
			this.m_strIP = strIP;
			this.m_objServerAddr = InetAddress.getByName(strIP); 
			if(m_objDatagramSocket == null)
				m_objDatagramSocket = new DatagramSocket();
			m_bRunOnServer = true;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	@Override 
	public void run() 
	{ 
		try 
		{
			byte[] data = new byte[m_nBufferSize]; // 수신받을 데이타크기를 맞게 정할것
			DatagramPacket packet = new DatagramPacket(data, m_nBufferSize);
			while (true == m_bRunOnServer) 
			{
				m_objDatagramSocket.receive(packet);
				String strFlag = new String(packet.getData()).trim();
				if(true == strFlag.equals(PCODE.RECOGNITION_RECEIVE_CALL_S))
				{
					goToOSJumperPhoneAcitvity();
				}
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
    
	public void sendVoiceMessageStart(byte[] req)
    {
    	try 
        {
    		DatagramPacket sendPacket = new DatagramPacket(req, req.length, m_objServerAddr, 24600);
    		m_objDatagramSocket.send(sendPacket);
		} 
        catch (IOException e){e.printStackTrace();}
    }
	
	public void sendVoiceMessage(String strMessage)
    {
        try 
        {
    		DatagramPacket sendPacket = new DatagramPacket(strMessage.getBytes(), 
    				strMessage.getBytes().length, m_objServerAddr, 24600);
    		m_objDatagramSocket.send(sendPacket);
		} 
        catch (IOException e){e.printStackTrace();}
    }
	
	/**
	 * VoIP를 위한 데이터 소켓을 해제한다.
	 */
    public void closeSocket()
    {
    	try
    	{
    		m_bRunOnServer = false;
    		m_objDatagramSocket.close();
    	}
    	catch (Exception e){e.printStackTrace();}
    }
    
    /**
     * VoIP를 위한 Phone 기본 정보를 설정 한다.
     */
	public void goToOSJumperPhoneAcitvity()
	{
		Intent objMainIntent = new Intent(m_objContext, OSJumperPhoneActivity.class);
    	objMainIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    	objMainIntent.putExtra("OSJUMPER_RECEIVER_IP", m_strIP); // 수신자의 IP 주소를 전달 한다.
    	m_objContext.startActivity(objMainIntent);
    	((Activity)m_objContext).overridePendingTransition(0,0);
	}
}
