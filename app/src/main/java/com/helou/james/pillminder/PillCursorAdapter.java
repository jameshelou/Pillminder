package com.helou.james.pillminder;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.helou.james.pillminder.Data.PillContract.PillEntry;

/**
 * Created by James H
 */

public class PillCursorAdapter extends CursorAdapter {

    public PillCursorAdapter (Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.pill_list_item, viewGroup, false);

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView pillNameTextView = (TextView) view.findViewById(R.id.pill_name_text_view);
        ImageView pillImageView = (ImageView) view.findViewById(R.id.pill_image_view);

        int nameColumnIndex = cursor.getColumnIndex(PillEntry.COLUMN_NAME);
        int imageColumnIndex = cursor.getColumnIndex(PillEntry.COLUMN_COLOUR);

        pillNameTextView.setText(cursor.getString(nameColumnIndex));
        pillImageView.setImageResource(PillEntry.chooseImage(cursor.getInt(imageColumnIndex)));
    }
}
