package com.bingham.smokerstatistics;


import java.text.DecimalFormat;
import java.util.Currency;
import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.ClipboardManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.bingham.smokerstatistics.Utils.VersionChecker;
import com.bingham.smokerstatistics.service.SmokingStatisticsService;

public class SmokerStatistics extends Activity {
	
	private SmokerClass mSmokerClass;
	private TextView mStatResults;
	private TextView mMoneySaved;
	private TextView mLifeSaved;
	private TextView mCigsNotSmoked;
	private TextView mStatHeader;
	private TextView mMilestone;
	private Button mSendTweet;
	private VersionChecker mvc;
	private Button mClipboard;

	private boolean mPaused = false;
	private Timer mTimer;
	
    private static final int ACTIVITY_SETTINGS=0;
    private static final int ABOUT_SETTINGS = 1;
    private static final int SETTINGS_ID = Menu.FIRST;
	private static final int ABOUT_ID = Menu.FIRST + 1;

	private final int MSG_UPDATESTATS = 0;
	private final int MSG_SENDTWEET = 1;
	private final int MSG_VERSIONUPGRADE = 2;
	private final int MSG_FETCHMARKETPLACE = 3;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		mSmokerClass = SmokerClass.createInstance("{default}", this);
		mStatResults = (TextView)findViewById(R.id.TextDifferenceValue);
		mMoneySaved = (TextView)findViewById(R.id.moneysavedvalue);
		mLifeSaved = (TextView)findViewById(R.id.lifeSavedValue);
		mCigsNotSmoked = (TextView)findViewById(R.id.cigsNotSmokedValue);
		mStatHeader = (TextView)findViewById(R.id.TextStatHeader);
		mMilestone = (TextView)findViewById(R.id.milestone);
		mClipboard = (Button)findViewById(R.id.ClipboardButton);
		
		TextView currencySymbol = (TextView)findViewById(R.id.Currency);
		Currency currency = Currency.getInstance(this.getResources().getConfiguration().locale);
		currencySymbol.setText(currency.getSymbol());
		
		if (mSmokerClass.getIsNew())
		{
			showSettings();
		}
		if (mSmokerClass.RunService())
		{
			this.startService(new Intent(this, SmokingStatisticsService.class));
		}
		else
		{
			this.stopService(new Intent(this, SmokingStatisticsService.class));
		}
		NotificationManager mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		mNM.cancelAll();

		mTimer = new Timer();
		mTimer.schedule(new UpdateStatsTimer(), 0);
		
		mSendTweet = (Button)findViewById(R.id.SendTweet);
        mSendTweet.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
            	mHandler.sendEmptyMessage(MSG_SENDTWEET);
            }
          
        });
        
        mClipboard.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) 
        	{
        		PutTextOnClipBoard();
        	}
        });
        
        Timer timerUpdates = new Timer();
        timerUpdates.schedule(new CheckForUpdates(), 400);
        
        Toast.makeText(this, "Press menu to Edit Settings", Toast.LENGTH_SHORT).show();
        
    }
    
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mPaused = false;
		mTimer.schedule(new UpdateStatsTimer(), 0);
		NotificationManager mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		mNM.cancelAll();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mPaused = true;
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		mPaused = false;
		mTimer.schedule(new UpdateStatsTimer(), 0);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		mPaused = false;
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

    class CheckForUpdates extends TimerTask {
    	public void run() {
            //CheckForUpdates();
    	}
    }

    private void CheckForUpdates()
    {

    	mvc = new VersionChecker();
    	if (mvc.upgradeAvailable(this.getPackageManager()))
    	{
    		mHandler.sendEmptyMessage(MSG_VERSIONUPGRADE);
    	}
    }
    
    private void showUpgrade()
    {
    	String message = "Version " + mvc.getVersion() + " of this program is available on the marketplace.\n" + 
    						"YES to go upgrade, No to upgrade at a later time.\n\n" + 
    						mvc.getUpgradedescription();
    	
		new AlertDialog.Builder(this)
		.setTitle("Version Upgrade")
		.setMessage(message)
		.setIcon(R.drawable.icon)
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton )
			{
				mHandler.sendEmptyMessage(MSG_FETCHMARKETPLACE);
			}
		})
		.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton )
			{
			}
		})
		.show();
    }
    
    private void getUpgradeFromMarketplace()
    {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse("market://search?q=pname:" + mvc.getMarketPlacePoint()));
		startActivity(i);
    }
    
    class UpdateStatsTimer extends TimerTask {
    	public void run() {
    		mHandler.sendEmptyMessage(MSG_UPDATESTATS);
    		if (!mPaused)
    			mTimer.schedule(new UpdateStatsTimer(), 1000);
    	}
    }
   
    public void updateStats() {
    	this.mSmokerClass.updateStatistics();

    	if (mSmokerClass.getHasAlreadyQuit())
    	{
    		mStatHeader.setText(R.string.smokingtime);
    	}
    	else
    	{
    		mStatHeader.setText(R.string.quittime);
    	}
    	String result = this.mSmokerClass.getStatDifference().dateDifference();
    	mStatResults.setText(result);
    	DecimalFormat decimalFormat = new DecimalFormat("###,###,##0.00");
    	mLifeSaved.setText(mSmokerClass.getLifeSavedString());
    	mCigsNotSmoked.setText(Long.toString(mSmokerClass.getTotalcigs()));
    	
    	mMilestone.setText(mSmokerClass.getCurrentMilestone());
    	
    	this.mMoneySaved.setText(decimalFormat.format(mSmokerClass.getMoneySaved()));
    	
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	MenuItem item = menu.add(0, SETTINGS_ID, 0, R.string.settings_menu);
    	item.setIcon(android.R.drawable.ic_menu_preferences);
       	item = menu.add(0, ABOUT_ID, 0, R.string.about_menu);
       	item.setIcon(android.R.drawable.ic_menu_info_details);
        return true;
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
    	
    	switch(item.getItemId()) {
    	case SETTINGS_ID:
    		showSettings();
    		return true;
    	case ABOUT_ID:
    		showAbout();
    		return true;
    	}
    	
    	return super.onMenuItemSelected(featureId, item);
    }
    
    private void showAbout() {
    	Intent i = new Intent(this, About.class);
    	startActivityForResult(i, ABOUT_SETTINGS);
    }
    
    private void showSettings() {
        Intent i = new Intent(this, Settings.class);
        startActivityForResult(i, ACTIVITY_SETTINGS);
    }

    public void PutTextOnClipBoard()
    {
        ClipboardManager clipboard = (ClipboardManager)this.getSystemService(CLIPBOARD_SERVICE);
    	String msg = mSmokerClass.buildShortStatList();
		clipboard.setText(msg);
		Toast
		.makeText(this, "Statistics saved to clipboard", Toast.LENGTH_SHORT)
		.show();
		
    }
    
    private void sendTweet() {
    	if (mSmokerClass != null) 
    	{
	    	String tweetmsg = mSmokerClass.buildShortStatList();
			
	    	if (mSmokerClass.getTwitterUser().length() > 0 && mSmokerClass.getTwitterPW().length() > 0)
	    	{
	    		Intent i = new Intent(SmokerStatistics.this, tweetActivity.class);
	    		i.putExtra(tweetActivity.EXTRA_TWEET, tweetmsg);
	    		i.putExtra(tweetActivity.EXTRA_USER, mSmokerClass.getTwitterUser());
	    		i.putExtra(tweetActivity.EXTRA_PW, mSmokerClass.getTwitterPW());
	    		startActivity(i);
	    	}
	    	else
	    	{
		    	Intent intent = new Intent("com.twidroid.SendTweet");
		    	intent.putExtra("com.twidroid.extra.MESSAGE", tweetmsg);
		    	intent.putExtra(Intent.EXTRA_TEXT, tweetmsg);
		    	try {
		    		startActivity(intent);
		    	} catch (ActivityNotFoundException e) {
			    	/* Handle Exception if Twidroid is not installed */
		    		new AlertDialog.Builder(this)
		    		.setTitle("Tweet Error")
		    		.setMessage("tweet not sent.  Twidroid is not installed and no password userid entered for twitter.")
		    		.setIcon(R.drawable.icon)
		    		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		    			public void onClick(DialogInterface dialog, int whichButton )
		    			{
		    				//finish();
		    			}
		    		})
		    		.show();
		
		    	}
	    	}
    	}
    }
    
    private Handler mHandler = new Handler() {
    	
    	@Override
    	public void handleMessage(Message msg) {
    		switch (msg.what)
    		{
    		case MSG_UPDATESTATS:
    			updateStats();
    			break;
    		case MSG_SENDTWEET:
    			sendTweet();
    			break;
    		case MSG_VERSIONUPGRADE:
    			showUpgrade();
    			break;
    		case MSG_FETCHMARKETPLACE:
    			getUpgradeFromMarketplace();
    			break;
    		}
    	}
    };
 
    public View makeView() {
        TextView t = new TextView(this);
        t.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        t.setTextSize(36);
        return t;
    }

    
}