/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.time.LocalDateTime;
/**
 *
 * @author Lukas
 */
public class Zprava { // TODO: Aktualizovat podle současných tabulek
    private int idZpravy;
    private String obsahZpravy;
    private LocalDateTime casOdeslani;
    private String jmenoAutora;

    public Zprava(int idZpravy, String obsahZpravy, LocalDateTime casOdeslani, String jmenoAutora) {
        this.idZpravy = idZpravy;
        this.obsahZpravy = obsahZpravy;
        this.casOdeslani = casOdeslani;
        this.jmenoAutora = jmenoAutora;
    }
    
    public Zprava(String obsahZpravy, LocalDateTime casOdeslani, String jmenoAutora) {
        this.obsahZpravy = obsahZpravy;
        this.casOdeslani = casOdeslani;
        this.jmenoAutora = jmenoAutora;
    }
    
    public String getObsahZpravy(){
        return obsahZpravy;
    }
    
    public String getJmenoAutora(){
        return jmenoAutora;
    }
    
    public LocalDateTime getCasOdeslani(){
        return this.casOdeslani;
    }

    public int getIdZpravy() {
        return idZpravy;
    }
}
