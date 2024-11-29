package com.techtravelcoder.educationalbooks.pdf;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.os.FileUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UPBHAPP.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_FILES = "Files";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_URL = "url";
    private static final String COLUMN_PATH = "path";
    private static final String COLUMN_BOOKKEY = "bookkey";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_BOOKMARK_STATUS = "status";


    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_FILES_TABLE = "CREATE TABLE " + TABLE_FILES + "("
                + COLUMN_NAME + " TEXT PRIMARY KEY,"
                + COLUMN_URL + " TEXT,"
                + COLUMN_PATH + " TEXT,"
                + COLUMN_BOOKKEY+" TEXT,"
                + COLUMN_TIME+" TEXT,"
                + COLUMN_BOOKMARK_STATUS+" TEXT"

                + ")";
        db.execSQL(CREATE_FILES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILES);
        onCreate(db);
    }

    public long addFile(String name, String url, String path,String key,String time,String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, name);
        values.put(COLUMN_URL, url);
        values.put(COLUMN_PATH, path);
        values.put(COLUMN_BOOKKEY, key);
        values.put(COLUMN_TIME, (String) time);
        values.put(COLUMN_BOOKMARK_STATUS, status);

        long id = db.insert(TABLE_FILES, null, values);
        db.close();
        return id;
    }

    public boolean updateFile(String name, String url, String path, String key, String time, String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Prepare the new values for update
        ContentValues values = new ContentValues();
        values.put(COLUMN_URL, url);
        values.put(COLUMN_PATH, path);
        values.put(COLUMN_BOOKKEY, key);
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_BOOKMARK_STATUS, status);

        int rowsAffected = db.update(TABLE_FILES, values, COLUMN_NAME + "=?", new String[]{name});

        db.close();
        return rowsAffected > 0; // Return true if the update was successful
    }

    public String getUpdateTime(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String time = null; // Variable to hold the time

        String query = "SELECT " + COLUMN_TIME + " FROM " + TABLE_FILES + " WHERE " + COLUMN_NAME + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{name});

        if (cursor != null && cursor.moveToFirst()) {
            time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME)); // Get the time
            cursor.close(); // Close the cursor
        }

        db.close();
        return time;
    }


    public String getUpdateStatus(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String status = null; // Variable to hold the status

        String query = "SELECT " + COLUMN_BOOKMARK_STATUS + " FROM " + TABLE_FILES + " WHERE " + COLUMN_NAME + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{name});

        if (cursor != null && cursor.moveToFirst()) {
            status = cursor.getString(cursor.getColumnIndex(COLUMN_BOOKMARK_STATUS)); // Get the status
            cursor.close(); // Close the cursor
        }

        db.close();
        return status;
    }

    public boolean updateStatusAndTime(String name, String newStatus, String newTime) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Prepare the new values for update
        ContentValues values = new ContentValues();
        values.put(COLUMN_BOOKMARK_STATUS, newStatus); // Update the status
        values.put(COLUMN_TIME, newTime);              // Update the time

        // Update the record in the database where name matches
        int rowsAffected = db.update(TABLE_FILES, values, COLUMN_NAME + "=?", new String[]{name});

        db.close();
        return rowsAffected > 0; // Return true if the update was successful
    }


    public String getFilePath(String url) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FILES, new String[]{COLUMN_PATH},
                COLUMN_URL + "=?", new String[]{url},
                null, null, null, null);

        String path = null;
        if (cursor != null && cursor.moveToFirst()) {
            path = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PATH));
            cursor.close();
        }
        return path;
    }

    public boolean fileExists(String url) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FILES, new String[]{COLUMN_URL},
                COLUMN_URL + "=?", new String[]{url},
                null, null, null, null);

        boolean exists = cursor != null && cursor.getCount() > 0;
        if (cursor != null) {
            cursor.close();
        }
        return exists;
    }

    public void deleteSQLITE() throws IOException {

        // Delete all entries from the SQLite database table
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_FILES);  // Clear your table data
        db.close();

    }

    public List<String> checkStatusAndReturnKeys() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> keys = new ArrayList<>(); // List to store keys of records with true status

        // Query to select all records where status is true, sorted by time
        Cursor cursor = db.query(
                TABLE_FILES,                    // The table to query
                new String[]{COLUMN_BOOKMARK_STATUS, COLUMN_BOOKKEY, COLUMN_TIME}, // The columns to return
                COLUMN_BOOKMARK_STATUS + "=?",  // The columns for the WHERE clause
                new String[]{"true"},            // The values for the WHERE clause
                null,                           // Group the rows
                null,                           // Filter by row groups
                COLUMN_TIME + " ASC"            // Sort order by time in ascending order
        );

        // Loop through the cursor results
        int count = 0;
        if (cursor != null) {
            while (cursor.moveToNext()) {
                count++;
                // Retrieve the status and key from the cursor
                String status = cursor.getString(cursor.getColumnIndex(COLUMN_BOOKMARK_STATUS));
                String key = cursor.getString(cursor.getColumnIndex(COLUMN_BOOKKEY));

                if ("true".equals(status)) {
                    // Show a Toast message if the status is true
                    //Toast.makeText(context, "Status is true! Key: " + key + " Count: " + count, Toast.LENGTH_SHORT).show();
                    keys.add(key); // Add the key to the list
                }
            }
            cursor.close(); // Always close the cursor
        }

        db.close(); // Close the database connection
        return keys; // Return the list of keys with true status
    }

    private boolean deleteFolderContents(File folder) {
        if (folder != null && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteFolderContents(file);
                    }
                    file.delete();  //
                }
            }
            return true;
        }
        return false;
    }




}
