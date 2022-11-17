package com.example.vennapp.database.models;

import java.util.Objects;

public class KontaktAvtale {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KontaktAvtale that = (KontaktAvtale) o;
        return Objects.equals(kontaktId, that.kontaktId) && Objects.equals(avtaleId, that.avtaleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kontaktId, avtaleId);
    }

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
