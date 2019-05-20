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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lukas
 */
public class KontaktManager {

    private Connection con;
    private final String SELECT_LAST_KONTAKT_ID = "SELECT max (id_kontaktu) FROM KONTAKTY_POHLED";
    private final String SELECT_KONTAKT_ID_BY_USER_ID = "SELECT id_kontaktu from KONTAKTY_POHLED WHERE UZIVATELE_ID_UZIVATELE = ?";
    private final String CREATE_VIEW = "CREATE OR REPLACE VIEW KONTAKTY_POHLED AS SELECT * FROM KONTAKTY";
    private final String C_V_KONTAKT_VYPIS = "CREATE OR REPLACE VIEW KONTAKT_VYPIS"
            + " AS SELECT KONTAKTY_UZIVATELU.KONTAKTY_ID_KONTAKTU, UZIVATELE.JMENO, UZIVATELE.PRIJMENI, UZIVATELE.PRIHLASEN, UZIVATELE.BLOKACE FROM KONTAKTY_UZIVATELU"
            + " INNER JOIN KONTAKTY ON KONTAKTY_UZIVATELU.KONTAKTY_ID_KONTAKTU = KONTAKTY.ID_KONTAKTU"
            + " INNER JOIN UZIVATELE ON KONTAKTY_UZIVATELU.UZIVATELE_ID_UZIVATELE = UZIVATELE.ID_UZIVATELE";
    private final String SELECT_KONTAKTY = "SELECT * FROM KONTAKTY_POHLED WHERE uzivatele_id_uzivatele = ?";
    private final String SELECT_KONTAKTY_UZIVATELE = "SELECT k.id_kontaktu, u2.id_uzivatele, u2.jmeno, u2.prijmeni, u2.prihlasen, u2.blokace"
            + " FROM UZIVATELE u"
            + " inner join kontakty_uzivatelu ku"
            + " on u.id_uzivatele = ku.uzivatele_id_uzivatele"
            + " inner join kontakty k"
            + " on ku.kontakty_id_kontaktu = k.id_kontaktu"
            + " inner join uzivatele u2"
            + " on k.uzivatele_id_uzivatele = u2.id_uzivatele"
            + " where u.id_uzivatele = ?";
    private final String SELECT_KONTAKT = "SELECT * FROM KONTAKTY_POHLED WHERE id_kontaktu = ? AND uzivatele_id_uzivatele = ?";
    private final String INSERT_KONTAKT = "INSERT INTO KONTAKTY(uzivatele_id_uzivatele) VALUES (?)";
    private final String INSERT_KONTAKTY_UZIVATELU = "INSERT INTO KONTAKTY_UZIVATELU(UZIVATELE_ID_UZIVATELE, KONTAKTY_ID_KONTAKTU) VALUES (?,?)";
    private final String DELETE = "DELETE FROM KONTAKTY WHERE id_kontaktu = ? AND uzivatele_id_uzivatele = ?";

    public KontaktManager(Connection con) {
        this.con = con;
        try {
            createView();
//            createViewKontaktVypis();
        } catch (SQLException ex) {
            Logger.getLogger(UzivatelManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void createView() throws SQLException {
        PreparedStatement prepare = con.prepareStatement(CREATE_VIEW);
        prepare.execute();
        con.commit();
    }

    private void createViewKontaktVypis() throws SQLException {
        PreparedStatement prepare = con.prepareStatement(C_V_KONTAKT_VYPIS);
        prepare.execute();
        con.commit();
    }

    /**
     * Vybere všechny kontakty daného uživatele
     *
     * @param id_uzivatele id uživatele
     * @return seznam kontaktů uživatele
     * @throws SQLException
     */
    public List<KontaktVypis> selectKontaktyUzivatele(int idUzivatele) throws SQLException {
        List<KontaktVypis> listSelect = new ArrayList<>();
        PreparedStatement prepare = con.prepareStatement(SELECT_KONTAKTY_UZIVATELE);
        prepare.setInt(1, idUzivatele);
        ResultSet result = prepare.executeQuery();

        while (result.next()) {
            listSelect.add(new KontaktVypis(result.getInt("id_kontaktu"), result.getInt("id_uzivatele"), result.getString("jmeno"), result.getString("prijmeni"), result.getInt("prihlasen"), result.getInt("blokace")));
        }
        return listSelect;
    }

    /**
     * Vybere kontakt uživatele
     *
     * @param id_kontaktu id kontaktu
     * @param id_uzivatele id uživatele
     * @return kontakt
     * @throws SQLException
     */
    public Kontakt selectKontakt(String id_kontaktu, String id_uzivatele) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(SELECT_KONTAKT);
        prepare.setString(1, id_kontaktu);
        prepare.setString(2, id_uzivatele);
        Kontakt kontakt;
        ResultSet result = prepare.executeQuery();
        kontakt = new Kontakt(result.getString("id_kontaktu"),
                result.getString("uzivatele_id_uzivatele"),
                result.getDate("datum_od"), result.getDate("datum_do"),
                result.getInt("platnost"), result.getString("poznamka"));
        return kontakt;
    }

    /**
     * Vloží kontakt do databáze
     *
     * @param id_kontaktu id kontaktu
     * @param idUzivatele id uživatele
     * @param datumOd počáteční platnost
     * @param datumDo koncová platnost
     * @param poznamka poznámka
     * @throws SQLException
     */
    private void insertKontakt(LocalDate datumOd, LocalDate datumDo, String poznamka) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(INSERT_KONTAKT);
        prepare.setDate(1, Date.valueOf(datumOd));
        prepare.setDate(2, Date.valueOf(datumDo));
        prepare.setInt(3, 1);
        prepare.setString(4, poznamka);
        prepare.execute();

        con.commit();
    }

    private void insertKontaktyUzivatelu(int idUzivatele, int idKontaktu) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(INSERT_KONTAKTY_UZIVATELU);
        prepare.setInt(1, idUzivatele);
        prepare.setInt(2, idKontaktu);

        prepare.execute();
        con.commit();
    }

    private int selectLastContactId() throws SQLException {
        PreparedStatement prepare = con.prepareStatement(SELECT_LAST_KONTAKT_ID);
        ResultSet res = prepare.executeQuery();
        res.next();
        return res.getInt(1);
    }

    private int selectContactIdByUserId(int idUzivatele) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(SELECT_KONTAKT_ID_BY_USER_ID);
        prepare.setInt(1, idUzivatele);
        ResultSet res = prepare.executeQuery();
        res.next();
        return res.getInt(1);
    }

    public void addToContacts(int idPrvniho, int idDruheho) throws SQLException {
        insertKontaktyUzivatelu(idPrvniho, selectContactIdByUserId(idDruheho));
        insertKontaktyUzivatelu(idDruheho, selectContactIdByUserId(idPrvniho));
    }

    /**
     * Smaže kontakt z databáze
     *
     * @param idKontaktu id kontaktu
     * @param idUzivatele id uživatele
     * @throws SQLException
     */
    public void deleteKontakt(String idKontaktu, String idUzivatele) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(DELETE);
        prepare.setString(1, idKontaktu);
        prepare.setString(2, idUzivatele);
        prepare.execute();
        con.commit();
    }
}
