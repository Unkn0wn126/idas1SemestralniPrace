/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Date;

/**
 *
 * @author Lukas
 */
public class Kontakt {
    private int idKontaktu;
    private int idUzivatele;

    public Kontakt(int idKontaktu, int idUzivatele) {
        this.idKontaktu = idKontaktu;
        this.idUzivatele = idUzivatele;
    }

    public int getIdKontaktu() {
        return idKontaktu;
    }

    public int getIdUzivatele() {
        return idUzivatele;
    }
}
