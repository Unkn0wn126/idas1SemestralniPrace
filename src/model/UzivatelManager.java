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
public class UzivatelManager {

    private Connection con;
    private Uzivatel currentUser;

    private final String SELECT_UZIVATELE = "SELECT * FROM UZIVATELE";
    private final String SELECT_UZIVATEL_BY_ID = "SELECT * FROM UZIVATELE WHERE id_uzivatele = ?";
    private final String SELECT_UZIVATEL_BY_ATTRIBUTE = "SELECT * FROM UZIVATELE WHERE id_uzivatele = ? OR jmeno = ? OR prijmeni = ?";
    private final String SELECT_UZIVATEL_LOGIN = "SELECT * FROM UZIVATELE WHERE login = ? AND heslo = ?";
    private final String SELECT_COUNT = "SELECT COUNT(*) from UZIVATELE";
    private final String INSERT_UZIVATEL = "INSERT INTO UZIVATELE(login, heslo, jmeno, prijmeni, rok_studia, eml, blokace, poznamka) VALUES (?,?,?,?,?,?,?,?)";
    private final String DELETE = "DELETE FROM UZIVATELE WHERE id_uzivatele = ?";
    private final String UPDATE_OBOR = "UPDATE UZIVATELE SET nazev = ?, popis = ?, akreditace_do = ?  where id_oboru = ?";

    public UzivatelManager(Connection con) {
        this.con = con;
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
        return uzivatel;
    }

    public List<Uzivatel> selectUzivateleByAttributes(String attribut) throws SQLException {
        List<Uzivatel> listSelect = new ArrayList<>();
        PreparedStatement prepare = con.prepareStatement(SELECT_UZIVATEL_BY_ATTRIBUTE);
        prepare.setString(1, attribut);
        prepare.setString(2, attribut);
        prepare.setString(3, attribut);
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
