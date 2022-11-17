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

public class KontaktProvider extends ContentProvider {
    public static final String _ID = "_id";

    private static final String DB_NAVN = "sharedKontakter.db";
    private static final int DB_VERSJON = 1;
    private final static String TABLE_SHARED_KONTAKTER = "SharedKontakter";
    public final static String PROVIDER = "com.example.vennapp.contentprovider.KontaktProvider";
    private static final int KONTAKT = 1;
    private static final int MKONTAKT = 2;
    static String KEY_FORNAVN = "Fornavn";
    static String KEY_ETTERNAVN = "Etternavn";
    static String KEY_TELEFON = "Telefon";


    KontaktProvider.DatabaseHelper DBhelper;
    SQLiteDatabase db;
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER + "/kontakt ");
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER, "kontakt", KONTAKT);
        uriMatcher.addURI(PROVIDER, "kontakt/#", MKONTAKT);
    }
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DB_NAVN, null, DB_VERSJON);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql= "CREATE TABLE IF NOT EXISTS " + TABLE_SHARED_KONTAKTER + "(" + _ID +" INTEGER PRIMARY KEY ," + KEY_FORNAVN + " TEXT," + KEY_ETTERNAVN + " TEXT," + KEY_TELEFON + " TEXT" + ")";
            Log.d("DatabaseHelper ", " oncreated sql " + sql);
            db.execSQL(sql);
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists " + TABLE_SHARED_KONTAKTER);
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
            case MKONTAKT:
                return "vnd.android.cursor.dir/vnd.example.kontakt";
            case KONTAKT:
                return "vnd.android.cursor.item/vnd.example.kontakt";
            default:
                throw new
                        IllegalArgumentException("Ugyldig URI" + uri);
        }
    }
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        db.insert(TABLE_SHARED_KONTAKTER, null, values);
        Cursor c = db.query(TABLE_SHARED_KONTAKTER, null, null, null, null, null, null);
        c.moveToLast();
        long minid = c.getLong(0);
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, minid);
    }
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cur = null;
        if (uriMatcher.match(uri) == KONTAKT) {
            cur = db.query(TABLE_SHARED_KONTAKTER, projection, _ID + "=" + uri.getPathSegments().get(1), selectionArgs, null, null, sortOrder);
            return cur;
        } else {
            cur = db.query(TABLE_SHARED_KONTAKTER, null, null, null, null, null, null);
            return cur;
        }
    }
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (uriMatcher.match(uri) == KONTAKT) {
            db.update(TABLE_SHARED_KONTAKTER, values, _ID + " = " + Long.parseLong(uri.getPathSegments().get(1)), null);
            getContext().getContentResolver().notifyChange(uri, null);
            return 1;
        }
        if (uriMatcher.match(uri) == MKONTAKT) {
            db.update(TABLE_SHARED_KONTAKTER, null, null, null);
            getContext().getContentResolver().notifyChange(uri, null);
            return 2;
        }
        return 0;
    }
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (uriMatcher.match(uri) == KONTAKT) {
            String[] selectionArguments = new String[]{uri.getPathSegments().get(1)};
            db.delete(TABLE_SHARED_KONTAKTER, _ID + " = ?", selectionArguments);
            getContext().getContentResolver().notifyChange(uri, null);
            return 1;
        }
        if (uriMatcher.match(uri) == MKONTAKT) {
            db.delete(TABLE_SHARED_KONTAKTER, null, null);
            getContext().getContentResolver().notifyChange(uri, null);
            return 2;
        }
        return 0;
    }
}
