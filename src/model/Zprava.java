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
public class Zprava {
    private int idZpravy;
    private String obsahZpravy;
    private LocalDateTime casOdeslani;
    private String autor;
    private List<Kontakt> prijemci;

    public Zprava(int idZpravy, String obsahZpravy, LocalDateTime casOdeslani, String autor) {
        this.idZpravy = idZpravy;
        this.obsahZpravy = obsahZpravy;
        this.casOdeslani = casOdeslani;
        this.autor = autor;
    }
    
    public String getObsahZpravy(){
        return obsahZpravy;
    }
    
    public String getAutor(){
        return autor;
    }
    
    public LocalDateTime getCasOdeslani(){
        return this.casOdeslani;
    }
}
