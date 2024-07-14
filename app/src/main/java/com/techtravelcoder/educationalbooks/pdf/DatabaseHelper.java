package com.techtravelcoder.educationalbooks.pdf;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "PDF.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_FILES = "Files";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_URL = "url";
    private static final String COLUMN_PATH = "path";
    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_FILES_TABLE = "CREATE TABLE " + TABLE_FILES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_URL + " TEXT,"
                + COLUMN_PATH + " TEXT"
                + ")";
        try {
            db.execSQL(CREATE_FILES_TABLE);
        } catch (Exception e) {
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This will drop the old tables and create the new ones
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILES);
        onCreate(db);
    }

    public long addFile(String name, String url, String path) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_URL, url);
        values.put(COLUMN_PATH, path);
        long id = db.insert(TABLE_FILES, null, values);
        db.close();
        return id;
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

    public void deleteAllFiles() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_FILES);
        db.close();
    }

    public void deleteFile(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FILES, COLUMN_NAME + "=?", new String[]{name});
        db.close();
    }
}
