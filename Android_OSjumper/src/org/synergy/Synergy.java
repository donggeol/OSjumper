/*
 * synergy -- mouse and keyboard sharing utility
 * Copyright (C) 2010 Shaun Patterson
 * Copyright (C) 2010 The Synergy Project
 * Copyright (C) 2009 The Synergy+ Project
 * Copyright (C) 2002 Chris Schoeneman
 * 
 * This package is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * found in the file COPYING that should have accompanied this file.
 * 
 * This package is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.synergy;

import org.synergy.base.Log;
//import org.synergy.common.screens.PlatformIndependentScreen;
import org.synergy.injection.Injection;
import org.synergy.mouse.ConnectManager;
import org.synergy.mouse.util.PhoneManager;
import org.synergy.phone.OSJumperPhoneActivity;
import org.synergy.phone.OSJumperPhoneManager;
import org.synergy.phone.util.StringLib;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Synergy extends Activity implements OnClickListener
{
	private final static String PROP_clientName = "clientName";
	private final static String PROP_serverHost = "serverHost";
	private final static String PROP_serverPORT = "serverPORT";
	
	private String m_strClientName = null;
	private String m_strServerHost = null;
	private String m_strServerPort = null;
	private String m_strMyPhoneIP = null;
    
	private EditText m_vClientNameEdit;
	private EditText m_vServerHostEdit;
	private EditText m_vServerPortEdit;
	private EditText m_vMyPhoneIPEdit;
	private EditText m_vReceiverPhoneIPEdit;

	private TextView m_vMyPhoneIPText;
	
	private Button m_vVoiceCallBtn;
	private Button m_vCallBtn;
	private Button m_vConnectBtn;
	private Button m_vDisconnectBtn;
	
	private ConnectManager m_objConnectManager;
	private OSJumperPhoneManager m_objOSJumperPhoneManager;
	
	static {
		System.loadLibrary ("synergy-jni");
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        init();
    }
    
    @Override
	public void onConfigurationChanged(Configuration newConfig) 
    {
		super.onConfigurationChanged(newConfig);
		if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE || 
				newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
		{
			m_objConnectManager.resize();
        }
	}

	private void init()
    {
    	m_objConnectManager = new ConnectManager(this);
    	m_objOSJumperPhoneManager = new OSJumperPhoneManager(this);
    	
    	initData();
    	initView();
    	initLog();
    	initPermission();
    }
    
    private void initData()
    {
    	SharedPreferences preferences = getPreferences(MODE_PRIVATE);
    	m_strClientName = preferences.getString(PROP_clientName, null);
    	m_strServerHost = preferences.getString(PROP_serverHost, null);
    	m_strServerPort = preferences.getString(PROP_serverPORT, null);
    	m_strMyPhoneIP = PhoneManager.getDeviceIpAddress();
    }
    
    private void initView()
    {
    	m_vClientNameEdit = (EditText)findViewById (R.id.main_client_name_edit);
    	m_vServerHostEdit = (EditText)findViewById (R.id.main_server_host_edit);
    	m_vServerPortEdit = (EditText)findViewById (R.id.main_server_port_edit);
    	m_vMyPhoneIPEdit = (EditText)findViewById (R.id.main_my_phone_ip_edit);
    	if (m_strClientName != null) {
    		m_vClientNameEdit.setText(m_strClientName);
        }
        if (m_strServerHost != null) {
        	m_vServerHostEdit.setText(m_strServerHost);
        }
        if (m_strServerPort != null) {
        	m_vServerPortEdit.setText(m_strServerPort);
        }
        if (m_strMyPhoneIP != null) {
        	m_vMyPhoneIPEdit.setText(m_strMyPhoneIP);
        }
    	m_vMyPhoneIPText = (TextView)findViewById (R.id.main_my_phone_ip_text);
    	
    	m_vReceiverPhoneIPEdit = (EditText)findViewById (R.id.main_receiver_phone_ip_edit);
    			
        m_vVoiceCallBtn = (Button) findViewById (R.id.main_voice_btn);
        m_vCallBtn = (Button) findViewById (R.id.main_call_btn);
        m_vConnectBtn = (Button) findViewById (R.id.main_connect_btn);
        m_vDisconnectBtn = (Button) findViewById (R.id.main_disconnect_btn);

        m_vMyPhoneIPText.setOnClickListener(this);
        m_vVoiceCallBtn.setOnClickListener(this);
        m_vCallBtn.setOnClickListener(this);
        m_vConnectBtn.setOnClickListener(this);
        m_vDisconnectBtn.setOnClickListener(this);
    }
    
    private void initLog()
    {
    	Log.setLogLevel (Log.Level.INFO);
        Log.debug ("Client starting....");
    }
    
    private void initPermission()
    {
    	try {Injection.setPermissionsForInputDevice();}catch (Exception e){e.printStackTrace();}
    }
    
    private void settingData()
    {
    	String strClientName = m_vClientNameEdit.getText().toString();
    	String strIpAddress = m_vServerHostEdit.getText().toString();
    	String strPortStr = m_vServerPortEdit.getText().toString();
    	
    	SharedPreferences preferences = getPreferences(MODE_PRIVATE);
    	SharedPreferences.Editor preferencesEditor = preferences.edit();
    	preferencesEditor.putString(PROP_clientName, strClientName);
    	preferencesEditor.putString(PROP_serverHost, strIpAddress);
    	preferencesEditor.putString(PROP_serverPORT, strPortStr);
    	preferencesEditor.commit();
    }

	@Override
	public void onClick(View view) 
	{
		switch(view.getId())
		{
			case R.id.main_my_phone_ip_text : 
			{
				Toast.makeText(Synergy.this, "My Phone IP Refresh", Toast.LENGTH_SHORT).show();
				m_strMyPhoneIP = PhoneManager.getDeviceIpAddress();
				if (m_strMyPhoneIP != null) {
					m_vMyPhoneIPEdit.setText(m_strMyPhoneIP);
		        }
			}
			break;
			case R.id.main_connect_btn : 
			{
		    	String strClientName = m_vClientNameEdit.getText().toString();
		    	String strIpAddress = m_vServerHostEdit.getText().toString();
		    	String strPortStr = m_vServerPortEdit.getText().toString();
		    	
		    	try
		    	{
		    		m_objConnectManager.connect(strClientName, strIpAddress, strPortStr);
		    		settingData();
		    	}
		    	catch(Exception e)
		    	{
		    		e.printStackTrace();
		    	}
			}
			break;
			case R.id.main_call_btn : 
			{
				//goToOSJumperPhoneAcitvity();
				String strReceiverIP = m_vReceiverPhoneIPEdit.getText().toString();
				if(false == StringLib.isEmpty(strReceiverIP))
					m_objOSJumperPhoneManager.onClickCallEvent(strReceiverIP);
				else
					Toast.makeText(this, "수신자 IP를 입력해 주시기 바랍니다.", Toast.LENGTH_SHORT).show();
			}
			break;
			case R.id.main_disconnect_btn : m_objConnectManager.disconnect (); break;
			case R.id.main_voice_btn : 
			{
		    	String strIpAddress = m_vServerHostEdit.getText().toString();
				m_objOSJumperPhoneManager.onClickVoiceCallEvent(m_objConnectManager.getIRemoteService(), strIpAddress); 
			}
			break;
		}
	}
	
	@Override
	public void onBackPressed() 
	{
		Intent intent = new Intent("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.HOME");
		startActivity(intent);
	}
	
	public void goToOSJumperPhoneAcitvity()
	{
		Intent objMainIntent = new Intent(this, OSJumperPhoneActivity.class);
    	objMainIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    	objMainIntent.putExtra("OSJUMPER_RECEIVER_IP", "211.189.127.185");
    	startActivity(objMainIntent);
    	overridePendingTransition(0,0);
	}
}
