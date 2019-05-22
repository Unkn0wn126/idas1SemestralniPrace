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

    public Predmet(int idPredmetu, String nazevPredmetu, String zkratkaPredmetu, String popis) {
        this.idPredmetu = idPredmetu;
        this.nazevPredmetu = nazevPredmetu;
        this.zkratkaPredmetu = zkratkaPredmetu;
        this.popis = popis;
    }
    
    @Override
    public String toString(){
        return String.format("Název: %s, Zkratka: %s, Popis: %s", nazevPredmetu, zkratkaPredmetu, popis);
    }
    
    
}
