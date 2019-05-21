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
    private final String GET_POCET_ZPRAV_BY_ID = "SELECT COUNT(ZPRAVY_ID_ZPRAVY)"
            + " FROM ZPRAVY_KONTAKTU"
            + " WHERE ZPRAVY_ID_ZPRAVY = ?";
    // Přijímání zpráv start
    private final String SELECT_ZPRAVY_KONTAKTU = "SELECT ZPRAVY_POHLED.ID_ZPRAVY, ZPRAVY_POHLED.OBSAH_ZPRAVY, ZPRAVY_POHLED.CAS_ODESLANI, ZPRAVY_POHLED.ID_ODESILATELE FROM ZPRAVY_POHLED"
            + " INNER JOIN ZPRAVY_KONTAKTU ON ZPRAVY_KONTAKTU.ZPRAVY_ID_ZPRAVY = ZPRAVY_POHLED.ID_ZPRAVY"
            + " INNER JOIN KONTAKTY ON ZPRAVY_KONTAKTU.KONTAKTY_ID_KONTAKTU = KONTAKTY.ID_KONTAKTU"
            + " WHERE (KONTAKTY.ID_KONTAKTU = ? AND ZPRAVY_POHLED.ID_ODESILATELE = ?) OR (KONTAKTY.ID_KONTAKTU = ? AND ZPRAVY_POHLED.ID_ODESILATELE = ?)"
            + " ORDER BY ZPRAVY_POHLED.cas_odeslani";
    private final String SELECT_JMENO_AUTORA = "SELECT jmeno FROM UZIVATELE WHERE id_uzivatele = ?";
    // Přijímání zpráv end
    private final String SELECT_ZPRAVA = "SELECT * FROM ZPRAVY_POHLED WHERE id_zpravy = ?";
    // Posílání zpráv start
    private final String INSERT_ZPRAVA = "INSERT INTO ZPRAVY(obsah_zpravy,cas_odeslani, id_odesilatele) VALUES (?,?,?)";
    private final String INSERT_ZPRAVY_KONTAKTU = "INSERT INTO ZPRAVY_KONTAKTU(zpravy_id_zpravy, kontakty_id_kontaktu) VALUES (?, ?)";
    // Posílání zpráv end
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
        PreparedStatement prepare = con.prepareStatement(CREATE_VIEW, ResultSet.CLOSE_CURSORS_AT_COMMIT);
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
            String jmeno = selectJmenoAutora(result.getInt("id_odesilatele"));
            listSelect.add(new Zprava(result.getInt("id_zpravy"), result.getString("obsah_zpravy"), result.getTimestamp("cas_odeslani").toLocalDateTime(), jmeno));

        }

        result.close();
        prepare.close();
        return listSelect;
    }

    private String selectJmenoAutora(int idAutora) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(SELECT_JMENO_AUTORA);
        prepare.setInt(1, idAutora);
        ResultSet result = prepare.executeQuery();

        result.next();
        String jmeno = result.getString("jmeno");
        result.close();
        prepare.close();
        return jmeno;
    }

    /**
     * Vybere všechny zprávy z databáze
     *
     * @return seznam zpráv
     * @throws SQLException
     */
    public List<Zprava> selectZpravyKontaktu(int idKontaktu, int idAutora, boolean selectGroupMessage) throws SQLException {
        List<Zprava> listSelect = new ArrayList<>();
        PreparedStatement prepare = con.prepareStatement(SELECT_ZPRAVY_KONTAKTU);
        prepare.setInt(1, idKontaktu);
        prepare.setInt(2, idAutora);
        prepare.setInt(3, idAutora);
        prepare.setInt(4, idKontaktu);
        ResultSet result = prepare.executeQuery();

        while (result.next()) {
            String jmenoAutora = selectJmenoAutora(result.getInt("id_odesilatele"));
            int count = getPocetVyskytu(result.getInt("id_zpravy"));
            if (selectGroupMessage && count > 1 || !selectGroupMessage) {
                listSelect.add(new Zprava(result.getInt("id_zpravy"), result.getString("obsah_zpravy"), result.getTimestamp("cas_odeslani").toLocalDateTime(), jmenoAutora));
            }

        }

        result.close();
        prepare.close();
        return listSelect;
    }

    public List<Zprava> selectZpravyVybranychKontaktu(List<Integer> idKontaktu, int idAutora) throws SQLException {
        boolean selectGroupMessage = idKontaktu.size() > 1;
        List<Zprava> zpravy = new ArrayList<>();
        for (Integer integer : idKontaktu) {
            zpravy.addAll(selectZpravyKontaktu(integer, idAutora, selectGroupMessage));
        }
        
        return zpravy;
    }

    private int getPocetVyskytu(int idZpravy) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(GET_POCET_ZPRAV_BY_ID);
        prepare.setInt(1, idZpravy);
        ResultSet result = prepare.executeQuery();
        result.next();
        int pocet = result.getInt(1);
        result.close();
        prepare.close();
        return pocet;
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
                result.getString("id_odesilatele"));

        result.close();
        prepare.close();
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
    private void insertZprava(String obsah, int id_odesilatele) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(INSERT_ZPRAVA, ResultSet.CLOSE_CURSORS_AT_COMMIT);
        prepare.setString(1, obsah);
        prepare.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
        prepare.setInt(3, id_odesilatele);
        prepare.execute();

        con.commit();
    }

    public void poslatZpravu(String obsah, int id_odesilatele, List<Integer> id_kontaktu) throws SQLException {
        insertZprava(obsah, id_odesilatele);
        for (Integer integer : id_kontaktu) {
            insertZpravaKontaktu(selectLastMessage(), integer);
        }

    }

    private int selectLastMessage() throws SQLException {
        PreparedStatement prepare = con.prepareStatement(SELECT_LAST_MESSAGE);
        ResultSet result = prepare.executeQuery();
        result.next();
        int index = result.getInt(1);
        result.close();
        prepare.close();
        return index;
    }

    private void insertZpravaKontaktu(int idZpravy, int idKontaktu) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(INSERT_ZPRAVY_KONTAKTU, ResultSet.CLOSE_CURSORS_AT_COMMIT);
        prepare.setInt(1, idZpravy);
        prepare.setInt(2, idKontaktu);
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
        PreparedStatement prepare = con.prepareStatement(DELETE, ResultSet.CLOSE_CURSORS_AT_COMMIT);
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
        PreparedStatement prepare = con.prepareStatement(UPDATE_ZPRAVA, ResultSet.CLOSE_CURSORS_AT_COMMIT);
        prepare.setString(1, nazev);
        prepare.setString(2, obsah);
        prepare.setInt(3, idZpravy);

        prepare.execute();
        con.commit();
    }
}
