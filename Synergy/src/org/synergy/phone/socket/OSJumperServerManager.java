package org.synergy.phone.socket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import android.os.Handler;

public class OSJumperServerManager extends Thread 
{
	private InputStream is;
	private OutputStream os;
	private BufferedInputStream dis;
	private BufferedOutputStream dos;

	private Socket cb_socket;
	private Handler m_objHandler;
	
	// �����ڸ޼ҵ�
	public OSJumperServerManager(Socket soc) 
	{
		// �Ű������� �Ѿ�� �ڷ� ����
		this.cb_socket = soc;
		User_network();
	}

	public void User_network() 
	{
		try 
		{
			is = cb_socket.getInputStream();
			dis = new BufferedInputStream(is);
			os = cb_socket.getOutputStream();
			dos = new BufferedOutputStream(os);
		} 
		catch (Exception e) 
		{
			System.out.println("ControlBox Function("+System.currentTimeMillis()+"):Stream Setting Error->"+e.getMessage());
		}
	}

	public void InMessage(String str) 
	{
		m_objHandler.sendEmptyMessage(0);
	}
	
	public void send_Message(String str) 
	{
		try 
		{
			dos.write(str.getBytes());
			dos.flush();
		} 
		catch (IOException e) 
		{
			System.out.println("ControlBox Function("+System.currentTimeMillis()+"):Message Send Error->"+e.getMessage());
		}
	}

	public void run() // ������ ����
	{
		while (true) 
		{
			try 
			{
				// ControlBox���� �޴� �޼���
				byte[] buffer = new byte[1024]; 
	        	int size = dis.read(buffer); 
	        	String msg = new String(buffer, 0, size);
	        	
	        	if(null != msg && 0 < msg.length())
	        		InMessage(msg);
			} 
			catch (Exception e) 
			{
				try 
				{
					dos.close();
					dis.close();
					cb_socket.close();
					break;
				} 
				catch (Exception ee) 
				{
					ee.printStackTrace();
				}
			}
		}
	}
}
