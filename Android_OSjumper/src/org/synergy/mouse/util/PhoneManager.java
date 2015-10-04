package org.synergy.mouse.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneManager 
{
	/**
	 * IMEI 
	 */
	public static String getDeviceID (Context context)
	{
		String strDeviceID = "";

		try
		{
			TelephonyManager manager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
			strDeviceID = manager.getDeviceId();

			if (isEmpty(strDeviceID))
			{
				strDeviceID = getGalTabID(context);
			}
		}
		catch (Exception e)
		{
			strDeviceID = getGalTabID(context);
		}
		return strDeviceID;
	}
	
	/**
	 *  갤럭시 탭등 IMEI가 없는 기기에 대한 고유 ID 생성  wifi를 통한 맥어드래스로 생성
	 * @param context
	 * @return
	 */
	public static String getGalTabID (Context context)
	{
		String strGalTabID = "";
		try
		{
			WifiManager wifiManger = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
			WifiInfo wifiInfo = wifiManger.getConnectionInfo();

			strGalTabID = wifiInfo.getMacAddress();

			if (isEmpty(strGalTabID))
			{
				strGalTabID = "0000";
			}
			else
			{
				String[] strTemp = strGalTabID.split(":");
				strGalTabID = "";
				for (int nNum = 0; nNum < strTemp.length; nNum++)
				{
					long nValue = Long.parseLong(strTemp[nNum], 16); // 16진수 변환
					strGalTabID = strGalTabID + Long.toString(nValue);
				}
				strGalTabID.replaceAll(".", "");

				int nNum = 0;

				while (15 != strGalTabID.length())
				{
					if (nNum < strGalTabID.length())
					{
						if (strGalTabID.length() < 15)
						{
							strGalTabID = strGalTabID + strGalTabID.charAt(nNum);
						}
						else
						{
							strGalTabID = strGalTabID.substring(0,15);
						}
					}

					if (nNum == 20)
					{
						strGalTabID = "0000";
						break;
					}
				}
			}
		}
		catch (Exception e)
		{
			strGalTabID = "0000";
		}
		return strGalTabID;
	}
	
	public static boolean isEmpty (String strValue)
	{
		return null == strValue || 0 == strValue.length() ? true : false;
	}
	
	/**
	 * Get ip address of the device
	 */
	public static String getDeviceIpAddress() 
	{
		String strIP = "127.118.122.163";
		
		try {
			for (Enumeration<NetworkInterface> enumeration = NetworkInterface
					.getNetworkInterfaces(); enumeration.hasMoreElements();) {
				NetworkInterface networkInterface = enumeration.nextElement();
				for (Enumeration<InetAddress> enumerationIpAddr = networkInterface
						.getInetAddresses(); enumerationIpAddr
						.hasMoreElements();) {
					InetAddress inetAddress = enumerationIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()
							&& inetAddress.getAddress().length == 4) {
						strIP = inetAddress.getHostAddress();
					}
				}
			}
		} catch (SocketException e) {
			Log.e("ERROR:", e.toString());
		}
		
		return strIP;
	}
}
