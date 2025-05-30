package com.example.mounthub;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USER_USERNAME = "username";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PASSWORD = "password";
    public static final String COLUMN_USER_INFO = "info";

    public static final String TABLE_NOTIFICATIONS = "notifications";
    public static final String COLUMN_NOTIFICATION_ID = "id";
    public static final String COLUMN_NOTIFICATION_MESSAGE = "message";

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
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_USERNAME + " TEXT UNIQUE NOT NULL," +
                COL_EMAIL + " TEXT UNIQUE NOT NULL," +
                COL_PASSWORD + " TEXT," +
                COL_USER_INFO + " TEXT)";
        db.execSQL(createUsersTable);

        String createLocationsTable = "CREATE TABLE " + TABLE_LOCATIONS + " (" +
                COL_LOCATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_LOCATION_NAME + " TEXT NOT NULL," +
                COL_LOCATION_LATITUDE + " REAL NOT NULL," +
                COL_LOCATION_LONGITUDE + " REAL NOT NULL," +
                COL_LOCATION_DESCRIPTION + " TEXT," +
                COL_LOCATION_TYPE + " TEXT)";
        db.execSQL(createLocationsTable);

        insertDummyUsers(db);
        insertDummyLocations(db);
      
        // This method is called when the database is created for the first time.
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(DUMMY_USER);

        String SQL_CREATE_NOTIFICATIONS =
                "CREATE TABLE " + TABLE_NOTIFICATIONS + " (" +
                        COLUMN_NOTIFICATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_NOTIFICATION_MESSAGE + " TEXT NOT NULL)";
        db.execSQL(SQL_CREATE_NOTIFICATIONS);

        // Insert some fake notifications
        insertFakeNotifications(db);
    }

    private void insertFakeNotifications(SQLiteDatabase db) {
        insertNotification(db, "Welcome to MountHub!");
        insertNotification(db, "A new trail near you has been added.");
        insertNotification(db, "Weekly hiking summary is ready.");
    }

    private void insertNotification(SQLiteDatabase db, String message) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTIFICATION_MESSAGE, message);
        db.insert(TABLE_NOTIFICATIONS, null, values);
    }

    public List<Notification> fetchNotifications() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        List<Notification> notifications = new ArrayList<>();

        try {
            cursor = db.query(TABLE_NOTIFICATIONS,
                    new String[]{COLUMN_NOTIFICATION_MESSAGE},
                    null, null, null, null, null);

            if (cursor.moveToFirst()) {
                do {
                    String message = cursor.getString(0);
                    notifications.add(new Notification(message));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("Database Error", "Error fetching notifications: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return notifications;
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
            if (e.getMessage().contains("UNIQUE constraint failed: users.username")) return -1;
            if (e.getMessage().contains("UNIQUE constraint failed: users.email")) return -2;
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

    public Trail[] searchForTrails(String query) {
        Log.d("SearchManage", "Search query/tab: " + query + "/Trails");
        // Dummy code instead of call to DataBase:
        List<Coordinate> route1 = Arrays.asList(new Coordinate(38.1, 23.7), new Coordinate(38.2, 23.8));
        List<Coordinate> route2 = Arrays.asList(new Coordinate(37.9, 23.6), new Coordinate(38.0, 23.5));
        List<Coordinate> route3 = Arrays.asList(new Coordinate(39.1, 22.7), new Coordinate(39.2, 22.8));
        List<Coordinate> route4 = Arrays.asList(new Coordinate(40.1, 21.7), new Coordinate(40.2, 21.8));

        List<String> photos = Arrays.asList("trail1.jpg", "trail2.jpg");

        List<Integer> reviewIds = Arrays.asList(101, 102, 103);

        List<Excursion> excursions = new ArrayList<>();

        Trail tempTrail1 = new Trail(1, "Mount Olympus Trail", route1, 5, Trail.Difficulty.HARD,
                "Scenic trail up Mount Olympus", photos, reviewIds, excursions, 16);

        Trail tempTrail2 = new Trail(2, "Parnitha Forest Walk", route2, 2, Trail.Difficulty.EASY,
                "Leisure walk through Parnitha forest", photos, reviewIds, excursions, 5);

        Trail tempTrail3 = new Trail(3, "Zagori Stone Bridge Path", route3, 3, Trail.Difficulty.MEDIUM,
                "Historic trail with stone bridges", photos, reviewIds, excursions, 8);

        Trail tempTrail4 = new Trail(4, "Vikos Gorge Hike", route4, 6, Trail.Difficulty.HARD,
                "Trek through the famous Vikos Gorge", photos, reviewIds, excursions, 12);

        // return found trails:
        return new Trail[] { tempTrail1, tempTrail2, tempTrail3, tempTrail4 };
    }

    public Location[] searchForLocations(String query) {
        Log.d("SearchManage", "Search query/tab: " + query + "/Locations");
        Location loc1 = new Location(1, "Mount Olympus","Mountain");
        Location loc2 = new Location(2, "Samaria Gorge", "Gorge");
        Location loc3 = new Location(3, "Lake Plastira", "Lake");
        Location loc4 = new Location(4, "Vikos Gorge", "Canyon");

        return new Location[] { loc1, loc2, loc3, loc4 };
    }

    public User[] searchForUsers(String query) {
        Log.d("SearchManage", "Search query/tab: " + query + "/Users");
        User user1 = new User(1, "George123", "giorgos@example.com", "passsssword", "I like maps!");
        User user2 = new User(1, "Hikerman", "hikehike@example.com", "passsssword123", "I HIKE ALL THE TIME");
        User user3 = new User(1, "GirlBoss", "girlboss@example.com", "passsssword212121", "I like maps too!");
        return new User[] {user1, user2, user3};
    }

    public boolean isLocDuplicate(String name, String type, Coordinate coords, Context ctx) {
        Random random = new Random();
        boolean isDup = random.nextBoolean();
        if (isDup) new AlertWindow(ctx).showAlert();
        return isDup;
    }

    public Map<String, List<?>> pointsNear(float lat, float lon) {
        List<Trail> trails = List.of(new Trail(1, "Appalachian Trail",
                List.of(new Coordinate(lat, lon), new Coordinate(lat, lon)), Trail.Difficulty.EASY, "Info"));
        List<Location> locations = List.of(new Location(1, "Loc1", new Coordinate(lat, lon), "mountain"));
        Map<String, List<?>> result = new HashMap<>();
        result.put("trails", trails);
        result.put("locations", locations);
        return result;
    }

    public Location getLocationDetails(int locationId) {
        return new Location(locationId, "Loc1", new Coordinate(38.2, 23.8), "Mountain");
    }

    public int addLocation(String name, String description, String additionalInfo, Coordinate coordinate) {

        return 1; // return location id
    }

    public void QueryLocInfo() {
        // TODO: Add location info to database
    }

//    public boolean editUser(User user) {
//        SQLiteDatabase
//    }
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
}
