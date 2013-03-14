package com.bingham.smokerstatistics.Utils;

import httphelper.HttpHandler;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.util.Log;


public class TwitterClass {

	private static final int STATUS_OK = 200;
	private static final int STATUS_NOT_MODIFIED = 304;
	private static final int STATUS_BAD_REQUEST = 400;
	private static final int STATUS_NOT_AUTHORIZED = 401;
	private static final int STATUS_FORBIDDEN = 403;
	private static final int STATUS_NOT_FOUND = 404;
	private static final int STATUS_INTERNAL_SERVER_ERROR = 500;
	private static final int STATUS_BAD_GATEWAY = 502;
	private static final int STATUS_SERVICE_UNAVAILABLE = 503;
	private static final int STATUS_UNKNOWN = 1000;
	
	private final String TAG = "TwitterClass";
	
	private String mUser;
	private String mPassword;
	private int mStatusCode;
	private String mStatusText;

	public TwitterClass(String user, String password) {
		super();
		mUser = user;
		mPassword = password;
	}

	public void setStatusCode(int mStatusCode) {
		this.mStatusCode = mStatusCode;
	}

	public int getStatusCode() {
		return mStatusCode;
	}

	public String getStatusText()
	{
		String returnValue = "";
		switch (mStatusCode)
		{
		case STATUS_OK:
			returnValue = "OK";
			break;
		case STATUS_NOT_MODIFIED:
			returnValue = "Not Modified: there was no data to return";
			break;
		case STATUS_BAD_REQUEST:
			returnValue = "Bad Request your request was invalid";
			break;
		case STATUS_NOT_AUTHORIZED:
			returnValue = "Not Authorized.  Your authentication was not valid or sufficient.";
			break;
		case STATUS_FORBIDDEN:
			returnValue = "The request is forbidden.";
			break;
		case STATUS_NOT_FOUND:
			returnValue = "Invalid URI, request was not found";
			break;
		case STATUS_INTERNAL_SERVER_ERROR:
			returnValue = "There is an internal twitter server error.";
			break;
		case STATUS_BAD_GATEWAY:
			returnValue = "There was a bad gateway error.  Twitter may be down, or doing upgrades.";
			break;
		case STATUS_SERVICE_UNAVAILABLE:
			returnValue = "Twitter is unavailable.";
			break;
		case STATUS_UNKNOWN:
			returnValue = mStatusText;
			break;
		}
		return returnValue;
	}
	
	public boolean sendStatusUpdate(String update)
	{
		String url = "http://twitter.com/statuses/update.json";

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("status", update));

		try
		{
			HttpResponse response = HttpHandler.Post(url, nvps, mUser, mPassword);
	    	if (response != null)
	    	{
	    		mStatusCode = response.getStatusLine().getStatusCode();
	    		return (mStatusCode == STATUS_OK);
	    	}
		}
		catch (Exception ex)
		{
			Log.e(TAG, ex.getMessage());
			mStatusText = ex.getMessage();
			mStatusCode = STATUS_UNKNOWN;
			return false;
		}
		mStatusText = "Unknown";
		mStatusCode = STATUS_UNKNOWN;
		return false;
		
	
	}
	
    public boolean checkUsernameAndPass() throws IOException{
        List <NameValuePair> nvps = new ArrayList <NameValuePair>();
        HttpResponse response = null;
        try {
			response = HttpHandler.Post("http://twitter.com/statuses/friends_timeline.json?count=0", 
			                nvps, mUser, mPassword);
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			Log.e(TAG, ex.getMessage());
		}
        if (response != null)
        {
        	if (response.getStatusLine().getStatusCode() == 200)
        		return true;
        }
        return false;

    }
    
}



