package com.example.mounthub;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager extends SQLiteOpenHelper {

    private static final String TAG = "DBManager";

    private static final String DATABASE_NAME = "mounthub.db";
    private static final int DATABASE_VERSION = 1;

    // Table Users
    public static final String TABLE_USERS = "users";
    public static final String COL_USER_ID = "id";
    public static final String COL_USERNAME = "username";
    public static final String COL_EMAIL = "email";

    // Table Locations
    public static final String TABLE_LOCATIONS = "locations";
    public static final String COL_LOCATION_ID = "id";
    public static final String COL_LOCATION_NAME = "name";
    public static final String COL_LOCATION_LATITUDE = "latitude";
    public static final String COL_LOCATION_LONGITUDE = "longitude";
    public static final String COL_LOCATION_DESCRIPTION = "description";

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users Table
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_USERNAME + " TEXT NOT NULL," +
                COL_EMAIL + " TEXT NOT NULL" +
                ")";
        db.execSQL(createUsersTable);

        // Create Locations Table
        String createLocationsTable = "CREATE TABLE " + TABLE_LOCATIONS + " (" +
                COL_LOCATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_LOCATION_NAME + " TEXT NOT NULL," +
                COL_LOCATION_LATITUDE + " REAL NOT NULL," +
                COL_LOCATION_LONGITUDE + " REAL NOT NULL," +
                COL_LOCATION_DESCRIPTION + " TEXT" +
                ")";
        db.execSQL(createLocationsTable);

        // Optionally insert some dummy data for testing
        insertDummyLocations(db);
        insertDummyUsers(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables and recreate if upgrading database version
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Example to insert dummy locations during creation
    private void insertDummyLocations(SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        values.put(COL_LOCATION_NAME, "Mount Everest");
        values.put(COL_LOCATION_LATITUDE, 27.9881);
        values.put(COL_LOCATION_LONGITUDE, 86.9250);
        values.put(COL_LOCATION_DESCRIPTION, "Highest mountain in the world.");
        db.insert(TABLE_LOCATIONS, null, values);

        values.clear();

        values.put(COL_LOCATION_NAME, "K2");
        values.put(COL_LOCATION_LATITUDE, 35.8808);
        values.put(COL_LOCATION_LONGITUDE, 76.5158);
        values.put(COL_LOCATION_DESCRIPTION, "Second highest mountain.");
        db.insert(TABLE_LOCATIONS, null, values);
    }

    private void insertDummyUsers(SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        values.put(COL_USERNAME, "user1");
        values.put(COL_EMAIL, "user1@example.com");
        db.insert(TABLE_USERS, null, values);

        values.clear();

        values.put(COL_USERNAME, "user2");
        values.put(COL_EMAIL, "user2@example.com");
        db.insert(TABLE_USERS, null, values);
    }

    // Fetch all locations from the database
    public List<Location> fetchAllLocations() {
        List<Location> locations = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_LOCATIONS;

        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    Location loc = new Location();
                    loc.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_LOCATION_ID)));
                    loc.setName(cursor.getString(cursor.getColumnIndexOrThrow(COL_LOCATION_NAME)));
                    loc.setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_LOCATION_LATITUDE)));
                    loc.setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_LOCATION_LONGITUDE)));
                    loc.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COL_LOCATION_DESCRIPTION)));

                    locations.add(loc);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "fetchAllLocations failed: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            // db.close(); // Don't close DB here; SQLiteOpenHelper manages it
        }

        return locations;
    }
}
