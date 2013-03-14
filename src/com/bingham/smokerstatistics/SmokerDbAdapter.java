package com.bingham.smokerstatistics;

import java.math.BigDecimal;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bingham.smokerstatistics.data.DatabaseHelper;

public class SmokerDbAdapter {

    public static final String KEY_QUITDATE = "quitdate";
    public static final String KEY_QUITTIME = "quittime";
    public static final String KEY_ROWID = "_id";
    public static final String KEY_SMOKERNAME = "smokername";
    public static final String KEY_CIGARETTESPERDAY = "cigsperday";
    public static final String KEY_COSTPERPACK = "costperpack";
    public static final String KEY_MINUTESPERCIG = "minpercig";
    public static final String KEY_NOTIFICATIONS = "notification";
    public static final String KEY_TWIDROID = "twidroid";
    public static final String KEY_FREQUENCY = "frequency";
    public static final String KEY_CIGFREQUENCY = "cigfrequency";
    public static final String KEY_CURRENCYFREQUENCY = "currencyfrequency";
    public static final String KEY_LIFEFREQUENCY = "lifefrequency";
    public static final String KEY_MILESTONES = "milestones";
    public static final String KEY_TWITTERUSER = "twitteruser";
    public static final String KEY_TWITTERPW = "twitterpw";
    
    public static final String[] COLUMNARRAY = {KEY_ROWID, KEY_QUITDATE, KEY_QUITTIME, KEY_SMOKERNAME, 
    											KEY_CIGARETTESPERDAY, KEY_COSTPERPACK, KEY_MINUTESPERCIG, 
    											KEY_NOTIFICATIONS, KEY_TWIDROID, KEY_FREQUENCY, 
    											KEY_CIGFREQUENCY, KEY_CURRENCYFREQUENCY, KEY_LIFEFREQUENCY, 
    											KEY_MILESTONES, KEY_TWITTERUSER, KEY_TWITTERPW};
    
    private static final String TAG = "SmokerDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    
    
    private static final String DATABASE_TABLE = "settings";

    private final Context mCtx;

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public SmokerDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public SmokerDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
    
    public void close() {
        mDbHelper.close();
    }


    /**
     * Create a new note using the title and body provided. If the note is
     * successfully created return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     * 
     * @param title the title of the note
     * @param body the body of the note
     * @return rowId or -1 if failed
     */
    public long createSetting(String quitdate, String quittime, String smokerName, int cigsPerDay, BigDecimal costPerPack,
    							int minPerCig, boolean notifications, boolean twidroid, int frequency, int cigfrequency, int currencyfrequency, 
    							int lifefrequency, boolean milestones, String twitteruser, String twitterpw) {
        ContentValues initialValues = new ContentValues();
        
        initialValues.put(KEY_QUITDATE, quitdate);
        initialValues.put(KEY_QUITTIME, quittime);
        initialValues.put(KEY_SMOKERNAME, smokerName);
        initialValues.put(KEY_CIGARETTESPERDAY, cigsPerDay);
        String costPerPackValue = "";
        if (costPerPack != null) {
        	costPerPackValue = costPerPack.toString();
        }
        initialValues.put(KEY_COSTPERPACK, costPerPackValue);
        initialValues.put(KEY_MINUTESPERCIG, minPerCig);
        initialValues.put(KEY_NOTIFICATIONS, notifications);
        initialValues.put(KEY_TWIDROID, twidroid);
        initialValues.put(KEY_FREQUENCY, frequency);
        initialValues.put(KEY_CIGFREQUENCY, cigfrequency);
        initialValues.put(KEY_CURRENCYFREQUENCY, currencyfrequency);
        initialValues.put(KEY_LIFEFREQUENCY, lifefrequency);
        initialValues.put(KEY_MILESTONES, milestones);
        initialValues.put(KEY_TWITTERUSER, twitteruser);
        initialValues.put(KEY_TWITTERPW, twitterpw);
        
        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Update the note using the details provided. The note to be updated is
     * specified using the rowId, and it is altered to use the title and body
     * values passed in
     * 
     */
    public boolean updateSetting(long rowId, String quitdate, String quittime, String smokerName, int cigsPerDay, BigDecimal costPerPack, 
    								int minPerCig, boolean notifications, boolean twidroid, int frequency, int cigfrequency, int currencyfrequency, 
        							int lifefrequency, boolean milestones, String twitterUser, String twitterPW) {
        ContentValues args = new ContentValues();
        args.put(KEY_QUITDATE, quitdate);
        args.put(KEY_QUITTIME, quittime);
        args.put(KEY_SMOKERNAME, smokerName);
        args.put(KEY_CIGARETTESPERDAY, cigsPerDay);
        args.put(KEY_COSTPERPACK, costPerPack.toString());
        args.put(KEY_MINUTESPERCIG, minPerCig);
        args.put(KEY_NOTIFICATIONS, notifications);
        args.put(KEY_TWIDROID, twidroid);
        args.put(KEY_FREQUENCY, frequency);
        args.put(KEY_CIGFREQUENCY, cigfrequency);
        args.put(KEY_CURRENCYFREQUENCY, currencyfrequency);
        args.put(KEY_LIFEFREQUENCY, lifefrequency);
        args.put(KEY_MILESTONES, milestones);
        args.put(KEY_TWITTERUSER, twitterUser);
        args.put(KEY_TWITTERPW, twitterPW);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Delete the note with the given rowId
     * 
     * @param rowId id of note to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteSetting(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Return a Cursor over the list of all notes in the database
     * 
     * @return Cursor over all notes
     */
    public Cursor fetchAllSettings() {

        return mDb.query(DATABASE_TABLE, COLUMNARRAY, null, null, null, null, null);
    }

    /**
     * Return a Cursor positioned at the note that matches the given rowId
     * 
     * @param rowId id of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor fetchSetting(long rowId) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, COLUMNARRAY, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
    
    public Cursor fetchSettingByName(String smokerName) throws SQLException {

    	
    	try
    	{
	    	Cursor mCursor = mDb.query(true, DATABASE_TABLE, COLUMNARRAY, 
	    								KEY_SMOKERNAME + "='" + smokerName + "'", null, null, null, null, null);
	    	if (mCursor != null) {
	    		mCursor.moveToFirst();
	    	}
	    	return mCursor;
    	}
    	catch (Exception ex)
    	{
    		Log.e(TAG, ex.getMessage());
    		return null;
    	}
    }

    

}
