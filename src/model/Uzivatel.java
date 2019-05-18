/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lukas
 */
public class Uzivatel {

    private String idUzivatele;
    private String jmeno;
    private String prijmeni;
    private String email;
    private String login;
    private int rokStudia;
    private int blokace;
    private List<Kontakt> kontakty;
    private String poznamka;
    private int online = 0;
    private boolean admin;

    public Uzivatel(String idUzivatele, String jmeno, String prijmeni, String email, String login, int rokStudia, int blokace, String poznamka) {
        this.idUzivatele = idUzivatele;
        this.jmeno = jmeno;
        this.prijmeni = prijmeni;
        this.email = email;
        this.login = login;
        this.rokStudia = rokStudia;
        this.blokace = blokace;
        this.poznamka = poznamka;
        this.kontakty = new ArrayList<>();
        this.admin = true;
    }

    public String getIdUzivatele() {
        return idUzivatele;
    }

    public String getJmeno() {
        return jmeno;
    }

    public String getPrijmeni() {
        return prijmeni;
    }

    public String getEmail() {
        return email;
    }

    public int getRokStudia() {
        return rokStudia;
    }

    public int getBlokace() {
        return blokace;
    }

    public List<Kontakt> getKontakty() {
        return kontakty;
    }

    public String getPoznamka() {
        return poznamka;
    }

    public int isOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public boolean isAdmin() {
        return admin;
    }
    
    @Override
    public String toString(){
        return String.format("Jméno: %s, Příjmení: %s, E-mail: %s, "
                + "Rok studia: %d, Blokace: %d, Poznámka: %s", jmeno, prijmeni, 
                email, rokStudia, blokace, poznamka);
    }
}
