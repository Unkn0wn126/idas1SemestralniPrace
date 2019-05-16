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
public class PredmetManager {

    private Connection con;
    private final String SELECT_PREDMETY = "SELECT * FROM PREDMETY";
    private final String SELECT_PREDMET = "SELECT * FROM PREDMETY WHERE id_predmetu = ?";
    private final String SELECT_PREDMET_BY_ATTRIBUTE = "SELECT * FROM PREDMETY WHERE zkratka_predmetu = ? OR nazev_predmetu = ?";
    private final String INSERT_PREDMET = "INSERT INTO PREDMETY(nazev_predmetu,zkratka_predmetu,popis) VALUES (?,?,?)";
    private final String DELETE = "DELETE FROM PREDMETY WHERE id_predmetu = ?";
    private final String UPDATE_NAZEV = "UPDATE PREDMETY SET nazev_predmetu = ? where id_predmetu = ?";
    private final String UPDATE_PREDMET = "UPDATE PREDMETY SET nazev_predmetu = ?, zkratka_predmetu = ?, popis = ?  where id_predmetu = ?";

    public PredmetManager(Connection con) {
        this.con = con;
    }

    /**
     * Vybere všechny předměty z databáze
     * @return seznam předmětů
     * @throws SQLException 
     */
    public List<Predmet> selectPredmety() throws SQLException {
        List<Predmet> listSelect = new ArrayList<>();
        PreparedStatement prepare = con.prepareStatement(SELECT_PREDMETY);
        ResultSet result = prepare.executeQuery();

        while (result.next()) {
            listSelect.add(new Predmet(result.getInt("id_predmetu"), result.getString("nazev_predmetu"), result.getString("zkratka_predmetu"), result.getString("popis")));

        }
        return listSelect;
    }

    /**
     * Vybere předmět podle id
     * @param idPredmetu id předmětu
     * @return předmět podle id
     * @throws SQLException 
     */
    public Predmet selectPredmet(int idPredmetu) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(SELECT_PREDMET);
        prepare.setInt(1, idPredmetu);
        Predmet predmet;
        ResultSet result = prepare.executeQuery();
        predmet = new Predmet(result.getInt("id_predmetu"), result.getString("nazev_predmetu"), result.getString("zkratka_predmetu"), result.getString("popis"));
        return predmet;
    }

    /**
     * Vybere předmět podle zkratky nebo názvu
     * @param attribute zkratka nebo název
     * @return předmět dle atributu
     * @throws SQLException 
     */
    public List<Predmet> selectPredmetyByAttribute(String attribute) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(SELECT_PREDMET_BY_ATTRIBUTE);
        prepare.setString(1, attribute);
        prepare.setString(2, attribute);
        List<Predmet> listSelect = new ArrayList<>();
        ResultSet result = prepare.executeQuery();

        while (result.next()) {
            listSelect.add(new Predmet(result.getInt("id_predmetu"), result.getString("nazev_predmetu"), result.getString("zkratka_predmetu"), result.getString("popis")));

        }

        return listSelect;
    }

    /**
     * Vloží předmět do databáze
     * @param nazev název předmětu
     * @param zkratka zkratka předmětu
     * @param popis popis předmětu
     * @throws SQLException 
     */
    public void insertPredmet(String nazev, String zkratka, String popis) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(INSERT_PREDMET);
        prepare.setString(1, nazev);
        prepare.setString(2, zkratka);
        prepare.setString(3, popis);
        prepare.execute();
        con.commit();
    }

    /**
     * Vymaže předmět z databáze
     * @param idPredmetu id předmětu pro smazání
     * @throws SQLException 
     */
    public void deletePredmet(int idPredmetu) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(DELETE);
        prepare.setInt(1, idPredmetu);
        prepare.execute();
        con.commit();
    }

    /**
     * Aktualizuje předmět dle parametrů
     * @param nazevPredmetu název předmětu
     * @param zkratkaPredmetu zkratka předmětu
     * @param popis popis předmětu
     * @param idPredmetu id předmětu
     * @throws SQLException 
     */
    public void updatePredmet(String nazevPredmetu, String zkratkaPredmetu, String popis, int idPredmetu) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(UPDATE_PREDMET);
        prepare.setString(1, nazevPredmetu);
        prepare.setString(2, zkratkaPredmetu);
        prepare.setString(3, popis);
        prepare.setInt(4, idPredmetu);

        prepare.execute();
        con.commit();
    }
}
