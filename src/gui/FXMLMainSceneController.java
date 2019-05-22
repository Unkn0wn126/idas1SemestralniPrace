/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import database.DbHelper;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import model.KontaktVypis;
import model.Predmet;
import model.Role;
import model.StudijniObor;
import model.StudijniPlan;
import model.Uzivatel;
import model.Zprava;

/**
 * FXML Controller class
 *
 * @author Lukas
 */
public class FXMLMainSceneController implements Initializable {

    /**
     * Komunikátor s databází
     */
    private DbHelper dbHelper;

    // Controllers start
    private FXMLLoader loader;
    private FXMLContactsController contactsController;
    private FXMLConversationController conversationController;
    private FXMLAccountViewController accountController;
    private FXMLUserListController userListController;
    private FXMLFieldListController fieldListController;
    private FXMLSubjectsListController subjectsListController;
    private FXMLCurriculumListController curriculumListController;
    private FXMLGroupFeedController groupFeedController;
    private FXMLRegisterController registerController;
    private FXMLAddSubjectController addSubjectController;
    private FXMLAddFieldController addFieldController;
    private FXMLAddCurriculumController addCurriculumController;
    private FXMLEditUserController editUserController;
    // Controllers end

    // Layouts start
    private GridPane contactsMenu;
    private GridPane conversationMenu;
    private GridPane accountMenu;
    private GridPane userListMenu;
    private GridPane fieldListMenu;
    private GridPane subjectsListMenu;
    private GridPane curriculumListMenu;
    private GridPane groupFeedMenu;
    private GridPane registerMenu;
    private GridPane addSubjectMenu;
    private GridPane addFieldMenu;
    private GridPane addCurriculumMenu;
    private GridPane editUserMenu;
    // Layouts end

    /**
     * Akce, která se má provést pro odhlášení
     */
    private Consumer<ActionEvent> logoutAction;

    /**
     * Pomocná proměnná pro určení statutu admina
     */
    private boolean userIsAdmin = false;

    /**
     * Levý panel
     */
    @FXML
    private FlowPane paneLeft;
    /**
     * Hlavní panel
     */
    @FXML
    private FlowPane paneCenter;

    /**
     * Menu pro přidávání nových záznamů Přístupné pouze uživateli s rolí
     * administrátora
     */
    @FXML
    private Menu menuPridat;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void setDbHelper(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
        userIsAdmin = true;
    }

    /**
     * Nastaví akci, která se má provést při pokusu o odhlášení uživatele
     *
     * @param logoutAction akce, která se má provést pro odhlášení
     */
    public void setLogoutAction(Consumer<ActionEvent> logoutAction) {
        this.logoutAction = logoutAction;
    }

    /**
     * Nastaví, zda se mají zobrazit možnosti určené pouze pro admina
     */
    public void setAdminPermissions() {
        userIsAdmin = dbHelper.getCurrentUser().isAdmin(); // TODO: Implementovat

        menuPridat.setDisable(!userIsAdmin);
        menuPridat.setVisible(userIsAdmin);
    }

    // Layout adjust start
    /**
     * Nastaví automatickou změnu velikosti potomka podle rodiče
     *
     * @param parent rodič
     * @param child potomek
     */
    private void setAutoResize(FlowPane parent, GridPane child) {
        parent.widthProperty().addListener((observable, oldValue, newValue) -> {
            child.setPrefWidth((double) newValue);
        });

        parent.heightProperty().addListener((observable, oldValue, newValue) -> {
            child.setPrefHeight((double) newValue);
        });
    }

    /**
     * Nastaví automatickou změnu velikosti potomka hlavního panelu
     *
     * @param child potomek hlavního panelu
     */
    private void setAutoResizeCenter(GridPane child) {
        setAutoResize(paneCenter, child);
    }

    /**
     * Aktualizuje rozměry potomka podle rodiče
     *
     * @param parent rodič
     * @param child potomek
     */
    private void updateDimensions(FlowPane parent, GridPane child) {
        child.setPrefWidth(parent.getWidth());
        child.setPrefHeight(parent.getHeight());
    }

    /**
     * Aktualizuje rozměry potomka hlavního panelu
     *
     * @param child potomek hlavního panelu
     */
    private void updateDimensionsCenter(GridPane child) {
        updateDimensions(paneCenter, child);
    }

    /**
     * Načte současně aktivní menu hlavního panelu
     *
     * @param child menu pro načtení
     */
    private void loadCurrentMenu(GridPane child) {
        // Menu není načteno v hlavním panelu
        if (!paneCenter.getChildren().contains(child)) {
            paneCenter.getChildren().clear();
            paneCenter.getChildren().add(child);

            updateDimensionsCenter(child);
        }
    }

    // Layout adjust end
    // Contacts menu section start
    /**
     * Načte seznam kontaktů a skupin uživatele do levého panelu
     */
    public void loadContactsMenu() {
        loader = new FXMLLoader();
        try {
            // Načtení layoutu kontaktů a skupin start
            loader.setLocation(getClass().getResource("FXMLContacts.fxml"));
            contactsMenu = loader.load();
            contactsController = loader.getController();

            setAutoResize(paneLeft, contactsMenu);

            paneLeft.getChildren().add(contactsMenu);
            // Načtení layoutu kontaktů a skupin end

            setContactsMenuActions();

            setContactsMenuData(dbHelper.getCurrentUser().getIdUzivatele());

        } catch (IOException ex) {
            Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Nastaví akce, které se mají provést při událostech v levém panelu
     */
    private void setContactsMenuActions() {
        // Při kliknutí na kontakt se načte konverzace s daným kontaktem
        contactsController.setSelectedContactChangedAction((t) -> {
            if (t.size() == 1 && t.get(0).getBlokace() != 0) {
                showDialog("Uživatel je zabloková adminem síťě. "
                        + "Posílat mu zprávy nyní nebude mít příliš velký efekt.");
            }
            loadConversationMenu(t);
        });

        // Pri kliknutí na skupinu se načte seznam zpráv v dané skupině
        contactsController.setSelectedGroupChangedAction((t) -> {
            loadGroupFeedMenu(t.getIdPlanu());
        });

        // Při kliknutí na kontextovou možnost "zobrazit profil" se
        // zobrazí informace o profilu vybraného uživatele
        contactsController.setContextMenuShowProfileAction((t) -> {
            try {
                loadAccountMenu(dbHelper.selectUzivatelByIdKontaktu(t.getIdKontaktu()));
            } catch (SQLException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        contactsController.setRemoveContactAction((t) -> {
            // TODO: odebrání kontaktu
        });

        contactsController.setShowGroupMembersAction((t) -> {
            try {
                List<Uzivatel> clenove = getClenoveSkupiny(t.getIdPlanu());
                loadUserListMenu(clenove);
            } catch (SQLException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    /**
     * Nastaví současný seznam dat pro zobrazení
     *
     * @throws SQLException při chybě komunikace s databází
     */
    public void setContactsMenuData(int idUzivatele) throws SQLException {
        contactsController.setContactDataSet(dbHelper.
                selectKontaktyUzivatele(dbHelper.getCurrentUser().getIdUzivatele()));

        if (userIsAdmin) {
            contactsController.setGroupsDataSet(dbHelper.selectStudijniPlany());
        } else {
            contactsController.setGroupsDataSet(getStudijniPlanyUzivatele(idUzivatele));
        }
    }

    // Contacts menu section end
    // Conversation menu section start
    /**
     * Načte menu s konverzací s kontaktem
     */
    private void loadConversationMenu(List<KontaktVypis> prijemci) {
        if (conversationMenu == null) { // Ošetření nullPointerException
            loader = new FXMLLoader();
            try {
                // Načtení layoutu start
                loader.setLocation(getClass().getResource("FXMLConversation.fxml"));
                conversationMenu = loader.load();
                conversationController = loader.getController();
                // Načtení layoutu end

                setAutoResizeCenter(conversationMenu);

                setConversationMenuActions();
            } catch (IOException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        loadCurrentMenu(conversationMenu);

        try {
            setConversationMenuData(prijemci);
        } catch (SQLException ex) {
            Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Nastaví potřebné instance tříd pro komunikaci s databází
     */
    private void setConversationMenuData(List<KontaktVypis> prijemci) throws SQLException {
        List<Integer> idKontaktu = new ArrayList<>();
        List<Zprava> intermed = new ArrayList<>();

        for (KontaktVypis kontaktVypis : prijemci) {
            idKontaktu.add(kontaktVypis.getIdKontaktu());
        }

        List<Zprava> zpr = dbHelper.selectZpravyVybranychKontaktu(idKontaktu, dbHelper.getCurrentUser().getIdUzivatele());
        intermed.addAll(zpr);

        Set<Integer> idSet = new HashSet<>();
        List<Zprava> a = intermed.stream().filter((t) -> idSet.add(t.getIdZpravy())).collect(Collectors.toList());

        a.sort((t, t1) -> {
            return (t.getCasOdeslani().compareTo(t1.getCasOdeslani()));
        });
        conversationController.updateMessages(a, dbHelper.getCurrentUser().getJmeno(), prijemci);
    }

    /**
     * Nastaví akce, které se mají vykonat při událostech v menu zpráv
     */
    private void setConversationMenuActions() {
        conversationController.setPoslatZpravuAction((t) -> {
            List<Integer> idKontaktu = new ArrayList<>();
            List<KontaktVypis> kontakty = contactsController.getSelectedUsers();
            for (KontaktVypis kontaktVypis : kontakty) {
                idKontaktu.add(kontaktVypis.getIdKontaktu());
            }
            try {
                dbHelper.poslatZpravu(t.getObsahZpravy(), dbHelper.getCurrentUser().getIdUzivatele(), idKontaktu);
                setConversationMenuData(kontakty);
            } catch (SQLException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    // Conversation menu section end
    // Account menu section start
    /**
     * Načte menu profilu uživatele
     *
     * @param uzivatel uživatel, jehož profil se má zobrazit
     */
    private void loadAccountMenu(Uzivatel uzivatel) {
        if (accountMenu == null) { // Ošetření nullPointerException
            loader = new FXMLLoader();
            try {
                // Načtení layoutu start
                loader.setLocation(getClass().getResource("FXMLAccountView.fxml"));
                accountMenu = loader.load();
                accountController = loader.getController();
                // Načtení layoutu end

                setAutoResizeCenter(accountMenu);

                setAccountMenuActions();

            } catch (IOException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        loadCurrentMenu(accountMenu);

        try {
            // Updatuje informace profilu podle současného uživatele
            setAccountMenuData(uzivatel);
        } catch (SQLException ex) {
            Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Nastaví třídy potřebné pro práci s databází
     */
    private void setAccountMenuData(Uzivatel uzivatel) throws SQLException {
        List<Role> role = dbHelper.selectRoleUzivatele(uzivatel.getIdUzivatele());
        List<StudijniPlan> studijniPlan = getStudijniPlanyUzivatele(uzivatel.getIdUzivatele());
        accountController.updateView(uzivatel, role, studijniPlan);
        accountController.setContactsDataSet(dbHelper.selectUzivateleVKontatechByIdUzivatele(uzivatel.getIdUzivatele()));
        accountController.setAddToContactsAction((t) -> {
            addUzivateleDoKontaktu(t);
        });
        accountController.setShowProfileAction((t) -> {
            loadAccountMenu(t);
        });
    }

    /**
     * Nastaví akce, které se mají provést při událostech v menu profilu
     *
     * @param uzivatel uživatel, kterému profil patří
     */
    private void setAccountMenuActions() {
        /*Nastaví použitelnost tlačítka pro editaci podle toho,
        jestli se jedná o vlastní profil uživatele nebo je uživatel admin*/
        accountController.setCanEditAction((t) -> {
            return (t.getIdUzivatele() == dbHelper.
                    getCurrentUser().getIdUzivatele() || userIsAdmin);
        });

        // Načte menu pro editaci profilu daného uživatele
        accountController.setEditButtonAction((t) -> {
            loadEditUserMenu(t);
        });
    }

    // Account menu section end
    // UserList menu section start
    /**
     * Nastaví akce, které se mají provést při událostech v menu seznamu
     * uživatelů
     */
    private void setUserListMenuActions() {
        // Přidá uživateli daného uživatele do kontaktů
        // Přidanému uživateli se automaticky přidá uživatel,
        // který si ho přidal také
        userListController.setAddToContactsAction((t) -> {
            addUzivateleDoKontaktu(t);
        });

        userListController.setContextMenuShowProfileAction((t) -> {
            loadAccountMenu(t);
        });
    }

    /**
     * Nastaví data pro zobrazení v seznamu uživatelů
     *
     * @param uzivatele seznam uživatelů
     */
    private void setUserListMenuData(List<Uzivatel> uzivatele) {
        userListController.setDataSet(uzivatele);
    }

    /**
     * Přidá daného uživatele do seznamu kontaktů lokálního uživatele
     *
     * @param t uživatel pro přidání
     */
    private void addUzivateleDoKontaktu(Uzivatel t) {
        Uzivatel uzivatel = dbHelper.getCurrentUser();

        // Uživatel existuje a není to ten samý uživatel, který
        // přidává
        if (t != null && t.getIdUzivatele() != uzivatel.getIdUzivatele()) {
            try {
                // Přidání uživatele do kontaktů současného uživatele
                dbHelper.addToContacts(dbHelper.getCurrentUser().getIdUzivatele(), t.getIdUzivatele());
                // Aktualizuje seznam kontaktů uživatele
                setContactsMenuData(uzivatel.getIdUzivatele());
            } catch (SQLException ex) {
                showAlert("Uživatel už je mezi kontakty zařazen");
            }
        } else if (t.getIdUzivatele() == uzivatel.getIdUzivatele()) {
            showAlert("Sebe sama nelze přidat do kontaktů!");
        }
    }

    /**
     * Načte seznam uživatelů
     *
     * @param uzivatele seznam uživatelů, kteří se mají zobrazit
     */
    private void loadUserListMenu(List<Uzivatel> uzivatele) {
        if (userListMenu == null) {
            loader = new FXMLLoader();
            try {
                // Načtení layoutu start
                loader.setLocation(getClass().getResource("FXMLUserList.fxml"));
                userListMenu = loader.load();
                userListController = loader.getController();
                // Načtení layoutu end

                setAutoResizeCenter(userListMenu);

                setUserListMenuActions();
            } catch (IOException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        loadCurrentMenu(userListMenu);

        // Aktualizuje seznam kontaktů uživatele
        setUserListMenuData(uzivatele);
    }

    // UserList menu section end
    // FieldList menu section start
    /**
     * Načte menu seznamu oborů
     *
     * @param obory seznam oborů pro zobrazení
     */
    private void loadFieldListMenu(List<StudijniObor> obory) {
        if (fieldListMenu == null) {
            loader = new FXMLLoader();
            try {
                // Načtení layoutu start
                loader.setLocation(getClass().getResource("FXMLFieldList.fxml"));
                fieldListMenu = loader.load();
                fieldListController = loader.getController();
                // Načtení layoutu end

                setAutoResizeCenter(fieldListMenu);

                setFieldListMenuActions();
            } catch (IOException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        loadCurrentMenu(fieldListMenu);

        // Aktualizuje seznam oborů pro zobrazení
        setFieldListData(obory);
    }

    /**
     * Nastaví akce, které se mají provést v menu seznamu oborů
     */
    private void setFieldListMenuActions() {
        // TODO: dodělat
    }

    /**
     * Nastaví data pro zobrazení
     *
     * @param obory seznam oborů pro zobrazení
     */
    private void setFieldListData(List<StudijniObor> obory) {
        fieldListController.setDataset(obory);
    }

    // FieldList menu section end
    // SubjectList menu section start
    private void setSubjectListMenuData(List<Predmet> predmety) {
        subjectsListController.setDataSet(predmety);
    }

    /**
     * Nastaví akce, které se mají provést při událostech v menu seznamu
     * předmětů
     */
    private void setSubjectListMenuActions() {
        // TODO: dodělat; přidat možnost zobrazení detailů o předmětu
    }

    /**
     * Načte menu seznamu předmětů
     *
     * @param predmety seznam předmětů pro vypsání
     */
    private void loadSubjectListMenu(List<Predmet> predmety) {
        if (subjectsListMenu == null) {
            loader = new FXMLLoader();
            try {
                // Načtení layoutu start
                loader.setLocation(getClass().getResource("FXMLSubjectList.fxml"));
                subjectsListMenu = loader.load();
                subjectsListController = loader.getController();
                // Načtení layoutu end

                setAutoResizeCenter(subjectsListMenu);

                setSubjectListMenuActions();
            } catch (IOException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        loadCurrentMenu(subjectsListMenu);

        // Aktualizuje seznam předmětů pro zobrazení
        setSubjectListMenuData(predmety);
    }
    // SubjectList menu section end

    // CurriculumList menu section start
    /**
     * Nastaví akce, které se mají provést při událostech v menu seznamu
     * studijních plánů
     */
    private void setCurriculumListMenuActions() {
        // TODO: dodělat; přidat možnost zobrazení informací o studijním plánu
    }

    private void setCurriculumListData(List<StudijniPlan> plany) {
        curriculumListController.setDataset(plany);
    }

    /**
     * Načte menu se seznamem studijních plánů
     *
     * @param plany seznam studijních plánů pro zobrazení
     */
    private void loadCurriculumListMenu(List<StudijniPlan> plany) {
        if (curriculumListMenu == null) {
            loader = new FXMLLoader();
            try {
                // Načtení layoutu start
                loader.setLocation(getClass().getResource("FXMLCurriculumList.fxml"));
                curriculumListMenu = loader.load();
                curriculumListController = loader.getController();
                // Načtení layoutu end

                setAutoResizeCenter(curriculumListMenu);

                setCurriculumListMenuActions();
            } catch (IOException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        loadCurrentMenu(curriculumListMenu);

        // Aktualizuje seznam studijních plánů
        setCurriculumListData(plany);
    }

    // CurriculumList menu section end
    // GroupFeed menu section start
    /**
     * Nastaví potřebné instance tříd pro komunikaci s databází
     */
    private void setGroupFeedMenuData(int idSkupiny) {
        try {
            groupFeedController.updateFeed(dbHelper.selectPrispevkySkupiny(idSkupiny), dbHelper.getCurrentUser());
        } catch (SQLException ex) {
            Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Nastaví akce, které se mají vykonat při událostech v příspěvků skupiny
     */
    private void setGroupFeedMenuActions() {
        groupFeedController.setBtnOdeslatAction((t) -> {
            try {
                int currGroupId = contactsController.getSelectedGroupId();
                dbHelper.sendPrispevekToGroup(t.getNazev(), t.getObsahPrispevku(), t.getCasOdeslani(), t.getPriorita(), t.getIdAutora(), currGroupId);
                loadGroupFeedMenu(currGroupId);
            } catch (SQLException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    /**
     * Načte menu se zprávami skupiny
     */
    private void loadGroupFeedMenu(int idSkupiny) {
        if (groupFeedMenu == null) { // Ošetření nullPointerException
            loader = new FXMLLoader();
            try {
                // Načtení layoutu start
                loader.setLocation(getClass().getResource("FXMLGroupFeed.fxml"));
                groupFeedMenu = loader.load();
                groupFeedController = loader.getController();
                // Načtení layoutu end

                setAutoResizeCenter(groupFeedMenu);

                setGroupFeedMenuActions();

            } catch (IOException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        loadCurrentMenu(groupFeedMenu);
        setGroupFeedMenuData(idSkupiny);
    }

    // GroupFeed menu section end
    // Register menu section start
    /**
     * Nastaví akce, které se mají provést v menu registrace uživatele
     */
    private void setRegisterMenuActions() {
        registerController.setRegisterAction((t) -> { // TODO: ošetřit vstupy
            String jmeno = registerController.getJmeno();
            String prijmeni = registerController.getPrijmeni();
            String login = registerController.getLogin();
            String heslo1 = registerController.getHeslo();
            String heslo2 = registerController.getHesloConfirm();
            String email = registerController.getEmail();
            String poznamka = registerController.getPoznamka();
            Integer rok = registerController.getRocnik();
//            StudijniPlan obor = registerController.getStudijniPlan();

            if (jmeno.length() > 0 && prijmeni.length() > 0
                    && login.length() > 0 && heslo1.length() > 0
                    && heslo2.length() > 0 && heslo1.equals(heslo2)
                    && email.length() > 0 && poznamka.length() > 0
                    && rok != null) {
//                try {
//                    // TODO: Dodělat zapsání uživatele do studijních plánů, atd.
//                    dbHelper.registerUser(jmeno, prijmeni, login, heslo1, email, poznamka, rok);
//String name, String surname, String password, String eml, String poznamka, Integer rocnik, List<StudijniPlan> studijniPlan, List<Role> roleUzivatele
//                } catch (SQLException ex) {
//                    Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
//                }
                showDialog("Uživatel úspěšně nahrán do databáze");
            }
        });

        // Při zrušení registrace se vyprázdní hlavní panel
        registerController.setCancelAction((t) -> {
            paneCenter.getChildren().clear();
        });
    }

    private void setRegisterMenuData(List<StudijniPlan> plany, List<Role> role, List<StudijniObor> obory) {
        registerController.setPlanyDataset(plany);
        registerController.setRoleDataset(role);
        registerController.setOboryDataset(obory);
    }

    /**
     * Načte menu registrace uživatele
     *
     * @param obory seznam oborů pro výběr
     */
    private void loadRegisterMenu(List<StudijniPlan> plany, List<Role> role, List<StudijniObor> obory) {
        if (registerMenu == null) {
            loader = new FXMLLoader();
            try {
                // Načtení layoutu start
                loader.setLocation(getClass().getResource("FXMLRegister.fxml"));
                registerMenu = loader.load();
                registerController = loader.getController();
                // Načtení layoutu end

                setAutoResizeCenter(registerMenu);

                setRegisterMenuActions();

            } catch (IOException ex) {
                showAlert("Neplatné vstupy pro uživatele");
            }
        }

        loadCurrentMenu(registerMenu);

        // Aktualizuje seznam oborů
        setRegisterMenuData(plany, role, obory);
    }

    // Register menu section end
    // AddSubject menu section start
    /**
     * Nastaví akce, které se mají provést při událostech v menu přidání
     * předmětu
     */
    private void setAddSubjectMenuActions() {
        // Při zrušení akce se hlavní panel vyprázdní
        addSubjectController.setBtnCancelAction((t) -> {
            paneCenter.getChildren().clear();
        });

        addSubjectController.setBtnSaveAction((t) -> {
//            try {
//                String popis = addSubjectController.getPopis();
//                String nazev = addSubjectController.getNazevPredmetu();
//                String zkratka = addSubjectController.getZkratkaPredmetu();
//
//                db.getPredmetManager().insertPredmet(nazev, zkratka, popis);
//                addSubjectController.clearInputs();
//                showDialog("Předmět úspěšně přidán do databáze");
//            } catch (IllegalArgumentException e) {
//                showAlert(e.getLocalizedMessage());
//            } catch (SQLException ex) {
//                showAlert(ex.getLocalizedMessage());
//            }
        });
    }

    private void setAddSubjectMenuData(List<StudijniPlan> plany) {
        addSubjectController.setPlanyData(plany);
    }

    /**
     * Zobrazí menu pro přidání předmětu
     */
    private void loadAddSubjectMenu(List<StudijniPlan> plany) {
        if (addSubjectMenu == null) {
            loader = new FXMLLoader();
            try {
                // Načtení layoutu start
                loader.setLocation(getClass().getResource("FXMLAddSubject.fxml"));
                addSubjectMenu = loader.load();
                addSubjectController = loader.getController();
                // Načtení layoutu end

                setAutoResizeCenter(addSubjectMenu);

                setAddSubjectMenuActions();
            } catch (IOException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        loadCurrentMenu(addSubjectMenu);

        setAddSubjectMenuData(plany);
    }

    // AddSubject menu section end
    // AddField menu section start
    /**
     * Nastaví akce, které se mají provést v menu přidání studijního plánu
     */
    private void setAddFieldMenuActions() {
        // Při zrušení akce přidání oboru se vyprázdní hlavní panel
        addFieldController.setBtnCancelAction((t) -> {
            paneCenter.getChildren().clear();
        });

        // Akce, která se má provést při pokusu o uložení oboru
        addFieldController.setBtnSaveAction((t) -> {
//            try {
//                String nazev = addFieldController.getNazev();
//                String zkratka = addFieldController.getZkratka();
//                String popis = addFieldController.getPopis();
//                LocalDate akreditaceDo = addFieldController.getAkreditaceDo();
//
//                db.getStudijniOborManager().insertStudijniObor(nazev, zkratka, popis, akreditaceDo);
//                addFieldController.clearInputs();
//
//                showDialog("Obor byl úspěšně nahrán");
//            } catch (IllegalArgumentException e) {
//                showAlert(e.getLocalizedMessage());
//            } catch (SQLException ex) {
//                showAlert(ex.getLocalizedMessage());
//            }
        });
    }

    /**
     * Zobrazí menu pro přidání oboru
     */
    private void loadAddFieldMenu() {
        if (addFieldMenu == null) {
            loader = new FXMLLoader();
            try {
                // Načtení layoutu start
                loader.setLocation(getClass().getResource("FXMLAddField.fxml"));
                addFieldMenu = loader.load();
                addFieldController = loader.getController();
                // Načtení layoutu end

                setAutoResizeCenter(addFieldMenu);

                setAddFieldMenuActions();

            } catch (IOException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        loadCurrentMenu(addFieldMenu);
    }
    // AddField menu section end

    // AccCurriculum menu section start
    /**
     * Nastaví akce, které se mají provést v menu přidání studijního plánu
     */
    private void setAddCurriculumMenuActions() {
        // Při zrušení přidání studijního plánu se vyprázdní hlavní panel
        addCurriculumController.setBtnCancelAction((t) -> {
            paneCenter.getChildren().clear();
        });

        // Akce, která se má provést při pokusu o uložení studijního plánu
        addCurriculumController.setBtnSaveAction((t) -> {
//            try {
//                String nazev = addCurriculumController.getNazev();
//                String popis = addCurriculumController.getPopis();
//                StudijniObor obor = addCurriculumController.getObor();
//                db.getStudijniPlanManager().insertStudijniPlan(nazev, obor.getIdOboru(), popis);
//                addCurriculumController.clearInputs();
//                showDialog("Studijní plán úspěšně nahrán");
//            } catch (IllegalArgumentException e) {
//                showAlert(e.getLocalizedMessage());
//            } catch (SQLException ex) {
//                showAlert(ex.getLocalizedMessage());
//            }
        });
    }

    private void setCurriculumMenuData(List<StudijniObor> obory) {
        addCurriculumController.setDataset(obory);
    }

    /**
     * Zobrazí menu pro přidání studijního plánu
     *
     * @param obory seznam oborů
     */
    private void loadAddCurriculumMenu(List<StudijniObor> obory) {
        if (addCurriculumMenu == null) {
            loader = new FXMLLoader();
            try {
                // Načtení layoutu start
                loader.setLocation(getClass().getResource("FXMLAddCurriculum.fxml"));
                addCurriculumMenu = loader.load();
                addCurriculumController = loader.getController();
                // Načtení layoutu end

                setAutoResizeCenter(addCurriculumMenu);

                setAddCurriculumMenuActions();

            } catch (IOException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        loadCurrentMenu(addCurriculumMenu);

        // Aktualizuje seznam oborů pro zobrazení
        setCurriculumMenuData(obory);
    }
    // AccCurriculum menu section end

    // EditUser menu section start
    /**
     * Nastaví akce, které se mají provést při událostech v menu editace
     * uživatele
     *
     * @param uzivatel
     */
    private void setEditUserMenuActions() { // TODO: Dodělat
        // Při zrušení editace se neuloží změny a zobrazí se info profilu
        editUserController.setBtnCancelEvent((t) -> {
            loadAccountMenu(t);
        });
    }

    /**
     * Nastaví instance potřebné pro práci s databází
     */
    private void setEditUserMenuData(Uzivatel uzivatel) throws SQLException {
        editUserController.updateViews(uzivatel);
        editUserController.updateRole(getRole());
        editUserController.updateStudijniObory(getStudijniObory());
        editUserController.updateStudijniPlany(getStudijniPlany());
    }

    /**
     * Načte menu úprav uživatele
     *
     * @param uzivatel uživatel pro úpravu
     */
    private void loadEditUserMenu(Uzivatel uzivatel) { // TODO: Přidat možnosti úprav pro admina
        if (editUserMenu == null) {
            loader = new FXMLLoader();
            try {
                // Načtení layoutu start
                loader.setLocation(getClass().getResource("FXMLEditUser.fxml"));
                editUserMenu = loader.load();
                editUserController = loader.getController();
                // Načtení layoutu end

                setAutoResizeCenter(editUserMenu);

                setEditUserMenuActions();

            } catch (IOException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        loadCurrentMenu(editUserMenu);

        try {
            setEditUserMenuData(uzivatel);
        } catch (SQLException ex) {
            Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // EditUser menu section end

    private List<StudijniPlan> getStudijniPlany() throws SQLException {
        return dbHelper.selectStudijniPlany();
    }

    private List<Role> getRole() throws SQLException {
        return dbHelper.selectRole();
    }

    private List<StudijniObor> getStudijniObory() throws SQLException {
        return dbHelper.selectStudijniObory();
    }

    // Dialogues start
    /**
     * Zobrazí menu pro vyhledávání
     *
     * @param searchAction akce, která se má pro vyhledání provést
     */
    private void showSearchMenu(Consumer<String> searchAction, String searchTerm) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText(null);
        dialog.setContentText("Zadejte " + searchTerm);
        dialog.setTitle("Vyhledávání");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            if (result.get().length() > 0) {
                searchAction.accept(result.get());
            } else {
                showAlert("Musíte zadat hledaný pojem");
            }
        }
    }

    /**
     * Zobrazí dialog s errorem
     *
     * @param text text erroru
     */
    private void showAlert(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(text);
        alert.setHeaderText(null);

        alert.showAndWait();
    }

    /**
     * Zobrazí informační dialog
     *
     * @param text text informace
     */
    private void showDialog(String text) {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Information");
        dialog.setHeaderText(null);
        dialog.setContentText(text);

        dialog.showAndWait();
    }
    // Dialogues end

    /**
     * Získá seznam studijních plánů uživatele
     *
     * @param idUzivatele id uživatele pro vyhledání
     * @return seznam studijních plánů zadaného uživatele
     * @throws SQLException
     */
    private List<StudijniPlan> getStudijniPlanyUzivatele(int idUzivatele) throws SQLException {
        return dbHelper.selectStudijniPlanyUzivatele(idUzivatele);
    }

    /**
     * Získá členy dané skupiny
     *
     * @param idPlanu id studijního plánu dané skupiny
     * @return seznam členů dané skupiny
     * @throws SQLException
     */
    private List<Uzivatel> getClenoveSkupiny(int idPlanu) throws SQLException {
        return dbHelper.selectUzivateleByStudijniPlan(idPlanu);
    }

    @FXML
    private void handleMenuItemLogoutAction(ActionEvent event) {
        if (logoutAction != null) { // Ošetření nullPointerException
            logoutAction.accept(event);
        }
    }

    @FXML
    private void handleMenuItemZobrazitAction(ActionEvent event) {
        loadAccountMenu(dbHelper.getCurrentUser());
    }

    @FXML
    private void handleMenuItemZobrazitUzivateleAction(ActionEvent event) {
        try {
            loadUserListMenu(dbHelper.selectUzivatele());
        } catch (SQLException ex) {
            Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleMenuItemZobrazitPredmetyAction(ActionEvent event) {
        try {
            loadSubjectListMenu(dbHelper.selectPredmety());
        } catch (SQLException ex) {
            Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleMenuItemZobrazitStudijniOboryAction(ActionEvent event) {
        try {
            loadFieldListMenu(dbHelper.selectStudijniObory());
        } catch (SQLException ex) {
            Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleMenuItemZobrazitStudijniPlanyAction(ActionEvent event) {
        try {
            loadCurriculumListMenu(dbHelper.selectStudijniPlany());
        } catch (SQLException ex) {
            Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleMenuItemVyhledatUzivateleAction(ActionEvent event) {
        showSearchMenu((t) -> {
            try {
                loadUserListMenu(dbHelper.selectUzivateleByAttributes(t));
            } catch (SQLException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }, "jméno nebo příjmení uživatele");
    }

    @FXML
    private void handleMenuItemVyhledatPredmetAction(ActionEvent event) {
        showSearchMenu((t) -> {
            try {
                loadSubjectListMenu(dbHelper.selectPredmetyByAttribute(t));
            } catch (SQLException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }, "název nebo zkratku předmětu");
    }

    @FXML
    private void handleMenuItemVyhledatStudijniOborAction(ActionEvent event) {
        showSearchMenu((t) -> {
            try {
                loadFieldListMenu(dbHelper.selectStudijniOboryByAttribute(t));
            } catch (SQLException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }, "jméno nebo zkratku studijního oboru");
    }

    @FXML
    private void handleMenuItemVyhledatStudijniPlanAction(ActionEvent event) {
        showSearchMenu((t) -> {
            try {
                loadCurriculumListMenu(dbHelper.selectStudijniPlanByAttribute(t));
            } catch (SQLException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }, "jméno studijního plánu");
    }

    @FXML
    private void handleMenuItemPridatUzivateleAction(ActionEvent event) {
        try {
            loadRegisterMenu(getStudijniPlany(), getRole(), getStudijniObory());
        } catch (SQLException ex) {
            Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleMenuItemPridatPredmetAction(ActionEvent event) {
        try {
            loadAddSubjectMenu(getStudijniPlany());
        } catch (SQLException ex) {
            Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleMenuItemPridatStudijniOborAction(ActionEvent event) {
        loadAddFieldMenu();
    }

    @FXML
    private void handleMenuItemPridatStudijniPlanAction(ActionEvent event) {
        try {
            loadAddCurriculumMenu(getStudijniObory());
        } catch (SQLException ex) {
            Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
