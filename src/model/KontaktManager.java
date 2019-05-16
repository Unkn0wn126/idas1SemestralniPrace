/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lukas
 */
public class KontaktManager {

    private Connection con;
    private final String SELECT_KONTAKTY = "SELECT * FROM KONTAKTY WHERE uzivatele_id_uzivatele = ?";
    private final String SELECT_KONTAKT = "SELECT * FROM KONTAKTY WHERE id_kontaktu = ? AND uzivatele_id_uzivatele = ?";
    private final String INSERT_KONTAKT = "INSERT INTO KONTAKTY(id_kontaktu,uzivatele_id_uzivatele,datum_od, datum_do, platnost, poznamka) VALUES (?,?,?,?,?,?)";
    private final String DELETE = "DELETE FROM KONTAKTY WHERE id_kontaktu = ? AND uzivatele_id_uzivatele = ?";

    public KontaktManager(Connection con) {
        this.con = con;
    }

    /**
     * Vybere všechny kontakty daného uživatele
     * @param id_uzivatele id uživatele
     * @return seznam kontaktů uživatele
     * @throws SQLException 
     */
    public List<Kontakt> selectKontakty(String id_uzivatele) throws SQLException {
        List<Kontakt> listSelect = new ArrayList<>();
        PreparedStatement prepare = con.prepareStatement(SELECT_KONTAKTY);
        prepare.setString(1, id_uzivatele);
        ResultSet result = prepare.executeQuery();

        while (result.next()) {
            listSelect.add(new Kontakt(result.getString("id_kontaktu"),
                    result.getString("uzivatele_id_uzivatele"),
                    result.getDate("datum_od"), result.getDate("datum_do"),
                    result.getInt("platnost"), result.getString("poznamka")));

        }
        return listSelect;
    }

    /**
     * Vybere kontakt uživatele
     * @param id_kontaktu id kontaktu
     * @param id_uzivatele id uživatele
     * @return kontakt
     * @throws SQLException 
     */
    public Kontakt selectKontakt(String id_kontaktu, String id_uzivatele) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(SELECT_KONTAKT);
        prepare.setString(1, id_kontaktu);
        prepare.setString(2, id_uzivatele);
        Kontakt kontakt;
        ResultSet result = prepare.executeQuery();
        kontakt = new Kontakt(result.getString("id_kontaktu"),
                result.getString("uzivatele_id_uzivatele"),
                result.getDate("datum_od"), result.getDate("datum_do"),
                result.getInt("platnost"), result.getString("poznamka"));
        return kontakt;
    }

    /**
     * Vloží kontakt do databáze
     * @param id_kontaktu id kontaktu
     * @param id_uzivatele id uživatele
     * @param datumOd počáteční platnost
     * @param datumDo koncová platnost
     * @param poznamka poznámka
     * @throws SQLException 
     */
    public void insertKontakt(String id_kontaktu, String id_uzivatele, LocalDate datumOd, LocalDate datumDo, String poznamka) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(INSERT_KONTAKT);
        prepare.setString(1, id_kontaktu);
        prepare.setString(2, id_uzivatele);
        prepare.setDate(3, Date.valueOf(datumOd));
        prepare.setDate(4, Date.valueOf(datumDo));
        prepare.setInt(5, 1);
        prepare.setString(6, poznamka);

        prepare.execute();
        con.commit();
    }

    /**
     * Smaže kontakt z databáze
     * @param idKontaktu id kontaktu
     * @param idUzivatele id uživatele
     * @throws SQLException 
     */
    public void deleteKontakt(String idKontaktu, String idUzivatele) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(DELETE);
        prepare.setString(1, idKontaktu);
        prepare.setString(2, idUzivatele);
        prepare.execute();
        con.commit();
    }
}
