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

    private int idUzivatele;
    private String jmeno;
    private String prijmeni;
    private String email;
    private String login;
    private String heslo;
    private int rokStudia;
    private int blokace;
    private List<Kontakt> kontakty;
    private List<Role> role;
    private String poznamka;
    private int online = 0;
    private boolean admin = false;

    public Uzivatel(int idUzivatele, String jmeno, String prijmeni, String email, String login, int rokStudia, int blokace, String poznamka) {
        this.idUzivatele = idUzivatele;
        this.jmeno = jmeno;
        this.prijmeni = prijmeni;
        this.email = email;
        this.login = login;
        this.rokStudia = rokStudia;
        this.blokace = blokace;
        this.poznamka = poznamka;
        this.kontakty = new ArrayList<>();
        this.admin = false;
    }
    
    public Uzivatel(int idUzivatele, String jmeno, String prijmeni, String email, int rokStudia, int blokace, String poznamka) {
        this.idUzivatele = idUzivatele;
        this.jmeno = jmeno;
        this.prijmeni = prijmeni;
        this.email = email;
        this.rokStudia = rokStudia;
        this.blokace = blokace;
        this.poznamka = poznamka;
        this.kontakty = new ArrayList<>();
        this.admin = false;
    }
    
    public Uzivatel(String jmeno, String prijmeni, String email, int rokStudia, String poznamka, String heslo) {
        this.jmeno = jmeno;
        this.prijmeni = prijmeni;
        this.email = email;
        this.rokStudia = rokStudia;
        this.blokace = 0;
        this.poznamka = poznamka;
        this.kontakty = new ArrayList<>();
        this.admin = false;
        this.heslo = heslo;
    }
    
    

    public int getIdUzivatele() {
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

    public void setBlokace(int blokace) {
        this.blokace = blokace;
    }

    public List<Kontakt> getKontakty() {
        return kontakty;
    }

    private void computeRights() {
        for (Role role1 : role) {
            if (role1.getIdRole() == 1) {
                admin = true;
            }
        }
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

    public List<Role> getRole() {
        return role;
    }

    public void setRole(List<Role> role) {
        this.role = role;
        computeRights();
    }

    public String getHeslo() {
        return heslo;
    }

    public void setHeslo(String heslo) {
        this.heslo = heslo;
    }

    public void setJmeno(String jmeno) {
        this.jmeno = jmeno;
    }

    public void setPrijmeni(String prijmeni) {
        this.prijmeni = prijmeni;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRokStudia(int rokStudia) {
        this.rokStudia = rokStudia;
    }

    public void setPoznamka(String poznamka) {
        this.poznamka = poznamka;
    }
    
    

    @Override
    public String toString() {
        return String.format("Jméno: %s, Příjmení: %s, E-mail: %s, "
                + "Rok studia: %d, Blokace: %d, Poznámka: %s", jmeno, prijmeni,
                email, rokStudia, blokace, poznamka);
    }
}
