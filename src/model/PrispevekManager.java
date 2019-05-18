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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lukas
 */
public class PrispevekManager {

    private Connection con;
    // TODO: Předělat selecty tak, aby používaly pohledy
    private final String SELECT_PRISPEVKY = "SELECT * FROM PRISPEVKY";
    private final String SELECT_PRISPEVEK = "SELECT * FROM PRISPEVKY WHERE id_prispevku = ?";
    private final String SELECT_KOMENTARE = "SELECT p.* FROM PRISPEVKY, c.* FROM PRISPEVKY,  WHERE p.prispevky_id_prispevku = c.id_prispevku AND p.prispevky_id_prispevku = ?";
    private final String INSERT_PRISPEVEK = "INSERT INTO PRISPEVKY(nazev,obsah,popis,tag) VALUES (?,?,?,?)";
    private final String DELETE = "DELETE FROM PRISPEVKY WHERE id_prispevku = ?";
    private final String UPDATE_PRISPEVEK = "UPDATE PRISPEVKY SET nazev = ?, obsah = ?, blokace = ?  where id_prispevku = ?";

    public PrispevekManager(Connection con) {
        this.con = con;
    }

    /**
     * Vrátí všechny veřené příspěvky
     *
     * @return seznam veřejných příspěvků
     * @throws SQLException
     */
    public List<Prispevek> selectPrispevky() throws SQLException {
        List<Prispevek> listSelect = new ArrayList<>();
        PreparedStatement prepare = con.prepareStatement(SELECT_PRISPEVKY);
        ResultSet result = prepare.executeQuery();

        while (result.next()) {
            listSelect.add(new Prispevek(result.getInt("id_prispevku"), result.getString("obsah_prispevku"), result.getTimestamp("cas_odeslani").toLocalDateTime(), result.getString("nazev"), result.getInt("blokace"), result.getString("tag"), selectKomentare(result.getInt("prispevky_id_prispevku"))));
        }
        return listSelect;
    }

    /**
     * Vybere komentáře příspěvku
     *
     * @param id id komentáře
     * @return seznam komentářů
     * @throws SQLException
     */
    public List<Prispevek> selectKomentare(int id) throws SQLException {
        List<Prispevek> listSelect = new ArrayList<>();
        PreparedStatement prepare = con.prepareStatement(SELECT_KOMENTARE);
        prepare.setInt(1, id);
        ResultSet result = prepare.executeQuery();

        while (result.next()) {
            listSelect.add(new Prispevek(result.getInt("id_prispevku"), result.getString("obsah_prispevku"), result.getTimestamp("cas_odeslani").toLocalDateTime(), result.getString("nazev"), result.getInt("blokace"), result.getString("tag"), selectKomentare(result.getInt("prispevky_id_prispevku"))));
        }
        return listSelect;
    }

    /**
     * Vybere příspěvek podle id
     *
     * @param idPrispevku id příspěvku
     * @return příspěvek podle id
     * @throws SQLException
     */
    public Prispevek selectPrispevek(int idPrispevku) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(SELECT_PRISPEVEK);
        prepare.setInt(1, idPrispevku);
        Prispevek prispevek;
        ResultSet result = prepare.executeQuery();
        prispevek = new Prispevek(result.getInt("id_prispevku"), result.getString("obsah_prispevku"), result.getTimestamp("cas_odeslani").toLocalDateTime(), result.getString("nazev"), result.getInt("blokace"), result.getString("tag"), selectKomentare(result.getInt("prispevky_id_prispevku")));
        return prispevek;
    }

    /**
     * Vloží příspěvek do databáze
     *
     * @param nazev název příspěvku
     * @param obsah obsah příspěvku
     * @param popis popis příspěvku
     * @param tag tag příspěvku
     * @throws SQLException
     */
    public void insertPrispevek(String nazev, String obsah, String popis, String tag) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(INSERT_PRISPEVEK);
        prepare.setString(1, nazev);
        prepare.setString(2, obsah);
        prepare.setString(3, popis);
        prepare.setString(4, tag);
        prepare.execute();
        con.commit();
    }

    /**
     * Vymaže příspěvek z databáze
     *
     * @param idPrispevku id příspěvku pro smazání
     * @throws SQLException
     */
    public void deletePrispevek(int idPrispevku) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(DELETE);
        prepare.setInt(1, idPrispevku);
        prepare.execute();
        con.commit();
    }

    /**
     * Aktualizuje příspěvek
     *
     * @param nazev název příspěvku
     * @param obsah obsah příspěvku
     * @param blokace blokace příspěvku
     * @param id_prispevku id příspěvku
     * @throws SQLException
     */
    public void updatePrispevek(String nazev, String obsah, int blokace, int id_prispevku) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(UPDATE_PRISPEVEK);
        prepare.setString(1, nazev);
        prepare.setString(2, obsah);
        prepare.setInt(3, blokace);
        prepare.setInt(4, id_prispevku);

        prepare.execute();
        con.commit();
    }
}
