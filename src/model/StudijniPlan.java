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
public class StudijniPlan { // TODO: Aktualizovat podle současných tabulek
    private int idPlanu;
    private String nazev;
    private int idOboru;
    private String popis;

    public StudijniPlan(int idPlanu, String nazev, int idOboru, String popis) {
        this.idPlanu = idPlanu;
        this.nazev = nazev;
        this.idOboru = idOboru;
        this.popis = popis;
    }
}
