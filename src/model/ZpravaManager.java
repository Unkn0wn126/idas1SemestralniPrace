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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lukas
 */
public class ZpravaManager {

    private Connection con;
    private final String SELECT_LAST_MESSAGE = "SELECT max (id_zpravy) FROM ZPRAVY_POHLED";
    private final String CREATE_VIEW = "CREATE OR REPLACE VIEW ZPRAVY_POHLED AS SELECT * FROM ZPRAVY";
    private final String SELECT_ZPRAVY = "SELECT * FROM ZPRAVY_POHLED";
    private final String SELECT_ZPRAVY_KONTAKTU = "SELECT * FROM ZPRAVY"
            + " INNER JOIN ZPRAVY_KONTAKTU ON ZPRAVY_KONTAKTU.ZPRAVY_ID_ZPRAVY = ZPRAVY.ID_ZPRAVY"
            + " INNER JOIN KONTAKTY ON ZPRAVY_KONTAKTU.KONTAKTY_ID_KONTAKTU = KONTAKTY.ID_KONTAKTU" // TODO: Dodělat logiku přijímání zpráv
            + " WHERE KONTAKTY.ID_KONTAKTU = ?";
    private final String SELECT_JMENO_AUTORA = "SELECT jmeno WHERE id_uzivatele = ?";
    private final String SELECT_ZPRAVA = "SELECT * FROM ZPRAVY_POHLED WHERE id_zpravy = ?";
    private final String INSERT_ZPRAVA = "INSERT INTO ZPRAVY(nazev,obsah_zpravy,cas_odeslani, odesilatel) VALUES (?,?,?,?)";
    private final String INSERT_ZPRAVY_KONTAKTU = "INSERT INTO ZPRAVY_KONTAKTU(zpravy_id_zpravy, kontakty_id_kontaktu) VALUES (?, ?)";
    private final String POSLAT_ZPRAVU = "declare"
            + " vRowid number(38,0)"
            + " begin"
            + " insert into ZPRAVY (NAZEV, OBSAH_ZPRAVY, CAS_ODESLANI, ODESILATEL)"
            + " values (?, ?, ?, ?)"
            + " returning ZPRAVY.ID_ZPRAVY into vRowid"
            + " insert into ZPRAVY_KONTAKTU (ZPRAVY_ID_ZPRAVY, KONTAKTY_ID_KONTAKTU) values (vRowid, ?)"
            + " end";
    private final String DELETE = "DELETE FROM ZPRAVY WHERE id_zpravy = ?";
    private final String UPDATE_ZPRAVA = "UPDATE ZPRAVY SET nazev = ?, obsah_zpravy = ?  where id_zpravy = ?";

    public ZpravaManager(Connection con) {
        this.con = con;
        try {
            createView();
        } catch (SQLException ex) {
            Logger.getLogger(UzivatelManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void createView() throws SQLException {
        PreparedStatement prepare = con.prepareStatement(CREATE_VIEW);
        prepare.execute();
        con.commit();
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
            String jmeno = selectJmenoAutora(result.getString("odesilatel"));
            listSelect.add(new Zprava(result.getInt("id_zpravy"), result.getString("obsah_zpravy"), result.getTimestamp("cas_odeslani").toLocalDateTime(), jmeno));

        }
        return listSelect;
    }

    private String selectJmenoAutora(String idAutora) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(SELECT_JMENO_AUTORA);
        prepare.setString(1, idAutora);
        ResultSet result = prepare.executeQuery();

        result.next();
        return result.getString("jmeno");
    }

    /**
     * Vybere všechny zprávy z databáze
     *
     * @return seznam zpráv
     * @throws SQLException
     */
    public List<Zprava> selectZpravyKontaktu(String idKontaktu) throws SQLException {
        List<Zprava> listSelect = new ArrayList<>();
        PreparedStatement prepare = con.prepareStatement(SELECT_ZPRAVY_KONTAKTU);
        prepare.setString(1, idKontaktu);
        ResultSet result = prepare.executeQuery();

        while (result.next()) {
            listSelect.add(new Zprava(result.getInt("id_zpravy"), result.getString("obsah_zpravy"), result.getTimestamp("cas_odeslani").toLocalDateTime(), result.getString("odesilatel")));

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
    public void insertZprava(String nazev, String obsah, String odesilatel, String id_kontaktu) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(INSERT_ZPRAVA);
        prepare.setString(1, nazev);
        prepare.setString(2, obsah);
        prepare.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
        prepare.setString(4, odesilatel);
        prepare.execute();
        insertZpravaKontaktu(selectLastMessage(), id_kontaktu);
        con.commit();
        
    }
    
    private int selectLastMessage() throws SQLException{
        PreparedStatement prepare = con.prepareStatement(SELECT_LAST_MESSAGE);
        ResultSet result = prepare.executeQuery();
        result.next();
        System.out.println(result.getInt(1));
        return result.getInt(1);
    }

    private void insertZpravaKontaktu(int idZpravy, String idKontaktu) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(INSERT_ZPRAVY_KONTAKTU);
        prepare.setInt(1, idZpravy);
        prepare.setString(2, idKontaktu);
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
     *
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
