/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Date;

/**
 *
 * @author Lukas
 */
public class Kontakt {
    private String idKontaktu;
    private String idUzivatele;
    private Date datumOd;
    private Date datumDo;
    private int platnost;
    private String poznamka;

    public Kontakt(String idKontaktu, String idUzivatele, Date datumOd, Date datumDo, int platnost, String poznamka) {
        this.idKontaktu = idKontaktu;
        this.idUzivatele = idUzivatele;
        this.datumOd = datumOd;
        this.datumDo = datumDo;
        this.platnost = platnost;
        this.poznamka = poznamka;
    }

    public String getIdKontaktu() {
        return idKontaktu;
    }

    public String getIdUzivatele() {
        return idUzivatele;
    }

    public Date getDatumOd() {
        return datumOd;
    }

    public Date getDatumDo() {
        return datumDo;
    }

    public int getPlatnost() {
        return platnost;
    }

    public String getPoznamka() {
        return poznamka;
    }
    
    @Override
    public String toString(){
        return Integer.toString(platnost);
    }
    
    
}
