package com.example.vennapp.database.models;

public class KontaktAvtale {
    private Long kontaktId;
    private Long avtaleId;

    public Long getAvtaleId() {
        return avtaleId;
    }

    public Long getKontaktId() {
        return kontaktId;
    }

    public void setAvtaleId(Long avtaleId) {
        this.avtaleId = avtaleId;
    }

    public void setKontaktId(Long kontaktId) {
        this.kontaktId = kontaktId;
    }
    public KontaktAvtale(Long kontaktId, Long avtaleId){
        this.kontaktId = kontaktId;
        this.avtaleId = avtaleId;
    }
    public  KontaktAvtale(){

    }
}
