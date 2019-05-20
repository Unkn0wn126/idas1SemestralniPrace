/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.time.LocalDate;

/**
 *
 * @author Lukas
 */
public class StudijniObor { // TODO: Aktualizovat podle současných tabulek
    private int idOboru;
    private String nazev;
    private String zkratka;
    private String popis;
    private LocalDate akreditaceDo;

    public StudijniObor(int idOboru, String nazev, String zkratka, String popis, LocalDate akreditaceDo) {
        this.idOboru = idOboru;
        this.nazev = nazev;
        this.zkratka = zkratka;
        this.popis = popis;
        this.akreditaceDo = akreditaceDo;
    }

    public int getIdOboru() {
        return idOboru;
    }

    public void setIdOboru(int idOboru) {
        this.idOboru = idOboru;
    }

    public String getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    public String getZkratka() {
        return zkratka;
    }

    public void setZkratka(String zkratka) {
        this.zkratka = zkratka;
    }

    public String getPopis() {
        return popis;
    }

    public void setPopis(String popis) {
        this.popis = popis;
    }

    public LocalDate getAkreditaceDo() {
        return akreditaceDo;
    }

    public void setAkreditaceDo(LocalDate akreditaceDo) {
        this.akreditaceDo = akreditaceDo;
    }
    
    @Override
    public String toString(){
        return String.format("%s", nazev);
    }
    
    
}
