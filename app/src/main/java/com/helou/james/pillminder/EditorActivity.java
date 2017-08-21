package com.helou.james.pillminder;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.helou.james.pillminder.Data.PillContract.PillEntry;

import static android.R.attr.actionMenuTextColor;
import static android.R.attr.data;

public class EditorActivity extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<Cursor>{

    private Spinner mColourSpinner;
    private ImageView mColourImageView;
    private EditText mNameEditText;
    private Spinner mTimeSpinner;

    private int mColour;
    private int mTime;

    private Uri mCurrentUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Examine the intent that was started this activity to setup for ADD or EDIT mode
        Intent receivedIntent = getIntent();
        mCurrentUri = receivedIntent.getData();

        if (mCurrentUri == null) { // add mode
            setTitle(getString(R.string.editor_activity_add_pill_title));
            invalidateOptionsMenu();
        } else { // edit mode
            setTitle(getString(R.string.editor_activity_edit_pill_title));
        }

        mColourSpinner = (Spinner) findViewById(R.id.colour_spinner);
        mColourImageView = (ImageView) findViewById(R.id.pill_editor_image_view);
        mNameEditText = (EditText) findViewById(R.id.name_edit_text);
        mTimeSpinner = (Spinner) findViewById(R.id.time_spinner);

        setupColourSpinner();
        setupTimeSpinner();

        getLoaderManager().initLoader(1, null, this);
    }

    /**
     * Get user input from input fields and save new pill or changes into database
     */
    private void savePill() {
        String nameString = mNameEditText.getText().toString().trim();

        if (nameString.isEmpty()) {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
            return;
        }

        ContentValues pillValues = new ContentValues();
        pillValues.put(PillEntry.COLUMN_NAME, nameString);
        pillValues.put(PillEntry.COLUMN_COLOUR, mColour);
        pillValues.put(PillEntry.COLUMN_TIME, mTime);
        pillValues.put(PillEntry.COLUMN_TAKEN, PillEntry.TAKEN_NO);

        // ADD or EDIT mode (INSERT or UPDATE query)
        // Check if there is a uri or null
        Intent intent = getIntent();
        Uri currentUri = intent.getData();

        if (currentUri == null) { // ADD
            Uri mNewPilLUri = getContentResolver().insert(PillEntry.CONTENT_URI, pillValues);

            if (mNewPilLUri == null) {
                Toast.makeText(this, getString(R.string.editor_activity_insert_pill_failed), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_activity_insert_pill_success), Toast.LENGTH_SHORT).show();
            }
        } else { // UPDATE
            // the uri already has the id attached to it
            int rowsUpdated = getContentResolver().update(currentUri, pillValues, null, null);

            if (rowsUpdated == 0) {
                Toast.makeText(this, getString(R.string.editor_activity_update_pill_failed), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_activity_update_pill_success), Toast.LENGTH_SHORT).show();
            }
        }

    }

    /**
     * Setup the COLOUR dropdown spinner
     */
    private void setupColourSpinner() {
        ArrayAdapter colourSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.colour_spinner_options, android.R.layout.simple_spinner_item);


        colourSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mColourSpinner.setAdapter(colourSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mColourSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.tablet_blue))) {
                        mColour = PillEntry.IMAGE_TABLET_BLUE;
                        mColourImageView.setImageResource(
                                PillEntry.chooseImage(PillEntry.IMAGE_TABLET_BLUE));
                    }
                    else if (selection.equals(getString(R.string.tablet_red))) {
                        mColour = PillEntry.IMAGE_TABLET_RED;
                        mColourImageView.setImageResource(
                                PillEntry.chooseImage(PillEntry.IMAGE_TABLET_RED));
                    }
                    else if (selection.equals(getString(R.string.tablet_darkblue))) {
                        mColour = PillEntry.IMAGE_TABLET_DARKBLUE;
                        mColourImageView.setImageResource(
                                PillEntry.chooseImage(PillEntry.IMAGE_TABLET_DARKBLUE));
                    }
                    else if (selection.equals(getString(R.string.tablet_green))) {
                        mColour = PillEntry.IMAGE_TABLET_GREEN;
                        mColourImageView.setImageResource(
                                PillEntry.chooseImage(PillEntry.IMAGE_TABLET_GREEN));
                    }
                    else if (selection.equals(getString(R.string.tablet_orange))) {
                        mColour = PillEntry.IMAGE_TABLET_ORANGE;
                        mColourImageView.setImageResource(
                                PillEntry.chooseImage(PillEntry.IMAGE_TABLET_ORANGE));
                    }
                    else if (selection.equals(getString(R.string.tablet_pink))) {
                        mColour = PillEntry.IMAGE_TABLET_PINK;
                        mColourImageView.setImageResource(
                                PillEntry.chooseImage(PillEntry.IMAGE_TABLET_PINK));
                    }
                    else if (selection.equals(getString(R.string.tablet_yellow))) {
                        mColour = PillEntry.IMAGE_TABLET_YELLOW;
                        mColourImageView.setImageResource(
                                PillEntry.chooseImage(PillEntry.IMAGE_TABLET_YELLOW));
                    }
                    else if (selection.equals(getString(R.string.capsule_blue))) {
                        mColour = PillEntry.IMAGE_CAPSULE_BLUE;
                        mColourImageView.setImageResource(
                                PillEntry.chooseImage(PillEntry.IMAGE_CAPSULE_BLUE));
                    }
                    else if (selection.equals(getString(R.string.capsule_red))) {
                        mColour = PillEntry.IMAGE_CAPSULE_RED;
                        mColourImageView.setImageResource(
                                PillEntry.chooseImage(PillEntry.IMAGE_CAPSULE_RED));
                    }
                    else if (selection.equals(getString(R.string.capsule_darkblue))) {
                        mColour = PillEntry.IMAGE_CAPSULE_DARKBLUE;
                        mColourImageView.setImageResource(
                                PillEntry.chooseImage(PillEntry.IMAGE_CAPSULE_DARKBLUE));
                    }
                    else if (selection.equals(getString(R.string.capsule_green))) {
                        mColour = PillEntry.IMAGE_CAPSULE_GREEN;
                        mColourImageView.setImageResource(
                                PillEntry.chooseImage(PillEntry.IMAGE_CAPSULE_GREEN));
                    }
                    else if (selection.equals(getString(R.string.capsule_orange))) {
                        mColour = PillEntry.IMAGE_CAPSULE_ORANGE;
                        mColourImageView.setImageResource(
                                PillEntry.chooseImage(PillEntry.IMAGE_CAPSULE_ORANGE));
                    }
                    else if (selection.equals(getString(R.string.capsule_pink))) {
                        mColour = PillEntry.IMAGE_CAPSULE_PINK;
                        mColourImageView.setImageResource(
                                PillEntry.chooseImage(PillEntry.IMAGE_CAPSULE_PINK));
                    } else {
                        mColour = PillEntry.IMAGE_CAPSULE_YELLOW;
                        mColourImageView.setImageResource(
                                PillEntry.chooseImage(PillEntry.IMAGE_CAPSULE_YELLOW));
                    }


                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mColour = PillEntry.IMAGE_TABLET_BLUE;
            } // Default blue
        });
    }

    /**
     * Setup the TIME dropdown spinner
     */
    private void setupTimeSpinner() {
        ArrayAdapter timeSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.time_spinner_options, android.R.layout.simple_spinner_item);


        timeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mTimeSpinner.setAdapter(timeSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.morning))) {
                        mTime = PillEntry.TIME_MORNING;
                    } else if (selection.equals(getString(R.string.afternoon))) {
                        mTime = PillEntry.TIME_AFTERNOON;
                    } else {
                        mTime = PillEntry.TIME_EVENING;
                    }
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mTime = PillEntry.TIME_MORNING;} // Default morning
        });
    }


    private void showConfirmDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.delete_dialog_question));
        builder.setPositiveButton(getString(R.string.delete_dialog_positive),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deletePill();
            }
        });
        builder.setNegativeButton(getString(R.string.delete_dialog_negative),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (dialogInterface != null) {
                    dialogInterface.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deletePill() {
        // Only delete if we are in edit mode
        if (mCurrentUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentUri, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.editor_activity_delete_pill_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_activity_delete_pill_success),
                        Toast.LENGTH_SHORT).show();
            }
        }

        finish(); // Close the activity as pill may no longer exist
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mCurrentUri == null) {
            MenuItem deleteItem = menu.findItem(R.id.delete_button);
            deleteItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_button:
                savePill();
                finish();
                return true;
            case R.id.delete_button:
                showConfirmDeleteDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }




    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        if (mCurrentUri == null) { // If we are adding a new pill, mCurrentUri should be null
            return null;
        }

        return new CursorLoader(this, mCurrentUri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor cursor) {
        // Do not load fields from the cursor if the cursor is null (therefore, not in edit mode)
        if (cursor == null || cursor.getCount() < 1) {
            return; // exit early
        }

        // Proceed with fetching the fields for the correct item (the _id of the passed uri)
        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(PillEntry.COLUMN_NAME);
            int colourColumnIndex = cursor.getColumnIndex(PillEntry.COLUMN_COLOUR);
            int timeColumnIndex = cursor.getColumnIndex(PillEntry.COLUMN_TIME);

            // Extract the data
            String name = cursor.getString(nameColumnIndex);

            // Correctly convert id of colours if user wants to edit a 'taken' pill or untaken pill
            int colour = cursor.getInt(colourColumnIndex);
            if (colour > 1000) {
                colour -= 1000;
            }
            Log.e("color_id in edit", String.valueOf(colour));
            int time = cursor.getInt(timeColumnIndex);

            // Update views with fetched data
            mNameEditText.setText(name);

            switch (colour) {
                case PillEntry.IMAGE_TABLET_BLUE:
                    mColourSpinner.setSelection(0);
                    break;
                case PillEntry.IMAGE_TABLET_RED:
                    mColourSpinner.setSelection(1);
                    break;
                case PillEntry.IMAGE_TABLET_DARKBLUE:
                    mColourSpinner.setSelection(2);
                    break;
                case PillEntry.IMAGE_TABLET_GREEN:
                    mColourSpinner.setSelection(3);
                    break;
                case PillEntry.IMAGE_TABLET_ORANGE:
                    mColourSpinner.setSelection(4);
                    break;
                case PillEntry.IMAGE_TABLET_PINK:
                    mColourSpinner.setSelection(5);
                    break;
                case PillEntry.IMAGE_TABLET_YELLOW:
                    mColourSpinner.setSelection(6);
                    break;
                case PillEntry.IMAGE_CAPSULE_BLUE:
                    mColourSpinner.setSelection(7);
                    break;
                case PillEntry.IMAGE_CAPSULE_RED:
                    mColourSpinner.setSelection(8);
                    break;
                case PillEntry.IMAGE_CAPSULE_DARKBLUE:
                    mColourSpinner.setSelection(9);
                    break;
                case PillEntry.IMAGE_CAPSULE_GREEN:
                    mColourSpinner.setSelection(10);
                    break;
                case PillEntry.IMAGE_CAPSULE_ORANGE:
                    mColourSpinner.setSelection(11);
                    break;
                case PillEntry.IMAGE_CAPSULE_PINK:
                    mColourSpinner.setSelection(12);
                    break;
                case PillEntry.IMAGE_CAPSULE_YELLOW:
                    mColourSpinner.setSelection(13);
                    break;
            }

            switch (time) {
                case PillEntry.TIME_MORNING:
                    mTimeSpinner.setSelection(0);
                    break;
                case PillEntry.TIME_AFTERNOON:
                    mTimeSpinner.setSelection(1);
                    break;
                case PillEntry.TIME_EVENING:
                    mTimeSpinner.setSelection(2);
                    break;
            }

            //mColourSpinner.setSelection(colour);
            //mTimeSpinner.setSelection(time);

            mColourImageView.setImageResource(PillEntry.chooseImage(colour));
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        // If loader is reset then clear all the EditText fields
        mNameEditText.setText("");
        mColourSpinner.setSelection(0);
        mTimeSpinner.setSelection(0);
    }
}




