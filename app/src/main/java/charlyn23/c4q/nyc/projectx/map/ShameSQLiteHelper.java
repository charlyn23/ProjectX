package charlyn23.c4q.nyc.projectx.map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

import charlyn23.c4q.nyc.projectx.Constants;
import charlyn23.c4q.nyc.projectx.shames.Shame;

public class ShameSQLiteHelper extends SQLiteOpenHelper {

    public ShameSQLiteHelper(Context context) {
        super(context, Constants.DB, null, Constants.VERSION);
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
        db.execSQL(SQL_CREATE_ENTRIES_INDEX);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public static abstract class DataEntry implements BaseColumns {
        public static final String TABLE_NAME = "listOfIncidences";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_SHAME_TYPE = "shameType";
        public static final String COLUMN_VERBAL_SHAME = "verbalShame";
        public static final String COLUMN_PHYSICAL_SHAME = "physicalShame";
        public static final String COLUMN_OTHER_SHAME = "otherShame";
        public static final String COLUMN_GROUP = "groupEffected";
        public static final String COLUMN_SHAME_DOING = "shameDoing";
        public static final String COLUMN_SHAME_FEEL = "shameFeel";
        public static final String COLUMN_ZIPCODE = "zipCode";
        public static final String COLUMN_TIMESTAMP = "timestamp";
        public static final String INDEX_TIMESTAMP = TABLE_NAME + "timestamp_idx";
    }

    private static final String SQL_CREATE_ENTRIES_INDEX =
            "CREATE INDEX " + DataEntry.INDEX_TIMESTAMP +
                    " ON " + DataEntry.TABLE_NAME  + "(" + DataEntry.COLUMN_TIMESTAMP + ")" ;

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " +
            DataEntry.TABLE_NAME + " (" +
            DataEntry._ID + " INTEGER PRIMARY KEY," +
            DataEntry.COLUMN_TIMESTAMP + " INTEGER," +
            DataEntry.COLUMN_LATITUDE + " INTEGER," +
            DataEntry.COLUMN_LONGITUDE + " INTEGER," +
            DataEntry.COLUMN_GROUP + " TEXT," +
            DataEntry.COLUMN_SHAME_TYPE + " TEXT," +
            DataEntry.COLUMN_VERBAL_SHAME + " TEXT," +
            DataEntry.COLUMN_PHYSICAL_SHAME + " TEXT," +
            DataEntry.COLUMN_OTHER_SHAME + " TEXT," +
            DataEntry.COLUMN_SHAME_DOING + " TEXT," +
            DataEntry.COLUMN_SHAME_FEEL + " TEXT," +
            DataEntry.COLUMN_ZIPCODE + " TEXT" + ")";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DataEntry.TABLE_NAME;

    public void insertData(List<Shame> results) {
        for (Shame incident : results) {
            insertRow(incident);
        }
    }

    public void insertRow(Shame incident) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DataEntry.COLUMN_TIMESTAMP, Integer.valueOf(incident.getString(Constants.SHAME_TIME_COLUMN).substring(0, 8)));
        values.put(DataEntry.COLUMN_LATITUDE, incident.getDouble(Constants.SHAME_LATITUDE_COLUMN));
        values.put(DataEntry.COLUMN_LONGITUDE, incident.getDouble(Constants.SHAME_LONGITUDE_COLUMN));
        values.put(DataEntry.COLUMN_GROUP, incident.getString(Constants.GROUP_COLUMN));
        values.put(DataEntry.COLUMN_SHAME_TYPE, incident.getString(Constants.SHAME_TYPE_COLUMN));
        values.put(DataEntry.COLUMN_VERBAL_SHAME, incident.getString(Constants.VERBAL_SHAME_COLUMN));
        values.put(DataEntry.COLUMN_PHYSICAL_SHAME, incident.getString(Constants.PHYSICAL_SHAME_COLUMN));
        values.put(DataEntry.COLUMN_OTHER_SHAME, incident.getString(Constants.OTHER_SHAME_COLUMN));
        values.put(DataEntry.COLUMN_SHAME_DOING, incident.getString(Constants.SHAME_DOING_COLUMN));
        values.put(DataEntry.COLUMN_SHAME_FEEL, incident.getString(Constants.SHAME_FEEL_COLUMN));
        values.put(DataEntry.COLUMN_ZIPCODE, incident.getString(Constants.SHAME_ZIPCODE_COLUMN));

        db.insertOrThrow(DataEntry.TABLE_NAME, null, values);
    }

    public List<Shame> loadData(String[] time) {
        SQLiteDatabase db = getWritableDatabase();
        String[] projection = {
                DataEntry.COLUMN_TIMESTAMP,
                DataEntry.COLUMN_LATITUDE,
                DataEntry.COLUMN_LONGITUDE,
                DataEntry.COLUMN_GROUP,
                DataEntry.COLUMN_SHAME_TYPE,
                DataEntry.COLUMN_VERBAL_SHAME,
                DataEntry.COLUMN_PHYSICAL_SHAME,
                DataEntry.COLUMN_OTHER_SHAME,
                DataEntry.COLUMN_SHAME_DOING,
                DataEntry.COLUMN_SHAME_FEEL,
                DataEntry.COLUMN_ZIPCODE
        };

        List<Shame> incidents = new ArrayList<>();
        Cursor cursor = db.query(
                DataEntry.TABLE_NAME, projection, DataEntry.COLUMN_TIMESTAMP + " > ?", time, null, null, null);

        if (cursor.getCount() > 1000) {
            removeSomeData();
        }

        while (cursor.moveToNext()) {
            incidents.add(new Shame(
                    cursor.getDouble(cursor.getColumnIndex(DataEntry.COLUMN_LATITUDE)),
                    cursor.getDouble(cursor.getColumnIndex(DataEntry.COLUMN_LONGITUDE)),
                    cursor.getString(cursor.getColumnIndex(DataEntry.COLUMN_SHAME_TYPE)),
                    cursor.getString(cursor.getColumnIndex(DataEntry.COLUMN_VERBAL_SHAME)),
                    cursor.getString(cursor.getColumnIndex(DataEntry.COLUMN_PHYSICAL_SHAME)),
                    cursor.getString(cursor.getColumnIndex(DataEntry.COLUMN_OTHER_SHAME)),
                    cursor.getString(cursor.getColumnIndex(DataEntry.COLUMN_SHAME_FEEL)),
                    cursor.getString(cursor.getColumnIndex(DataEntry.COLUMN_SHAME_DOING)),
                    cursor.getString(cursor.getColumnIndex(DataEntry.COLUMN_TIMESTAMP)),
                    cursor.getString(cursor.getColumnIndex(DataEntry.COLUMN_GROUP)),
                    cursor.getString(cursor.getColumnIndex(DataEntry.COLUMN_ZIPCODE))));
        }

        cursor.close();
        return incidents;
    }

    //counts types of instances
    public int[] countTypes (String zipCode) {
        Cursor cursor = null;
        int numCatcall = 0;
        int numPhysical = 0;
        int numOther = 0;
        SQLiteDatabase db = getWritableDatabase();
        int[] types = new int[3];

        String[] projection = {
                DataEntry.COLUMN_SHAME_TYPE,
        };

        if (zipCode.length() == 0) {
            cursor = db.query(DataEntry.TABLE_NAME, projection, DataEntry.COLUMN_SHAME_TYPE + " = ?", new String[]{Constants.VERBAL}, null, null, null);
            numCatcall = cursor.getCount();
            cursor = db.query(DataEntry.TABLE_NAME, projection, DataEntry.COLUMN_SHAME_TYPE + " = ?", new String[]{Constants.PHYSICAL}, null, null, null);
            numPhysical = cursor.getCount();
            cursor = db.query(DataEntry.TABLE_NAME, projection, DataEntry.COLUMN_SHAME_TYPE + " = ?", new String[]{Constants.OTHER}, null, null, null);
            numOther = cursor.getCount();

        }

        else if (zipCode.length() > 0){
            cursor = db.query(DataEntry.TABLE_NAME, projection, DataEntry.COLUMN_ZIPCODE + " = ? and " + DataEntry.COLUMN_SHAME_TYPE + " = ?", new String[] {zipCode, Constants.VERBAL}, null, null, null);
            numCatcall = cursor.getCount();
            cursor = db.query(DataEntry.TABLE_NAME, projection, DataEntry.COLUMN_ZIPCODE + " = ? and " + DataEntry.COLUMN_SHAME_TYPE + " = ?", new String[]{zipCode, Constants.PHYSICAL}, null, null, null);
            numPhysical = cursor.getCount();
            cursor = db.query(DataEntry.TABLE_NAME, projection, DataEntry.COLUMN_ZIPCODE + " = ? and " + DataEntry.COLUMN_SHAME_TYPE + " = ?", new String[]{zipCode, Constants.OTHER}, null, null, null);
            numOther = cursor.getCount();
        }
        cursor.close();
        types[0] = numCatcall;
        types[1] = numPhysical;
        types[2] = numOther;
        return types;
    }

    //counts types of groups
    public int[] countGroups (String zipCode) {
        Cursor cursor = null;
        int numWomen = 0;
        int numPOC = 0;
        int numLGBTQ = 0;
        int numMinor = 0;
        int numOther = 0;
        SQLiteDatabase db = getWritableDatabase();
        int[] types = new int[5];

        String[] projection = {
                DataEntry.COLUMN_GROUP,
        };

        if (zipCode.length() == 0) {
            cursor = db.query(DataEntry.TABLE_NAME, projection, DataEntry.COLUMN_GROUP + " = ?", new String[]{Constants.WOMAN}, null, null, null);
            numWomen = cursor.getCount();
            cursor = db.query(DataEntry.TABLE_NAME, projection, DataEntry.COLUMN_GROUP + " = ?", new String[]{Constants.POC}, null, null, null);
            numPOC = cursor.getCount();
            cursor = db.query(DataEntry.TABLE_NAME, projection, DataEntry.COLUMN_GROUP + " = ?", new String[]{Constants.LGBTQ}, null, null, null);
            numLGBTQ = cursor.getCount();
            cursor = db.query(DataEntry.TABLE_NAME, projection, DataEntry.COLUMN_GROUP + " = ?", new String[]{Constants.MINOR}, null, null, null);
            numMinor = cursor.getCount();
            cursor = db.query(DataEntry.TABLE_NAME, projection, DataEntry.COLUMN_GROUP + " = ?", new String[]{Constants.OTHER}, null, null, null);
            numOther = cursor.getCount();

        }

        else if (zipCode.length() > 0){
            cursor = db.query(DataEntry.TABLE_NAME, projection, DataEntry.COLUMN_ZIPCODE + " = ? and " + DataEntry.COLUMN_GROUP + " = ?", new String[] {zipCode, Constants.WOMAN}, null, null, null);
            numWomen = cursor.getCount();
            cursor = db.query(DataEntry.TABLE_NAME, projection, DataEntry.COLUMN_ZIPCODE + " = ? and " + DataEntry.COLUMN_GROUP + " = ?", new String[]{zipCode, Constants.POC}, null, null, null);
            numPOC = cursor.getCount();
            cursor = db.query(DataEntry.TABLE_NAME, projection, DataEntry.COLUMN_ZIPCODE + " = ? and " + DataEntry.COLUMN_GROUP + " = ?", new String[]{zipCode, Constants.LGBTQ}, null, null, null);
            numLGBTQ = cursor.getCount();
            cursor = db.query(DataEntry.TABLE_NAME, projection, DataEntry.COLUMN_ZIPCODE + " = ? and " + DataEntry.COLUMN_GROUP + " = ?", new String[]{zipCode, Constants.MINOR}, null, null, null);
            numMinor = cursor.getCount();
            cursor = db.query(DataEntry.TABLE_NAME, projection, DataEntry.COLUMN_ZIPCODE + " = ? and " + DataEntry.COLUMN_GROUP + " = ?", new String[]{zipCode, Constants.OTHER}, null, null, null);
            numOther = cursor.getCount();
        }
        cursor.close();
        types[0] = numWomen;
        types[1] = numPOC;
        types[2] = numLGBTQ;
        types[3] = numMinor;
        types[4] = numOther;
        return types;
    }

    public void removeSomeData() {
        SQLiteDatabase db = getWritableDatabase();
        String selectQuery = "DELETE FROM " + DataEntry.TABLE_NAME + " WHERE _id IN " +
                "(SELECT _id FROM " + DataEntry.TABLE_NAME + " ORDER BY _id ASC LIMIT 500)";
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        cursor.close();
        Log.i("SQLite Delete", "Removed 500 incidences from Database");
    }
}