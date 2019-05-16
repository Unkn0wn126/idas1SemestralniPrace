/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Lukas
 */
public class Prispevek {
    private int idPrispevku;
    private String obsahPrispevku;
    private LocalDateTime casOdeslani;
    private String nazev;
    private int blokace;
    private String tag;
    private List<Prispevek> komentare;

    public Prispevek(int idPrispevku, String obsahPrispevku, LocalDateTime casOdeslani, String nazev, int blokace, String tag, List<Prispevek> komentare) {
        this.idPrispevku = idPrispevku;
        this.obsahPrispevku = obsahPrispevku;
        this.casOdeslani = casOdeslani;
        this.nazev = nazev;
        this.blokace = blokace;
        this.tag = tag;
        this.komentare = komentare;
    }
    
    
}
