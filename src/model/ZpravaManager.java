/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lukas
 */
public class ZpravaManager {
        private Connection con;
    private final String SELECT_ZPRAVY = "SELECT * FROM ZPRAVY";
    private final String SELECT_ZPRAVA = "SELECT * FROM ZPRAVY WHERE id_zpravy = ?";
    private final String INSERT_ZPRAVA = "INSERT INTO ZPRAVY(nazev,obsah_zpravy,cas_odeslani, odesilatel) VALUES (?,?,?,?)";
    private final String DELETE = "DELETE FROM ZPRAVY WHERE id_zpravy = ?";
    private final String UPDATE_ZPRAVA = "UPDATE ZPRAVY SET nazev = ?, obsah_zpravy = ?  where id_zpravy = ?";

    public ZpravaManager(Connection con) {
        this.con = con;
    }

    /**
     * Vybere všechny zprávy z databáze
     *
     * @return seznam zpráv
     * @throws SQLException
     */
    public List<Zprava> selectZpravy() throws SQLException {
        List<Zprava> listSelect = new ArrayList<>();
        PreparedStatement prepare = con.prepareStatement(SELECT_ZPRAVY);
        ResultSet result = prepare.executeQuery();

        while (result.next()) {
            listSelect.add(new Zprava(result.getInt("id_zpravy"), result.getString("nazev"), result.getTimestamp("cas_odeslani").toLocalDateTime(), result.getString("odesilatel")));

        }
        return listSelect;
    }

    /**
     * Vybere zprávu podle id
     *
     * @param idRole id zprávy
     * @return zpráva podle id
     * @throws SQLException
     */
    public Zprava selectZprava(int idRole) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(SELECT_ZPRAVA);
        prepare.setInt(1, idRole);
        Zprava zprava;
        ResultSet result = prepare.executeQuery();
        zprava = new Zprava(result.getInt("id_zpravy"), 
                result.getString("nazev"), 
                result.getTimestamp("cas_odeslani").toLocalDateTime(), 
                result.getString("odesilatel"));
        return zprava;
    }

    /**
     * Vloží zprávu do databáze
     *
     * @param nazev název zprávy
     * @param obsah obsah zprávy
     * @param odesilatel odesilatel zprávy
     * @throws SQLException
     */
    public void insertZprava(String nazev, String obsah, String odesilatel) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(INSERT_ZPRAVA);
        prepare.setString(1, nazev);
        prepare.setString(2, obsah);
        prepare.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
        prepare.setString(4, odesilatel);
        prepare.execute();
        con.commit();
    }

    /**
     * Vymaže zprávu z databáze
     *
     * @param idZpravy id zprávy pro smazání
     * @throws SQLException
     */
    public void deleteZprava(int idZpravy) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(DELETE);
        prepare.setInt(1, idZpravy);
        prepare.execute();
        con.commit();
    }

    
    /**
     * Aktualizuje zprávu dle parametrů
     * @param nazev název zprávy
     * @param obsah obsah zprávy
     * @param idZpravy id zprávy
     * @throws SQLException 
     */
    public void updateZprava(String nazev, String obsah, int idZpravy) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(UPDATE_ZPRAVA);
        prepare.setString(1, nazev);
        prepare.setString(2, obsah);
        prepare.setInt(3, idZpravy);

        prepare.execute();
        con.commit();
    }
}
