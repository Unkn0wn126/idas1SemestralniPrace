package database;

import java.sql.Connection;
import java.sql.SQLException;
import model.KontaktManager;
import model.PredmetManager;
import model.PrispevekManager;
import model.RoleManager;
import model.StudijniOborManager;
import model.StudijniPlanManager;
import model.UzivatelManager;
import model.ZpravaManager;

/**
 * @author Lukas
 */
public class DatabaseHelper {

    private Connection con;
    private UzivatelManager uzivatelManager;
    private StudijniOborManager studijniOborManager;
    private PredmetManager predmetManager;
    private StudijniPlanManager studijniPlanManager;
    private KontaktManager kontaktManager;
    private PrispevekManager prispevekManager;
    private RoleManager roleManager;
    private ZpravaManager zpravaManager;

    public DatabaseHelper(Connection con) throws SQLException {
        this.con = con;
    }

    public UzivatelManager getUzivatelManager() {
        if (uzivatelManager == null) {
            uzivatelManager = new UzivatelManager(con);
        }
        return uzivatelManager;
    }

    public StudijniOborManager getStudijniOborManager() {
        if (studijniOborManager == null) {
            studijniOborManager = new StudijniOborManager(con);
        }
        return studijniOborManager;
    }

    public PredmetManager getPredmetManager() {
        if (predmetManager == null) {
            predmetManager = new PredmetManager(con);
        }
        return predmetManager;
    }

    public StudijniPlanManager getStudijniPlanManager() {
        if (studijniPlanManager == null) {
            studijniPlanManager = new StudijniPlanManager(con);
        }
        return studijniPlanManager;
    }

    public KontaktManager getKontaktManager() {
        if (kontaktManager == null) {
            kontaktManager = new KontaktManager(con);
        }
        return kontaktManager;
    }

    public PrispevekManager getPrispevekManager() {
        if (prispevekManager == null) {
            prispevekManager = new PrispevekManager(con);
        }
        return prispevekManager;
    }

    public RoleManager getRoleManager() {
        if (roleManager == null) {
            roleManager = new RoleManager(con);
        }
        return roleManager;
    }

    public ZpravaManager getZpravaManager() {
        if (zpravaManager == null) {
            zpravaManager = new ZpravaManager(con);
        }
        return zpravaManager;
    }

}
