package com.example.vennapp.database.models;

public class Kontakt {
    private Long _ID;
    private String fornavn;
    private String etternavn;
    private String telefonNummer;
    public Kontakt() {

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
