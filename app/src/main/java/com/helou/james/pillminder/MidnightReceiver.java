package com.helou.james.pillminder;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.helou.james.pillminder.Data.PillContract.PillEntry;
import com.helou.james.pillminder.Data.PillDbHelper;

/**
 * Created by James H
 */

public class MidnightReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("MidnightReceiver", "Got to midnight onReceive()");


        // For each UNTAKEN image row, set TAKEN to TAKEN_NO except maintain their
        // image untaken state
        String selectionUntaken = PillEntry.COLUMN_COLOUR + "<? AND " +
                PillEntry.COLUMN_TAKEN + "=?";
        String[] selectionUntakenArgs = new String[]{ "100", String.valueOf(PillEntry.TAKEN_NO) };
        Cursor untakenImageCursor = context.getContentResolver().query(PillEntry.CONTENT_URI, null,
                selectionUntaken, selectionUntakenArgs, null);

        int idColumnIndex = untakenImageCursor.getColumnIndex(PillEntry._ID);
        while (untakenImageCursor.moveToNext()) {
            int id = untakenImageCursor.getInt(idColumnIndex);

            ContentValues untakenNewValues = new ContentValues();
            untakenNewValues.put(PillEntry.COLUMN_TAKEN, PillEntry.TAKEN_NO);

            String untakenWhere = PillEntry._ID + "=?";
            String[] untakenArgs = new String[] { String.valueOf(id) };
            context.getContentResolver().update(PillEntry.CONTENT_URI, untakenNewValues, untakenWhere,
                    untakenArgs);
        }
        untakenImageCursor.close();


        // Find all entries with TAKEN image
        String selection = PillEntry.COLUMN_COLOUR + ">=?";
        String[] selectionArgs = new String[] { "100" };
        Cursor takenImageCursor = context.getContentResolver().query(PillEntry.CONTENT_URI,
                null,selection, selectionArgs, null);

        // For each TAKEN image row, set taken status to TAKEN_NO and set its image to untaken state
        // (i.e current image id - 100)
        idColumnIndex = takenImageCursor.getColumnIndex(PillEntry._ID);
        int colourColumnIndex = takenImageCursor.getColumnIndex(PillEntry.COLUMN_COLOUR);
        while (takenImageCursor.moveToNext()) {
            int id = takenImageCursor.getInt(idColumnIndex);
            int colour = takenImageCursor.getInt(colourColumnIndex);

            ContentValues takenNewValues = new ContentValues();
            takenNewValues.put(PillEntry.COLUMN_TAKEN, PillEntry.TAKEN_NO);
            takenNewValues.put(PillEntry.COLUMN_COLOUR, colour-100); // revert to untaken state

            String takenWhere = PillEntry._ID + "=?";
            String[] takenArgs = new String[] { String.valueOf(id) };
            context.getContentResolver().update(PillEntry.CONTENT_URI, takenNewValues,
                    takenWhere, takenArgs);
        }
        takenImageCursor.close();

    }
}
