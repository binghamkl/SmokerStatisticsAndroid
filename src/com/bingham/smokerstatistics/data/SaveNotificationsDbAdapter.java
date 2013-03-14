package com.bingham.smokerstatistics.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SaveNotificationsDbAdapter {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_TOTALDAYS =  "totaldays";
    public static final String KEY_TOTALCIGS = "totalcigs";
    public static final String KEY_TOTALCURRENCY = "totalcurrency";
    public static final String KEY_TOTALLIFE = "totallife";
    public static final String KEY_MESSAGE = "lastmessage";
    
    public static final String[] COLUMNARRAY = {KEY_ROWID, KEY_TOTALDAYS, KEY_TOTALCIGS, KEY_TOTALCURRENCY, 
    											KEY_TOTALLIFE, KEY_MESSAGE};
    
    private static final String TAG = "SaveNotificationsDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    
    private static final String DATABASE_TABLE = "notifications";

    private final Context mCtx;

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public SaveNotificationsDbAdapter(Context ctx) {
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
    public SaveNotificationsDbAdapter open() throws SQLException {
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
    
    public long createSetting(int id, int totaldays, long totalcigs, long totalcurrency, int totallife, 
    					String lastMessage) 
    {
        ContentValues initialValues = new ContentValues();
        
        initialValues.put(KEY_ROWID, id);
        initialValues.put(KEY_TOTALDAYS, totaldays);
        initialValues.put(KEY_TOTALCIGS, totalcigs);
        initialValues.put(KEY_TOTALCURRENCY, totalcurrency);
        initialValues.put(KEY_TOTALLIFE, totallife);
        initialValues.put(KEY_MESSAGE, lastMessage);
        
        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Update the note using the details provided. The note to be updated is
     * specified using the rowId, and it is altered to use the title and body
     * values passed in
     * 
     */
    public boolean updateSetting(long rowId, int totaldays, long totalcigs, long totalcurrency, 
    				int totallife, String lastMessage) {
        ContentValues args = new ContentValues();
        args.put(KEY_TOTALDAYS, totaldays);
        args.put(KEY_TOTALCIGS, totalcigs);
        args.put(KEY_TOTALCURRENCY, totalcurrency);
        args.put(KEY_TOTALLIFE, totallife);
        args.put(KEY_MESSAGE, lastMessage);

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
    

	
}
