package org.synergy.phone.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.synergy.phone.util.PCODE;
import org.synergy.phone.util.RCODE;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.os.Handler;

public class OSJumperSocketClient implements Runnable
{
	private final int m_nBufferSize = 1600;
	private static final int RECORDER_SAMPLERATE = 8000; // 44100
	private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_DEFAULT;
	private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
	
	private String m_strIP = "211.189.127.116";
	private final int PORT = 24500; // PORT번호
	
	private DatagramSocket m_objDatagramSocket;
	private InetAddress m_objServerAddr;
	private int bufferSize = 0;
	private AudioTrack m_objAudioTrack;
	private boolean m_bRunOnServer = false;
	private int m_nOverallBytes = 0;
	private Handler m_objHandler;
	
    public OSJumperSocketClient(String strIP, Handler objHandler)
    {
    	try 
    	{
	    	this.m_strIP = strIP;
	    	this.m_objHandler = objHandler;
			this.m_objServerAddr = InetAddress.getByName(m_strIP); //Local Address
	    	bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE,
					RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING);
			
			m_objAudioTrack = new AudioTrack(AudioManager.STREAM_VOICE_CALL, RECORDER_SAMPLERATE, 
					RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING, bufferSize, AudioTrack.MODE_STREAM);
			
			if(m_objDatagramSocket == null)
				m_objDatagramSocket = new DatagramSocket(PORT);
			m_bRunOnServer = true;
		} 
    	catch (Exception e) 
    	{
			e.printStackTrace();
		} 
    }
    
    public void onVoiceStart()
    {
    	sendVoiceMessage(PCODE.RECOGNITION_CALL_START_S);
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
    		DatagramPacket sendPacket = new DatagramPacket(strMessage.getBytes(), strMessage.getBytes().length,
    				m_objServerAddr, 24600);
    		m_objDatagramSocket.send(sendPacket);
		} 
        catch (IOException e){e.printStackTrace();}
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

	@Override
	public void run() {
		try
        {
        	byte [] data = new byte[m_nBufferSize]; // 수신받을 데이타크기를 맞게 정할것
            DatagramPacket packet = new DatagramPacket(data, m_nBufferSize);

            while (true == m_bRunOnServer)
            {
            	m_objDatagramSocket.receive(packet);
            	
            	String strFlag = new String(packet.getData()).trim();
				if(true == strFlag.equals(PCODE.RECOGNITION_REJECT_CALL_S))
				{
					m_objHandler.sendEmptyMessage(RCODE.RECOGNITION_SERVICE_REJECT);
				}
				else
				{
	            	m_nOverallBytes += m_objAudioTrack.write(packet.getData(), 0, packet.getData().length);
	            	
	            	if(m_nOverallBytes > bufferSize)
	                	m_objAudioTrack.play();
				}
            }
        }
        catch (Exception e) 
        {
        	e.printStackTrace();
        }
	}
}
