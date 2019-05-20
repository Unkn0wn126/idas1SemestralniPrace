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
public class Skupina { // TODO: Aktualizovat podle současných tabulek
    private int idPrispevku;
    private int idStudijnihoPlanu;

    public Skupina(int idPrispevku, int idStudijnihoPlanu) {
        this.idPrispevku = idPrispevku;
        this.idStudijnihoPlanu = idStudijnihoPlanu;
    }
    
    @Override
    public String toString(){
        return "Skupina";
    }
}
