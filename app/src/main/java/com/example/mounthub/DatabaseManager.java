package com.example.mounthub;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.Duration;

public class DatabaseManager extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mounthubDB.db";
    private static final int DATABASE_VERSION = 3;

    // users table
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USER_USERNAME = "username";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PASSWORD = "password";
    public static final String COLUMN_USER_INFO = "info";

    // SQL statement to create your table
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_USER_USERNAME + " TEXT UNIQUE NOT NULL," +
                    COLUMN_USER_EMAIL + " TEXT UNIQUE NOT NULL," +
                    COLUMN_USER_PASSWORD + " TEXT," +
                    COLUMN_USER_INFO + " TEXT)";

    private static final String DUMMY_USER = "INSERT INTO " + TABLE_USERS + " (" +
            COLUMN_USER_USERNAME + ", " +
            COLUMN_USER_EMAIL + ", " +
            COLUMN_USER_PASSWORD + ", " +
            COLUMN_USER_INFO + ") VALUES ('test', 'test', 'test', 'test')";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_USERS;

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // This method is called when the database is created for the first time.
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(DUMMY_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
         db.execSQL(SQL_DELETE_ENTRIES);
         onCreate(db);
    }

    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        // TODO(optional): hash password before putting into database

        cv.put(COLUMN_USER_USERNAME, user.getUsername());
        cv.put(COLUMN_USER_EMAIL, user.getEmail());
        cv.put(COLUMN_USER_PASSWORD, user.getPassword());
        cv.put(COLUMN_USER_INFO, user.getInfo());

        long userId;
        try {
            userId = db.insertOrThrow(TABLE_USERS, null, cv);
        } catch (SQLiteConstraintException e) {
            if (e.getMessage().contains("UNIQUE constraint failed: users.username")) {
                // Username already exists
                return -1;
            }

            if (e.getMessage().contains("UNIQUE constraint failed: users.email")) {
                // Email already exists
                return -2;
            }

            Log.e("Database Error", "Error inserting user: " + e.getMessage());
            return 0;
        } finally {
            db.close();
        }

        // clean
        db.close();

        return userId;
    }

    public boolean deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_USER_ID + " = ?";
        String[] whereArgs = {String.valueOf(user.getId())};
        int success = db.delete(TABLE_USERS, whereClause, whereArgs);

        // clean
        db.close();

        return success > 0;
    }

    public User fetchUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;
        Cursor cursor = null;

        try {
            String[] projection = {
                    COLUMN_USER_ID,
                    COLUMN_USER_USERNAME,
                    COLUMN_USER_EMAIL,
                    COLUMN_USER_PASSWORD,
                    COLUMN_USER_INFO
            };

            String selection = COLUMN_USER_USERNAME + " = ?";
            String[] selectionArgs = { username };

            cursor = db.query(
                    TABLE_USERS,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndexOrThrow(COLUMN_USER_ID);
//                int usernameIndex = cursor.getColumnIndexOrThrow(COLUMN_USER_USERNAME);
                int emailIndex = cursor.getColumnIndexOrThrow(COLUMN_USER_EMAIL);
                int passwordIndex = cursor.getColumnIndexOrThrow(COLUMN_USER_PASSWORD);
                int infoIndex = cursor.getColumnIndexOrThrow(COLUMN_USER_INFO);

                int id = cursor.getInt(idIndex);
                String email = cursor.getString(emailIndex);
                String password = cursor.getString(passwordIndex);
                String info = cursor.getString(infoIndex);

                user = new User(id, username, email, password, info);
            }
            else {
                return null;
            }
        } catch (Exception e) {
            Log.e("Database Error", "Error fetching user by name: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return user;
    }

//    public boolean editUser(User user) {
//        SQLiteDatabase
//    }

    public Map<String, List<?>> pointsNear(float lat, float lon) {
        // TODO: fetch actual locations/trails near the location

          List<Trail>  trails = new ArrayList<>();
          List<Location> locations = new ArrayList<>();
//        trails.add(new Trail("Appalachian Trail",
//                             "The Appalachian Trail is a 2,181-mile long public footpath that traverses the scenic, wooded, pastoral, wild, and culturally resonant lands of the Appalachian Mountains.",
//                              4.5f, 500, "Difficult", 10, 2500, 34.9622f, -84.2659f));
//        trails.add(new Trail("Pacific Crest Trail",
//                             "The Pacific Crest Trail is a 2,650-mile long-distance hiking and equestrian trail aligned with the highest portion of the Sierra Nevada and Cascade mountain ranges.",
//                              4.8f, 800, "Very Difficult", 150, 3000, 32.5953f, -116.4610f));
//        trails.add(new Trail("John Muir Trail",
//                             "The John Muir Trail is a long-distance trail in the Sierra Nevada mountain range of California, passing through Yosemite, Kings Canyon and Sequoia National Parks.",
//                              4.9f, 300, "Difficult", 21, 1400, 37.7401f, -119.5733f));
//        trails.add(new Trail("Wonderland Trail",
//                             "The Wonderland Trail is a 93-mile hiking trail that circumnavigates Mount Rainier in Mount Rainier National Park, Washington.",
//                              4.7f, 150, "Very Difficult", 10, 2200, 46.7760f, -121.7269f));

        trails.add(new Trail(
            1,
            "Appalachian Trail",
            List.of(new Coordinate(38.24630040417543, 21.734617569821097), new Coordinate(38.24630040417543, 21.734617569821097)),
            Trail.Difficulty.EASY,
            "Info"
        ));

        trails.add(new Trail(
            2,
            "Appalachian Trail",
            List.of(new Coordinate(38.246684768604155, 21.737499370110385), new Coordinate(38.24630040417543, 21.734617569821097)),
            Trail.Difficulty.EASY,
            "Info"
        ));

        locations.add(new Location(
                1,
                "Loc1",
                new Coordinate(38.249519070522766, 21.736482232637087),
                "mountain"
        ));

        locations.add(new Location(
                2,
                "Loc1",
                new Coordinate(38.24419048197976, 21.73351452466244),
                "mountain"
        ));

        return Map.of(
                "trails", trails,
                "locations", locations
        );

//        return List.of(
//                new Coordinate(38.24630040417543, 21.734617569821097),
//                new Coordinate(38.246684768604155, 21.737499370110385),
//                new Coordinate(38.249519070522766, 21.736482232637087),
//                new Coordinate(38.24419048197976, 21.73351452466244)
//                new Coordinate(28, 50),
//                new Coordinate(77, 6),
//                new Coordinate(3, 71),
//                new Coordinate(55, 23),
//                new Coordinate(89, 44),
//                new Coordinate(19, 68)
//        );
    }

    public Location getLocationDetails(int locationId) {
        return  new Location(1, "Loc1", new Coordinate(38.24630040417543, 21.734617569821097), "mountain");
    }
}
