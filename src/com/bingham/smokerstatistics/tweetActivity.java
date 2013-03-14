package com.bingham.smokerstatistics;

import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.bingham.smokerstatistics.Utils.TwitterClass;

public class tweetActivity extends Activity {

	public static final String EXTRA_TWEET = "tweet";
	public static final String EXTRA_USER = "user";
	public static final String EXTRA_PW = "password";
	private static final String LENGTH_TEXT = "Message Length is %d with %d characters left";

	
	private final int DIALOG_SENDTWEET = 0;
	private EditText mTweet;
	private TextView mLengthText;
	private Button mSend;
	private Button mCancel;
	private String mMessage;
	private String mUser;
	private String mPW;
	private ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
		setContentView(R.layout.tweet);
        getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, 
                android.R.drawable.ic_dialog_alert);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        
		Bundle extras = this.getIntent().getExtras();
		if (extras != null)
		{
			mMessage = extras.getString(EXTRA_TWEET);
			mUser = extras.getString(EXTRA_USER);
			mPW = extras.getString(EXTRA_PW);
		}
		
		mTweet = (EditText)findViewById(R.id.tweetValue);
		mLengthText = (TextView)findViewById(R.id.tweetLength);
		mSend = (Button)findViewById(R.id.Send);
		mCancel = (Button)findViewById(R.id.Cancel);

		mTweet.setOnKeyListener(tweetKeyListener);
		mTweet.setText(mMessage);
		setLengthText();
		
		mSend.setOnClickListener(sendOnClick);
		mCancel.setOnClickListener(sendOnClick);
		
	}

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) 
        {
        case DIALOG_SENDTWEET:
            	progressDialog = new ProgressDialog(this);
            	progressDialog.setMessage("Please wait while sending tweet");
            	progressDialog.setIndeterminate(true);
            	progressDialog.setCancelable(false);
                return progressDialog;
            
        }
        return null;
    }

	private void setLengthText()
	{
		int totalChars = mTweet.getText().toString().length();
		int charactersLeft = 140 - totalChars;
		String lengthText = String.format(LENGTH_TEXT, totalChars, charactersLeft);
		mLengthText.setText( lengthText );
	}
	
	private OnKeyListener tweetKeyListener = new OnKeyListener(){
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if ( event.getAction() == KeyEvent.ACTION_UP )
			{
				setLengthText();
			}
			return false;
		}
	};

	private class tweetTask extends TimerTask{

		@Override
		public void run() {
			sendTweet();
		}
	};
	
	private void sendTweet()
	{
		String tweet = mTweet.getText().toString().trim();
		TwitterClass twitter = new TwitterClass(mUser, mPW);
		if (!twitter.sendStatusUpdate(tweet))
		{
			progressDialog.dismiss();
			AlertDialog.Builder builder = new AlertDialog.Builder(tweetActivity.this);
			builder 
				.setTitle("Tweet failed")
				.setMessage("The tweet could not be sent.\n\n" + Integer.toString(twitter.getStatusCode()) + "\n" + twitter.getStatusText())
				.setPositiveButton("OK", new Dialog.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				})
				.show();
		}
		else
		{
			progressDialog.dismiss();
			finish();
		}
	}
	
	private OnClickListener sendOnClick = new OnClickListener(){

		@Override
		public void onClick(View v) {
			
			if (v == mSend)
			{
				Timer timer = new Timer();
				timer.schedule(new tweetTask(), 0);
				showDialog(DIALOG_SENDTWEET);
			}
			if (v == mCancel)
			{
				finish();
			}
		}
	};
	
	
}
