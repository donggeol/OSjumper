package org.synergy.phone.ptop;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.synergy.phone.util.PCODE;

import android.os.Handler;

public class OSJumperPeerToPeerServer implements Runnable
{
	private final int m_nBufferSize = 1600;
	private final int PORT = 24600; // 포트번호
	private DatagramSocket m_objDatagramSocket; // 연결소켓 
	private boolean m_bRunOnServer = false;
	private boolean m_bRunOnVoice = false;
	
	private Handler m_objHandler;
	
	private int m_nInputPeerPort = 0;
	private InetAddress m_objInetAddress;

	private String m_strOSJumperPCIP;
	
	public OSJumperPeerToPeerServer(Handler objHandler, String strOSJumperPCIP)
	{
		try 
		{
			this.m_strOSJumperPCIP = strOSJumperPCIP;
			this.m_objHandler = objHandler;
			if(m_objDatagramSocket == null)
				m_objDatagramSocket = new DatagramSocket(PORT);
			m_bRunOnServer = true;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
	}
	
	public void setRunOnVoice(boolean bRunOnVoice)
	{
		this.m_bRunOnVoice = bRunOnVoice;
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
				if(true == strFlag.equals(PCODE.RECOGNITION_REQUEST_CALL_S))
				{
					this.sendVoiceToPCServer(PCODE.RECOGNITION_REQUEST_CALL_S);
					
					m_nInputPeerPort = packet.getPort();
					m_objInetAddress = packet.getAddress();
					
					m_objHandler.sendEmptyMessage(PCODE.RECOGNITION_REQUEST_CALL);
				}
				else if(true == strFlag.equals(PCODE.RECOGNITION_REQUEST_END_S))
				{
					m_bRunOnVoice = false;
				}
				else if(true == strFlag.equals(PCODE.RECOGNITION_CALL_END_S))
				{
					m_objHandler.sendEmptyMessage(PCODE.RECOGNITION_CALL_END);
				}
				else if(true == strFlag.equals(PCODE.RECOGNITION_CALL_START_S))
				{
					m_nInputPeerPort = packet.getPort();
					m_objInetAddress = packet.getAddress();
				}
				else if(true == strFlag.equals(PCODE.RECOGNITION_CALL_START_FROM_SERVER_S))
				{
					m_objHandler.sendEmptyMessage(PCODE.RECOGNITION_CALL_START_FROM_SERVER);
				}
				else if(true == strFlag.equals(PCODE.RECOGNITION_CALL_REJECT_FROM_SERVER_S))
				{
					m_objHandler.sendEmptyMessage(PCODE.RECOGNITION_CALL_REJECT_FROM_SERVER);
				}
				
				if(true == m_bRunOnVoice)
				{
					switch(packet.getPort())
					{
						// 24600포트는 모바일로부터 전송된 음성으로 PC로 포워딩 한다.
						case 24500 : sendVoiceToPCServer(packet.getData()); break; 
						// 24700포트는 PC로부터 전송된 음성으로 모바일로 포워딩 한다.
						case 24700 : sendVoiceToMobileClient(packet.getData()); break;
					}
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
		if(0 != m_nInputPeerPort && null != m_objInetAddress)
		{
	    	try 
	        {
	    		DatagramPacket sendPacket = new DatagramPacket(req, req.length, 
	    				m_objInetAddress, m_nInputPeerPort);
	    		m_objDatagramSocket.send(sendPacket);
			} 
	        catch (IOException e){e.printStackTrace();}
		}
    }
	
	private void sendVoiceToPCServer(String strMessage)
    {
		try 
        {
			InetAddress objServerAddr = InetAddress.getByName(m_strOSJumperPCIP); 
    		DatagramPacket sendPacket = new DatagramPacket(strMessage.getBytes(), strMessage.getBytes().length,
    				objServerAddr, 24700);
    		m_objDatagramSocket.send(sendPacket);
		} 
        catch (IOException e){e.printStackTrace();}
    }
	
	private void sendVoiceToPCServer(byte[] req)
    {
		try 
        {
			InetAddress objServerAddr = InetAddress.getByName(m_strOSJumperPCIP); 
    		DatagramPacket sendPacket = new DatagramPacket(req, req.length, objServerAddr, 24700);
    		m_objDatagramSocket.send(sendPacket);
		} 
        catch (IOException e){e.printStackTrace();}
    }
	
	private void sendVoiceToMobileClient(byte[] req)
    {
		try 
        {
			DatagramPacket sendPacket = new DatagramPacket(req, req.length, m_objInetAddress, 24500);
    		m_objDatagramSocket.send(sendPacket);
		} 
        catch (IOException e){e.printStackTrace();}
    }
	
	public void sendVoiceToMobileClient(String strMessage)
    {
		try 
        {
    		DatagramPacket sendPacket = new DatagramPacket(strMessage.getBytes(), strMessage.getBytes().length,
    				m_objInetAddress, 24500);
    		m_objDatagramSocket.send(sendPacket);
		} 
        catch (IOException e){e.printStackTrace();}
    }
	
	public void sendVoiceMessage(String strMessage)
    {
		if(0 != m_nInputPeerPort && null != m_objInetAddress)
		{
	        try 
	        {
	    		DatagramPacket sendPacket = new DatagramPacket(strMessage.getBytes(), strMessage.getBytes().length,
	    				m_objInetAddress, m_nInputPeerPort);
	    		m_objDatagramSocket.send(sendPacket);
			} 
	        catch (IOException e){e.printStackTrace();}
		}
    }
	
    public void closeSocket()
    {
    	try
    	{
    		m_bRunOnServer = false;
    		m_objDatagramSocket.disconnect();
    		m_objDatagramSocket.close();
    	}
    	catch (Exception e){e.printStackTrace();}
    }
}
