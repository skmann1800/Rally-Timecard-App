package com.example.rallytimingapp.sql;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.rallytimingapp.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserDatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 5;

    // Database Name
    private static final String DATABASE_NAME = "UserManager.db";

    // User table name
    private static final String TABLE_USER = "user";

    // User Table Columns names
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_USERNAME = "user_username";
    private static final String COLUMN_USER_PASSWORD = "user_password";
    private static final String COLUMN_USER_ROLE = "user_role";
    private static final String COLUMN_USER_ROLE_ID = "user_role_id";

    // Create table SQL query
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_USERNAME + " TEXT,"
            + COLUMN_USER_PASSWORD + " TEXT," + COLUMN_USER_ROLE + " TEXT," + COLUMN_USER_ROLE_ID + " INTEGER" + ")";

    // Drop table SQL query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;

    public UserDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_USER_TABLE);
        onCreate(db);
    }

    // Method to remove all entries from the database
    public void empty() {
        List<User> userList = getAllUsers();
        for (int i = 0; i < userList.size(); i++) {
            deleteUser(userList.get(i));
        }
    }

    // Method to add an entry to the database
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_USERNAME, user.getUsername());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        values.put(COLUMN_USER_ROLE, user.getRole());
        values.put(COLUMN_USER_ROLE_ID, user.getId());

        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close();
    }

    // Method to return the entry with the given username and role
    @SuppressLint("Range")
    public User getUser(String username, String role) {
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_USERNAME,
                COLUMN_USER_PASSWORD,
                COLUMN_USER_ROLE,
                COLUMN_USER_ROLE_ID
        };

        User user = new User();

        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_USER_USERNAME + " = ?" + " AND " + COLUMN_USER_ROLE + " = ?";
        // selection argument
        String[] selectionArgs = {username, role};

        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,             //columns to return
                selection,        //columns for the WHERE clause
                selectionArgs,     //The values for the WHERE clause
                null,        //group the rows
                null,         //filter by row groups
                null);         //The sort order

        if (cursor.moveToFirst()) {
            user.setUserId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
            user.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_USER_USERNAME)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
            user.setRole(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ROLE)));
            user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ROLE_ID))));
        }
        cursor.close();
        db.close();

        return user;
    }

    // Method to return the entry with the given role and associated role ID
    @SuppressLint("Range")
    public User getUserByRoleID(String role, int roleID) {
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_USERNAME,
                COLUMN_USER_PASSWORD,
                COLUMN_USER_ROLE,
                COLUMN_USER_ROLE_ID
        };

        User user = new User();

        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_USER_ROLE + " = ?" + " AND " + COLUMN_USER_ROLE_ID + " = ?";
        // selection argument
        String[] selectionArgs = {role, String.valueOf(roleID)};

        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,             //columns to return
                selection,        //columns for the WHERE clause
                selectionArgs,     //The values for the WHERE clause
                null,        //group the rows
                null,         //filter by row groups
                null);         //The sort order

        if (cursor.moveToFirst()) {
            user.setUserId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
            user.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_USER_USERNAME)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
            user.setRole(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ROLE)));
            user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ROLE_ID))));
        }
        cursor.close();
        db.close();

        return user;
    }

    // Method to return a list of all entries in the database
    @SuppressLint("Range")
    public List<User> getAllUsers() {
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_USERNAME,
                COLUMN_USER_PASSWORD,
                COLUMN_USER_ROLE,
                COLUMN_USER_ROLE_ID
        };

        String sortOrder = COLUMN_USER_USERNAME + " ASC";
        List<User> userList = new ArrayList<User>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,             //columns to return
                null,        //columns for the WHERE clause
                null,     //The values for the WHERE clause
                null,        //group the rows
                null,         //filter by row groups
                sortOrder);         //The sort order

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setUserId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                user.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_USER_USERNAME)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
                user.setRole(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ROLE)));
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ROLE_ID))));
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return userList;
    }

    // Method to return a list of all entries with the given role
    @SuppressLint("Range")
    public List<User> getAllOfRole(String role) {
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_USERNAME,
                COLUMN_USER_PASSWORD,
                COLUMN_USER_ROLE,
                COLUMN_USER_ROLE_ID
        };

        String sortOrder = COLUMN_USER_ROLE_ID + " ASC";
        List<User> userList = new ArrayList<User>();

        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_USER_ROLE + " = ?";
        // selection argument
        String[] selectionArgs = {role};

        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,             //columns to return
                selection,        //columns for the WHERE clause
                selectionArgs,     //The values for the WHERE clause
                null,        //group the rows
                null,         //filter by row groups
                sortOrder);         //The sort order

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setUserId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                user.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_USER_USERNAME)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
                user.setRole(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ROLE)));
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ROLE_ID))));
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return userList;
    }

    // Method to update an entry in the database
    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_USERNAME, user.getUsername());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        values.put(COLUMN_USER_ROLE, user.getRole());
        values.put(COLUMN_USER_ROLE_ID, user.getId());
        // updating row
        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getUserId())});
        db.close();
    }

    // Method to delete an entry from the database
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getUserId())});
        db.close();
    }

    // Method to check if an entry with the given username exists
    public boolean checkUser(String username) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_USERNAME + " = ?";
        // selection argument
        String[] selectionArgs = {username};
        // query user table with condition

        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    // Method to check if an entry with the given role and associated id exists
    public boolean checkUser(String role, int id) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_ROLE + " = ?" + " AND " + COLUMN_USER_ROLE_ID + " = ?";
        // selection argument
        String[] selectionArgs = {role, String.valueOf(id)};
        // query user table with condition

        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    // Method to check if a user exists which has the given username, password and role
    public boolean checkUser(String username, String password, String role) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_USERNAME + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?" + " AND " + COLUMN_USER_ROLE + " = ?";
        // selection arguments
        String[] selectionArgs = {username, password, role};
        // query user table with conditions

        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }
}
