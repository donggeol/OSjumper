package org.synergy.client.file;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;

public class WaitDialog extends Thread
{
	private Context m_Context;
	private String m_Msg;
	private ProgressDialog m_Progress;
	private Looper m_Looper;
	
	public WaitDialog(Context context, String msg)
	{
		this.m_Context = context;
		this.m_Msg = msg;
		setDaemon(true);
	}

	@Override
	public void run() 
	{
		super.run();
		
		try
		{
			Looper.prepare();
			m_Progress = new ProgressDialog(m_Context);
			m_Progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			m_Progress.setMessage(m_Msg);
			m_Progress.setCancelable(true);
			m_Progress.setButton("취소", new DialogInterface.OnClickListener() 
			{
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					m_Progress.dismiss();
				}
			});
			m_Progress.show();
			m_Looper.loop();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public static void stop(WaitDialog dig)
	{
		try
		{
			dig.m_Progress.dismiss();
			Thread.sleep(100);
			dig.m_Looper.quit();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public boolean checkNetwokState() 
	{
		ConnectivityManager manager = (ConnectivityManager) m_Context.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo lte_4g = manager.getNetworkInfo(ConnectivityManager.TYPE_WIMAX);
		
		boolean blte_4g = false;
		
		if (lte_4g != null)
			blte_4g = lte_4g.isConnected();
		
		if (mobile.isConnected() || wifi.isConnected() || blte_4g)
		{
			return true;
		}
		else
		{
			new AlertDialog.Builder(m_Context)
			.setTitle("네트워크 오류")
			.setMessage("네트워크 상태를 확인해 주십시요.")
			.setPositiveButton("다시 시도", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int whichButton) 
				{
					checkNetwokState();
				}
			})
			.setNegativeButton("종료", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int whichButton) 
				{
				}
			}).show();
			return false;
		}
	}
}
