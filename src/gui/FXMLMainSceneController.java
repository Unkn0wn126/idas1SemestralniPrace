/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import database.DatabaseHelper;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import model.Predmet;
import model.StudijniObor;
import model.StudijniPlan;
import model.Uzivatel;

/**
 * FXML Controller class
 *
 * @author Lukas
 */
public class FXMLMainSceneController implements Initializable {

    /**
     * Instance třídy pro ovládání databáze
     */
    private DatabaseHelper db;

    // Controllers start
    private FXMLLoader loader;
    private FXMLContactsController contactsController;
    private FXMLConversationController conversationController;
    private FXMLGroupFeedController groupFeedController;
    private FXMLAccountViewController accountController;
    private FXMLUserListController userListController;
    private FXMLSubjectsListController subjectsListController;
    private FXMLFieldListController fieldListController;
    private FXMLCurriculumListController curriculumListController;
    private FXMLRegisterController registerController;
    private FXMLEditUserController editUserController;
    private FXMLAddSubjectController addSubjectController;
    private FXMLAddCurriculumController addCurriculumController;
    private FXMLAddFieldController addFieldController;
    // Controllers end

    // Layouty podscén start
    private GridPane contactsMenu;
    private GridPane conversationMenu;
    private GridPane groupFeedMenu;
    private GridPane accountMenu;
    private GridPane userListMenu;
    private GridPane subjectsListMenu;
    private GridPane fieldListMenu;
    private GridPane curriculumListMenu;
    private GridPane registerMenu;
    private GridPane editUserMenu;
    private GridPane addSubjectMenu;
    private GridPane addCurriculumMenu;
    private GridPane addFieldMenu;
    // Layouty podscén end

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
     * Akce, která se má provést pro odhlášení
     */
    private Consumer<ActionEvent> logoutAction;

    private boolean userIsAdmin = false;

    /**
     * Menu pro přidávání nových záznamů Přístupné pouze uživateli s rolí
     * administrátora
     */
    @FXML
    private Menu menuPridat; // TODO: Zpřístupnit pouze adminovi

    /**
     * Nastaví akci, která se má provést při pokusu o odhlášení uživatele
     *
     * @param logoutAction akce, která se má provést pro odhlášení
     */
    public void setLogoutAction(Consumer<ActionEvent> logoutAction) {
        this.logoutAction = logoutAction;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void setAdminPermissions() {
        userIsAdmin = db.getUzivatelManager().getCurrentUser().isAdmin();

        menuPridat.setDisable(!userIsAdmin);
        menuPridat.setVisible(userIsAdmin);
    }

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

    /**
     * Nastaví akce, které se mají provést při událostech v levém panelu
     */
    private void setContactsMenuActions() {
        // Při kliknutí na kontakt se načte konverzace s daným kontaktem
        contactsController.setSelectedContactChangedAction((t) -> {
            loadConversationMenu(); // načtení scény konverzace
            conversationController.updateMessages(); // update dat
        });

        // Pri kliknutí na skupinu se načte seznam zpráv v dané skupině
        contactsController.setSelectedGroupChangedAction((t) -> {
            loadGroupFeedMenu(); // update dat
        });

        // Při kliknutí na kontextovou možnost "zobrazit profil" se
        // zobrazí informace o profilu vybraného uživatele
        contactsController.setContextMenuShowProfileAction((t) -> {
            loadAccountMenu(contactsController.getSelectedUsers().get(0));
        });

        // Při kliknutí na kontextovou možnost "poslat zprávu" se zobrazí
        // menu pro posílání zpráv. Výběrem vícero kontaktů dojde k možnosti
        // poslat zprávu více kontaktům
        contactsController.setContextMenuSendMessageAction((t) -> {
            // Získání seznamu vybraných uživatelů
            List<Uzivatel> users = contactsController.getSelectedUsers();
            for (Object user : users) { // TODO: Dodělat akci pro zasílání zpráv; nebude nutné získávat seznam vybraných uživatelů tady; typ consumeru
                System.out.println(user);
            }
        });
    }

    /**
     * Nastaví potřebné instance tříd pro komunikaci s databází a nastaví
     * současný seznam dat pro zobrazení
     *
     * @throws SQLException při chybě komunikace s databází
     */
    public void setContactsMenuData() throws SQLException {
        // Vložení potřebné instance třídy pro ovládání tabulky UZIVATELE
        contactsController.setUzivatelManager(db.getUzivatelManager());

        // Nastavení dat pro zobrazení
        // TODO: Předělat, aby používalo joiny (možná bude stačit jenom KontaktManager...)
        contactsController.setContactDataSet(db.getKontaktManager().selectKontakty(db.getUzivatelManager().getCurrentUser().getIdUzivatele()));
    }

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

            setContactsMenuData();

        } catch (IOException ex) {
            Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleMenuItemLogoutAction(ActionEvent event) {
        if (logoutAction != null) { // Ošetření nullPointerException
            logoutAction.accept(event);
        }
    }

    /**
     * Nastaví akce, které se mají vykonat při událostech v menu zpráv
     */
    private void setConversationMenuActions() {
        // TODO: Dodělat, napojit na databázi
        // Consumery, které budou přijímat parametr typu uživatel, aby se mohl provést join přes kontakty
    }

    /**
     * Nastaví potřebné instance tříd pro komunikaci s databází
     */
    private void setConversationMenuData() { // TODO: Zbavit se database helperů v menu konverzace...
        // Nastavení instance třídy pro ovládání tabulky UZIVATELE
        conversationController.setUzivatelManager(db.getUzivatelManager()); // TODO: Zjistit, jestli by nestačila instance uzivatele

        // Nastavení instance třídy pro ovládání tabulky ZPRAVY
        conversationController.setZpravaManager(db.getZpravaManager());
    }

    /**
     * Načte menu s konverzací s kontaktem
     */
    private void loadConversationMenu() { // TODO: Zjistit, jestli nejde, aby to drželo instanci kontaktu
        if (conversationMenu == null) { // Ošetření nullPointerException
            loader = new FXMLLoader();
            try {
                // Načtení layoutu start
                loader.setLocation(getClass().getResource("FXMLConversation.fxml"));
                conversationMenu = loader.load();
                conversationController = loader.getController();
                // Načtení layoutu end

                setAutoResizeCenter(conversationMenu);

                setConversationMenuData();

                setConversationMenuActions();
            } catch (IOException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        loadCurrentMenu(conversationMenu);
    }

    /**
     * Nastaví potřebné instance tříd pro komunikaci s databází
     */
    private void setGroupFeedMenuData() {
        groupFeedController.updateFeed();
    }

    /**
     * Nastaví akce, které se mají vykonat při událostech v příspěvků skupiny
     */
    private void setGroupFeedMenuActions() {
        // TODO: dodělat, napojit na databázi
        // TODO: Vymyslet, jak to provést, aby se to všechno dalo řídit odsud; možná to nepůjde... v tom případě pak nastavit helpery a vykonat tam
    }

    /**
     * Načte menu se zprávami skupiny
     */
    private void loadGroupFeedMenu() { // TODO: Upravit, aby se zobrazovaly skutečné příspěvky dané skupiny...
        if (groupFeedMenu == null) { // Ošetření nullPointerException
            loader = new FXMLLoader();
            try {
                // Načtení layoutu start
                loader.setLocation(getClass().getResource("FXMLGroupFeed.fxml"));
                groupFeedMenu = loader.load();
                groupFeedController = loader.getController();
                // Načtení layoutu end

                setAutoResizeCenter(groupFeedMenu);

                setGroupFeedMenuData();

                setGroupFeedMenuActions();

            } catch (IOException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        loadCurrentMenu(groupFeedMenu); // TODO: Potřeba SkupinaManager(?), PrispevekManager
    }

    /**
     * Nastaví pomocnou třídu pro ovládání databáze
     *
     * @param db
     */
    public void setDatabaseHelper(DatabaseHelper db) {
        this.db = db;
    }

    /**
     * Nastaví třídy potřebné pro práci s databází
     */
    private void setAccountMenuData() {
        // Nastaví instanci třídy pro úpravy tabulky KONTAKTY
        accountController.setKontaktManager(db.getKontaktManager());

        // Nastaví instanci třídy pro úpravy tabulky UZIVATELE
        accountController.setUzivatelManager(db.getUzivatelManager());
    }

    /**
     * Nastaví akce, které se mají provést při událostech v menu profilu
     *
     * @param uzivatel uživatel, kterému profil patří
     */
    private void setAccountMenuActions(Uzivatel uzivatel) { // TODO: předělat, aby to nepotřebovalo v parametru uživatele
        /*Nastaví použitelnost tlačítka pro editaci podle toho,
        jestli se jedná o vlastní profil uživatele nebo je uživatel admin*/
        accountController.setEnableButton(uzivatel.getIdUzivatele().
                equals(db.getUzivatelManager().getCurrentUser().getIdUzivatele())
                || userIsAdmin);

        // Updatuje informace profilu podle současného uživatele
        accountController.updateView(uzivatel);

        // Načte menu pro editaci profilu daného uživatele
        accountController.setEditButtonAction((t) -> {
            loadEditUserMenu(uzivatel);
        });
    }

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

                setAccountMenuData();

            } catch (IOException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        loadCurrentMenu(accountMenu);

        // TODO: presunout setAccountMenuActions do vytváření instance a nahradit to tady updatem uživatele
        setAccountMenuActions(uzivatel);
    }

    /**
     * Nastaví akce, které se mají provést při událostech v menu seznamu
     * uživatelů
     */
    private void setUserListMenuActions() {
        // Přidá uživateli daného uživatele do kontaktů
        // Přidanému uživateli se automaticky přidá uživatel,
        // který si ho přidal také
        userListController.setAddToContactsAction((t) -> { // TODO: better přes joiny
            Uzivatel uzivatel = db.getUzivatelManager().getCurrentUser();

            // Uživatel existuje a není to ten samý uživatel, který
            // přidává
            if (t != null && !t.getIdUzivatele().equals(uzivatel.getIdUzivatele())) {
                try {
                    // Přidání uživatele do kontaktů současného uživatele
                    db.getKontaktManager().insertKontakt(t.getIdUzivatele(),
                            uzivatel.getIdUzivatele(), LocalDate.now(),
                            LocalDate.now().plusYears(3), "Kontakt");

                    // Přidání současného uživatele do kontaktů přidávaného uživatele
                    db.getKontaktManager().insertKontakt(uzivatel.getIdUzivatele(),
                            t.getIdUzivatele(), LocalDate.now(),
                            LocalDate.now().plusYears(3), "Kontakt");

                    // Aktualizuje seznam kontaktů uživatele
                    contactsController.setContactDataSet(db.getKontaktManager().selectKontakty(uzivatel.getIdUzivatele())); // TODO: Předělat
                } catch (SQLException ex) {
                    showAlert(ex.getLocalizedMessage());
                }
            } else if (t.getIdUzivatele().equals(uzivatel.getIdUzivatele())) {
                showAlert("Sebe sama nelze přidat do kontaktů!");
            }
        });
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
        userListController.setDataSet(uzivatele);
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
        subjectsListController.setDataSet(predmety);
    }

    /**
     * Nastaví akce, které se mají provést v menu seznamu oborů
     */
    private void setFieldListMenuActions() {
        // TODO: dodělat
    }

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
        fieldListController.setDataset(obory);
    }

    /**
     * Nastaví akce, které se mají provést při událostech v menu seznamu
     * studijních plánů
     */
    private void setCurriculumListMenuActions() {
        // TODO: dodělat; přidat možnost zobrazení informací o studijním plánu
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
        curriculumListController.setDataset(plany);
    }

    /**
     * Nastaví akce, které se mají provést v menu registrace uživatele
     */
    private void setRegisterMenuActions() {
        registerController.setRegisterAction((t) -> { // TODO: ošetřit vstupy (přímo v controlleru(?)), přidat textfield pro původní heslo
            String jmeno = registerController.getJmeno();
            String prijmeni = registerController.getPrijmeni();
            String login = registerController.getLogin();
            String heslo1 = registerController.getHeslo();
            String heslo2 = registerController.getHesloConfirm();
            String email = registerController.getEmail();
            String poznamka = registerController.getPoznamka();
            Integer rok = registerController.getRocnik();
            StudijniObor obor = registerController.getStudijniObor();

            if (jmeno.length() > 0 && prijmeni.length() > 0
                    && login.length() > 0 && heslo1.length() > 0
                    && heslo2.length() > 0 && heslo1.equals(heslo2)
                    && email.length() > 0 && poznamka.length() > 0
                    && rok != null) {
                try {
                    // TODO: Dodělat zapsání uživatele do studijních plánů, atd.
                    db.getUzivatelManager().registerUser(jmeno, prijmeni, login, heslo1, email, poznamka, rok);
                } catch (SQLException ex) {
                    Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
                }
                showDialog("Uživatel úspěšně nahrán do databáze");
            }
        });

        // Při zrušení registrace se vyprázdní hlavní panel
        registerController.setCancelAction((t) -> {
            paneCenter.getChildren().clear();
        });
    }

    /**
     * Načte menu registrace uživatele
     *
     * @param obory seznam oborů pro výběr
     */
    private void loadRegisterMenu(List<StudijniObor> obory) {
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
        registerController.setOboryDataset(obory);
    }

    /**
     * Nastaví akce, které se mají provést při událostech v menu editace
     * uživatele
     *
     * @param uzivatel
     */
    private void setEditUserMenuActions(Uzivatel uzivatel) { // TODO: Dodělat, zkontrolovat správnost metody; zařídit, aby to nepotřebovalo parametr
        // Při zrušení editace se neuloží změny a zobrazí se info profilu
        editUserController.setBtnCancelEvent((t) -> {
            loadAccountMenu(uzivatel);
        });
    }

    /**
     * Nastaví instance potřebné pro práci s databází
     */
    private void setEditUserMenuData() {
        // TODO: Dodělat; dávat jenom surová data; ne database helpery
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

                setEditUserMenuActions(uzivatel);

            } catch (IOException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        loadCurrentMenu(editUserMenu);

        try {
            // Aktualizuje informace o uživateli pro zobrazení
            editUserController.updateViews(uzivatel);
        } catch (SQLException ex) {
            Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Nastaví akce, které se mají provést při událostech v menu přidání
     * předmětu
     */
    private void setAddSubjectMenuActions() {
        // Při zrušení akce se hlavní panel vyprázdní
        addSubjectController.setBtnCancelAction((t) -> {
            paneCenter.getChildren().clear();
        });

        // TODO: Dodělat
    }

    /**
     * Zobrazí menu pro přidání předmětu
     */
    private void loadAddSubjectMenu() { // TODO: Implementovat funkcionalitu přidání předmětu, ošetřovat vstupy
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
    }

    /**
     * Nastaví akce, které se mají provést v menu přidání studijního plánu
     */
    private void setAddCurriculumMenuActions() {
        // Při zrušení přidání studijního plánu se vyprázdní hlavní panel
        addCurriculumController.setBtnCancelAction((t) -> {
            paneCenter.getChildren().clear();
        });

        // Akce, která se má provést při pokusu o uložení studijního plánu
        addCurriculumController.setBtnSaveAction((t) -> { // TODO: Ošetřovat vstupy (už v controlleru(?))
            String nazev = addCurriculumController.getNazev();
            String popis = addCurriculumController.getPopis();
            StudijniObor obor = addCurriculumController.getObor();

            if (nazev.length() > 0 && popis.length() > 0 && obor != null) {
                try {
                    // TODO: Dodělat integraci s databází
                    db.getStudijniPlanManager().insertStudijniPlan(nazev, obor, popis);
                    showDialog("Studijní plán úspěšně nahrán");
                } catch (SQLException ex) {
                    showAlert("Neplatné záznamy pro studijní plán");
                }
            }
        });
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
        addCurriculumController.setDataset(obory);
    }

    /**
     * Nastaví akce, které se mají provést v menu přidání studijního plánu
     */
    private void setAddFieldMenuActions() {
        // Při zrušení akce přidání oboru se vyprázdní hlavní panel
        addFieldController.setBtnCancelAction((t) -> {
            paneCenter.getChildren().clear();
        });

        // Akce, která se má provést při pokusu o uložení oboru
        addFieldController.setBtnSaveAction((t) -> { // TODO: Ošetřovat vstupy (už v controlleru(?))
            String nazev = addFieldController.getNazev();
            String zkratka = addFieldController.getZkratka();
            String popis = addFieldController.getPopis();
            LocalDate akreditaceDo = addFieldController.getAkreditaceDo();

            if (nazev.length() > 0 && zkratka.length() > 0 && popis.length() > 0 && akreditaceDo != null) {
                try {
                    db.getStudijniOborManager().insertStudijniObor(nazev, zkratka, popis, akreditaceDo);
                    addFieldController.clearInputs();
                    showDialog("Obor byl úspěšně nahrán");
                } catch (SQLException ex) {
                    Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                showAlert("Neplatné hodnoty pro obor");
            }
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

    @FXML
    private void handleMenuItemZobrazitAction(ActionEvent event) {
        loadAccountMenu(db.getUzivatelManager().getCurrentUser());
    }

    @FXML
    private void handleMenuItemZobrazitUzivateleAction(ActionEvent event) throws SQLException {
        loadUserListMenu(db.getUzivatelManager().selectUzivatele());
    }

    @FXML
    private void handleMenuItemZobrazitPredmetyAction(ActionEvent event) {
        try {
            loadSubjectListMenu(db.getPredmetManager().selectPredmety());
        } catch (SQLException ex) {
            Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleMenuItemZobrazitStudijniOboryAction(ActionEvent event) {
        try {
            loadFieldListMenu(db.getStudijniOborManager().selectStudijniObory());
        } catch (SQLException ex) {
            Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleMenuItemZobrazitStudijniPlanyAction(ActionEvent event) {
        try {
            loadCurriculumListMenu(db.getStudijniPlanManager().selectStudijniPlany());
        } catch (SQLException ex) {
            Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleMenuItemVyhledatUzivateleAction(ActionEvent event) {
        showSearchMenu((t) -> {
            try {
                loadUserListMenu(db.getUzivatelManager().selectUzivateleByAttributes(t));
            } catch (SQLException ex) {
                showAlert("Uživatele se nepodařilo nalézt");
            }
        }, "jméno nebo příjmení uživatele");
    }

    @FXML
    private void handleMenuItemVyhledatPredmetAction(ActionEvent event) {
        showSearchMenu((t) -> {
            try {
                loadSubjectListMenu(db.getPredmetManager().selectPredmetyByAttribute(t));
            } catch (SQLException ex) {
                showAlert("Předmět se nepodařilo nalézt");
            }
        }, "zkratku nebo název předmětu");
    }

    @FXML
    private void handleMenuItemVyhledatStudijniOborAction(ActionEvent event) {
        showSearchMenu((t) -> {
            try {
                loadFieldListMenu(db.getStudijniOborManager().selectStudijniOboryByAttribute(t));
            } catch (SQLException ex) {
                showAlert("Předmět se nepodařilo nalézt");
            }
        }, "zkratku nebo název studijního oboru");
    }

    @FXML
    private void handleMenuItemVyhledatStudijniPlanAction(ActionEvent event) {
        showSearchMenu((t) -> {
            try {
                loadCurriculumListMenu(db.getStudijniPlanManager().selectStudijniPlany()); // TODO: Dodělat možnost pro vyhledávání
            } catch (SQLException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }, "zkratku nebo název studijního plánu");
    }

    @FXML
    private void handleMenuItemPridatUzivateleAction(ActionEvent event) {
        try {
            loadRegisterMenu(db.getStudijniOborManager().selectStudijniObory());
        } catch (SQLException ex) {
            Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleMenuItemPridatPredmetAction(ActionEvent event) {
        loadAddSubjectMenu();
    }

    @FXML
    private void handleMenuItemPridatStudijniOborAction(ActionEvent event) {
        loadAddFieldMenu();
    }

    @FXML
    private void handleMenuItemPridatStudijniPlanAction(ActionEvent event) {
        try {
            loadAddCurriculumMenu(db.getStudijniOborManager().selectStudijniObory());
        } catch (SQLException ex) {
            Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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

}
