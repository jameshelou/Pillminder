package com.helou.james.pillminder;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.helou.james.pillminder.Data.PillDbHelper;
import com.helou.james.pillminder.Data.PillProvider;
import com.helou.james.pillminder.Data.PillContract.PillEntry;

import java.util.Calendar;


/**
 * Created by James H
 */

public class NotificationReceiver extends BroadcastReceiver {

    NotificationManager notificationManager;
    Intent openActivityIntent;
    PendingIntent pendingIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Setup for notification
        notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        openActivityIntent = new Intent(context, MainActivity.class);
        openActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        pendingIntent = PendingIntent.getActivity(context, 100,
                openActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Log.e("NotificationReceiver", "We got to onReceive()");


        PillDbHelper dbHelper = PillDbHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);


        // Handle checking only pills that are meant to be checked at
        // either morning, noon or evening
        if (currentHour >= 6 && currentHour <= 12) { // MORNING
            Log.e("NotificationReceiver", "We got to MORNING");
            String selection = PillEntry.COLUMN_TIME + "=? AND " + PillEntry.COLUMN_TAKEN + "=?";
            String[] selectionArgs = new String[] {String.valueOf(PillEntry.TIME_MORNING),
                    String.valueOf(PillEntry.TAKEN_NO) };

            Cursor cursor = db.query(PillEntry.TABLE_NAME, null, selection, selectionArgs,
                    null, null, null);

            sendNotification(context, cursor, PillEntry.TIME_MORNING);

        } else if (currentHour >= 14 && currentHour <= 18) { // NOON
            Log.e("NotificationReceiver", "We got to AFTERNOON");
            String selection = PillEntry.COLUMN_TIME + "=? AND " + PillEntry.COLUMN_TAKEN + "=?";
            String[] selectionArgs = new String[] {String.valueOf(PillEntry.TIME_AFTERNOON),
                    String.valueOf(PillEntry.TAKEN_NO) };

            Cursor cursor = db.query(PillEntry.TABLE_NAME, null, selection, selectionArgs,
                    null, null, null);

            sendNotification(context, cursor, PillEntry.TIME_AFTERNOON);
        } else { // EVENING
            Log.e("NotificationReceiver", "We got to EVENING");
            String selection = PillEntry.COLUMN_TIME + "=? AND " + PillEntry.COLUMN_TAKEN + "=?";
            String[] selectionArgs = new String[] {String.valueOf(PillEntry.TIME_EVENING),
                    String.valueOf(PillEntry.TAKEN_NO) };

            Cursor cursor = db.query(PillEntry.TABLE_NAME, null, selection, selectionArgs,
                    null, null, null);

            sendNotification(context, cursor, PillEntry.TIME_EVENING);
        }

        db.close();
    }



    private void sendNotification(Context context, Cursor cursor, int timeSegment) {
        if (cursor.getCount() == 0) { // If all pills have been taken don't notify
            Log.e("NotificationReceiver", "We got to cursorcount = 0");
            cursor.close();
            return;
        } else {
            Log.e("NotificationReceiver", "We got to cursorcount >= 1");
            String timeSegmentString;
            switch (timeSegment) {
                case PillEntry.TIME_MORNING:
                    timeSegmentString = context.getString(R.string.notification_morning_append);
                    break;
                case PillEntry.TIME_AFTERNOON:
                    timeSegmentString = context.getString(R.string.notification_afternoon_append);
                    break;
                case PillEntry.TIME_EVENING:
                    timeSegmentString = context.getString(R.string.notification_evening_append);
                    break;
                default:
                    timeSegmentString = context.getString(R.string.notification_default_append);
                    break;
            }

            // Create notification
            int untakenPillCount = cursor.getCount();

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setContentIntent(pendingIntent);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentTitle(context.getString(R.string.app_name));
            if (untakenPillCount == 1) {
                builder.setContentText(context.getString(R.string.notification_message_part1) +
                        " " + String.valueOf(untakenPillCount) + " " +
                        context.getString(R.string.notification_message_part2_single) + " " +
                        timeSegmentString);
            } else {
                builder.setContentText(context.getString(R.string.notification_message_part1) +
                        " " + String.valueOf(untakenPillCount) + " " +
                        context.getString(R.string.notification_message_part2_multiple) + " " +
                        timeSegmentString);
            }

            builder.setAutoCancel(true);

            notificationManager.notify(100, builder.build());
            cursor.close();
        }
    }

}
