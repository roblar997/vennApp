package com.example.vennapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.vennapp.database.models.Avtale;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBHandlerAvtale extends SQLiteOpenHelper {

    static String KEY_ID = "_ID";
    static String KEY_DATO = "Dato";
    static String KEY_TID = "Tid";
    static String KEY_MELDING = "Melding";
    static String TABLE_AVTALER = "Avtaler";
//

    static int DATABASE_VERSION = 3;static String DATABASE_NAME = "telefonkontakt";
    public DBHandlerAvtale(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public int oppdaterAvtale(SQLiteDatabase db, Avtale avtale) {
        ContentValues values = new ContentValues();
        values.put(KEY_DATO,avtale.getDato());
        values.put(KEY_TID, avtale.getTid());
        values.put(KEY_MELDING, avtale.getMelding());
        int endret = db.update(TABLE_AVTALER , values, KEY_ID + "= ?",
                new String[]{String.valueOf(avtale.get_ID())});
        return endret;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String LAG_TABELL = "CREATE TABLE IF NOT EXISTS " + TABLE_AVTALER+ "(" + KEY_ID +" INTEGER PRIMARY KEY," + KEY_DATO + " TEXT," + KEY_TID + " TEXT," + KEY_MELDING + " TEXT" +")";
        Log.d("SQL", LAG_TABELL);
        db.execSQL(LAG_TABELL);
    }
    public void slettAvtale(SQLiteDatabase db, Long inn_id) {
        onCreate(db);//Unngå sjeldne bugs, at tabellen ikke finnes
        db.delete(TABLE_AVTALER , KEY_ID + " =? ",
                new String[]{Long.toString(inn_id)});
    }
    public Long getMaxId(SQLiteDatabase db){
        String query = "SELECT MAX ( " + KEY_ID + ") FROM " + TABLE_AVTALER;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                return cursor.getLong(0);
            }
            while (cursor.moveToNext());
        }
        return 1L;
    }
    public List<Avtale> finnAlleAvtalerMedGittDato(SQLiteDatabase db,String date) {
        onCreate(db);//Unngå sjeldne bugs, at tabellen ikke finnes
        List<Avtale> avtaleListe = new ArrayList<Avtale>();
        String selectQuery = "SELECT * FROM " + TABLE_AVTALER + " t1 WHERE t1.Dato='" + date+"'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {

                Avtale avtale = new Avtale();
                avtale.set_ID(cursor.getLong(0));
                avtale.setDato(cursor.getString(1));
                avtale.setTid(cursor.getString(2));
                avtale.setMelding(cursor.getString(3));
                avtaleListe.add(avtale);}
            while (cursor.moveToNext());
            cursor.close();
        }
        return avtaleListe;
    }
    public List<Avtale> finnAlleAvtalerMedGittDato(SQLiteDatabase db, java.sql.Date date) {
        onCreate(db);//Unngå sjeldne bugs, at tabellen ikke finnes
        return finnAlleAvtalerMedGittDato(db,date.toString());

    }
    public List<Avtale> finnAlleAvtaler(SQLiteDatabase db) {
        onCreate(db);//Unngå sjeldne bugs, at tabellen ikke finnes
        List<Avtale> avtaleListe = new ArrayList<Avtale>();
        String selectQuery = "SELECT * FROM " + TABLE_AVTALER;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Avtale avtale = new Avtale();
                avtale.set_ID(cursor.getLong(0));
                avtale.setDato(cursor.getString(1));
                avtale.setTid(cursor.getString(2));
                avtale.setMelding(cursor.getString(3));
                avtaleListe.add(avtale);}
                      while (cursor.moveToNext());
                cursor.close();
             }
              return avtaleListe;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AVTALER );

    }

    public void leggTilAvtale(SQLiteDatabase db, Avtale avtale) {
        onCreate(db);//Unngå sjeldne bugs, at tabellen ikke finnes
        ContentValues values = new ContentValues();
        values.put(KEY_DATO,  avtale.getDato());
        values.put(KEY_TID, avtale.getTid());
        values.put(KEY_MELDING, avtale.getMelding());
        db.insert(TABLE_AVTALER , null, values);
    }
    @Override
    public void close() {
        super.close();

    }
}
