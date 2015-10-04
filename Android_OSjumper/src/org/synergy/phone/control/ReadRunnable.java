package org.synergy.phone.control;

import java.io.IOException;
import java.io.InputStream;

import com.google.android.voicesearch.endpointer.EndpointerInputStream;
import com.google.android.voicesearch.endpointer.MicrophoneInputStream;

public class ReadRunnable implements Runnable 
{
	private volatile boolean record = false;
	private boolean running = true;
	private InputStream mis = null;
	private EndpointerInputStream eis = null;
	private byte[] buffer = new byte[1600];
	
	/**
	 * 음성 통화 VoIP를 위한 마이크 타입, 주파수, 음폭, 등등을 초기 값으로 설정
	 * @param nSTTEndTime, listener
	 * @throws IOException
	 */
	public ReadRunnable(int nSTTEndTime, EndpointerInputStream.Listener listener) throws IOException
	{
		mis = new MicrophoneInputStream();
		eis = new EndpointerInputStream(mis, 2, new Long(nSTTEndTime), new Long(nSTTEndTime), new Long(nSTTEndTime));
		eis.setListener(listener);
	}
	
	@Override
	public void run() 
	{
		while (running) 
		{
			while (record) 
			{
				try 
				{
					eis.read(buffer); 
				} 
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	public void startRecord() 
	{
		record = true;
	}

	public void stopRecord() 
	{
		record = false;
		running = false;

		eis.requestClose();
		
		try {mis.close();}catch (Exception e){e.printStackTrace();}
		try {eis.close();}catch (Exception e){e.printStackTrace();}
	}

	public void stopRun() 
	{
		running = false;
	}
}
