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
public class RoleManager {

    private Connection con;
    private final String CREATE_VIEW = "CREATE OR REPLACE VIEW ROLE_POHLED AS SELECT * FROM ROLE";
    private final String SELECT_ROLE = "SELECT * FROM ROLE_POHLED";
    private final String SELECT_ROLI = "SELECT * FROM ROLE_POHLED WHERE id_role = ?";
    private final String INSERT_ROLE = "INSERT INTO ROLE(jmeno_role,opravneni,poznamka) VALUES (?,?,?)";
    private final String DELETE = "DELETE FROM ROLE WHERE id_role = ?";
    private final String UPDATE_ROLE = "UPDATE ROLE SET jmeno_role = ?, opravneni = ?, poznamka = ?  where id_role = ?";

    public RoleManager(Connection con) {
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
     * Vybere všechny role z databáze
     *
     * @return seznam rolí
     * @throws SQLException
     */
    public List<Role> selectRole() throws SQLException {
        List<Role> listSelect = new ArrayList<>();
        PreparedStatement prepare = con.prepareStatement(SELECT_ROLE);
        ResultSet result = prepare.executeQuery();

        while (result.next()) {
            listSelect.add(new Role(result.getInt("id_role"),
                    result.getString("jmeno_role"),
                    result.getString("opravneni"),
                    result.getString("poznamka")));

        }
        return listSelect;
    }

    /**
     * Vybere roli podle id
     *
     * @param idRole id role
     * @return role podle id
     * @throws SQLException
     */
    public Role selectRoli(int idRole) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(SELECT_ROLI);
        prepare.setInt(1, idRole);
        Role role;
        ResultSet result = prepare.executeQuery();
        role = new Role(result.getInt("id_role"),
                result.getString("jmeno_role"),
                result.getString("opravneni"),
                result.getString("poznamka"));
        return role;
    }

    /**
     * Vloží roli do databáze
     *
     * @param jmenoRole název role
     * @param opravneni opravneni role
     * @param poznamka poznamka role
     * @throws SQLException
     */
    public void insertRole(String jmenoRole, String opravneni, String poznamka) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(INSERT_ROLE);
        prepare.setString(1, jmenoRole);
        prepare.setString(2, opravneni);
        prepare.setString(3, poznamka);
        prepare.execute();
        con.commit();
    }

    /**
     * Vymaže role z databáze
     *
     * @param idRole id role pro smazání
     * @throws SQLException
     */
    public void deleteRole(int idRole) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(DELETE);
        prepare.setInt(1, idRole);
        prepare.execute();
        con.commit();
    }

    /**
     * Aktualizuje roli dle parametrů
     *
     * @param jmenoRole jméno role
     * @param opravneni oprávnění
     * @param poznamka poznámka
     * @param idRole id role
     * @throws SQLException
     */
    public void updateRole(String jmenoRole, String opravneni, String poznamka, int idRole) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(UPDATE_ROLE);
        prepare.setString(1, jmenoRole);
        prepare.setString(2, opravneni);
        prepare.setString(3, poznamka);
        prepare.setInt(4, idRole);

        prepare.execute();
        con.commit();
    }
}
