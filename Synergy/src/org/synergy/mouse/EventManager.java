package org.synergy.mouse;

import java.io.IOException;

import org.synergy.mouse.parser.PaserManager;

public class EventManager 
{
	private boolean m_bMouseDown = false;
    private PaserManager m_objPaserManager;
	
	// Variables_About Programmatically Click Event
	private Process process; 
	private Runtime runtime;										// Runtime Object
	private boolean IsProcessKilled = false;					// Is Process Killed?										// Process : Execute Click Event
	
	public EventManager()
	{
		runtime = Runtime.getRuntime();
		this.m_objPaserManager = new PaserManager();
		m_objPaserManager.initPaserManager();
	}
	
	/**
	 * 마우스 눌림동작
	 * @return 마우스 눌림 :true
	 */
	public boolean isMouseDown() 
	{
		return m_bMouseDown;
	}
	
	/**
	 * 마우스 눌림동작 설정
	 * @param bMouseDown
	 */
	public void setMouseDown(boolean bMouseDown) 
	{
		this.m_bMouseDown = bMouseDown;
	}

	/**
	 * 클립보드 메시지 등록
	 * @param strClipBoardMessage
	 */
	public void setClipBoardMessage(String strClipBoardMessage) 
	{
		String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
		strClipBoardMessage = strClipBoardMessage.replaceAll(match, " ");
		strClipBoardMessage = strClipBoardMessage.trim();
		m_objPaserManager.setClipBoardMessage(strClipBoardMessage);
	}

	/**
	 * 한번 클릭 이벤트
	 * @param x, y
	 */
	public void onSingleTap(int x, int y)
	{
		try 
		{ 						
			if(IsProcessKilled == false) 
			{
				process = runtime.exec("su -c input tap " + x + " " + y);
				IsProcessKilled = true;
			}
			else 
			{
				process.destroy();
				process = runtime.exec("su -c input tap " + x + " " + y);
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 마우스 슬라이드 이벤트
	 * @param x, y
	 */
	public void onSwipScreen(int down_x, int down_y,int up_x,int up_y)
	   {
	      try 
	      {
	         if(IsProcessKilled == false) 
	         {
	            process = runtime.exec("su -c input swipe " + down_x + " " + down_y + " " + up_x + " " + up_y);
	            IsProcessKilled = true;
	         }
	         else 
	         {
	            process.destroy();
	            process = runtime.exec("su -c input swipe " + down_x + " " + down_y + " " + up_x + " " + up_y);
	         }
	      } 
	      catch (IOException e) 
	      {
	         e.printStackTrace();
	      }
	   }
	
	/**
	 * 키보드 입력 이벤트
	 * @param strMessage
	 */
	public void onKeyBoard(String strID)
	{
		String strMessage = m_objPaserManager.getPaserManager(strID);
		try 
		{
			int nID = Integer.parseInt(strID);
			if(IsProcessKilled == false) 
			{
				switch(nID)
				{
					case 14:
					case 15:
					case 28:
					case 57:
					case 59:
					case 327:
					case 339:
					{
						process = runtime.exec("su -c input keyevent \""+strMessage+"\"");
					}
					break;
					default :
					{
						process = runtime.exec("su -c input text \""+strMessage+"\"");
					}	
					break;
				}
				IsProcessKilled = true;
			}
			else 
			{ 
				process.destroy();
				switch(nID)
				{
					case 14:
					case 15:
					case 28:
					case 57:
					case 59:
					case 327:
					case 339:
					{
						process = runtime.exec("su -c input keyevent \""+strMessage+"\"");
					}
					break;
					default :
					{
						process = runtime.exec("su -c input text \""+strMessage+"\"");
					}	
					break;
				}
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
