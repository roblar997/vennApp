package com.example.vennapp.database.models;

import java.util.Objects;

public class Kontakt {
    private Long _ID;
    private String fornavn;
    private String etternavn;
    private String telefonNummer;
    public Kontakt() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Kontakt kontakt = (Kontakt) o;
        return Objects.equals(_ID, kontakt._ID) && Objects.equals(fornavn, kontakt.fornavn) && Objects.equals(etternavn, kontakt.etternavn) && Objects.equals(telefonNummer, kontakt.telefonNummer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_ID, fornavn, etternavn, telefonNummer);
    }

    public Kontakt(String fornavn, String etternavn, String telefonNummer) {
        this.fornavn = fornavn;
        this.etternavn = etternavn;
        this.telefonNummer = telefonNummer;
    }

    public Long get_ID() {
        return _ID;
    }

    public void set_ID(Long _ID) {
        this._ID = _ID;
    }

    public String getEtternavn() {
        return etternavn;
    }

    public String getFornavn() {
        return fornavn;
    }

    public String getTelefonNummer() {
        return telefonNummer;
    }


    public void setEtternavn(String etternavn) {
        this.etternavn = etternavn;
    }

    public void setFornavn(String fornavn) {
        this.fornavn = fornavn;
    }

    public void setTelefonNummer(String telefonNummer) {
        this.telefonNummer = telefonNummer;
    }

}
