package com.helou.james.pillminder.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.helou.james.pillminder.Data.PillContract.PillEntry;

/**
 * Created by James H
 */

public class PillDbHelper extends SQLiteOpenHelper {
    private static PillDbHelper mInstance = null;

    public static final String DB_NAME = "pills.db";
    public static final int DB_VERSION = 1;

    // Set up DB instructions
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + PillEntry.TABLE_NAME + " (" +
                    PillEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PillEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                    PillEntry.COLUMN_COLOUR + " INT NOT NULL DEFAULT 0, " +
                    PillEntry.COLUMN_TIME + " INT NOT NULL DEFAULT 0, " +
                    PillEntry.COLUMN_TAKEN + " INT NOT NULL DEFAULT 0)";

    // Delete/update DB instructions
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + PillEntry.TABLE_NAME;

    // To guarantee singleton property and only have one DB helper exist
    public static PillDbHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new PillDbHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    public PillDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
