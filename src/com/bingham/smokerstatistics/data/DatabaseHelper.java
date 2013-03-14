package com.bingham.smokerstatistics.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "data";
    private static final int DATABASE_VERSION = 31;
    private static final String TAG = "DatabaseHelper";

    /**
     * Database creation sql statement
     */
    private static final String SETTINGS_CREATE =
            "create table settings (_id integer primary key autoincrement, "
                    + "smokername varchar(30) not null, quitdate date not null, quittime text not null, "
                    + "cigsperday int not null, costperpack varchar(30) not null, minpercig int not null, "
                    + "notification BOOLEAN not null, twidroid BOOLEAN not null, "
                    + "frequency int not null, cigfrequency int not null, currencyfrequency int not null, "
                    + "lifefrequency int not null, milestones BOOLEAN not null, "
                    + "twitteruser varchar(30), twitterpw varchar(30) );";
    
    private static String ALTER_SETTINGS_ADD_TWITTERUSER = "ALTER TABLE settings ADD COLUMN twitteruser varchar(30); ";
    private static String ALTER_SETTINGS_ADD_TWITTERPW = "ALTER TABLE settings ADD COLUMN twitterpw varchar(30);";

    /**
     * Database creation sql statement
     */
    private static final String NOTIFICATIONS_CREATE =
            "create table notifications (_id integer primary key, "
                    + "totaldays int not null, totalcigs long not null, "
                    + "totalcurrency long not null, totallife int not null, "
                    + "lastmessage varchar(1000) NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SETTINGS_CREATE);
        db.execSQL(NOTIFICATIONS_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
    {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
    	switch (oldVersion)
    	{
    	case 30 :
    		db.execSQL(ALTER_SETTINGS_ADD_TWITTERPW);
    		break;
    	case 29 :
    		db.execSQL(ALTER_SETTINGS_ADD_TWITTERUSER);
    		db.execSQL(ALTER_SETTINGS_ADD_TWITTERPW);
    		break;
    	case 28 :
            db.execSQL("DROP TABLE IF EXISTS notifications");
            db.execSQL(NOTIFICATIONS_CREATE);
    		db.execSQL(ALTER_SETTINGS_ADD_TWITTERUSER);
    		db.execSQL(ALTER_SETTINGS_ADD_TWITTERPW);
    		break;
    	default :
        	db.execSQL("DROP TABLE IF EXISTS settings");
    		db.execSQL("DROP TABLE IF EXISTS notifications");
    		onCreate(db);
    		break;
    	}
    }


}
