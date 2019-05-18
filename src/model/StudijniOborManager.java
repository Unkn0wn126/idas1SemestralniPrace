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
public class StudijniOborManager {

    private Connection con;
    // TODO: Předělat selecty tak, aby používaly pohledy
    private final String SELECT_STUDIJNI_OBORY = "SELECT * FROM STUDIJNI_OBORY";
    private final String SELECT_STUDIJNI_OBOR = "SELECT * FROM STUDIJNI_OBORY WHERE id_oboru = ?";
    private final String SELECT_STUDIJNI_OBORY_BY_ATTRIBUTE = "SELECT * FROM STUDIJNI_OBORY WHERE nazev LIKE ? OR zkratka_oboru LIKE ?";
    private final String INSERT_STUDIJNI_OBOR = "INSERT INTO STUDIJNI_OBORY(NAZEV, ZKRATKA_OBORU, POPIS, AKREDITACE_DO) VALUES (?,?,?,?)";
    private final String DELETE = "DELETE FROM STUDIJNI_OBORY WHERE id_oboru = ?";
    private final String UPDATE_OBOR = "UPDATE STUDIJNI_OBORY SET nazev = ?, popis = ?, akreditace_do = ?  where id_oboru = ?";

    public StudijniOborManager(Connection con) {
        this.con = con;
    }

    public List<StudijniObor> selectStudijniObory() throws SQLException {
        List<StudijniObor> listSelect = new ArrayList<>();
        PreparedStatement prepare = con.prepareStatement(SELECT_STUDIJNI_OBORY);
        ResultSet result = prepare.executeQuery();

        while (result.next()) {
            listSelect.add(new StudijniObor(result.getInt("id_oboru"),
                    result.getString("nazev"), result.getString("zkratka_oboru"),
                    result.getString("popis"),
                    result.getDate("akreditace_do").toLocalDate()));

        }
        return listSelect;
    }

    public StudijniObor selectStudijniObor(int idOboru) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(SELECT_STUDIJNI_OBOR);
        prepare.setInt(1, idOboru);
        StudijniObor obor;
        ResultSet result = prepare.executeQuery();
        obor = new StudijniObor(result.getInt("id_oboru"),
                result.getString("nazev"), result.getString("zkratka_oboru"),
                result.getString("popis"),
                result.getDate("akreditace_do").toLocalDate());
        return obor;
    }

    public List<StudijniObor> selectStudijniOboryByAttribute(String attribute) throws SQLException {
        List<StudijniObor> listSelect = new ArrayList<>();
        PreparedStatement prepare = con.prepareStatement(SELECT_STUDIJNI_OBORY_BY_ATTRIBUTE);
        String attr = "%" + attribute + "%";
        prepare.setString(1, attr);
        prepare.setString(2, attr);
        ResultSet result = prepare.executeQuery();

        while (result.next()) {
            listSelect.add(new StudijniObor(result.getInt("id_oboru"),
                    result.getString("nazev"), result.getString("zkratka_oboru"),
                    result.getString("popis"),
                    result.getDate("akreditace_do").toLocalDate()));

        }
        return listSelect;
    }

    public void insertStudijniObor(String nazev, String zkratka, String popis, LocalDate akreditaceDo) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(INSERT_STUDIJNI_OBOR);
        prepare.setString(1, nazev);
        prepare.setString(2, zkratka);
        prepare.setString(3, popis);
        prepare.setDate(4, Date.valueOf(akreditaceDo));
        prepare.execute();
        con.commit();
    }

    /**
     * Vymaže obor z databáze
     *
     * @param idOboru id oboru pro smazání
     * @throws SQLException
     */
    public void deleteObor(int idOboru) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(DELETE);
        prepare.setInt(1, idOboru);
        prepare.execute();
        con.commit();
    }

    /**
     * Aktualizuje obor
     *
     * @param nazev název oboru
     * @param popis popis oboru
     * @param akreditaceDo akreditace do
     * @param id_prispevku id oboru
     * @throws SQLException
     */
    public void updateObor(String nazev, String popis, LocalDate akreditaceDo, int id_prispevku) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(UPDATE_OBOR);
        prepare.setString(1, nazev);
        prepare.setString(2, popis);
        prepare.setDate(3, Date.valueOf(akreditaceDo));
        prepare.setInt(4, id_prispevku);

        prepare.execute();
        con.commit();
    }
}
