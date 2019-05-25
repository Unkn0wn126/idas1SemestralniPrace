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
public class StudijniPlan {
    private int idPlanu;
    private String nazev;
    private int idOboru;
    private String popis;
    private boolean selected;

    public StudijniPlan(int idPlanu, String nazev, int idOboru, String popis) {
        this.idPlanu = idPlanu;
        this.nazev = nazev;
        this.idOboru = idOboru;
        this.popis = popis;
    }
    
    public StudijniPlan(String nazev, int idOboru, String popis) {
        this.nazev = nazev;
        this.idOboru = idOboru;
        this.popis = popis;
    }

    public int getIdPlanu() {
        return idPlanu;
    }

    public String getNazev() {
        return nazev;
    }

    public int getIdOboru() {
        return idOboru;
    }

    public String getPopis() {
        return popis;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setIdPlanu(int idPlanu) {
        this.idPlanu = idPlanu;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    public void setIdOboru(int idOboru) {
        this.idOboru = idOboru;
    }

    public void setPopis(String popis) {
        this.popis = popis;
    }
    
    public String toString(){
        return String.format("Název plánu: %s Popis: %s", nazev, popis);
    }
}
