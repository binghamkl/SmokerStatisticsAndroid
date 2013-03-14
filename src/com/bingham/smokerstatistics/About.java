package com.bingham.smokerstatistics;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

public class About extends Activity {

	TextView mVersion;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		
		mVersion = (TextView)this.findViewById(R.id.Version);
		PackageManager pm = this.getPackageManager();
		try
		{
			PackageInfo pi = pm.getPackageInfo("com.bingham.smokerstatistics", 0);
			mVersion.setText("Version : " + pi.versionName);
		}
		catch(Exception ex)
		{
			
		}
		
	}

}
