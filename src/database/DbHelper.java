/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.Kontakt;
import model.KontaktVypis;
import model.Predmet;
import model.Prispevek;
import model.Role;
import model.StudijniObor;
import model.StudijniPlan;
import model.Uzivatel;
import model.Zprava;

/**
 *
 * @author Lukas
 */
public class DbHelper {

    /**
     * Konektor pro komunikaci s databází
     */
    private Connection con;

    /**
     * Instance současně lokálně přihlášeného uživatele
     */
    private Uzivatel currentUser;

    // Statements for uzivatele start
    private final String CV_UZIVATELE_POHLED = "CREATE OR REPLACE VIEW UZIVATELE_POHLED AS SELECT * FROM UZIVATELE";
    private final String SET_UZIVATEL_ONLINE = "UPDATE UZIVATELE_POHLED SET PRIHLASEN = 1 WHERE ID_UZIVATELE = ?";
    private final String SET_UZIVATEL_OFFLINE = "UPDATE UZIVATELE_POHLED SET PRIHLASEN = 0 WHERE ID_UZIVATELE = ?";
    private final String SELECT_UZIVATEL_ONLINE = "SELECT prihlasen FROM STAVY_ONLINE WHERE ID_UZIVATELE = ?";
    private final String SELECT_UZIVATEL_ZABLOKOVAN = "SELECT BLOKACE FROM UZIVATELE_BLOKACE WHERE ID_UZIVATELE = ?";
    private final String SELECT_UZIVATELE_ALL = "SELECT * FROM UZIVATELE_POHLED";
    private final String SELECT_UZIVATEL_BY_ID = "SELECT * FROM UZIVATELE_POHLED WHERE id_uzivatele = ?";
    private final String SELECT_JMENO_UZIVATEL_BY_ID = "SELECT * FROM UZIVATEL_JMENO_PRIJMENI WHERE id_uzivatele = ?";
    private final String SELECT_UZIVATEL_BY_ID_KONTAKTU = "SELECT DISTINCT uzp2.id_uzivatele, uzp2.jmeno, uzp2.prijmeni, uzp2.rok_studia, uzp2.eml, uzp2.blokace, uzp2.poznamka"
            + " FROM KONTAKTY_UZIVATELU ku"
            + " inner join UZIVATELE_POHLED uzp on uzp.id_uzivatele = ku.uzivatele_id_uzivatele"
            + " inner join kontakty k on ku.kontakty_id_kontaktu = k.id_kontaktu"
            + " inner join UZIVATELE_POHLED uzp2 on k.uzivatele_id_uzivatele = uzp2.id_uzivatele"
            + " WHERE ku.kontakty_id_kontaktu = ?";
    private final String SELECT_UZIVATEL_BY_ATTRIBUTE = "SELECT * FROM UZIVATELE_POHLED WHERE UPPER(jmeno) LIKE UPPER(?) OR UPPER(prijmeni) LIKE UPPER(?)";
    private final String SELECT_UZIVATEL_LOGIN = "SELECT * FROM UZIVATELE_POHLED WHERE login = ? AND heslo = ?";
    private final String SELECT_UZIVATELE_COUNT = "SELECT COUNT(*) from UZIVATELE_POHLED";
    private final String INSERT_UZIVATEL = "INSERT INTO UZIVATELE(login, heslo, jmeno, prijmeni, rok_studia, eml, blokace, poznamka) VALUES (?,?,?,?,?,?,?,?)";
    private final String DELETE_UZIVATEL = "DELETE FROM UZIVATELE WHERE id_uzivatele = ?";
    // Statements for uzivatele end

    // Statements for role start
    private final String SELECT_ROLE = "SELECT * FROM UZIVATELE_ROLE WHERE ID_UZIVATELE = ?";
    private final String SELECT_ROLE_ALL = "SELECT * FROM ROLE";
    // Statements for role end

    // Statements for kontakty start
    private final String SELECT_UZIVATELE_V_KONTAKTECH_BY_ID_UZIVATELE = "SELECT uzp2.id_uzivatele, uzp2.jmeno, uzp2.prijmeni, uzp2.rok_studia, uzp2.eml, uzp2.blokace, uzp2.poznamka"
            + " FROM UZIVATELE_POHLED uzp"
            + " inner join kontakty_uzivatelu ku on uzp.id_uzivatele = ku.uzivatele_id_uzivatele"
            + " inner join kontakty k on ku.kontakty_id_kontaktu = k.id_kontaktu"
            + " inner join UZIVATELE_POHLED uzp2 on k.uzivatele_id_uzivatele = uzp2.id_uzivatele"
            + " WHERE UZP.id_uzivatele = ?";
    private final String SELECT_LAST_KONTAKT_ID = "SELECT max (id_kontaktu) FROM KONTAKTY_POHLED";
    private final String SELECT_KONTAKT_ID_BY_USER_ID = "SELECT id_kontaktu from KONTAKTY_POHLED WHERE UZIVATELE_ID_UZIVATELE = ?";
    private final String CV_KONTAKTY_POHLED = "CREATE OR REPLACE VIEW KONTAKTY_POHLED AS SELECT * FROM KONTAKTY";
    private final String SELECT_KONTAKTY_ALL = "SELECT * FROM KONTAKTY_POHLED WHERE uzivatele_id_uzivatele = ?";
    private final String SELECT_KONTAKTY_UZIVATELE = "SELECT k.id_kontaktu, u2.id_uzivatele, u2.jmeno, u2.prijmeni, u2.prihlasen, u2.blokace"
            + " FROM UZIVATELE_POHLED u"
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
    private final String DELETE_KONTAKT = "DELETE FROM KONTAKTY WHERE id_kontaktu = ? AND uzivatele_id_uzivatele = ?";
    private final String ODEBRAT_Z_KONTAKTU = "DELETE FROM KONTAKTY_UZIVATELU WHERE KONTAKTY_ID_KONTAKTU = ? AND UZIVATELE_ID_UZIVATELE = ?"; // TODO: dodělat
    // Statements for kontakty end

    // Statements for zpravy start
    private final String SELECT_LAST_MESSAGE = "SELECT max (id_zpravy) FROM ZPRAVY_POHLED";
    private final String CV_ZPRAVY_POHLED = "CREATE OR REPLACE VIEW ZPRAVY_POHLED AS SELECT * FROM ZPRAVY";
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
    // Statements for zpravy end

    // Statements for studijniPlany start
    private final String CV_STUDIJNI_PLANY_POHLED = "CREATE OR REPLACE VIEW STUDIJNI_PLANY_POHLED AS SELECT * FROM STUDIJNI_PLANY";
    private final String SELECT_STUDIJNI_PLANY = "SELECT * FROM STUDIJNI_PLANY_POHLED";
    private final String SELECT_STUDIJNI_PLAN = "SELECT * FROM STUDIJNI_PLANY_POHLED WHERE id_planu = ?";
    private final String SELECT_STUDIJNI_PLANY_UZIVATELE = "select * FROM STUDIJNI_PLANY_UZIVATELE where id_uzivatele = ?";
    private final String SELECT_STUDIJNI_PLANY_PREDMETU = "select * FROM STUDIJNI_PLANY_PREDMETU where id_predmetu = ?";
    private final String SELECT_UZIVATELE_BY_STUDIJNI_PLAN = "select * FROM CLENOVE_SKUPINY where id_planu = ?";
    private final String SELECT_STUDIJNI_PLANY_OBORU = "select * FROM STUDIJNI_PLANY_POHLED where SO_ID_OBORU = ?";
    private final String SELECT_STUDIJNI_PLAN_BY_ATTRIBUTE = "SELECT * FROM STUDIJNI_PLANY_POHLED WHERE UPPER(nazev) LIKE UPPER(?)";
    private final String INSERT_STUDIJNI_PLAN = "INSERT INTO STUDIJNI_PLANY(nazev, so_id_oboru, popis) VALUES (?,?,?)";
    private final String INSERT_STUDIJNI_PLANY_UZIVATELU = "INSERT INTO STUDIJNI_PLANY_UZIVATELU(UZIVATELE_ID_UZIVATELE, SP_ID_PLANU) VALUES (?, ?)";
    private final String DELETE_STUDIJNI_PLAN = "DELETE FROM STUDIJNI_PLANY WHERE id_planu = ?";
    private final String UPDATE_STUDIJNI_PLAN = "UPDATE STUDIJNI_PLANY SET nazev = ?, popis = ?, so_id_oboru = ? where id_planu = ?";
    // Statements for studijniPlany end

    // Statements for studijniObory start
    private final String CV_STUDIJNI_OBORY_POHLED = "CREATE OR REPLACE VIEW STUDIJNI_OBORY_POHLED AS SELECT * FROM STUDIJNI_OBORY";
    private final String SELECT_STUDIJNI_OBORY = "SELECT * FROM STUDIJNI_OBORY_POHLED";
    private final String SELECT_STUDIJNI_OBOR = "SELECT * FROM STUDIJNI_OBORY_POHLED WHERE id_oboru = ?";
    private final String SELECT_STUDIJNI_OBORY_BY_ATTRIBUTE = "SELECT * FROM STUDIJNI_OBORY_POHLED WHERE UPPER(nazev) LIKE UPPER(?) OR UPPER(zkratka_oboru) LIKE UPPER(?)";
    private final String INSERT_STUDIJNI_OBOR = "INSERT INTO STUDIJNI_OBORY(NAZEV, ZKRATKA_OBORU, POPIS, AKREDITACE_DO) VALUES (?,?,?,?)";
    private final String DELETE_OBOR = "DELETE FROM STUDIJNI_OBORY WHERE id_oboru = ?";
    private final String UPDATE_OBOR = "UPDATE STUDIJNI_OBORY SET nazev = ?, popis = ?, akreditace_do = ?  where id_oboru = ?";
    // Statements for studijniObory end

    // Statements for predmety start
    private final String CV_PREDMETY_POHLED = "CREATE OR REPLACE VIEW PREDMETY_POHLED AS SELECT * FROM PREDMETY";
    private final String SELECT_PREDMETY = "SELECT * FROM PREDMETY_POHLED";
    private final String SELECT_PREDMETY_SP = "SELECT * FROM PREDMETY_SP WHERE ID_PLANU = ?";
    private final String SELECT_PREDMETY_UZIVATELE = "SELECT * FROM PREDMETY_UZIVATELE WHERE ID_UZIVATELE = ?";
    private final String SELECT_PREDMET = "SELECT * FROM PREDMETY_POHLED WHERE id_predmetu = ?";
    private final String SELECT_PREDMET_BY_ATTRIBUTE = "SELECT * FROM PREDMETY_POHLED WHERE UPPER(zkratka_predmetu) LIKE UPPER(?) OR UPPER(nazev_predmetu) LIKE UPPER(?)";
    private final String INSERT_PREDMET = "INSERT INTO PREDMETY(nazev_predmetu,zkratka_predmetu,popis) VALUES (?,?,?)";
    private final String DELETE_PREDMET = "DELETE FROM PREDMETY WHERE id_predmetu = ?";
    private final String UPDATE_PREDMET_NAZEV = "UPDATE PREDMETY SET nazev_predmetu = ? where id_predmetu = ?";
    private final String UPDATE_PREDMET = "UPDATE PREDMETY SET nazev_predmetu = ?, zkratka_predmetu = ?, popis = ?  where id_predmetu = ?";
    // Statements for predmety end

    // Statements for prispevky start
    private final String CV_PRISPEVKY_POHLED = "CREATE OR REPLACE VIEW PRISPEVKY_POHLED AS SELECT * FROM PRISPEVKY";
    private final String SELECT_LAST_PRISPEVEK_ID = "SELECT max (id_prispevku) FROM PRISPEVKY_POHLED";
    private final String SELECT_PRISPEVKY = "SELECT * FROM PRISPEVKY_POHLED";
    private final String SELECT_PRISPEVEK = "SELECT * FROM PRISPEVKY_POHLED WHERE id_prispevku = ?";
    private final String SELECT_KOMENTARE = "SELECT * FROM PRISPEVKY_POHLED WHERE PRISPEVKY_ID_PRISPEVKU = ?";
    private final String SELECT_PRISPEVKY_SKUPINY = "select p.id_prispevku, p.obsah_prispevku, p.cas_odeslani, p.blokace, p.priorita_prispevku, p.id_autora, p.nazev from prispevky p"
            + " inner join skupiny s on s.prispevky_id_prispevku = p.id_prispevku"
            + " inner join studijni_plany sp on sp.id_planu = s.sp_id_planu"
            + " where sp.id_planu = ?";
    private final String INSERT_PRISPEVEK = "INSERT INTO PRISPEVKY(obsah_prispevku, cas_odeslani, blokace, priorita_prispevku, id_autora, nazev) VALUES (?,?,?,?,?,?)";
    private final String INSERT_KOMENTAR = "INSERT INTO PRISPEVKY(obsah_prispevku, cas_odeslani, blokace, priorita_prispevku, id_autora, prispevky_id_prispevku) VALUES (?,?,?,?,?,?)";
    private final String INSERT_PRISPEVEK_SKUPINY = "INSERT INTO SKUPINY(PRISPEVKY_ID_PRISPEVKU, SP_ID_PLANU) VALUES (?,?)";
    private final String DELETE_PRISPEVEK = "DELETE FROM PRISPEVKY WHERE id_prispevku = ?";
    private final String UPDATE_PRISPEVEK = "UPDATE PRISPEVKY SET nazev = ?, obsah = ?, blokace = ?  where id_prispevku = ?";
    // Statements for prispevky end

    public DbHelper(Connection con) throws SQLException {
        this.con = con;
        createViewUzivatelePohled();
        createViewZpravyPohled();
        createViewStudijniObory();
        createViewStudijniPlany();
        createViewPredmety();
        createViewPrispevky();
    }

    public Connection getCon() {
        return con;
    }

    // Methods for uzivatele start
    /**
     * Vytvoří pohled do tabulky uzivatele
     *
     * @throws SQLException
     */
    private void createViewUzivatelePohled() throws SQLException {
        PreparedStatement prepare = con.prepareStatement(CV_UZIVATELE_POHLED, ResultSet.CLOSE_CURSORS_AT_COMMIT);
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
        PreparedStatement prepare = con.prepareStatement(SELECT_UZIVATELE_ALL);
        ResultSet result = prepare.executeQuery();

        while (result.next()) {
            listSelect.add(new Uzivatel(result.getInt("id_uzivatele"),
                    result.getString("jmeno"), result.getString("prijmeni"),
                    result.getString("EML"), result.getString("login"),
                    result.getInt("rok_studia"), result.getInt("blokace"),
                    result.getString("poznamka")));

        }

        result.close();
        prepare.close();
        return listSelect;
    }

    public int selectUzivatelOnline(int idUzivatele) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(SELECT_UZIVATEL_ONLINE);
        prepare.setInt(1, idUzivatele);
        ResultSet result = prepare.executeQuery();
        result.next();
        int online = result.getInt("prihlasen");
        result.close();
        prepare.close();

        return online;
    }

    public int selectUzivatelBan(int idUzivatele) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(SELECT_UZIVATEL_ZABLOKOVAN);
        prepare.setInt(1, idUzivatele);
        ResultSet result = prepare.executeQuery();
        result.next();
        int online = result.getInt("blokace");
        result.close();
        prepare.close();

        return online;
    }

    /**
     * Najde v databázi konkrétního uživatele podle jeho id
     *
     * @param idUzivatele
     * @return uživatele s příslušným id
     * @throws SQLException
     */
    public Uzivatel selectUzivatelById(int idUzivatele) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(SELECT_UZIVATEL_BY_ID);
        prepare.setInt(1, idUzivatele);
        Uzivatel uzivatel;
        ResultSet result = prepare.executeQuery();
        result.next();
        uzivatel = new Uzivatel(result.getInt("id_uzivatele"),
                result.getString("jmeno"), result.getString("prijmeni"),
                result.getString("EML"), result.getString("login"),
                result.getInt("rok_studia"), result.getInt("blokace"),
                result.getString("poznamka"));

        uzivatel.setRole(selectRoleUzivatele(idUzivatele));
        result.close();
        prepare.close();
        return uzivatel;
    }

    /**
     * Najde v databázi jméno a příjmení konkrétního uživatele podle jeho id
     *
     * @param idUzivatele
     * @return uživatele s příslušným id
     * @throws SQLException
     */
    public String[] selectJmenoUzivateleById(int idUzivatele) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(SELECT_JMENO_UZIVATEL_BY_ID);
        prepare.setInt(1, idUzivatele);
        ResultSet result = prepare.executeQuery();
        result.next();
        String jmeno = result.getString("jmeno");
        String prijmeni = result.getString("prijmeni");
        String[] jmena = {jmeno, prijmeni};
        result.close();
        prepare.close();
        return jmena;
    }

    /**
     * Vybere uživatele na základě příslušného id kontaktu
     *
     * @param idKontaktu id kontaktu daného uživatele
     * @return uživatel dle daného id kontaktu
     * @throws SQLException
     */
    public Uzivatel selectUzivatelByIdKontaktu(int idKontaktu) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(SELECT_UZIVATEL_BY_ID_KONTAKTU);
        prepare.setInt(1, idKontaktu);
        Uzivatel uzivatel;
        ResultSet result = prepare.executeQuery();
        result.next();
        uzivatel = new Uzivatel(result.getInt("id_uzivatele"),
                result.getString("jmeno"), result.getString("prijmeni"),
                result.getString("EML"),
                result.getInt("rok_studia"), result.getInt("blokace"),
                result.getString("poznamka"));

        uzivatel.setRole(selectRoleUzivatele(result.getInt("id_uzivatele")));
        result.close();
        prepare.close();
        return uzivatel;
    }

    /**
     * Vrátí seznam uživatelů odpovídajících danému kritériu
     *
     * @param attribut jméno nebo příjmení uživatele
     * @return seznam uživatelů splňujícíh podmínky
     * @throws SQLException
     */
    public List<Uzivatel> selectUzivateleByAttributes(String attribut) throws SQLException {
        List<Uzivatel> listSelect = new ArrayList<>();
        PreparedStatement prepare = con.prepareStatement(SELECT_UZIVATEL_BY_ATTRIBUTE);
        prepare.setString(1, "%" + attribut + "%");
        prepare.setString(2, "%" + attribut + "%");
        ResultSet result = prepare.executeQuery();

        while (result.next()) {
            listSelect.add(new Uzivatel(result.getInt("id_uzivatele"),
                    result.getString("jmeno"), result.getString("prijmeni"),
                    result.getString("EML"), result.getString("login"),
                    result.getInt("rok_studia"), result.getInt("blokace"),
                    result.getString("poznamka")));

        }
        result.close();
        prepare.close();
        return listSelect;
    }

    /**
     * Vrátí seznam uživatelů na základě seznamu kontaktů dle id uživatele
     *
     * @param idUzivatele id uživatele, kterému seznam kontaktů patří
     * @return seznam uživatelů
     * @throws SQLException
     */
    public List<Uzivatel> selectUzivateleVKontatechByIdUzivatele(int idUzivatele) throws SQLException {
        List<Uzivatel> listSelect = new ArrayList<>();

        PreparedStatement prepare = con.prepareStatement(SELECT_UZIVATELE_V_KONTAKTECH_BY_ID_UZIVATELE);
        prepare.setInt(1, idUzivatele);
        ResultSet result = prepare.executeQuery();

        while (result.next()) {
            Uzivatel uzivatel = new Uzivatel(result.getInt("id_uzivatele"),
                    result.getString("jmeno"), result.getString("prijmeni"),
                    result.getString("EML"),
                    result.getInt("rok_studia"), result.getInt("blokace"),
                    result.getString("poznamka"));
            listSelect.add(uzivatel);
        }

        result.close();
        prepare.close();
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
    public int prihlasUzivatele(String login, String heslo) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(SELECT_UZIVATEL_LOGIN);
        prepare.setString(1, login);
        prepare.setString(2, heslo);
        ResultSet result = prepare.executeQuery();
        try {
            result.next();
            int idUzivatele = result.getInt("ID_UZIVATELE");

            int ban = selectUzivatelBan(idUzivatele);
            if (ban != 0) {
                return 1;
            }

            int online = selectUzivatelOnline(idUzivatele);
            if (online != 0) {
                return 2;
            }

            setCurrentUser(selectUzivatelById(idUzivatele));
            prepare.close();
            result.close();
            return 0;
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
            return -1;
        }
    }

    /**
     * Nastaví součadného uživatele
     *
     * @param user uživatel, který má být nastaven jako současný
     */
    public void setCurrentUser(Uzivatel user) throws SQLException {
        this.currentUser = user;
        currentUser.setOnline(1);
        setUserOnline(currentUser.getIdUzivatele());
    }

    public void unsetCurrentUser() throws SQLException {
        if (currentUser != null) {
            currentUser.setOnline(0);
            setUserOffline(currentUser.getIdUzivatele());
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
        PreparedStatement prepare = con.prepareStatement(SELECT_UZIVATELE_COUNT);
        ResultSet result = prepare.executeQuery();
        result.next();
        int count = result.getInt(1);
        result.close();
        return count;
    }

    /**
     * Nastaví uživateli v databázi statut online
     *
     * @param id_uzivatele id uživatele pro nastavení
     * @throws SQLException
     */
    private void setUserOnline(int id_uzivatele) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(SET_UZIVATEL_ONLINE, ResultSet.CLOSE_CURSORS_AT_COMMIT);
        prepare.setInt(1, id_uzivatele);

        prepare.execute();
        con.commit();
    }

    /**
     * Nastaví uživateli v databázi statut offline
     *
     * @param id_uzivatele id uživatele pro nastavení
     * @throws SQLException
     */
    private void setUserOffline(int id_uzivatele) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(SET_UZIVATEL_OFFLINE, ResultSet.CLOSE_CURSORS_AT_COMMIT);
        prepare.setInt(1, id_uzivatele);

        prepare.execute();
        con.commit();
    }

    /**
     * Zaregistruje uživatele
     *
     * @param name jméno uživatele
     * @param surname příjení uživatele
     * @param password heslo uživatele
     * @param eml e-mail uživatele
     * @param poznamka poznámka uživatele
     * @param rocnik ročník uživatele
     * @param studijniPlan studijní plány uživatele
     * @param roleUzivatele role uživatele
     * @throws SQLException
     */
    public void registerUser(String name, String surname, String password, String eml, String poznamka, Integer rocnik, List<StudijniPlan> studijniPlan, List<Role> roleUzivatele) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(INSERT_UZIVATEL, ResultSet.CLOSE_CURSORS_AT_COMMIT); // TODO Předělat příkaz insert_uzivatel, dodat přidání do rolí uživatelů a studijních plánů uživatelů
        prepare.setString(1, password);
        prepare.setString(2, name);
        prepare.setString(3, surname);
        prepare.setInt(4, rocnik);
        prepare.setString(5, eml);
        prepare.setInt(6, 0);
        prepare.setString(7, poznamka);
        prepare.execute();
        con.commit();
    }

    // Methods for uzivatele end
    // Methods for roleUzivatelu start
    /**
     * Získá seznam rolí daného uživatele
     *
     * @param idUzivatele id uživatele
     * @return seznam rolí uživatele
     * @throws SQLException
     */
    public List<Role> selectRoleUzivatele(int idUzivatele) throws SQLException {
        List<Role> listSelect = new ArrayList<>();
        PreparedStatement prepare = con.prepareStatement(SELECT_ROLE);
        prepare.setInt(1, idUzivatele);
        ResultSet result = prepare.executeQuery();

        while (result.next()) {
            Role role = new Role(result.getInt("id_role"), result.getString("jmeno_role"), result.getString("opravneni"), result.getString("poznamka"));
            listSelect.add(role);
        }

        result.close();
        prepare.close();

        return listSelect;
    }

    /**
     * Získá seznam všech rolí
     *
     * @return seznam rolí uživatele
     * @throws SQLException
     */
    public List<Role> selectRole() throws SQLException {
        List<Role> listSelect = new ArrayList<>();
        PreparedStatement prepare = con.prepareStatement(SELECT_ROLE_ALL);
        ResultSet result = prepare.executeQuery();

        while (result.next()) {
            Role role = new Role(result.getInt("id_role"), result.getString("jmeno_role"), result.getString("opravneni"), result.getString("poznamka"));
            listSelect.add(role);
        }

        result.close();
        prepare.close();

        return listSelect;
    }
    // Methods for roleUzivatelu end

    // Methods for kontakty start
    /**
     * Vybere všechny kontakty daného uživatele
     *
     * @param idUzivatele
     * @return seznam kontaktů uživatele
     * @throws SQLException
     */
    public List<KontaktVypis> selectKontaktyUzivatele(int idUzivatele) throws SQLException {
        List<KontaktVypis> listSelect = new ArrayList<>();
        PreparedStatement prepare = con.prepareStatement(SELECT_KONTAKTY_UZIVATELE);
        prepare.setInt(1, idUzivatele);
        ResultSet result = prepare.executeQuery();

        while (result.next()) {
            listSelect.add(new KontaktVypis(result.getInt("id_kontaktu"),
                    result.getInt("id_uzivatele"), result.getString("jmeno"),
                    result.getString("prijmeni"), result.getInt("prihlasen"),
                    result.getInt("blokace")));
        }
        result.close();
        prepare.close();
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
        kontakt = new Kontakt(result.getInt("id_kontaktu"),
                result.getInt("uzivatele_id_uzivatele"));
        result.close();
        prepare.close();
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
        PreparedStatement prepare = con.prepareStatement(INSERT_KONTAKT, ResultSet.CLOSE_CURSORS_AT_COMMIT);
        prepare.setDate(1, Date.valueOf(datumOd));
        prepare.setDate(2, Date.valueOf(datumDo));
        prepare.setInt(3, 1);
        prepare.setString(4, poznamka);
        prepare.execute();

        con.commit();
    }

    private void insertKontaktyUzivatelu(int idUzivatele, int idKontaktu) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(INSERT_KONTAKTY_UZIVATELU, ResultSet.CLOSE_CURSORS_AT_COMMIT);
        prepare.setInt(1, idUzivatele);
        prepare.setInt(2, idKontaktu);

        prepare.execute();
        con.commit();
    }

    private int selectLastContactId() throws SQLException {
        PreparedStatement prepare = con.prepareStatement(SELECT_LAST_KONTAKT_ID);
        ResultSet res = prepare.executeQuery();
        res.next();
        int id = res.getInt(1);
        res.close();
        prepare.close();
        return id;
    }

    private int selectContactIdByUserId(int idUzivatele) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(SELECT_KONTAKT_ID_BY_USER_ID);
        prepare.setInt(1, idUzivatele);
        ResultSet res = prepare.executeQuery();
        res.next();
        int id = res.getInt(1);
        res.close();
        prepare.close();
        return id;
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
        PreparedStatement prepare = con.prepareStatement(DELETE_KONTAKT, ResultSet.CLOSE_CURSORS_AT_COMMIT);
        prepare.setString(1, idKontaktu);
        prepare.setString(2, idUzivatele);
        prepare.execute();
        con.commit();
    }
    // Methods for kontakty end

    // Methods for zpravy start
    private void createViewZpravyPohled() throws SQLException {
        PreparedStatement prepare = con.prepareStatement(CV_ZPRAVY_POHLED, ResultSet.CLOSE_CURSORS_AT_COMMIT);
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

    /**
     * Vrátí jménou autora podle jeho id
     *
     * @param idAutora id autora
     * @return jméno autora zprávy
     * @throws SQLException
     */
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

    /**
     * Vrátí seznam zpráv vybraných kontaktů
     *
     * @param idKontaktu seznam id kontaktů
     * @param idAutora id autora zprávy
     * @return seznam příslušných zpráv
     * @throws SQLException
     */
    public List<Zprava> selectZpravyVybranychKontaktu(List<Integer> idKontaktu, int idAutora) throws SQLException {
        boolean selectGroupMessage = idKontaktu.size() > 1;
        List<Zprava> zpravy = new ArrayList<>();
        for (Integer integer : idKontaktu) {
            zpravy.addAll(selectZpravyKontaktu(integer, idAutora, selectGroupMessage));
        }

        return zpravy;
    }

    /**
     * Zjistí, kolikrát se zpráva v databázi vyskytuje
     *
     * @param idZpravy id hledané zprávy
     * @return počet výskytů zprávy
     * @throws SQLException
     */
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

    /**
     * Pošle zprávu danému kontaktu
     *
     * @param obsah obsah zprávy
     * @param id_odesilatele id autora
     * @param id_kontaktu seznam id příjemců
     * @throws SQLException
     */
    public void poslatZpravu(String obsah, int id_odesilatele, List<Integer> id_kontaktu) throws SQLException {
        insertZprava(obsah, id_odesilatele);
        for (Integer integer : id_kontaktu) {
            insertZpravaKontaktu(selectLastMessage(), integer);
        }

    }

    /**
     * Získá id poslední poslané zprávy
     *
     * @return id zprávy
     * @throws SQLException
     */
    private int selectLastMessage() throws SQLException {
        PreparedStatement prepare = con.prepareStatement(SELECT_LAST_MESSAGE);
        ResultSet result = prepare.executeQuery();
        result.next();
        int index = result.getInt(1);
        result.close();
        prepare.close();
        return index;
    }

    /**
     * Zaznamená poslání zprávy kontaktu
     *
     * @param idZpravy id dané zprávy
     * @param idKontaktu id kontaktu
     * @throws SQLException
     */
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
    // Methods for zpravy end

    // Methods for studijniPlany start
    /**
     * Vytvoří pohled studijních plánů
     *
     * @throws SQLException
     */
    private void createViewStudijniPlany() throws SQLException {
        PreparedStatement prepare = con.prepareStatement(CV_STUDIJNI_PLANY_POHLED);
        prepare.execute();
        con.commit();
    }

    /**
     * Vrátí seznam všech studijních plánů
     *
     * @return seznam studijních plánů
     * @throws SQLException
     */
    public List<StudijniPlan> selectStudijniPlany() throws SQLException {
        List<StudijniPlan> listSelect = new ArrayList<>();
        PreparedStatement prepare = con.prepareStatement(SELECT_STUDIJNI_PLANY);
        ResultSet result = prepare.executeQuery();

        while (result.next()) {
            listSelect.add(new StudijniPlan(result.getInt("id_planu"), result.getString("nazev"), result.getInt("so_id_oboru"), result.getString("popis")));

        }
        return listSelect;
    }

    /**
     * Vrátí seznam všech studijních plánů daného oboru
     *
     * @param idOboru id oboru
     * @return seznam studijních plánů
     * @throws SQLException
     */
    public List<StudijniPlan> selectStudijniPlanyOboru(int idOboru) throws SQLException {
        List<StudijniPlan> listSelect = new ArrayList<>();
        PreparedStatement prepare = con.prepareStatement(SELECT_STUDIJNI_PLANY_OBORU);
        prepare.setInt(1, idOboru);
        ResultSet result = prepare.executeQuery();

        while (result.next()) {
            listSelect.add(new StudijniPlan(result.getInt("id_planu"), result.getString("nazev"), result.getInt("so_id_oboru"), result.getString("popis")));

        }
        prepare.close();
        result.close();
        return listSelect;
    }

    /**
     * Vrátí seznam studijních plánů uživatele
     *
     * @param idUzivatele id daného uživatele
     * @return seznam studijních plánů uživatele
     * @throws SQLException
     */
    public List<StudijniPlan> selectStudijniPlanyUzivatele(int idUzivatele) throws SQLException {
        List<StudijniPlan> listSelect = new ArrayList<>();
        PreparedStatement prepare = con.prepareStatement(SELECT_STUDIJNI_PLANY_UZIVATELE);
        prepare.setInt(1, idUzivatele);
        ResultSet result = prepare.executeQuery();

        while (result.next()) {
            listSelect.add(new StudijniPlan(result.getInt("id_planu"), result.getString("nazev"), result.getInt("so_id_oboru"), result.getString("popis")));

        }
        return listSelect;
    }
    /**
     * Vrátí seznam studijních plánů uživatele
     *
     * @param idPredmetu id daného předmětu
     * @return seznam studijních plánů uživatele
     * @throws SQLException
     */
    public List<StudijniPlan> selectStudijniPlanyPredmetu(int idPredmetu) throws SQLException {
        List<StudijniPlan> listSelect = new ArrayList<>();
        PreparedStatement prepare = con.prepareStatement(SELECT_STUDIJNI_PLANY_PREDMETU);
        prepare.setInt(1, idPredmetu);
        ResultSet result = prepare.executeQuery();

        while (result.next()) {
            listSelect.add(new StudijniPlan(result.getInt("id_planu"), result.getString("nazev"), result.getInt("so_id_oboru"), result.getString("popis")));

        }
        return listSelect;
    }

    /**
     * Vybere konkrétní studijní plán
     *
     * @param idPlanu id plánu
     * @return studijní plán
     * @throws SQLException
     */
    public StudijniPlan selectStudijniPlan(int idPlanu) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(SELECT_STUDIJNI_PLAN);
        prepare.setInt(1, idPlanu);
        StudijniPlan plan;
        ResultSet result = prepare.executeQuery();
        plan = new StudijniPlan(result.getInt("id_planu"), result.getString("nazev"), result.getInt("so_id_oboru"), result.getString("popis"));
        return plan;
    }

    /**
     * Vrátí seznam studijních plánů odpovídajících zadaným atributům
     *
     * @param nazev název studijního plánu
     * @return seznam studijních plánů
     * @throws SQLException
     */
    public List<StudijniPlan> selectStudijniPlanByAttribute(String nazev) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(SELECT_STUDIJNI_PLAN_BY_ATTRIBUTE);
        prepare.setString(1, "%" + nazev + "%");
        List<StudijniPlan> listSelect = new ArrayList<>();
        ResultSet result = prepare.executeQuery();
        while (result.next()) {
            listSelect.add(new StudijniPlan(result.getInt("id_planu"), result.getString("nazev"), result.getInt("so_id_oboru"), result.getString("popis")));
        }
        return listSelect;
    }

    /**
     * Vrátí seznam uživatelů, zapsaných do studijního plánu
     *
     * @param idPlanu id plánu
     * @return seznam uživatelů
     * @throws SQLException
     */
    public List<Uzivatel> selectUzivateleByStudijniPlan(int idPlanu) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(SELECT_UZIVATELE_BY_STUDIJNI_PLAN);
        prepare.setInt(1, idPlanu);
        List<Uzivatel> listSelect = new ArrayList<>();
        ResultSet result = prepare.executeQuery();
        while (result.next()) {
            listSelect.add(new Uzivatel(result.getInt("id_uzivatele"),
                    result.getString("jmeno"), result.getString("prijmeni"),
                    result.getString("EML"),
                    result.getInt("rok_studia"), result.getInt("blokace"),
                    result.getString("poznamka")));
        }
        result.close();
        prepare.close();
        return listSelect;
    }

    /**
     * Vloží studijní plán do databáze
     *
     * @param nazev název plánu
     * @param idOboru id oboru
     * @param popis popis studijního plánu
     * @throws SQLException
     */
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
    // Methods for studijniPlany start

    // Methods for studijniObory start
    /**
     * Vytvoří pohled studijních oborů
     *
     * @throws SQLException
     */
    private void createViewStudijniObory() throws SQLException {
        PreparedStatement prepare = con.prepareStatement(CV_STUDIJNI_OBORY_POHLED);
        prepare.execute();
        con.commit();
    }

    /**
     * Vrátí seznam všech studijních oborů
     *
     * @return seznam studijních oborů
     * @throws SQLException
     */
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

        result.close();
        prepare.close();
        return listSelect;
    }

    /**
     * Vrátí studijní obor podle jeho id
     *
     * @param idOboru id oboru
     * @return studijní obor
     * @throws SQLException
     */
    public StudijniObor selectStudijniObor(int idOboru) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(SELECT_STUDIJNI_OBOR);
        prepare.setInt(1, idOboru);
        StudijniObor obor;
        ResultSet result = prepare.executeQuery();
        result.next();
        obor = new StudijniObor(result.getInt("id_oboru"),
                result.getString("nazev"), result.getString("zkratka_oboru"),
                result.getString("popis"),
                result.getDate("akreditace_do").toLocalDate());
        result.close();
        prepare.close();
        return obor;
    }

    /**
     * Vrátí seznam studijních oborů dle daného atributu
     *
     * @param attribute atribut pro vyhledávání
     * @return seznam studijních oborů
     * @throws SQLException
     */
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

    /**
     * Vloží studijní obor do databáze
     *
     * @param nazev název oboru
     * @param zkratka zkratka oboru
     * @param popis popis oboru
     * @param akreditaceDo akreditace do oboru
     * @throws SQLException
     */
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
    // Methods for studijniObory end

    // Methods for predmety start
    /**
     * Vytvoří pohled předmětů
     *
     * @throws SQLException
     */
    private void createViewPredmety() throws SQLException {
        PreparedStatement prepare = con.prepareStatement(CV_PREDMETY_POHLED);
        prepare.execute();
        con.commit();
    }

    /**
     * Vybere všechny předměty z databáze
     *
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
     * Vybere všechny předměty daného uživatele z databáze
     *
     * @return seznam předmětů
     * @throws SQLException
     */
    public List<Predmet> selectPredmetyUzivatele(int idUzivatele) throws SQLException {
        List<Predmet> listSelect = new ArrayList<>();
        PreparedStatement prepare = con.prepareStatement(SELECT_PREDMETY_UZIVATELE);
        prepare.setInt(1, idUzivatele);
        ResultSet result = prepare.executeQuery();

        while (result.next()) {
            listSelect.add(new Predmet(result.getInt("id_predmetu"), result.getString("nazev_predmetu"), result.getString("zkratka_predmetu"), result.getString("popis")));

        }

        result.close();
        prepare.close();
        return listSelect;
    }

    /**
     * Vybere všechny předměty daného plánu z databáze
     *
     * @param idPlanu
     * @return seznam předmětů
     * @throws SQLException
     */
    public List<Predmet> selectPredmetyStudijnihoPlanu(int idPlanu) throws SQLException {
        List<Predmet> listSelect = new ArrayList<>();
        PreparedStatement prepare = con.prepareStatement(SELECT_PREDMETY_SP);
        prepare.setInt(1, idPlanu);
        ResultSet result = prepare.executeQuery();

        while (result.next()) {
            listSelect.add(new Predmet(result.getInt("id_predmetu"), result.getString("nazev_predmetu"), result.getString("zkratka_predmetu"), result.getString("popis")));

        }

        result.close();
        prepare.close();
        return listSelect;
    }

    /**
     * Vybere předmět podle id
     *
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
     *
     * @param attribute zkratka nebo název
     * @return předmět dle atributu
     * @throws SQLException
     */
    public List<Predmet> selectPredmetyByAttribute(String attribute) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(SELECT_PREDMET_BY_ATTRIBUTE);
        String attr = "%" + attribute + "%";
        prepare.setString(1, attr);
        prepare.setString(2, attr);
        List<Predmet> listSelect = new ArrayList<>();
        ResultSet result = prepare.executeQuery();

        while (result.next()) {
            listSelect.add(new Predmet(result.getInt("id_predmetu"), result.getString("nazev_predmetu"), result.getString("zkratka_predmetu"), result.getString("popis")));

        }

        return listSelect;
    }

    /**
     * Vloží předmět do databáze
     *
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
     *
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
     *
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
    // Methods for predmety end

    // Methods for prispevky start
    /**
     * Vytvoří pohled pro příspěvky
     *
     * @throws SQLException
     */
    private void createViewPrispevky() throws SQLException {
        PreparedStatement prepare = con.prepareStatement(CV_PRISPEVKY_POHLED);
        prepare.execute();
        con.commit();
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
            Uzivatel uziv = selectUzivatelById(result.getInt("id_autora"));
            String jmeno = uziv.getJmeno() + " " + uziv.getPrijmeni();
            listSelect.add(new Prispevek(result.getInt("id_prispevku"), result.getString("obsah_prispevku"), result.getTimestamp("cas_odeslani").toLocalDateTime(), result.getInt("blokace"), result.getInt("priorita_prispevku"), result.getInt("id_autora"), result.getString("nazev"), jmeno));
        }
        return listSelect;
    }

    /**
     * Vybere komentáře příspěvku
     *
     * @param idPrispevku idPrispevku komentáře
     * @return seznam komentářů
     * @throws SQLException
     */
    public List<Prispevek> selectKomentare(int idPrispevku) throws SQLException {
        List<Prispevek> listSelect = new ArrayList<>();
        PreparedStatement prepare = con.prepareStatement(SELECT_KOMENTARE);
        prepare.setInt(1, idPrispevku);
        ResultSet result = prepare.executeQuery();

        while (result.next()) {
            String[] jmena = selectJmenoUzivateleById(result.getInt("id_autora"));
            String jmeno = jmena[0] + " " + jmena[1];
            listSelect.add(new Prispevek(result.getInt("id_prispevku"), result.getString("obsah_prispevku"), result.getTimestamp("cas_odeslani").toLocalDateTime(), result.getInt("blokace"), result.getInt("priorita_prispevku"), result.getInt("id_autora"), result.getString("nazev"), jmeno)); // TODO: idAutora není potřeba
        }

        result.close();
        prepare.close();
        return listSelect;
    }

    public List<Prispevek> selectPrispevkySkupiny(int idSkupiny) throws SQLException {
        List<Prispevek> listSelect = new ArrayList<>();
        PreparedStatement prepare = con.prepareStatement(SELECT_PRISPEVKY_SKUPINY);
        prepare.setInt(1, idSkupiny);
        ResultSet result = prepare.executeQuery();

        while (result.next()) {
            Uzivatel uziv = selectUzivatelById(result.getInt("id_autora"));
            String jmeno = uziv.getJmeno() + " " + uziv.getPrijmeni();
            listSelect.add(new Prispevek(result.getInt("id_prispevku"), result.getString("obsah_prispevku"), result.getTimestamp("cas_odeslani").toLocalDateTime(), result.getInt("blokace"), result.getInt("priorita_prispevku"), result.getInt("id_autora"), result.getString("nazev"), jmeno));
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
        Uzivatel uziv = selectUzivatelById(result.getInt("id_autora"));
        String jmeno = uziv.getJmeno() + " " + uziv.getPrijmeni();
        prispevek = new Prispevek(result.getInt("id_prispevku"), result.getString("obsah_prispevku"), result.getTimestamp("cas_odeslani").toLocalDateTime(), result.getInt("blokace"), result.getInt("priorita_prispevku"), result.getInt("id_autora"), result.getString("nazev"), jmeno);
        return prispevek;
    }

    /**
     * Vloží příspěvek do databáze
     *
     * @param nazev název příspěvku
     * @param obsah obsah příspěvku
     * @param casOdeslani
     * @param priorita
     * @param idAutora
     * @throws SQLException
     */
    private void insertPrispevek(String nazev, String obsah, LocalDateTime casOdeslani, int priorita, int idAutora) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(INSERT_PRISPEVEK);
        prepare.setString(1, obsah);
        prepare.setTimestamp(2, Timestamp.valueOf(casOdeslani));
        prepare.setInt(3, 0);
        prepare.setInt(4, priorita);
        prepare.setInt(5, idAutora);
        prepare.setString(6, nazev);
        prepare.execute();
        con.commit();
    }

    /**
     * Vloží příspěvek do databáze
     *
     * @param nazev název příspěvku
     * @param obsah obsah příspěvku
     * @param casOdeslani
     * @param priorita
     * @param idAutora
     * @throws SQLException
     */
    private void insertKomentar(String obsah, LocalDateTime casOdeslani, int idAutora, int idNadrazeneho) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(INSERT_KOMENTAR);
        prepare.setString(1, obsah);
        prepare.setTimestamp(2, Timestamp.valueOf(casOdeslani));
        prepare.setInt(3, 0);
        prepare.setInt(4, 0);
        prepare.setInt(5, idAutora);
        prepare.setInt(6, idNadrazeneho);
        prepare.execute();
        con.commit();
    }

    private void insertPrispevekSkupiny(int idPrispevku, int idSkupiny) throws SQLException {
        PreparedStatement prepare = con.prepareStatement(INSERT_PRISPEVEK_SKUPINY);
        prepare.setInt(1, idPrispevku);
        prepare.setInt(2, idSkupiny);
        prepare.execute();
        con.commit();
    }

    private int selectLastPrispevekId() throws SQLException {
        PreparedStatement prepare = con.prepareStatement(SELECT_LAST_PRISPEVEK_ID);
        ResultSet result = prepare.executeQuery();
        result.next();
        int id = result.getInt(1);
        result.close();
        prepare.close();
        return id;
    }

    public void sendPrispevekToGroup(String nazev, String obsah, LocalDateTime casOdeslani, int priorita, int idAutora, int idSkupiny) throws SQLException {
        insertPrispevek(nazev, obsah, casOdeslani, priorita, idAutora);
        insertPrispevekSkupiny(selectLastPrispevekId(), idSkupiny);
    }

    public void sendKomentar(String obsah, LocalDateTime casOdeslani, int idAutora, int idNadrazeneho) throws SQLException {
        insertKomentar(obsah, casOdeslani, idAutora, idNadrazeneho);
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
    // Methods for prispevky end
}
