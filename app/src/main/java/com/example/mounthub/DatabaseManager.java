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
import java.util.List;

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

    // notifications table
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
}