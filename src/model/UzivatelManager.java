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
public class UzivatelManager {

    private Connection con;
    private Uzivatel currentUser;

    private final String CREATE_VIEW = "CREATE OR REPLACE VIEW UZIVATELE_POHLED AS SELECT * FROM UZIVATELE";
    private final String CREATE_VIEW_ = "CREATE OR REPLACE VIEW UZIVATELE_POHLED AS SELECT * FROM UZIVATELE";
    private final String SELECT_ROLE = "SELECT ROLE.* FROM UZIVATELE INNER JOIN ROLE_UZIVATELU ON ROLE_UZIVATELU.UZIVATELE_ID_UZIVATELE = UZIVATELE.ID_UZIVATELE"
            + " INNER JOIN ROLE ON ROLE_UZIVATELU.ROLE_ID_ROLE = ROLE.ID_ROLE"
            + " WHERE UZIVATELE.ID_UZIVATELE = ?";
    private final String SELECT_UZIVATELE = "SELECT * FROM UZIVATELE_POHLED";
    private final String SELECT_UZIVATEL_BY_ID = "SELECT * FROM UZIVATELE_POHLED WHERE id_uzivatele = ?";
    private final String SELECT_UZIVATEL_BY_ATTRIBUTE = "SELECT * FROM UZIVATELE_POHLED WHERE jmeno LIKE ? OR prijmeni LIKE ?";
    private final String SELECT_UZIVATEL_LOGIN = "SELECT * FROM UZIVATELE_POHLED WHERE login = ? AND heslo = ?";
    private final String SELECT_COUNT = "SELECT COUNT(*) from UZIVATELE_POHLED";
    private final String INSERT_UZIVATEL = "INSERT INTO UZIVATELE(login, heslo, jmeno, prijmeni, rok_studia, eml, blokace, poznamka) VALUES (?,?,?,?,?,?,?,?)";
    private final String DELETE = "DELETE FROM UZIVATELE WHERE id_uzivatele = ?";
    private final String UPDATE_OBOR = "UPDATE UZIVATELE SET nazev = ?, popis = ?, akreditace_do = ?  where id_oboru = ?";

    public UzivatelManager(Connection con) {
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
     * Vrátí seznam všech uživatelů v databázi
     *
     * @return seznam všech uživatelů v databázi
     * @throws SQLException při chybě komunikace s databází
     */
    public List<Uzivatel> selectUzivatele() throws SQLException {
        List<Uzivatel> listSelect = new ArrayList<>();
        PreparedStatement prepare = con.prepareStatement(SELECT_UZIVATELE);
        ResultSet result = prepare.executeQuery();

        while (result.next()) {
            listSelect.add(new Uzivatel(result.getString("id_uzivatele"),
                    result.getString("jmeno"), result.getString("prijmeni"),
                    result.getString("EML"), result.getString("login"),
                    result.getInt("rok_studia"), result.getInt("blokace"),
                    result.getString("poznamka")));

        }
        return listSelect;
    }

    /**
     * Najde v databázi konkrétního uživatele podle jeho id
     *
     * @param idUzivatele
     * @return uživatele s příslušným id
     * @throws SQLException
     */
    public Uzivatel selectUzivatelById(String idUzivatele) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(SELECT_UZIVATEL_BY_ID);
        prepare.setString(1, idUzivatele);
        Uzivatel uzivatel;
        ResultSet result = prepare.executeQuery();
        result.next();
        uzivatel = new Uzivatel(result.getString("id_uzivatele"),
                result.getString("jmeno"), result.getString("prijmeni"),
                result.getString("EML"), result.getString("login"),
                result.getInt("rok_studia"), result.getInt("blokace"),
                result.getString("poznamka"));
        
        uzivatel.setRole(selectRoleUzivatele(idUzivatele));
        System.out.println(uzivatel.getRole().toArray().toString());
        return uzivatel;
    }

    public List<Uzivatel> selectUzivateleByAttributes(String attribut) throws SQLException {
        List<Uzivatel> listSelect = new ArrayList<>();
        PreparedStatement prepare = con.prepareStatement(SELECT_UZIVATEL_BY_ATTRIBUTE);
        prepare.setString(1, "%" + attribut + "%");
        prepare.setString(2, "%" + attribut + "%");
        ResultSet result = prepare.executeQuery();

        while (result.next()) {
            listSelect.add(new Uzivatel(result.getString("id_uzivatele"),
                    result.getString("jmeno"), result.getString("prijmeni"),
                    result.getString("EML"), result.getString("login"),
                    result.getInt("rok_studia"), result.getInt("blokace"),
                    result.getString("poznamka")));

        }
        return listSelect;
    }

    private List<Role> selectRoleUzivatele(String idUzivatele) throws SQLException {
        List<Role> listSelect = new ArrayList<>();
        PreparedStatement prepare = con.prepareStatement(SELECT_ROLE);
        prepare.setString(1, idUzivatele);
        ResultSet result = prepare.executeQuery();

        while (result.next()) {
            Role role = new Role(result.getInt("id_role"), result.getString("jmeno_role"), result.getString("opravneni"), result.getString("poznamka"));
            listSelect.add(role);
        }

        return listSelect;
    }

    /**
     * Podle zadaných parametrů zjistí, jestli daný uživatel existuje. Pokud
     * uživatel existuje, nastaví ho jako současného uživatele
     *
     * @param login login uživatele
     * @param heslo heslo uživatele
     * @return true, pokud vstupy souhlasí; false, pokud neodpovídá login nebo
     * heslo
     * @throws SQLException při chybě komunikace s databází
     */
    public boolean prihlasUzivatele(String login, String heslo) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(SELECT_UZIVATEL_LOGIN);
        prepare.setString(1, login);
        prepare.setString(2, heslo);
        ResultSet result = prepare.executeQuery();
        try {
            result.next();
            setCurrentUser(selectUzivatelById(result.getString("ID_UZIVATELE")));
            return true;
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        }
    }

    /**
     * Nastaví součadného uživatele
     *
     * @param user uživatel, který má být nastaven jako současný
     */
    public void setCurrentUser(Uzivatel user) {
        this.currentUser = user;
        currentUser.setOnline(1);
    }

    public void unsetCurrentUser() {
        if (currentUser != null) {
            currentUser.setOnline(0);
            this.currentUser = null;
        }
    }

    /**
     * Vrátí současného uživatele
     *
     * @return současný uživatel
     */
    public Uzivatel getCurrentUser() {
        return currentUser;
    }

    /**
     * Vrátí současný počet uživatelů evidovaných v databázi
     *
     * @return počet uživatelů v databázi
     * @throws SQLException při chybě komunikace s databází
     */
    public int getPocet() throws SQLException {
        PreparedStatement prepare = con.prepareStatement(SELECT_COUNT);
        ResultSet result = prepare.executeQuery();
        result.next();
        return result.getInt(1);
    }

    public void registerUser(String name, String surname, String login, String password, String eml, String poznamka, Integer rocnik) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(INSERT_UZIVATEL);
        prepare.setString(1, login);
        prepare.setString(2, password);
        prepare.setString(3, name);
        prepare.setString(4, surname);
        prepare.setInt(5, rocnik);
        prepare.setString(6, eml);
        prepare.setInt(7, 0);
        prepare.setString(8, poznamka);
        prepare.execute();
        con.commit();
    }
}
