package com.bingham.smokerstatistics.service;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;

import com.bingham.smokerstatistics.R;
import com.bingham.smokerstatistics.SmokerClass;
import com.bingham.smokerstatistics.SmokerStatistics;
import com.bingham.smokerstatistics.Utils.TwitterClass;
import com.bingham.smokerstatistics.data.SaveNotificationsDbAdapter;

public class SmokingStatisticsService extends Service {

	private SmokerClass mSmokerClass;
	private NotificationManager mNM;
	private int mTotalDays = -1;
	private long mTotalCigs = -1;
	private long mTotalCurrency = -1;
	private int mTotalLife = -1;
	private String mMilestone = "";
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mSmokerClass = SmokerClass.createInstance("{default}", this);
		
		mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

		SaveNotificationsDbAdapter saveNotify = new SaveNotificationsDbAdapter(this);
		saveNotify.open();
		
		Cursor setting = saveNotify.fetchSetting(1);
		if (setting != null && setting.getCount() > 0)
		{
			mTotalDays = setting.getInt(setting.getColumnIndexOrThrow(SaveNotificationsDbAdapter.KEY_TOTALDAYS));
			mTotalCigs = setting.getLong(setting.getColumnIndexOrThrow(SaveNotificationsDbAdapter.KEY_TOTALCIGS));
			mTotalCurrency = setting.getLong(setting.getColumnIndexOrThrow(SaveNotificationsDbAdapter.KEY_TOTALCURRENCY));
			mTotalLife = setting.getInt(setting.getColumnIndexOrThrow(SaveNotificationsDbAdapter.KEY_TOTALLIFE));
			mMilestone = setting.getString(setting.getColumnIndexOrThrow(SaveNotificationsDbAdapter.KEY_MESSAGE));
		}
		setting.close();
		saveNotify.close();
		
		Timer timer = new Timer();
		timer.schedule(new UpdateStatsTimer(), 0, 1000 * 60 * 30);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	public void SaveSettings()
	{
		SaveNotificationsDbAdapter saveNotify = new SaveNotificationsDbAdapter(this);
		saveNotify.open();
		long newId;
		Cursor setting = saveNotify.fetchSetting(1);
		if (setting != null && setting.getCount() > 0)
		{
			saveNotify.updateSetting(1, mTotalDays, mTotalCigs, mTotalCurrency, mTotalLife, mMilestone);
		}
		else
		{
			newId =  saveNotify.createSetting(1, mTotalDays, mTotalCigs, mTotalCurrency, mTotalLife, mMilestone);
		}
		setting.close();
		saveNotify.close();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

    class UpdateStatsTimer extends TimerTask {
    	public void run() {

    		
    		mSmokerClass.updateStatistics();
    		int modNow = 0;
    		int modPrev = 0;
    		long modLongPrev = 0;
    		long modLongNow = 0;
    		
    		int currentDays = mSmokerClass.getStatDifference().getTotalDays();
    		int freq = mSmokerClass.getFrequency();
    		if (freq > 0)
    		{
	    		modNow = (int)(currentDays / freq);
	    		modPrev = (int)(mTotalDays / freq);
	    		if (modPrev != modNow)
	    		{
	    			mTotalDays = currentDays;
	    			SendNotifications(mSmokerClass.getShortDayStatistics(), false);
	    		}
    		}
    		
    		long currentCigs = mSmokerClass.getTotalcigs();
    		freq = mSmokerClass.getCigsFrequency();
    		if (freq > 0)
    		{
    			modLongPrev = (long)(mTotalCigs / mSmokerClass.getCigsFrequency());
    			modLongNow = (long)(currentCigs / mSmokerClass.getCigsFrequency());
    			if (modLongPrev != modLongNow)
    			{
    				mTotalCigs = currentCigs;
    				SendNotifications(mSmokerClass.getShortCigStatistics(), false);
    			}
    		}
    		
    		long currentCurrency = (long)mSmokerClass.getMoneySaved();
    		freq = mSmokerClass.getCurrencyFrequency();
    		if (freq > 0)
    		{
    			modPrev = (int)(mTotalCurrency / freq);
    			modNow = (int)(currentCurrency / freq);
    			if (modNow != modPrev)
    			{
    				mTotalCurrency = currentCurrency;
    				SendNotifications(mSmokerClass.getShortCurrencyStats(), false);
    			}
    		}
  
    		int currentLife = mSmokerClass.getLifeTotalDays();
    		freq = mSmokerClass.getLifeFrequency();
    		if (freq > 0)
    		{
    			modPrev = (int)(mTotalLife / freq);
    			modNow = (int)(currentLife / freq);
    			if (modNow != modPrev)
    			{
    				mTotalLife = currentLife;
    				SendNotifications(mSmokerClass.getShortLifeStats(), false);
    			}
    		}
    
    		if (mSmokerClass.getMilestones())
    		{
    			String currentMilestone = mSmokerClass.getCurrentMilestone();
    			if (!currentMilestone.equalsIgnoreCase(mMilestone))
    			{
    				mMilestone = currentMilestone;
    				showNotification(mMilestone);
    				if (mSmokerClass.getTwidroid())
    				{
    					//mTweet = mMilestone;
    				}
    			}
    				
    		}
    		
    		SaveSettings();
    		
    	}
    }

    private void SendNotifications(String message, boolean messageOnly)
    {
    	String sendMessage = message;
		if (mSmokerClass.getNotifications())
		{
			if (!messageOnly)
				sendMessage = "You " + message;
			showNotification(sendMessage);
		}
		if (mSmokerClass.getTwidroid())
		{
			if (!messageOnly)
				sendMessage = "I " + message;
			SendTweet(sendMessage);
			
			
		}
    }
    
    private void SendTweet(String tweet)
    {
    	try
    	{
        	if (mSmokerClass != null)
        	{
    			if (mSmokerClass.getTwitterUser().length() > 0 && mSmokerClass.getTwitterPW().length() > 0)
    			{
    				TwitterClass twitter = new TwitterClass(mSmokerClass.getTwitterUser(), mSmokerClass.getTwitterPW());
    				try
    				{
    					if (!twitter.sendStatusUpdate(tweet))
    					{
    						showNotification("Error sending tweet\n" + Integer.toString(twitter.getStatusCode()) + "\n" + twitter.getStatusText());
    					}
    				}
    				catch (Exception ex)
    				{
    					showNotification("Error sending tweet\n" + ex.getMessage());
    				}
    			}
        	}
    	
    	}
    	catch (Exception ex)
    	{
    		
    	}
    	finally
    	{
    	
    	}
    }
    
    /**
     * Show a notification while this service is running.
     */
    private void showNotification(String text) {

        // Set the icon, scrolling text and timestamp
        Notification notification = new Notification(R.drawable.icon, text,
                System.currentTimeMillis());

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, SmokerStatistics.class), 0);

        // Set the info for the views that show in the notification panel.
        notification.setLatestEventInfo(this, "Smoking Statistics",
                       text, contentIntent);

        // Send the notification.
        // We use a string id because it is a unique number.  We use it later to cancel.
        mNM.notify(R.string.alert, notification);
    }


}
