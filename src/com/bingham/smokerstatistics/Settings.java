package com.bingham.smokerstatistics;

import java.math.BigDecimal;
import java.util.Calendar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.bingham.smokerstatistics.Utils.TwitterClass;
import com.bingham.smokerstatistics.service.SmokingStatisticsService;


public class Settings extends Activity {

	 private DatePicker mQuitDate;
	 private TimePicker mQuitTime;
	 private SmokerClass mSmoker;
	 private EditText mCigsPerDay;
	 private EditText mCostPerPack;
	 private EditText mLifeSavedPerCig;
	 private String mErrorMessage;
	 private boolean mCancelFinish;
	 private CheckBox mNotifications;
	 private CheckBox mTwidroid;
	 private EditText mFrequency;
	 private CheckBox mMilestone;
	 private EditText mCigFrequency;
	 private EditText mCurrencyFrequency;
	 private EditText mLifeFrequency;
	 private EditText mTwitterUser;
	 private EditText mTwitterPW;
	 private Button mTestTwitter;
	 private LinearLayout mTwitterLayout;
	 private LinearLayout mNotifyLayout;

	 private final int DIALOGALERTSAVEERRORS = 0;
	 private final int DIALOGCANCEL = 1;
	 private final int DIALOGSAVING = 2;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);

		mQuitDate = (DatePicker)findViewById(R.id.DatePicker01);
		mQuitTime = (TimePicker)findViewById(R.id.TimePicker01);
		mCigsPerDay = (EditText)findViewById(R.id.cigarrettesperdayvalue);
		mCostPerPack = (EditText)findViewById(R.id.CostPerPackValue);
		mLifeSavedPerCig = (EditText)findViewById(R.id.LifeSavedPerCigValue);
		mNotifications = (CheckBox)findViewById(R.id.SendNotifications);
		mTwidroid = (CheckBox)findViewById(R.id.Twidroid);
		mFrequency = (EditText)findViewById(R.id.frequencyValue);
		mMilestone = (CheckBox)findViewById(R.id.Milestones);
		mCigFrequency = (EditText)findViewById(R.id.frequencycigsValue);
		mCurrencyFrequency = (EditText)findViewById(R.id.frequencycurrencyValue);
		mLifeFrequency = (EditText)findViewById(R.id.frequencylifeValue);
		mTwitterUser = (EditText)findViewById(R.id.TwitterUserValue);
		mTwitterPW = (EditText)findViewById(R.id.TwitterPWValue);
		mTestTwitter = (Button)findViewById(R.id.CheckTwitter);
		Button resetDateTime = (Button)findViewById(R.id.ResetTime);
		resetDateTime.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Calendar cal = Calendar.getInstance();
				
				mQuitDate.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), null);
				mQuitTime.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
				mQuitTime.setCurrentMinute(cal.get(Calendar.MINUTE));
			}}
		);
		
		mTestTwitter.setOnClickListener(checkTwitterClick);
		
		mSmoker = SmokerClass.createInstance("{default}", this);

		mQuitDate.init(mSmoker.getQuitDateTime().get(Calendar.YEAR), mSmoker.getQuitDateTime().get(Calendar.MONTH), mSmoker.getQuitDateTime().get(Calendar.DATE), null);
		mQuitTime.setCurrentMinute(mSmoker.getQuitDateTime().get(Calendar.MINUTE));
		int testInt = mSmoker.getQuitDateTime().get(Calendar.HOUR_OF_DAY);
		mQuitTime.setCurrentHour(testInt);
		
		mCigsPerDay.setText(Integer.toString(mSmoker.getCigsPerDay()));
		mCostPerPack.setText(mSmoker.getCostPerPack().toString());
		mLifeSavedPerCig.setText(Integer.toString(mSmoker.getMinutesPerCig()));
		mNotifications.setChecked(mSmoker.getNotifications());
		mTwidroid.setChecked(mSmoker.getTwidroid());
		mTwidroid.setOnClickListener(twidroidClickListenter);
		mFrequency.setText(Integer.toString(mSmoker.getFrequency()));
		mMilestone.setChecked(mSmoker.getMilestones());
		mCigFrequency.setText(Integer.toString(mSmoker.getCigsFrequency()));
		mCurrencyFrequency.setText(Integer.toString(mSmoker.getCurrencyFrequency()));
		mLifeFrequency.setText(Integer.toString(mSmoker.getLifeFrequency()));
		mTwitterUser.setText(mSmoker.getTwitterUser());
		mTwitterPW.setText(mSmoker.getTwitterPW());
		mTwitterLayout = (LinearLayout)findViewById(R.id.TwitterLayout);
		mNotifyLayout = (LinearLayout)findViewById(R.id.NotificationLayoutPref);
		
        Button confirmButton = (Button) findViewById(R.id.Save);
        confirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
            	saveValues();
            	if (!mCancelFinish)
            	{
            		setResult(RESULT_OK);
            		finish();
            	}
            }
          
        });
        
        Button cancelButton = (Button)findViewById(R.id.Cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		showCancelDialog();
        		//mHandler.sendEmptyMessage(DIALOGCANCEL);
        	}
        });
        
        final Button cpdDecrement = (Button)findViewById(R.id.cpdDecrement);
        cpdDecrement.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		IncrementValue(mCigsPerDay, -1);
        	}
        });
        
        final Button cpdIncrement = (Button)findViewById(R.id.cpdIncrement);
        cpdIncrement.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		IncrementValue(mCigsPerDay, 1);
        	}
        });
        
        final Button lpcDecrement = (Button)findViewById(R.id.DecrementLife);
        lpcDecrement.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		IncrementValue(mLifeSavedPerCig, -1);
        	}
        });
        
        final Button lpcIncrement = (Button)findViewById(R.id.IncrementLife);
        lpcIncrement.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		IncrementValue(mLifeSavedPerCig, 1);
        	}
        });
        
        mTwidroid.setOnCheckedChangeListener(twidroidCheckChanged);
        mNotifications.setOnCheckedChangeListener(notificationsCheckedChanged);
        setTwitterLayoutVisibility();
        setNotificationLayoutVisibility();
        
        Toast.makeText(this, "Press Save at the bottom.", Toast.LENGTH_SHORT).show();
        
	}
	
	private OnClickListener checkTwitterClick = new OnClickListener()
	{

		@Override
		public void onClick(View v) {
			String user = mTwitterUser.getText().toString().trim();
			String pw = mTwitterPW.getText().toString().trim();
			TwitterClass twitter = new TwitterClass(user, pw);
			try
			{
				if (twitter.checkUsernameAndPass())
				{
					ShowAlert("Twitter Check", "User Name and Password are valid.");
				}
				else
				{
					ShowAlert("Twitter Check", "User Name and Password check failed.");
				}
			}
			catch (Exception ex)
			{
				ShowAlert("Twitter Check", "Check cause an exception \n" + ex.getMessage());
			}

		}
		
	};

	private void ShowAlert(String title, String message)
	{
		new AlertDialog.Builder(this)
		.setTitle(title)
		.setMessage(message)
		.setIcon(R.drawable.icon)
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton )
			{
				//finish();
			}
		})
		.show();
	}
	
	private OnClickListener twidroidClickListenter = new OnClickListener() 
	{

		@Override
		public void onClick(View v) {
			
		}
		
	};
	
	private void IncrementValue(EditText editView, int value) {
		int itemValue = Integer.parseInt(editView.getText().toString());
		itemValue += value;
		if (itemValue < 1)
			itemValue = 1;
		editView.setText(Integer.toString(itemValue));
	}
	
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	saveValues();
    }
	
	private void saveValues() {

		mSmoker.setQuitDateTime(mQuitDate.getYear(), mQuitDate.getMonth(), mQuitDate.getDayOfMonth(), mQuitTime.getCurrentHour(), mQuitTime.getCurrentMinute());
		
		int tempInt = 0;
		boolean success = true;
		String errorDescription = "";
		
		tempInt = parseValue(mCigsPerDay.getText().toString(), tempInt);
		if (tempInt >= 0)
		{
			mSmoker.setCigsPerDay(tempInt);
		}
		else
		{
			success = false;
			errorDescription += "Could not parse the Cigaretts per day field \n";
		}

		BigDecimal tempDecimal = new BigDecimal(this.mCostPerPack.getText().toString());
		if (tempDecimal != null)
		{
			mSmoker.setCostPerPack(tempDecimal);
		}

		tempInt = parseValue(mLifeSavedPerCig.getText().toString(), tempInt);
		if (tempInt >= 0)
		{
			mSmoker.setMinutesPerCig(tempInt);
		}
		else
		{
			success = false;
			errorDescription += "Could not parse min. per Cigarrette\n";
		}
		tempInt = parseValue(mFrequency.getText().toString(), tempInt);
		if (tempInt >= 0)
		{
			mSmoker.setFrequency(tempInt);
		}
		else
		{
			success = false;
			errorDescription += "Could not parse frequency\n";
		}
		mSmoker.setNotifications(mNotifications.isChecked());
		mSmoker.setTwidroid(mTwidroid.isChecked());
		mSmoker.setTwitterUser(mTwitterUser.getText().toString());
		mSmoker.setTwitterPW(mTwitterPW.getText().toString());
		mSmoker.setMilestones(mMilestone.isChecked());
		
		tempInt = parseValue(mCigFrequency.getText().toString(), tempInt);
		if (tempInt >= 0)
		{
			mSmoker.setCigsFrequency(tempInt);
		}
		else
		{
			success = false;
			errorDescription += "Could not parse cig frequency";
		}
		tempInt = parseValue(mCurrencyFrequency.getText().toString(), tempInt);
		if (tempInt >= 0)
		{
			mSmoker.setCurrencyFrequency(tempInt);
		}
		else
		{
			success = false;
			errorDescription += "Could not parse currency frequency.";
		}
		tempInt = parseValue(mLifeFrequency.getText().toString(), tempInt);
		if (tempInt >= 0)
		{
			mSmoker.setLifeFrequency(tempInt);
		}
		else
		{
			success = false;
			errorDescription += "Could nto parse life frequency.";
		}
		
		if (!mSmoker.IsValid())
		{
			errorDescription += mSmoker.getErrorMessage();
			success = false;
		}
		if (success)
		{
			this.mSmoker.Save();
			mCancelFinish = false;
			mHandler.sendEmptyMessage(DIALOGSAVING);
		}
		else
		{
			mHandler.sendEmptyMessage(DIALOGALERTSAVEERRORS);
			mCancelFinish = true;
			this.mErrorMessage = errorDescription;
		}
		
		
	}
	
	private int parseValue(String input, int outValue)
	{
		try
		{
			return Integer.parseInt(input);
		}
		catch (NumberFormatException ex)
		{
			outValue = 0;
			return -1;
		}
	}
	
	private void showAlert()
	{
		new AlertDialog.Builder(this)
		.setTitle("Save Errors")
		.setMessage(mErrorMessage)
		.setIcon(R.drawable.icon)
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton )
			{
				//finish();
			}
		})
		.show();
		
	}
	
	private void showCancelDialog()
	{
		new AlertDialog.Builder(this)
		.setTitle("Cancel Edit")
		.setMessage("You are leaving the settings without saving.  Do you want cancel without saving?\n\nYES exits without saving.\nNO cancels the close.")
		.setIcon(R.drawable.icon)
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton )
			{
				setResult(RESULT_CANCELED);
				finish();
			}
		})
		.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton )
			{
			}
		})
		.show();
	}
	
	private void showSaveToast()
	{
		if (mSmoker.RunService())
		{
			this.startService(new Intent(this, SmokingStatisticsService.class));
		}
		else
		{
			this.stopService(new Intent(this, SmokingStatisticsService.class));
			clearNotifications();
		}
		Toast
		.makeText(this, "Save Settings", Toast.LENGTH_SHORT)
		.show();
	}

	private void clearNotifications()
	{
		NotificationManager mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		mNM.cancelAll();
	}
	
    private Handler mHandler = new Handler() {
    	
    	@Override
    	public void handleMessage(Message msg) {
    		switch (msg.what)
    		{
    		case DIALOGALERTSAVEERRORS:
    			showAlert();
    			break;
    		case DIALOGCANCEL:
    			showCancelDialog();
    			break;
    		case DIALOGSAVING:
    			showSaveToast();
    			break;
    			
    		}
    	}
    };

    private void setTwitterLayoutVisibility()
    {
		if (mTwidroid.isChecked())
		{
			mTwitterLayout.setVisibility(View.VISIBLE);
		}
		else
		{
			mTwitterLayout.setVisibility(View.GONE);
			
		}
    }
    
	private void setNotificationLayoutVisibility() {
		// TODO Auto-generated method stub
		if (mTwidroid.isChecked() || mNotifications.isChecked())
			mNotifyLayout.setVisibility(View.VISIBLE);
		else
			mNotifyLayout.setVisibility(View.GONE);
	}
	
    private OnCheckedChangeListener twidroidCheckChanged = new OnCheckedChangeListener(){

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			setTwitterLayoutVisibility();
			setNotificationLayoutVisibility();
		}
	};

	private OnCheckedChangeListener notificationsCheckedChanged = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			setNotificationLayoutVisibility();
		}

};

}
