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
    private List<Prispevek> komentare;
    private int priorita;
    private int idAutora;
    private int idRodice;
    private String jmenoAutora;

    public Prispevek(int idPrispevku, String obsahPrispevku, LocalDateTime casOdeslani, List<Prispevek> komentare, int blokace, int priorita, int idAutora, String nazev, String jmenoAutora) {
        this.idPrispevku = idPrispevku;
        this.obsahPrispevku = obsahPrispevku;
        this.casOdeslani = casOdeslani;
        this.komentare = komentare;
        this.blokace = blokace;
        this.priorita = priorita;
        this.idAutora = idAutora;
        this.nazev = nazev;
        this.jmenoAutora = jmenoAutora;
    }

    public Prispevek(String obsahPrispevku, LocalDateTime casOdeslani, int idAutora, int idRodice) {
        this.obsahPrispevku = obsahPrispevku;
        this.casOdeslani = casOdeslani;
        this.idAutora = idAutora;
        this.idRodice = idRodice;
    }
    
    public Prispevek(String obsahPrispevku, LocalDateTime casOdeslani, int blokace, int priorita, int idAutora, String nazev) {
        this.obsahPrispevku = obsahPrispevku;
        this.casOdeslani = casOdeslani;
        this.blokace = blokace;
        this.priorita = priorita;
        this.idAutora = idAutora;
        this.nazev = nazev;
    }
    
    public Prispevek(String obsahPrispevku, LocalDateTime casOdeslani, int blokace, int priorita, int idAutora, String nazev, String jmenoAutora) {
        this.obsahPrispevku = obsahPrispevku;
        this.casOdeslani = casOdeslani;
        this.blokace = blokace;
        this.priorita = priorita;
        this.idAutora = idAutora;
        this.nazev = nazev;
        this.jmenoAutora = jmenoAutora;
    }
    
    public Prispevek(String obsahPrispevku, LocalDateTime casOdeslani, int blokace, int priorita, int idAutora, String nazev, int idRodice) {
        this.obsahPrispevku = obsahPrispevku;
        this.casOdeslani = casOdeslani;
        this.blokace = blokace;
        this.priorita = priorita;
        this.idAutora = idAutora;
        this.nazev = nazev;
        this.idRodice = idRodice;
    }

    public Prispevek(int idPrispevku, String obsahPrispevku, LocalDateTime casOdeslani, int blokace, int priorita, int idAutora, String nazev, String jmenoAutora) {
        this.idPrispevku = idPrispevku;
        this.obsahPrispevku = obsahPrispevku;
        this.casOdeslani = casOdeslani;
        this.blokace = blokace;
        this.priorita = priorita;
        this.idAutora = idAutora;
        this.nazev = nazev;
        this.jmenoAutora = jmenoAutora;
    }

    public String getObsahPrispevku() {
        return this.obsahPrispevku;
    }

    public LocalDateTime getCasOdeslani() {
        return this.casOdeslani;
    }

    public String getNazev() {
        return this.nazev;
    }

    public List<Prispevek> getKomentare() {
        return this.komentare;
    }

    public int getIdPrispevku() {
        return idPrispevku;
    }

    public int getBlokace() {
        return blokace;
    }

    public int getPriorita() {
        return priorita;
    }

    public int getIdAutora() {
        return idAutora;
    }

    public void setObsahPrispevku(String obsahPrispevku) {
        this.obsahPrispevku = obsahPrispevku;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    public void setBlokace(int blokace) {
        this.blokace = blokace;
    }

    public void setPriorita(int priorita) {
        this.priorita = priorita;
    }

    public void setKomentare(List<Prispevek> komentare) {
        this.komentare = komentare;
    }

    public String getJmenoAutora() {
        return jmenoAutora;
    }

    public int getIdRodice() {
        return idRodice;
    }
    
    

}
