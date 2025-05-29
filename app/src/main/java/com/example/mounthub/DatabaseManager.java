package com.example.mounthub;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseManager";

    private static final String DATABASE_NAME = "mounthubDB.db";
    private static final int DATABASE_VERSION = 3;

    // Users Table
    public static final String TABLE_USERS = "users";
    public static final String COL_USER_ID = "id";
    public static final String COL_USERNAME = "username";
    public static final String COL_EMAIL = "email";
    public static final String COL_PASSWORD = "password";
    public static final String COL_USER_INFO = "info";

    // Locations Table
    public static final String TABLE_LOCATIONS = "locations";
    public static final String COL_LOCATION_ID = "id";
    public static final String COL_LOCATION_NAME = "name";
    public static final String COL_LOCATION_LATITUDE = "latitude";
    public static final String COL_LOCATION_LONGITUDE = "longitude";
    public static final String COL_LOCATION_DESCRIPTION = "description";
    public static final String COL_LOCATION_TYPE = "locationType";

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users Table
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_USERNAME + " TEXT UNIQUE NOT NULL," +
                COL_EMAIL + " TEXT UNIQUE NOT NULL," +
                COL_PASSWORD + " TEXT," +
                COL_USER_INFO + " TEXT)";
        db.execSQL(createUsersTable);

        // Create Locations Table
        String createLocationsTable = "CREATE TABLE " + TABLE_LOCATIONS + " (" +
                COL_LOCATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_LOCATION_NAME + " TEXT NOT NULL," +
                COL_LOCATION_LATITUDE + " REAL NOT NULL," +
                COL_LOCATION_LONGITUDE + " REAL NOT NULL," +
                COL_LOCATION_DESCRIPTION + " TEXT," +
                COL_LOCATION_TYPE + " TEXT)";
        db.execSQL(createLocationsTable);

        // Insert dummy data
        insertDummyUsers(db);
        insertDummyLocations(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);
        onCreate(db);
    }

    private void insertDummyUsers(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, "test");
        values.put(COL_EMAIL, "test@test.com");
        values.put(COL_PASSWORD, "test");
        values.put(COL_USER_INFO, "Test user");
        db.insert(TABLE_USERS, null, values);
    }

    private void insertDummyLocations(SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        values.put(COL_LOCATION_NAME, "Mount Everest");
        values.put(COL_LOCATION_LATITUDE, 27.9881);
        values.put(COL_LOCATION_LONGITUDE, 86.9250);
        values.put(COL_LOCATION_DESCRIPTION, "Highest mountain in the world.");
        values.put(COL_LOCATION_TYPE, "Mountain");
        db.insert(TABLE_LOCATIONS, null, values);

        values.clear();

        values.put(COL_LOCATION_NAME, "K2");
        values.put(COL_LOCATION_LATITUDE, 35.8808);
        values.put(COL_LOCATION_LONGITUDE, 76.5158);
        values.put(COL_LOCATION_DESCRIPTION, "Second highest mountain.");
        values.put(COL_LOCATION_TYPE, "Mountain");
        db.insert(TABLE_LOCATIONS, null, values);
    }

    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_USERNAME, user.getUsername());
        cv.put(COL_EMAIL, user.getEmail());
        cv.put(COL_PASSWORD, user.getPassword());
        cv.put(COL_USER_INFO, user.getInfo());

        long userId;
        try {
            userId = db.insertOrThrow(TABLE_USERS, null, cv);
        } catch (SQLiteConstraintException e) {
            if (e.getMessage().contains("UNIQUE constraint failed: users.username")) {
                return -1;
            }
            if (e.getMessage().contains("UNIQUE constraint failed: users.email")) {
                return -2;
            }
            Log.e(TAG, "Error inserting user: " + e.getMessage());
            return 0;
        } finally {
            db.close();
        }
        return userId;
    }

    public boolean deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deleted = db.delete(TABLE_USERS, COL_USER_ID + " = ?", new String[]{String.valueOf(user.getId())});
        db.close();
        return deleted > 0;
    }

    public User fetchUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        User user = null;

        try {
            cursor = db.query(
                    TABLE_USERS,
                    null,
                    COL_USERNAME + " = ?",
                    new String[]{username},
                    null, null, null
            );

            if (cursor != null && cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(COL_PASSWORD));
                String info = cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_INFO));
                user = new User(id, username, email, password, info);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching user: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return user;
    }

    public List<Location> fetchAllLocations() {
        List<Location> locations = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_LOCATIONS;
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_LOCATION_ID));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_LOCATION_NAME));
                    double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_LOCATION_LATITUDE));
                    double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_LOCATION_LONGITUDE));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow(COL_LOCATION_DESCRIPTION));
                    String locationType = cursor.getString(cursor.getColumnIndexOrThrow(COL_LOCATION_TYPE));

                    Location location = new Location(id, name, locationType, description, latitude, longitude);
                    location.setCoordinates(new Coordinate(latitude, longitude));
                    location.setDescription(description);

                    locations.add(location);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "fetchAllLocations failed: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return locations;
    }

    // Optional: addLocation method
    public long addLocation(String name, String description, String type, Coordinate coordinate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_LOCATION_NAME, name);
        values.put(COL_LOCATION_LATITUDE, coordinate.getLatitude());
        values.put(COL_LOCATION_LONGITUDE, coordinate.getLongitude());
        values.put(COL_LOCATION_DESCRIPTION, description);
        values.put(COL_LOCATION_TYPE, type);

        long id = db.insert(TABLE_LOCATIONS, null, values);
        db.close();
        return id;
    }
}
