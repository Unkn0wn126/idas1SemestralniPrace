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
public class TypPrispevkuManager {
    // TODO: DELETE
        private Connection con;
    private final String SELECT_TYPY_PRISPEVKU = "SELECT * FROM TYPY_PRISPEVKU";
    private final String SELECT_TYP_PRISPEVKU = "SELECT * FROM TYPY_PRISPEVKU WHERE id_typ_prispevku = ?";
    private final String INSERT_TYP_PRISPEVKU = "INSERT INTO TYPY_PRISPEVKU(nazev,zkratka,prispevky_id_prispevku) VALUES (?,?,?)";
    private final String DELETE = "DELETE FROM TYPY_PRISPEVKU WHERE id_typ_prispevku = ?";
    private final String UPDATE_TYP_PRISPEVKU = "UPDATE TYPY_PRISPEVKU SET nazev = ?, zkratka = ?, prispevky_id_prispevku = ?  where id_typ_prispevku = ?";

    public TypPrispevkuManager(Connection con) {
        this.con = con;
    }

    /**
     * Vybere všechny typy příspěvků z databáze
     *
     * @return seznam typů příspěvků
     * @throws SQLException
     */
    public List<TypPrispevku> selectTypyPrispevku() throws SQLException {
        List<TypPrispevku> listSelect = new ArrayList<>();
        PreparedStatement prepare = con.prepareStatement(SELECT_TYPY_PRISPEVKU);
        ResultSet result = prepare.executeQuery();

        while (result.next()) {
            listSelect.add(new TypPrispevku(result.getInt("id_typ_prispevku"), 
                    result.getString("nazev"), result.getString("zkratka")));

        }
        return listSelect;
    }

    /**
     * Vybere typ příspěvku podle id
     *
     * @param idRole id typu příspěvku
     * @return typ příspěvku podle id
     * @throws SQLException
     */
    public TypPrispevku selectTypPrispevku(int idRole) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(SELECT_TYP_PRISPEVKU);
        prepare.setInt(1, idRole);
        TypPrispevku typPrispevku;
        ResultSet result = prepare.executeQuery();
        typPrispevku = new TypPrispevku(result.getInt("id_typ_prispevku"), 
                    result.getString("nazev"), result.getString("zkratka"));
        return typPrispevku;
    }

    /**
     * Vloží typ příspěvku do databáze
     *
     * @param nazev název typu příspěvku
     * @param zkratka zkratka typu příspěvku
     * @param id_prispevku id_prispevku typu příspěvku
     * @throws SQLException
     */
    public void insertRole(String nazev, String zkratka, int id_prispevku) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(INSERT_TYP_PRISPEVKU);
        prepare.setString(1, nazev);
        prepare.setString(2, zkratka);
        prepare.setInt(3, id_prispevku);
        prepare.execute();
        con.commit();
    }

    /**
     * Vymaže typ příspěvku z databáze
     *
     * @param idTypPrispevku id typu příspěvku pro smazání
     * @throws SQLException
     */
    public void deleteRole(int idTypPrispevku) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(DELETE);
        prepare.setInt(1, idTypPrispevku);
        prepare.execute();
        con.commit();
    }

    /**
     * Aktualizuje typ příspěvku dle parametrů
     *
     * @param nazev jméno typu příspěvku
     * @param zkratka oprávnění
     * @param idPrispevku poznámka
     * @param idTypPrispevku id typu příspěvku
     * @throws SQLException
     */
    public void updateRole(String nazev, String zkratka, int idPrispevku, int idTypPrispevku) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(UPDATE_TYP_PRISPEVKU);
        prepare.setString(1, nazev);
        prepare.setString(2, zkratka);
        prepare.setInt(3, idPrispevku);
        prepare.setInt(4, idTypPrispevku);

        prepare.execute();
        con.commit();
    }
}
