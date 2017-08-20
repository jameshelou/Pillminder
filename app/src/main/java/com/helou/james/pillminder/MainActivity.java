package com.helou.james.pillminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;


import java.util.Calendar;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.app.LoaderManager;
import android.view.View;

import com.helou.james.pillminder.Data.PillContract.PillEntry;

import static android.Manifest.permission_group.CALENDAR;


public class MainActivity extends AppCompatActivity {

    private PillCursorAdapter mMorningCursorAdapter;
    private PillCursorAdapter mAfternoonCursorAdapter;
    private PillCursorAdapter mEveningCursorAdapter;

    private RecyclerView mMorningRecyclerView;
    private RecyclerView mAfternoonRecyclerView;
    private RecyclerView mEveningRecyclerView;

    private RecyclerAdapter mMorningRecyclerAdapter;
    private RecyclerAdapter mAfternoonRecyclerAdapter;
    private RecyclerAdapter mEveningRecyclerAdapter;

    private LinearLayoutManager mMorningLayoutManager;
    private LinearLayoutManager mAfternoonLayoutManager;
    private LinearLayoutManager mEveningLayoutManager;

    private AlarmManager alarmManager;

    public static final int MORNING_ALARM_ID = 100;
    public static final int AFTERNOON_ALARM_ID = 200;
    public static final int EVENING_ALARM_ID = 300;
    public static final int MIDNIGHT_ALARM_ID = 1000;

    private static boolean MORNING_ALARM_SET;
    private static boolean AFTERNOON_ALARM_SET;
    private static boolean EVENING_ALARM_SET;
    private static boolean MIDNIGHT_ALARM_SET;


    private LoaderManager.LoaderCallbacks<Cursor> mMorningLoader = new
            LoaderManager.LoaderCallbacks<Cursor>() {
                @Override
                public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                    String[] projection = new String[] { PillEntry._ID, PillEntry.COLUMN_NAME,
                            PillEntry.COLUMN_COLOUR};

                    String[] initMorningArgs = new String[] {String.valueOf(PillEntry.TIME_MORNING)};
                    return new CursorLoader(MainActivity.this, PillEntry.CONTENT_URI, projection,
                            PillEntry.COLUMN_TIME + "=?", initMorningArgs, null);
                }

                @Override
                public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                    mMorningRecyclerAdapter.changeCursor(cursor);
                    mMorningCursorAdapter.swapCursor(cursor);
                }

                @Override
                public void onLoaderReset(Loader<Cursor> loader) {
                    mMorningCursorAdapter.swapCursor(null);
                }
            };

    private LoaderManager.LoaderCallbacks<Cursor> mAfternoonLoader = new
            LoaderManager.LoaderCallbacks<Cursor>() {
                @Override
                public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                    String[] projection = new String[] { PillEntry._ID, PillEntry.COLUMN_NAME,
                            PillEntry.COLUMN_COLOUR};
                    String[] initNoonArgs = new String[] {String.valueOf(PillEntry.TIME_AFTERNOON)};

                    return new CursorLoader(MainActivity.this, PillEntry.CONTENT_URI, projection,
                            PillEntry.COLUMN_TIME + "=?", initNoonArgs, null);
                }

                @Override
                public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                    mAfternoonRecyclerAdapter.changeCursor(cursor);
                    mAfternoonCursorAdapter.swapCursor(cursor);
                }

                @Override
                public void onLoaderReset(Loader<Cursor> loader) {
                    mAfternoonCursorAdapter.swapCursor(null);
                }
            };

    private LoaderManager.LoaderCallbacks<Cursor> mEveningLoader = new
            LoaderManager.LoaderCallbacks<Cursor>() {
                @Override
                public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                    String[] projection = new String[] { PillEntry._ID, PillEntry.COLUMN_NAME,
                            PillEntry.COLUMN_COLOUR};
                    String[] initEveningArgs = new String[] {String.valueOf(PillEntry.TIME_EVENING)};

                    return new CursorLoader(MainActivity.this, PillEntry.CONTENT_URI, projection,
                            PillEntry.COLUMN_TIME + "=?", initEveningArgs, null);
                }

                @Override
                public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                    mEveningRecyclerAdapter.changeCursor(cursor);
                    mEveningCursorAdapter.swapCursor(cursor);
                }

                @Override
                public void onLoaderReset(Loader<Cursor> loader) {
                    mEveningCursorAdapter.swapCursor(null);
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getLoaderManager().initLoader(0, null, mMorningLoader);
        getLoaderManager().initLoader(1, null, mAfternoonLoader);
        getLoaderManager().initLoader(2, null, mEveningLoader);

        // SET UP EACH TIME CATEGORY CURSOR ADAPTER
        mMorningCursorAdapter = new PillCursorAdapter(this, null);
        mAfternoonCursorAdapter = new PillCursorAdapter(this, null);
        mEveningCursorAdapter = new PillCursorAdapter(this, null);


        // SET MORNING RECYCLER VIEW LAYOUT MANAGER
        mMorningRecyclerView = (RecyclerView) findViewById(R.id.morning_recycler_view);
        mMorningLayoutManager = new LinearLayoutManager(this);
        mMorningLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mMorningRecyclerView.setLayoutManager(mMorningLayoutManager);

        // SET MORNING RECYCLER ADAPTER WITH ITS CURSOR ADAPTER
        mMorningRecyclerAdapter = new RecyclerAdapter(this, mMorningCursorAdapter);


        // SET AFTERNOON RECYCLER VIEW LAYOUT MANAGER
        mAfternoonRecyclerView = (RecyclerView) findViewById(R.id.afternoon_recycler_view);
        mAfternoonLayoutManager = new LinearLayoutManager(this);
        mAfternoonLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mAfternoonRecyclerView.setLayoutManager(mAfternoonLayoutManager);

        // SET AFTERNOON RECYCLER ADAPTER WITH ITS CURSOR ADAPTER
        mAfternoonRecyclerAdapter = new RecyclerAdapter(this, mAfternoonCursorAdapter);


        // SET EVENING RECYCLER VIEW LAYOUT MANAGER
        mEveningRecyclerView = (RecyclerView) findViewById(R.id.evening_recycler_view);
        mEveningLayoutManager = new LinearLayoutManager(this);
        mEveningLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mEveningRecyclerView.setLayoutManager(mEveningLayoutManager);

        // SET EVENING RECYCLER ADAPTER WITH ITS CURSOR ADAPTER
        mEveningRecyclerAdapter = new RecyclerAdapter(this, mEveningCursorAdapter);


        // SET EACH RECYCLER VIEW ITS RECYCLER (lowkey cursor) ADAPTER
        mMorningRecyclerView.setAdapter(mMorningRecyclerAdapter);
        mAfternoonRecyclerView.setAdapter(mAfternoonRecyclerAdapter);
        mEveningRecyclerView.setAdapter(mEveningRecyclerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu from menu_main.xml file
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_button:
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
                return true;
            case R.id.delete_all_button:
                getContentResolver().delete(PillEntry.CONTENT_URI, null, null);
                mMorningRecyclerAdapter.notifyDataSetChanged();
                mAfternoonRecyclerAdapter.notifyDataSetChanged();
                mEveningRecyclerAdapter.notifyDataSetChanged();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
      * Checks which time segments are active in order to set alarms for only the active ones
      */
    private void checkActiveTimeSegments() {
        String morningSelect = PillEntry.COLUMN_TIME + "=?";
        String[] morningArgs = new String[] {String.valueOf(PillEntry.TIME_MORNING)};

        Cursor morningCursor = getContentResolver().query(PillEntry.CONTENT_URI, null, morningSelect,
                morningArgs, null);

        if (morningCursor.getCount() >= 1 && !MORNING_ALARM_SET) {
            Log.e("MainActivity", "setting up MORN alarm");
            setAlarm(MORNING_ALARM_ID);
            MORNING_ALARM_SET = true;
        } else if (morningCursor.getCount() == 0 && MORNING_ALARM_SET) {
            Log.e("MainActivity", "dont set up MORN alarm");
            cancelAlarm(MORNING_ALARM_ID);
            MORNING_ALARM_SET = false;
        }
        morningCursor.close();



        String noonSelect = PillEntry.COLUMN_TIME + "=?";
        String[] noonArgs = new String[] {String.valueOf(PillEntry.TIME_AFTERNOON)};

        Cursor noonCursor = getContentResolver().query(PillEntry.CONTENT_URI, null, noonSelect,
                noonArgs, null);

        if (noonCursor.getCount() >= 1 && !AFTERNOON_ALARM_SET) {
            Log.e("MainActivity", "setting up NOON alarm");
            setAlarm(AFTERNOON_ALARM_ID);
            AFTERNOON_ALARM_SET = true;
        } else if (noonCursor.getCount() == 0 && AFTERNOON_ALARM_SET) {
            Log.e("MainActivity", "dont set up NOON alarm");
            cancelAlarm(AFTERNOON_ALARM_ID);
            AFTERNOON_ALARM_SET = false;
        }
        noonCursor.close();



        String eveningSelect = PillEntry.COLUMN_TIME + "=?";
        String[] eveningArgs = new String[] {String.valueOf(PillEntry.TIME_EVENING)};

        Cursor eveningCursor = getContentResolver().query(PillEntry.CONTENT_URI, null, eveningSelect,
                eveningArgs, null);

        if (eveningCursor.getCount() >= 1 && !EVENING_ALARM_SET) {
            Log.e("MainActivity", "setting up EVENING alarm");
            setAlarm(EVENING_ALARM_ID);
            EVENING_ALARM_SET = true;
        } else if (eveningCursor.getCount() == 0 && EVENING_ALARM_SET) {
            Log.e("MainActivity", "dont set up EVENING alarm");
            cancelAlarm(EVENING_ALARM_ID);
            EVENING_ALARM_SET = false;
        }
        eveningCursor.close();


        // Set midnight alarm if table contains any entries
        Cursor itemsCursor = getContentResolver().query(PillEntry.CONTENT_URI, null, null,
                null, null);
        if (itemsCursor.getCount() > 0 && !MIDNIGHT_ALARM_SET) {
            Log.e("MainActivity", "Table has >0 rows - calling resetAtMidnight()...");
            resetAtMidnight();
            MIDNIGHT_ALARM_SET = true;
        } else if (itemsCursor.getCount() == 0 && MIDNIGHT_ALARM_SET) {
            cancelAlarm(MIDNIGHT_ALARM_ID);
            MIDNIGHT_ALARM_SET = false;
            Log.e("MainActivity", "Midnight alarm CANCELLED");
        }
        itemsCursor.close();
    }

    /*
     * Sets up alarm and takes in the alarmId of the time segment to make the alarms for
     */
    private void setAlarm(int alarmId) {
        Calendar calendar = Calendar.getInstance();

        switch (alarmId) {
            case MORNING_ALARM_ID:
                calendar.set(Calendar.HOUR_OF_DAY,9);
                calendar.set(Calendar.MINUTE,0);
                calendar.set(Calendar.SECOND,0);
                break;
            case AFTERNOON_ALARM_ID:
                calendar.set(Calendar.HOUR_OF_DAY,15);
                calendar.set(Calendar.MINUTE,0);
                calendar.set(Calendar.SECOND,0);
                break;
            case EVENING_ALARM_ID:
                calendar.set(Calendar.HOUR_OF_DAY,20);
                calendar.set(Calendar.MINUTE,0);
                calendar.set(Calendar.SECOND,0);
                break;
        }



        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }


    private void cancelAlarm(int alarmId) {
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
    }

    private void resetAtMidnight() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }

        Intent intent = new Intent(getApplicationContext(), MidnightReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                MIDNIGHT_ALARM_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent);
        Log.e("MainActivity", Long.toString(calendar.getTimeInMillis()));
        Log.e("MainActivity", "resetAtMidnight() called");
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkActiveTimeSegments();
    }
}