/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Lukas
 */
public class KontaktVypis {
    private int idKontaktu;
    private int idUzivatele;
    private String jmeno;
    private String prijmeni;
    private int prihlasen;
    private int blokace;

    public KontaktVypis(int idKontaktu, int idUzivatele, String jmeno, String prijmeni, int online, int blokace) {
        this.idKontaktu = idKontaktu;
        this.idUzivatele = idUzivatele;
        this.jmeno = jmeno;
        this.prijmeni = prijmeni;
        this.prihlasen = online;
        this.blokace = blokace;
    }

    public int getIdKontaktu() {
        return idKontaktu;
    }

    public void setIdKontaktu(int idKontaktu) {
        this.idKontaktu = idKontaktu;
    }

    public String getJmeno() {
        return jmeno;
    }

    public void setJmeno(String jmeno) {
        this.jmeno = jmeno;
    }

    public String getPrijmeni() {
        return prijmeni;
    }

    public void setPrijmeni(String prijmeni) {
        this.prijmeni = prijmeni;
    }

    public int getBlokace() {
        return blokace;
    }

    public void setBlokace(int blokace) {
        this.blokace = blokace;
    }

    public int getPrihlasen() {
        return prihlasen;
    }

    public void setPrihlasen(int prihlasen) {
        this.prihlasen = prihlasen;
    }

    public int getIdUzivatele() {
        return idUzivatele;
    }

    public void setIdUzivatele(int idUzivatele) {
        this.idUzivatele = idUzivatele;
    }
    
    public String toString(){
        return "Jméno: " + jmeno + " Příjmení: " + prijmeni + " Přihlášen: " + prihlasen + " Blokace: " + blokace;
    }
}
