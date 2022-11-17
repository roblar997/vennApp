package com.example.vennapp.database.models;

import java.sql.Date;
import java.sql.Time;
import java.util.Objects;

public class Avtale {
    private Long _ID;
    private String tid;
    private String dato;
    private String melding;

    public Avtale(){

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Avtale avtale = (Avtale) o;
        return Objects.equals(_ID, avtale._ID) && Objects.equals(tid, avtale.tid) && Objects.equals(dato, avtale.dato) && Objects.equals(melding, avtale.melding);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_ID, tid, dato, melding);
    }

    public Avtale(String dato, String tid, String melding) {
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
