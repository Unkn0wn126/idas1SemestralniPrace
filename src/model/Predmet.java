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
public class Predmet {
    private int idPredmetu;
    private String nazevPredmetu;
    private String zkratkaPredmetu;
    private String popis;
    private boolean selected;

    public Predmet(int idPredmetu, String nazevPredmetu, String zkratkaPredmetu, String popis) {
        this.idPredmetu = idPredmetu;
        this.nazevPredmetu = nazevPredmetu;
        this.zkratkaPredmetu = zkratkaPredmetu;
        this.popis = popis;
        this.selected = false;
    }
    
    @Override
    public String toString(){
        return String.format("Název: %s, Zkratka: %s, Popis: %s", nazevPredmetu, zkratkaPredmetu, popis);
    }

    public int getIdPredmetu() {
        return idPredmetu;
    }

    public String getNazevPredmetu() {
        return nazevPredmetu;
    }

    public String getZkratkaPredmetu() {
        return zkratkaPredmetu;
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
    
}
