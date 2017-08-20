package com.helou.james.pillminder;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.helou.james.pillminder.Data.PillContract.PillEntry;

import static android.R.attr.id;

/**
 * Created by James H
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private PillCursorAdapter mCursorAdapter;

    private Context mContext;

    public RecyclerAdapter(Context context, PillCursorAdapter adapter) {
        mContext = context;
        mCursorAdapter = adapter;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the view INSIDE the CursorAdapter (use it's newView)
        View v = mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent);
        final ViewHolder holder = new ViewHolder(v);

        // On item click listener for changing taken status
        holder.pillImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("POS of click: ", Integer.toString(holder.getAdapterPosition()));
                Log.e("_id of click: ", Long.toString(getItemId(holder.getAdapterPosition())));

                // Retrieve the correct item from the DB and initialize the cursor
                Uri uri = ContentUris.withAppendedId(PillEntry.CONTENT_URI,
                        getItemId(holder.getAdapterPosition()));
                Cursor checkCursor = mContext.getContentResolver().query(uri, null,
                        null, null, null);
                checkCursor.moveToFirst();

                // Get the taken attribute and its colour
                int takenColumnIndex = checkCursor.getColumnIndex(PillEntry.COLUMN_TAKEN);
                int taken = checkCursor.getInt(takenColumnIndex);

                int colourColumnIndex = checkCursor.getColumnIndex(PillEntry.COLUMN_COLOUR);
                int colourId = checkCursor.getInt(colourColumnIndex);

                // Change taken status when user clicks on item
                ContentValues newValues = new ContentValues();
                if (taken == PillEntry.TAKEN_NO) {
                    newValues.put(PillEntry.COLUMN_TAKEN, PillEntry.TAKEN_YES);
                    newValues.put(PillEntry.COLUMN_COLOUR, colourId + 100);
                    mContext.getContentResolver().update(uri, newValues, null, null);
                } else {
                    newValues.put(PillEntry.COLUMN_TAKEN, PillEntry.TAKEN_NO);
                    newValues.put(PillEntry.COLUMN_COLOUR, colourId - 100);
                    mContext.getContentResolver().update(uri, newValues, null, null);
                }
                checkCursor.close();
            }
        });


        // Long click listener for changing details of the item
        holder.pillImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.e("POS of long click: ", Integer.toString(holder.getAdapterPosition()));
                Log.e("_id of long click: ", Long.toString(getItemId(holder.getAdapterPosition())));

                Intent intent = new Intent(mContext, EditorActivity.class);

                // Set up URI of clicked item to send with the intent
                Uri uri = ContentUris.withAppendedId(PillEntry.CONTENT_URI,
                        getItemId(holder.getAdapterPosition()));
                intent.setData(uri);

                mContext.startActivity(intent);
                return true;
            }
        });



        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Bind the view INSIDE the CursorAdapter (use it's bindView)
        mCursorAdapter.getCursor().moveToPosition(position);
        mCursorAdapter.bindView(holder.itemView, mContext, mCursorAdapter.getCursor());
    }

    @Override
    public long getItemId(int position) {
        if(mCursorAdapter.getCursor().moveToPosition(position)) {
            int idColumnIndex = mCursorAdapter.getCursor().getColumnIndex(PillEntry._ID);
            return mCursorAdapter.getCursor().getLong(idColumnIndex);
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return mCursorAdapter.getCount();
    }

    // Changes the cursor adapters cursor to the newest cursor returned from OnLoadFinished()
    public void changeCursor(Cursor cursor){
        mCursorAdapter.changeCursor(cursor);
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Declare view items here
        private TextView pillNameTextView;
        private ImageView pillImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            pillNameTextView = (TextView) itemView.findViewById(R.id.pill_name_text_view);
            pillImageView = (ImageView) itemView.findViewById(R.id.pill_image_view);
        }
    }
}