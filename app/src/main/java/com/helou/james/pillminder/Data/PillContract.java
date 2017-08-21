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
        // TABLET IMAGE IDS
        public static final int IMAGE_TABLET_BLUE = 100;
        public static final int IMAGE_TABLET_RED = 101;
        public static final int IMAGE_TABLET_DARKBLUE = 102;
        public static final int IMAGE_TABLET_GREEN = 103;
        public static final int IMAGE_TABLET_ORANGE = 104;
        public static final int IMAGE_TABLET_PINK = 105;
        public static final int IMAGE_TABLET_YELLOW = 106;

        public static final int IMAGE_TABLET_BLUE_CHECKED = 1100;
        public static final int IMAGE_TABLET_RED_CHECKED = 1101;
        public static final int IMAGE_TABLET_DARKBLUE_CHECKED = 1102;
        public static final int IMAGE_TABLET_GREEN_CHECKED = 1103;
        public static final int IMAGE_TABLET_ORANGE_CHECKED = 1104;
        public static final int IMAGE_TABLET_PINK_CHECKED = 1105;
        public static final int IMAGE_TABLET_YELLOW_CHECKED = 1106;

        public static final int IMAGE_CAPSULE_BLUE = 300;
        public static final int IMAGE_CAPSULE_RED = 301;
        public static final int IMAGE_CAPSULE_DARKBLUE = 302;
        public static final int IMAGE_CAPSULE_GREEN = 303;
        public static final int IMAGE_CAPSULE_ORANGE = 304;
        public static final int IMAGE_CAPSULE_PINK = 305;
        public static final int IMAGE_CAPSULE_YELLOW = 306;

        public static final int IMAGE_CAPSULE_BLUE_CHECKED = 1300;
        public static final int IMAGE_CAPSULE_RED_CHECKED = 1301;
        public static final int IMAGE_CAPSULE_DARKBLUE_CHECKED = 1302;
        public static final int IMAGE_CAPSULE_GREEN_CHECKED = 1303;
        public static final int IMAGE_CAPSULE_ORANGE_CHECKED = 1304;
        public static final int IMAGE_CAPSULE_PINK_CHECKED = 1305;
        public static final int IMAGE_CAPSULE_YELLOW_CHECKED = 1306;

        // Public helper method to point appropriate constant colour id to drawable resource
        public static int chooseImage(int colour_id) {
            switch (colour_id) {
                case IMAGE_TABLET_BLUE:
                    return R.drawable.tablet_blue;
                case IMAGE_TABLET_RED:
                    return R.drawable.tablet_red;
                case IMAGE_TABLET_DARKBLUE:
                    return R.drawable.tablet_darkblue;
                case IMAGE_TABLET_GREEN:
                    return R.drawable.tablet_green;
                case IMAGE_TABLET_ORANGE:
                    return R.drawable.tablet_orange;
                case IMAGE_TABLET_PINK:
                    return R.drawable.tablet_pink;
                case IMAGE_TABLET_YELLOW:
                    return R.drawable.tablet_yellow;
                case IMAGE_TABLET_BLUE_CHECKED:
                    return R.drawable.tablet_blue_check;
                case IMAGE_TABLET_RED_CHECKED:
                    return R.drawable.tablet_red_check;
                case IMAGE_TABLET_DARKBLUE_CHECKED:
                    return R.drawable.tablet_darkblue_check;
                case IMAGE_TABLET_GREEN_CHECKED:
                    return R.drawable.tablet_green_check;
                case IMAGE_TABLET_ORANGE_CHECKED:
                    return R.drawable.tablet_orange_check;
                case IMAGE_TABLET_PINK_CHECKED:
                    return R.drawable.tablet_pink_check;
                case IMAGE_TABLET_YELLOW_CHECKED:
                    return R.drawable.tablet_yellow_check;
                case IMAGE_CAPSULE_BLUE:
                    return R.drawable.capsule_blue;
                case IMAGE_CAPSULE_RED:
                    return R.drawable.capsule_red;
                case IMAGE_CAPSULE_DARKBLUE:
                    return R.drawable.capsule_darkblue;
                case IMAGE_CAPSULE_GREEN:
                    return R.drawable.capsule_green;
                case IMAGE_CAPSULE_ORANGE:
                    return R.drawable.capsule_orange;
                case IMAGE_CAPSULE_PINK:
                    return R.drawable.capsule_pink;
                case IMAGE_CAPSULE_YELLOW:
                    return R.drawable.capsule_yellow;
                case IMAGE_CAPSULE_BLUE_CHECKED:
                    return R.drawable.capsule_blue_check;
                case IMAGE_CAPSULE_RED_CHECKED:
                    return R.drawable.capsule_red_check;
                case IMAGE_CAPSULE_DARKBLUE_CHECKED:
                    return R.drawable.capsule_darkblue_check;
                case IMAGE_CAPSULE_GREEN_CHECKED:
                    return R.drawable.capsule_green_check;
                case IMAGE_CAPSULE_ORANGE_CHECKED:
                    return R.drawable.capsule_orange_check;
                case IMAGE_CAPSULE_PINK_CHECKED:
                    return R.drawable.capsule_pink;
                case IMAGE_CAPSULE_YELLOW_CHECKED:
                    return R.drawable.capsule_yellow_check;
                default:
                    return 0;
            }
        }

    }
}
