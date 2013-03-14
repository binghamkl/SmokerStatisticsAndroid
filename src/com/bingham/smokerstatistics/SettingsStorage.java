package com.bingham.smokerstatistics;

import java.text.DateFormat;
import java.util.Date;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class SettingsStorage extends Activity {

	private SharedPreferences settings;
	private Date mquittime;
	private Date mquitdate;
	
	private static final String QUITDATEKEY = "QuiteDate";
	private static final String QUITTIMEKEY = "QuiteTime";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		settings = PreferenceManager.getDefaultSharedPreferences(this);
		setSettings();
	}

	public Date getQuitTime() {
		return mquittime;
	}

	public void setQuitTime(Date quittime) {
		mquittime = quittime;
	}

	public Date getQuitdate() {
		return mquitdate;
	}

	public void setQuitdate(Date quitdate) {
		this.mquitdate = quitdate;
	}
	
	private void setSettings() {
		Long timevalue = settings.getLong(QUITTIMEKEY, 0);
		mquittime.setTime(timevalue);
		
		String datevalue = settings.getString(QUITDATEKEY, "");
		DateFormat df = DateFormat.getInstance();
		try {
			mquitdate = df.parse(datevalue);
		}
		catch(Exception ex) {
			mquitdate = new Date();
		}
		finally {
		
		}
	}
	
	public void Save() {
		
		Editor editor = settings.edit();
		editor.putLong(QUITTIMEKEY, mquittime.getTime());
		editor.putString(QUITDATEKEY, mquitdate.toString());
		editor.commit();
		
	}
	
}
