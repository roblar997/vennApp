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

public class KontaktAvtaleProvider extends ContentProvider {
    public static final String KEY_ID1 = "kontaktId";
    public static final String KEY_ID2 = "avtaleId";
    static String KEY_REF_ID1 = "_ID";
    static String KEY_REF_TABLE1 = "Kontakter";
    static String KEY_REF_ID2 = "_ID";
    static String KEY_REF_TABLE2 = "Avtaler";
    private static final String DB_NAVN = "sharedKontaktAvtaler.db";
    private static final int DB_VERSJON = 1;
    private final static String TABLE_SHARED_KONTAKTAVTALE = "SharedKontaktAvtale";
    public final static String PROVIDER = "com.example.vennapp.contentprovider.KontaktAvtaleProvider";
    private static final int KONTAKTAVTALE = 1;
    private static final int MKONTAKTAVTALE = 2;
    private static final int KONTAKTAVTALESUB1 = 3;
    private static final int KONTAKTAVTALESUB2 = 4;
    private static final int KONTAKTAVTALESUB3 = 5;
    private static final int KONTAKTAVTALESUB4 = 6;

    KontaktAvtaleProvider.DatabaseHelper DBhelper;
    SQLiteDatabase db;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER, "kontaktavtale", MKONTAKTAVTALE);
        uriMatcher.addURI(PROVIDER, "kontaktavtale/#-#", KONTAKTAVTALESUB1);
        uriMatcher.addURI(PROVIDER, "kontaktavtale/*-#", KONTAKTAVTALESUB2);
        uriMatcher.addURI(PROVIDER, "kontaktavtale/#-*", KONTAKTAVTALESUB3);
        uriMatcher.addURI(PROVIDER, "kontaktavtale/*-*", KONTAKTAVTALESUB4);
    }
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DB_NAVN, null, DB_VERSJON);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql ="CREATE TABLE IF NOT EXISTS " + TABLE_SHARED_KONTAKTAVTALE + "("
                    + KEY_ID1 + " INTEGER,"
                    + KEY_ID2 + " INTEGER,"
                    + "FOREIGN KEY("+ KEY_ID1 + ") REFERENCES " + KEY_REF_TABLE1 + "(" + KEY_REF_ID1 + "),"
                    + "FOREIGN KEY("+ KEY_ID2 + ") REFERENCES " + KEY_REF_TABLE2 + "(" + KEY_REF_ID2 + "),"
                    + "PRIMARY KEY(" + KEY_ID1 + "," + KEY_ID2 + "))";

            Log.d("DatabaseHelper ", " oncreated sql " + sql);
            db.execSQL(sql);
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists " + TABLE_SHARED_KONTAKTAVTALE);
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
            case MKONTAKTAVTALE | KONTAKTAVTALESUB1  | KONTAKTAVTALESUB2  | KONTAKTAVTALESUB3  | KONTAKTAVTALESUB4:
                return "vnd.android.cursor.dir/vnd.example.kontaktavtale";
            case KONTAKTAVTALE  :
                return "vnd.android.cursor.item/vnd.example.kontaktavtale";
            default:
                throw new
                        IllegalArgumentException("Ugyldig URI" + uri);
        }
    }
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        db.insert(TABLE_SHARED_KONTAKTAVTALE, null, values);
        Cursor c = db.query(TABLE_SHARED_KONTAKTAVTALE, null, null, null, null, null, null);
        c.moveToLast();
        long minid = c.getLong(0);
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, minid);
    }
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cur = null;
        if (uriMatcher.match(uri) == KONTAKTAVTALE) {
            cur = db.query(TABLE_SHARED_KONTAKTAVTALE, projection, KEY_ID1 + "=" + uri.getPathSegments().get(1), selectionArgs, null, null, sortOrder);
        } else {
            cur = db.query(TABLE_SHARED_KONTAKTAVTALE, null, null, null, null, null, null);
        }
        return cur;
    }
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (uriMatcher.match(uri) == KONTAKTAVTALE) {
            db.update(TABLE_SHARED_KONTAKTAVTALE, values, KEY_ID1 + " = " + uri.getPathSegments().get(1) + " AND " + KEY_ID2 + " = " + uri.getPathSegments().get(2), null);
            getContext().getContentResolver().notifyChange(uri, null);
            return 1;
        }
        if (uriMatcher.match(uri) == MKONTAKTAVTALE) {
            db.update(TABLE_SHARED_KONTAKTAVTALE, null, null, null);
            getContext().getContentResolver().notifyChange(uri, null);
            return 2;
        }
        return 0;
    }
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d("Got path segment part 1 in delete: ", uri.getPathSegments().get(1) + " " );

        if (uriMatcher.match(uri) == KONTAKTAVTALESUB4) {

            Log.d("Case delete all rows: ", uri.getPathSegments().get(1));
            int res = db.delete(TABLE_SHARED_KONTAKTAVTALE, null, null);
            getContext().getContentResolver().notifyChange(uri, null);
            return res;
        }
        else if (uriMatcher.match(uri) == KONTAKTAVTALESUB3) {
            Log.d("Case delete all rows with spesific avtale: ", uri.getPathSegments().get(1));
            String[] selectionArguments = new String[]{uri.getPathSegments().get(1).split("-")[1]};
            int res = db.delete(TABLE_SHARED_KONTAKTAVTALE,  KEY_ID2 + " = ?", selectionArguments);
            getContext().getContentResolver().notifyChange(uri, null);
            return res;
        }
        else if (uriMatcher.match(uri) == KONTAKTAVTALESUB2) {
            Log.d("Case delete all rows with spesific contact: ", uri.getPathSegments().get(1));
            String[] selectionArguments = new String[]{uri.getPathSegments().get(1).split("-")[0]};
            int res = db.delete(TABLE_SHARED_KONTAKTAVTALE, KEY_ID1 + " = ? ", selectionArguments);
            getContext().getContentResolver().notifyChange(uri, null);
            return res;
        }

        else if (uriMatcher.match(uri) == KONTAKTAVTALESUB1) {
            Log.d("Case delete one row: ", uri.getPathSegments().get(1));
            String[] selectionArguments = new String[]{uri.getPathSegments().get(1).split("-")[0],uri.getPathSegments().get(1).split("-")[1]};
            int res = db.delete(TABLE_SHARED_KONTAKTAVTALE, KEY_ID1 + " = ?  AND " + KEY_ID2 + " = ?", selectionArguments);
            getContext().getContentResolver().notifyChange(uri, null);
            return res;
        }

        return 0;
    }


}
