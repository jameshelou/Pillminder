package com.helou.james.pillminder.Data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import com.helou.james.pillminder.R;

/**
 * Created by James H
 */

public final class PillContract {

    // URI constants
    public static final String CONTENT_AUTHORITY = "com.helou.james.pillminder";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_PILLS = "pills";


    // No one should create instance of this contract
    private PillContract() {};


    // Inner class for pill table and it's constants
    public static class PillEntry implements BaseColumns {
        // URI final constant
        public static final Uri CONTENT_URI = Uri.withAppendedPath(
                BASE_CONTENT_URI, PATH_PILLS);

        // MIME types
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/" + CONTENT_AUTHORITY + "/" + PATH_PILLS;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                "/" + CONTENT_AUTHORITY + "/" + PATH_PILLS;

        // Table name
        public static final String TABLE_NAME = "pills";

        // Columns for the table
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_COLOUR = "colour";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_TAKEN = "taken";


        // Time segment constants
        public static final int TIME_MORNING = 0;
        public static final int TIME_AFTERNOON = 1;
        public static final int TIME_EVENING = 2;

        // Pill taken constants
        public static final int TAKEN_NO = 0;
        public static final int TAKEN_YES = 1;

        // Image id constants
        public static final int COLOUR_BLACK = 1;
        public static final int COLOUR_BLUE = 2;
        public static final int COLOUR_DARKBLUE = 3;
        public static final int COLOUR_GREEN = 4;
        public static final int COLOUR_ORANGE = 5;
        public static final int COLOUR_PINK = 6;
        public static final int COLOUR_PURPLE = 7;
        public static final int COLOUR_RED = 8;
        public static final int COLOUR_YELLOW = 9;
        public static final int COLOUR_BLACK_CHECKMARK = 101;
        public static final int COLOUR_BLUE_CHECKMARK = 102;
        public static final int COLOUR_DARKBLUE_CHECKMARK = 103;
        public static final int COLOUR_GREEN_CHECKMARK = 104;
        public static final int COLOUR_ORANGE_CHECKMARK = 105;
        public static final int COLOUR_PINK_CHECKMARK = 106;
        public static final int COLOUR_PURPLE_CHECKMARK = 107;
        public static final int COLOUR_RED_CHECKMARK = 108;
        public static final int COLOUR_YELLOW_CHECKMARK = 109;


        // Public helper method to point appropriate constant colour id to drawable resource
        public static int chooseImage(int colour_id) {
            switch (colour_id) {
                case COLOUR_BLACK:
                    return R.drawable.pill_black;
                case COLOUR_BLUE:
                    return R.drawable.pill_blue;
                case COLOUR_DARKBLUE:
                    return R.drawable.pill_darkblue;
                case COLOUR_GREEN:
                    return R.drawable.pill_green;
                case COLOUR_ORANGE:
                    return R.drawable.pill_orange;
                case COLOUR_PINK:
                    return R.drawable.pill_pink;
                case COLOUR_PURPLE:
                    return R.drawable.pill_purple;
                case COLOUR_RED:
                    return R.drawable.pill_red;
                case COLOUR_YELLOW:
                    return R.drawable.pill_yellow;
                case COLOUR_BLACK_CHECKMARK:
                    return R.drawable.pill_black_check;
                case COLOUR_BLUE_CHECKMARK:
                    return R.drawable.pill_blue_check;
                case COLOUR_DARKBLUE_CHECKMARK:
                    return R.drawable.pill_darkblue_check;
                case COLOUR_GREEN_CHECKMARK:
                    return R.drawable.pill_green_check;
                case COLOUR_ORANGE_CHECKMARK:
                    return R.drawable.pill_orange_check;
                case COLOUR_PINK_CHECKMARK:
                    return R.drawable.pill_pink_check;
                case COLOUR_PURPLE_CHECKMARK:
                    return R.drawable.pill_purple_check;
                case COLOUR_RED_CHECKMARK:
                    return R.drawable.pill_red_check;
                case COLOUR_YELLOW_CHECKMARK:
                    return R.drawable.pill_yellow_check;
                default:
                    return 0;
            }
        }

    }
}
