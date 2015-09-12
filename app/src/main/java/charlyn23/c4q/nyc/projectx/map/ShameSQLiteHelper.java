package charlyn23.c4q.nyc.projectx.map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sufeizhao on 9/10/15.
 */
public class ShameSQLiteHelper extends SQLiteOpenHelper {

    private static final String DB = "geofenceDB";
    private static final int VERSION = 1;

    public ShameSQLiteHelper(Context context) {
        super(context, DB, null, VERSION);
    }

    private static ShameSQLiteHelper INSTANCE;

    public static synchronized ShameSQLiteHelper getInstance(Context context) {
        if (INSTANCE == null)
            INSTANCE = new ShameSQLiteHelper(context.getApplicationContext());
        return INSTANCE;
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

    public static abstract class DataEntry implements BaseColumns {
        public static final String TABLE_NAME = "activeGeofence";
        public static final String COLUMN_OBJECT = "objectId";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_GROUP = "group";
    }

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " +
            DataEntry.TABLE_NAME + " (" +
            DataEntry._ID + " INTEGER PRIMARY KEY," +
            DataEntry.COLUMN_OBJECT + " Text," +
            DataEntry.COLUMN_LATITUDE + " INTEGER," +
            DataEntry.COLUMN_LONGITUDE + " INTEGER," +
            DataEntry.COLUMN_GROUP + " TEXT" + ")";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DataEntry.TABLE_NAME;

    public void insertData(List<ShameGeofence> results) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(DataEntry.TABLE_NAME, null, null);

        for (ShameGeofence geo : results) {
            insertRow(geo);
        }
    }

    public void insertRow(ShameGeofence geo) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DataEntry.COLUMN_OBJECT, geo.getObjectId());
        values.put(DataEntry.COLUMN_LATITUDE, geo.getLatitude());
        values.put(DataEntry.COLUMN_LONGITUDE, geo.getLongitude());
        values.put(DataEntry.COLUMN_GROUP, geo.getGroup());

        db.insertOrThrow(DataEntry.TABLE_NAME, null, values);
    }

//    public List<String> loadData(String[] findGroup) {
//        SQLiteDatabase db = getWritableDatabase();
//        String[] projection = {
//                DataEntry.COLUMN_OBJECT,
//                DataEntry.COLUMN_LATITUDE,
//                DataEntry.COLUMN_LONGITUDE,
//                DataEntry.COLUMN_GROUP
//        };
//
//        List<String> data = new ArrayList<>();
//
//        Cursor cursor = db.query(
//                DataEntry.TABLE_NAME, projection, "group = ?", findGroup, null, null, null);
//
//        while (cursor.moveToNext()) {
//            data.add(cursor.getInt(cursor.getColumnIndex(DataEntry.COLUMN_YEAR)) + " " +
//                    cursor.getString(cursor.getColumnIndex(DataEntry.COLUMN_ETHNICITY)) + " " +
//                    cursor.getString(cursor.getColumnIndex(DataEntry.COLUMN_SEX)) + " " +
//                    cursor.getString(cursor.getColumnIndex(DataEntry.COLUMN_CAUSE_OF_DEATH)) + " " +
//                    cursor.getInt(cursor.getColumnIndex(DataEntry.COLUMN_COUNT)) + " " +
//                    cursor.getInt(cursor.getColumnIndex(DataEntry.COLUMN_PERCENT)));
//        }
//
//        cursor.close();
//        return data;
//    }
}