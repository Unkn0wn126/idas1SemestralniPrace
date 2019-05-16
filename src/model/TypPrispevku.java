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
public class TypPrispevku {
    private int idTypuPrispevku;
    private String nazev;
    private String zkratka;

    public TypPrispevku(int idTypuPrispevku, String nazev, String zkratka) {
        this.idTypuPrispevku = idTypuPrispevku;
        this.nazev = nazev;
        this.zkratka = zkratka;
    }
    
    
}
