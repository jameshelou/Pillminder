package com.helou.james.pillminder.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.helou.james.pillminder.Data.PillContract.PillEntry;

import static android.icu.lang.UCharacter.JoiningGroup.PE;

/**
 * Created by James H
 */

public class PillProvider extends ContentProvider {

    private PillDbHelper mDbHelper;

    // URI matcher codes
    // URI code for whole pill table
    private static final int PILLS = 100;

    // URI code for pill getting row at _ID
    private static final int PILLS_ID = 101;

    // URI matcher object - by default pass in NO_MATCH for root URI
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        // Adding URI pattern for whole pill table
        sUriMatcher.addURI(PillContract.CONTENT_AUTHORITY, PillContract.PATH_PILLS, PILLS);

        // Adding URI pattern for pill row
        sUriMatcher.addURI(PillContract.CONTENT_AUTHORITY, PillContract.PATH_PILLS + "/#",
                PILLS_ID);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = PillDbHelper.getInstance(getContext());
        //mDbHelper = new PillDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor;

        // Try match the Uri using the matcher
        int match = sUriMatcher.match(uri);
        switch (match) {
            case PILLS:
                // Return a cursor with the full table
                cursor = db.query(PillEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case PILLS_ID:
                // Return a cursor with the row identified by _ID
                selection = PillEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = db.query(PillEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Query failed - URI cannot be recognized " +
                uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case PILLS:
                return insertPill(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insert failed - URI cannot be recognized");
        }
    }

    /**
     * Private helper method for inserting pill into DB with data validation
     */
    private Uri insertPill(Uri uri, ContentValues values) {
        // Data validation
        String name = values.getAsString(PillEntry.COLUMN_NAME);
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Pill name cannot be null or empty");
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long newRowId = db.insert(PillEntry.TABLE_NAME, null, values);

        if (newRowId == -1) {
            Log.e("PillProvider.java", "Failed to insert row for " + uri);
            return null; // null means insertion failed
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, newRowId);
    }


    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case PILLS:
                return updatePills(uri, contentValues, selection, selectionArgs);
            case PILLS_ID:
                selection = PillEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updatePills(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update failed - URI cannot be recognized");
        }
    }

    /**
     * Private helper method for updating pills in the DB with data validation
     */
    private int updatePills(Uri uri, ContentValues values, String selection,
                            String[] selectionArgs) {
        if (values.size() == 0) {
            return 0;
        }

        if (values.containsKey(PillEntry.COLUMN_NAME)) {
            String name = values.getAsString(PillEntry.COLUMN_NAME);
            if (name == null || name.isEmpty()) {
                throw new IllegalArgumentException("Pill name cannot be null or empty");
            }
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowsChanged = db.update(PillEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsChanged != 0) getContext().getContentResolver().notifyChange(uri, null);

        return rowsChanged;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowsDeleted;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case PILLS:
                rowsDeleted = db.delete(PillEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0) getContext().getContentResolver().notifyChange(uri, null);
                return rowsDeleted;
            case PILLS_ID:
                selection = PillEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = db.delete(PillEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0) getContext().getContentResolver().notifyChange(uri, null);
                return rowsDeleted;
            default:
                throw new IllegalArgumentException("Delete failed - URI cannot be recognized");
        }
    }

    @Override
    public String getType(Uri uri) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case PILLS:
                return PillEntry.CONTENT_LIST_TYPE;
            case PILLS_ID:
                return PillEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("URI couldn't be matched");
        }
    }
}
