package com.example.vennapp.database.models;

import java.sql.Date;
import java.sql.Time;

public class Avtale {
    private Long _ID;
    private String tid;
    private String dato;
    private String melding;

    public Avtale(){

    }
    public Avtale(String dato,String tid,String melding) {
        this.tid = tid;
        this.dato = dato;
        this.melding = melding;
    }



    public String getDato() {
        return dato;
    }

    public String getTid() {
        return tid;
    }

    public Long get_ID() {
        return _ID;
    }

    public void set_ID(Long _ID) {
        this._ID = _ID;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }


    public void setTid(String tid) {
        this.tid = tid;
    }
    public String getMelding() {
        return melding;
    }

    public void setMelding(String melding) {
        this.melding = melding;
    }
}
