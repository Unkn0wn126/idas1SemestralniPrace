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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lukas
 */
public class StudijniPlanManager {

    private Connection con;
    private final String CREATE_VIEW = "CREATE OR REPLACE VIEW STUDIJNI_PLANY_POHLED AS SELECT * FROM STUDIJNI_PLANY";
    private final String SELECT_STUDIJNI_PLANY = "SELECT * FROM STUDIJNI_PLANY_POHLED";
    private final String SELECT_STUDIJNI_PLAN = "SELECT * FROM STUDIJNI_PLANY_POHLED WHERE id_planu = ?";
    private final String SELECT_STUDIJNI_PLAN_BY_ATTRIBUTE = "SELECT * FROM STUDIJNI_PLANY_POHLED WHERE nazev LIKE ?";
    private final String INSERT_STUDIJNI_PLAN = "INSERT INTO STUDIJNI_PLANY(id_planu,studijni_obory_id_oboru, popis) VALUES (?,?,?)";
    private final String DELETE = "DELETE FROM STUDIJNI_PLANY WHERE id_planu = ?";
    private final String UPDATE_STUDIJNI_PLAN = "UPDATE STUDIJNI_PLANY SET nazev = ?, popis = ? where id_planu = ?";

    public StudijniPlanManager(Connection con) {
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

    public List<StudijniPlan> selectStudijniPlany() throws SQLException {
        List<StudijniPlan> listSelect = new ArrayList<>();
        PreparedStatement prepare = con.prepareStatement(SELECT_STUDIJNI_PLANY);
        ResultSet result = prepare.executeQuery();

        while (result.next()) {
            listSelect.add(new StudijniPlan(result.getInt("id_planu"), result.getString("nazev"), result.getInt("id_oboru"), result.getString("popis")));

        }
        return listSelect;
    }

    public StudijniPlan selectStudijniPlan(int idPlanu) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(SELECT_STUDIJNI_PLAN);
        prepare.setInt(1, idPlanu);
        StudijniPlan plan;
        ResultSet result = prepare.executeQuery();
        plan = new StudijniPlan(result.getInt("id_planu"), result.getString("nazev"), result.getInt("studijni_obory_id_oboru"), result.getString("popis"));
        return plan;
    }

    public List<StudijniPlan> selectStudijniPlanByAttribute(String nazev) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(SELECT_STUDIJNI_PLAN_BY_ATTRIBUTE);
        prepare.setString(1, "%" + nazev + "%");
        List<StudijniPlan> listSelect = new ArrayList<>();
        ResultSet result = prepare.executeQuery();
        while (result.next()) {
            listSelect.add(new StudijniPlan(result.getInt("id_planu"), result.getString("nazev"), result.getInt("id_oboru"), result.getString("popis")));
        }
        return listSelect;
    }

    public void insertStudijniPlan(String nazev, int idOboru, String popis) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(INSERT_STUDIJNI_PLAN);
        prepare.setString(1, nazev);
        prepare.setInt(2, idOboru);
        prepare.setString(3, popis);
        prepare.execute();
        con.commit();
    }

    /**
     * Vymaže obor z databáze
     *
     * @param idOboru id oboru pro smazání
     * @throws SQLException
     */
    public void deleteStudijniPlan(int idOboru) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(DELETE);
        prepare.setInt(1, idOboru);
        prepare.execute();
        con.commit();
    }

    /**
     * Aktualizuje studijní plán
     *
     * @param nazev název studijního plánu
     * @param popis popis studijního plánu
     * @param id_prispevku id studijního plánu
     * @throws SQLException
     */
    public void updateStudijniPlan(String nazev, String popis, int id_prispevku) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(UPDATE_STUDIJNI_PLAN);
        prepare.setString(1, nazev);
        prepare.setString(2, popis);
        prepare.setInt(3, id_prispevku);

        prepare.execute();
        con.commit();
    }
}
