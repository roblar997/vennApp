package com.example.vennapp.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AvtaleProvider extends ContentProvider {
    public static final String _ID = "_id";
    public static final String TITTEL = "Tittel";
    private static final String DB_NAVN = "sharedAvtaler.db";
    private static final int DB_VERSJON = 1;
    static String KEY_DATO = "Dato";
    static String KEY_TID = "Tid";
    static String KEY_MELDING = "Melding";
    private final static String TABLE_SHARED_AVTALER = "SharedAvtaler";
    public final static String PROVIDER = "com.example.vennapp.contentprovider.AvtaleProvider";
    private static final int AVTALE = 1;
    private static final int MAVTALE = 2;
    AvtaleProvider.DatabaseHelper DBhelper;
    SQLiteDatabase db;
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER + "/avtale");
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER, "avtale", MAVTALE);
        uriMatcher.addURI(PROVIDER, "avtale/#", AVTALE);
    }
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DB_NAVN, null, DB_VERSJON);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql= "CREATE TABLE IF NOT EXISTS " + TABLE_SHARED_AVTALER+ "(" + _ID +" INTEGER PRIMARY KEY," + KEY_DATO + " TEXT," + KEY_TID + " TEXT," + KEY_MELDING + " TEXT" +")";
            Log.d("DatabaseHelper ", " oncreated sql " + sql);
            db.execSQL(sql);
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists " + TABLE_SHARED_AVTALER);
            Log.d("DatabaseHelper", "updated");
            onCreate(db);
        }
    }
    @Override
    public boolean onCreate() {
        DBhelper = new DatabaseHelper(getContext());
        db = DBhelper.getWritableDatabase();
        return true;
    }



    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case MAVTALE:
                return "vnd.android.cursor.dir/vnd.example.avtale";
            case AVTALE:
                return "vnd.android.cursor.item/vnd.example.avtale";
            default:
                throw new
                        IllegalArgumentException("Ugyldig URI" + uri);
        }
    }
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        db.insert(TABLE_SHARED_AVTALER, null, values);
        Cursor c = db.query(TABLE_SHARED_AVTALER, null, null, null, null, null, null);
        c.moveToLast();
        long minid = c.getLong(0);
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, minid);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cur = null;
        if (uriMatcher.match(uri) == AVTALE) {
            cur = db.query(TABLE_SHARED_AVTALER, projection, _ID + "=" + uri.getPathSegments().get(1), selectionArgs, null, null, sortOrder);
            return cur;
        } else {
            cur = db.query(TABLE_SHARED_AVTALER, null, null, null, null, null, null);
            return cur;
        }
    }
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (uriMatcher.match(uri) == AVTALE) {
            db.update(TABLE_SHARED_AVTALER, values, _ID + " = " + uri.getPathSegments().get(1), null);
            getContext().getContentResolver().notifyChange(uri, null);
            return 1;
        }
        if (uriMatcher.match(uri) == MAVTALE) {
            db.update(TABLE_SHARED_AVTALER, null, null, null);
            getContext().getContentResolver().notifyChange(uri, null);
            return 2;
        }
        return 0;
    }
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (uriMatcher.match(uri) == AVTALE) {
            db.delete(TABLE_SHARED_AVTALER, _ID + " = " + uri.getPathSegments().get(1), selectionArgs);
            getContext().getContentResolver().notifyChange(uri, null);
            return 1;
        }
        if (uriMatcher.match(uri) == MAVTALE) {
            db.delete(TABLE_SHARED_AVTALER, null, null);
            getContext().getContentResolver().notifyChange(uri, null);
            return 2;
        }
        return 0;
    }
}
