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
public class Role {
    private int idRole;
    private String jmenoRole;
    private String opravneni;
    private String poznamka;
    private boolean selected;

    public Role(int idRole, String jmenoRole, String opravneni, String poznamka) {
        this.idRole = idRole;
        this.jmenoRole = jmenoRole;
        this.opravneni = opravneni;
        this.poznamka = poznamka;
    }

    public int getIdRole() {
        return idRole;
    }

    public String getJmenoRole() {
        return jmenoRole;
    }

    public String getOpravneni() {
        return opravneni;
    }

    public String getPoznamka() {
        return poznamka;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
    public String toString(){
        return "Jméno role: " + jmenoRole + " Oprávnění: " + opravneni;
    }
}
