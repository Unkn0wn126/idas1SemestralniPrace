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
import model.Prispevek;
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
    private FXMLShowCurriculumController showCurriculumController;
    private FXMLShowFieldController showFieldController;
    private FXMLShowSubjectController showSubjectController;
    private FXMLEditPostController editPostController;
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
    private GridPane showCurriculumMenu;
    private GridPane showFiledMenu;
    private GridPane showSubjectMenu;
    private GridPane editPost;
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
    public void setAdminPermissions() throws SQLException {
        List<Role> role = getRoleUzivatele(dbHelper.getCurrentUser().getIdUzivatele());
        userIsAdmin = role.stream().anyMatch((t) -> {
            return t.getIdRole() == 1;
        });

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
                showDialog("Uživatel je zablokován adminem sítě. "
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
            try {
                dbHelper.odebratZKontaktu(t.getIdKontaktu(), dbHelper.getCurrentUser().getIdUzivatele());
                contactsController.setContactDataSet(getKontaktyUzivatele(dbHelper.getCurrentUser().getIdUzivatele()));
            } catch (SQLException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        contactsController.setShowGroupMembersAction((t) -> {
            try {
                List<Uzivatel> clenove = getClenoveSkupiny(t.getIdPlanu());
                loadUserListMenu(clenove);
            } catch (SQLException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        contactsController.setShowGroupDetailAction((t) -> {
            loadShowCurriculumMenu(t);
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
        conversationController.updateMessages(a, dbHelper.getCurrentUser(), prijemci);
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
        List<Predmet> predmety = getPredmetyUzivatele(uzivatel.getIdUzivatele());

        accountController.updateView(uzivatel, role, studijniPlan, predmety, userIsAdmin);
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

        accountController.setShowSubjectDetailAction((t) -> {
            loadShowSubjectMenu(t);
        });

        accountController.setDeleteSubjectAction((t) -> {
            try {
                deletePredmet(t.getIdPredmetu());
                showDialog("Předmět úspěšně smazán");
            } catch (SQLException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        accountController.setDeleteCurriculumAction((t) -> {
            try {
                deletePlan(t.getIdPlanu());
                showDialog("Studijní plán úspěšně smazán");
            } catch (SQLException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        accountController.setShowCurriculumDetailAction((t) -> {
            loadShowCurriculumMenu(t);
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

        userListController.setDeleteUserAction((t) -> {
            try {
                if (t.getIdUzivatele() != dbHelper.getCurrentUser().getIdUzivatele()) {
                    deleteUser(t.getIdUzivatele());
                    showDialog("Uživatel úspěšně smazán");
                }else{
                    showAlert("Sebe sama nelze odstranit!");
                }
            } catch (SQLException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        userListController.setBanUserAction((t) -> {
            try {
                if (t.getIdUzivatele() != dbHelper.getCurrentUser().getIdUzivatele()) {
                    int ban = dbHelper.selectUzivatelBan(t.getIdUzivatele());
                    dbHelper.setUserBanned(t.getIdUzivatele(), ++ban % 2);
                    setContactsMenuData(dbHelper.getCurrentUser().getIdUzivatele());
                } else {
                    showAlert("Pokusit zablokovat sám sebe není ta správná strategie správy sítě");
                }

            } catch (SQLException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    /**
     * Nastaví data pro zobrazení v seznamu uživatelů
     *
     * @param uzivatele seznam uživatelů
     */
    private void setUserListMenuData(List<Uzivatel> uzivatele) {
        userListController.setDataSet(uzivatele, userIsAdmin);
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
                List<KontaktVypis> kontakty = getKontaktyUzivatele(uzivatel.getIdUzivatele());
                boolean canBeAdded = true;
                for (KontaktVypis kontaktVypis : kontakty) {
                    if (kontaktVypis.getIdUzivatele() == t.getIdUzivatele()) {
                        showAlert("Uživatel už je mezi kontakty zařazen");
                        canBeAdded = false;
                        break;
                    }
                }
                if (canBeAdded) {
                    // Přidání uživatele do kontaktů současného uživatele
                    dbHelper.addToContacts(dbHelper.getCurrentUser().getIdUzivatele(), t.getIdUzivatele());
                    // Aktualizuje seznam kontaktů uživatele
                    setContactsMenuData(uzivatel.getIdUzivatele());
                }

            } catch (SQLException ex) {
                showAlert(ex.getLocalizedMessage());
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
        fieldListController.setDeleteFieldAction((t) -> {
            try {
                deleteObor(t.getIdOboru());
                showDialog("Obor úspěšně smazán");
            } catch (SQLException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        fieldListController.setShowFieldDetailAction((t) -> {
            loadShowFieldMenu(t);
        });
    }

    /**
     * Nastaví data pro zobrazení
     *
     * @param obory seznam oborů pro zobrazení
     */
    private void setFieldListData(List<StudijniObor> obory) {
        fieldListController.setDataset(obory, userIsAdmin);
    }

    // FieldList menu section end
    // SubjectList menu section start
    private void setSubjectListMenuData(List<Predmet> predmety) {
        subjectsListController.setDataSet(predmety, userIsAdmin);
    }

    /**
     * Nastaví akce, které se mají provést při událostech v menu seznamu
     * předmětů
     */
    private void setSubjectListMenuActions() {
        subjectsListController.setDeleteAction((t) -> {
            try {
                deletePredmet(t.getIdPredmetu());
                showDialog("Předmět byl úspěšně smazán");
                loadSubjectListMenu(getPredmety());
            } catch (SQLException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        subjectsListController.setShowSubjectDetailAction((t) -> {
            loadShowSubjectMenu(t);
        });
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
        curriculumListController.setShowCurriculumDetailAction((t) -> {
            loadShowCurriculumMenu(t);
        });

        curriculumListController.setDeleteCurriculumAction((t) -> {
            try {
                deletePlan(t.getIdPlanu());
                showDialog("Studijní plán úspěšně smazán");
                loadCurriculumListMenu(getStudijniPlany());
            } catch (SQLException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void setCurriculumListData(List<StudijniPlan> plany) {
        curriculumListController.setDataset(plany, userIsAdmin);
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
            List<Prispevek> prispevky = dbHelper.selectPrispevkySkupiny(idSkupiny);
            for (Prispevek prispevek : prispevky) {
                loadComments(prispevek);
            }

            List<Role> role = getRoleUzivatele(dbHelper.getCurrentUser().getIdUzivatele());
            groupFeedController.updateFeed(prispevky, dbHelper.getCurrentUser(), userIsAdmin);
            List<Integer> priority = getListOfPriorities(role);
            groupFeedController.updatePriorities(priority);
        } catch (SQLException ex) {
            Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadComments(Prispevek prispevek) throws SQLException {
        prispevek.setKomentare(dbHelper.selectKomentare(prispevek.getIdPrispevku()));
        List<Prispevek> komentare = prispevek.getKomentare();
        if (komentare != null) {
            for (Prispevek prispevek1 : komentare) {
                prispevek1.setIdRodice(prispevek.getIdPrispevku());
                loadComments(prispevek1);
            }
        }
    }

    /**
     * Nastaví akce, které se mají vykonat při událostech v příspěvků skupiny
     */
    private void setGroupFeedMenuActions() {
        groupFeedController.setBtnOdeslatAction((t) -> {
            try {
                if (groupFeedController.inputIsOk()) {
                    int currGroupId = contactsController.getSelectedGroupId();
                    dbHelper.sendPrispevekToGroup(t.getNazev(), t.getObsahPrispevku(), t.getCasOdeslani(), t.getPriorita(), t.getIdAutora(), currGroupId);
                    loadGroupFeedMenu(currGroupId);
                } else {
                    showAlert("Neplatný vstup");
                }
            } catch (SQLException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        groupFeedController.setKomentarBtnOdeslatAction((t) -> {
            try {
                dbHelper.sendKomentar(t.getObsahPrispevku(), t.getCasOdeslani(), t.getIdAutora(), t.getIdRodice());
            } catch (SQLException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        groupFeedController.setEditPostAction((t) -> {
            loadEditPostMenu(t);
        });

        groupFeedController.setBlockPostAction((t) -> {
            int blokace = t.getBlokace();
            try {
                dbHelper.updatePrispevekBlokace(++blokace % 2, t.getIdPrispevku());
                loadGroupFeedMenu(contactsController.getSelectedGroupId());
            } catch (SQLException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        groupFeedController.setDeletePostAction((t) -> {
            try {
                dbHelper.deletePrispevek(t.getIdPrispevku());
                showDialog("Příspěvek úspěšně smazán");
                loadGroupFeedMenu(contactsController.getSelectedGroupId());
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
        registerController.setRegisterAction((t) -> {
            Uzivatel uziv = t;
            List<Role> role = registerController.getRoleUzivatele();
            List<StudijniPlan> plany = registerController.getStudijniPlan();

            try {
                dbHelper.registerUser(t.getJmeno(), t.getPrijmeni(), t.getHeslo(), t.getEmail(), t.getPoznamka(), t.getRokStudia(), plany, role);
                showDialog("Uživatel úspěšně nahrán do databáze");
            } catch (SQLException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
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

        addSubjectController.setUpdateAction((t) -> {
            Predmet pr = t;
            List<StudijniPlan> plany = addSubjectController.getSeznamPlanu();
            try {
                dbHelper.deletePredmetSPByPredmetId(t.getIdPredmetu());
            } catch (SQLException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
            for (StudijniPlan studijniPlan : plany) {
                try {
                    dbHelper.insertPredmetSp(studijniPlan.getIdPlanu(), t.getIdPredmetu());
                } catch (SQLException ex) {
                    Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            try {
                dbHelper.updatePredmet(t.getNazevPredmetu(), t.getZkratkaPredmetu(), t.getPopis(), t.getIdPredmetu());
                showDialog("Předmět úspěšně aktualizován");
                paneCenter.getChildren().clear();
            } catch (SQLException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        addSubjectController.setAddAction((t) -> {
            Predmet pr = t;
            List<StudijniPlan> plany = addSubjectController.getSeznamPlanu();
            List<Integer> idPlanu = new ArrayList<>();
            for (StudijniPlan studijniPlan : plany) {
                idPlanu.add(studijniPlan.getIdPlanu());
            }
            try {
                dbHelper.vlozPredmet(t.getNazevPredmetu(), t.getZkratkaPredmetu(), t.getPopis(), idPlanu);
                showDialog("Předmět úspěšně nahrán");
            } catch (SQLException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void setAddSubjectMenuData(List<StudijniPlan> plany, Predmet predmet) throws SQLException {
        if (predmet != null) {
            List<StudijniPlan> currPlany = getStudijniPlanyPredmetu(predmet.getIdPredmetu());
            addSubjectController.setPlanyData(plany, currPlany, predmet);
        } else {
            addSubjectController.setPlanyData(plany);
        }

    }

    /**
     * Zobrazí menu pro přidání předmětu
     */
    private void loadAddSubjectMenu(List<StudijniPlan> plany, Predmet predmet) {
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

        try {
            setAddSubjectMenuData(plany, predmet);
        } catch (SQLException ex) {
            Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        addFieldController.setAddFieldAction((t) -> {
            try {
                dbHelper.insertStudijniObor(t.getNazev(), t.getZkratka(), t.getPopis(), t.getAkreditaceDo());
                showDialog("Obor úspěšně nahrán");
                paneCenter.getChildren().clear();
            } catch (SQLException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        addFieldController.setUpdateFieldAction((t) -> {
            try {
                dbHelper.updateObor(t.getNazev(), t.getPopis(), t.getAkreditaceDo(), t.getIdOboru());
                showDialog("Obor úspěšně aktualizován");
                loadShowFieldMenu(t);
            } catch (SQLException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void setAddFieldMenuData(StudijniObor obor) throws SQLException {
        if (obor != null) {
            addFieldController.setDataset(obor);
        } else {
            addFieldController.setDataset();
        }

    }

    /**
     * Zobrazí menu pro přidání oboru
     */
    private void loadAddFieldMenu(StudijniObor obor) {
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
        try {
            setAddFieldMenuData(obor);
        } catch (SQLException ex) {
            Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        addCurriculumController.setUpdateAction((t) -> {
            try {
                dbHelper.updateStudijniPlan(t.getNazev(), t.getPopis(), t.getIdOboru(), t.getIdPlanu());
                showDialog("Studijní plán úspěšně aktualizován");
                loadShowCurriculumMenu(t);
            } catch (SQLException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        addCurriculumController.setAddAction((t) -> {
            try {
                dbHelper.insertStudijniPlan(t.getNazev(), t.getIdOboru(), t.getPopis());
                showDialog("Studijní plán úspěšně vložen");
                paneCenter.getChildren().clear();
            } catch (SQLException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void setAddCurriculumMenuData(List<StudijniObor> obory, StudijniPlan plan) throws SQLException {
        if (plan == null) {
            addCurriculumController.setDataset(obory);
        } else {
            addCurriculumController.setDataset(obory, plan);
        }

    }

    /**
     * Zobrazí menu pro přidání studijního plánu
     *
     * @param obory seznam oborů
     */
    private void loadAddCurriculumMenu(List<StudijniObor> obory, StudijniPlan plan) {
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

        try {
            // Aktualizuje seznam oborů pro zobrazení
            setAddCurriculumMenuData(obory, plan);
        } catch (SQLException ex) {
            Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // AddCurriculum menu section end

    // EditUser menu section start
    /**
     * Nastaví akce, které se mají provést při událostech v menu editace
     * uživatele
     *
     * @param uzivatel
     */
    private void setEditUserMenuActions() {
        // Při zrušení editace se neuloží změny a zobrazí se info profilu
        editUserController.setBtnCancelEvent((t) -> {
            loadAccountMenu(t);
        });

        editUserController.setBtnSaveEvent((t) -> {
            boolean inserted = false;
            try {
                editUserController.oldPasswordAgrees(dbHelper.selectHesloUzivatele(t.getIdUzivatele()));
                dbHelper.updateUzivatel(t.getJmeno(), t.getPrijmeni(), t.getRokStudia(), t.getEmail(), t.getBlokace(), t.getPoznamka(), t.getHeslo(), t.getIdUzivatele());
                inserted = true;
            } catch (SQLException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (inserted) {
                try {
                    dbHelper.deleteRoleUzivatele(t.getIdUzivatele());
                    dbHelper.deleteStudijniPlanUzivatele(t.getIdUzivatele());
                } catch (SQLException ex) {
                    Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
                }

                List<Role> roleUzivatele = editUserController.getRoleUzivatele();
                List<StudijniPlan> planyUzivatele = editUserController.getStudijniPlan();
                for (Role role : roleUzivatele) {
                    try {
                        dbHelper.insertRoleUzivatele(role.getIdRole(), t.getIdUzivatele());
                    } catch (SQLException ex) {
                        Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                for (StudijniPlan studijniPlan : planyUzivatele) {
                    try {
                        dbHelper.insertStudijniPlanUzivatele(studijniPlan.getIdPlanu(), t.getIdUzivatele());
                    } catch (SQLException ex) {
                        Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                showDialog("Změny úspěšně uloženy");
                loadAccountMenu(t);
            }
        });

        editUserController.setCanEditAction((t) -> {
            return userIsAdmin && t.getIdUzivatele() != dbHelper.getCurrentUser().getIdUzivatele();
        });
    }

    /**
     * Nastaví instance potřebné pro práci s databází
     */
    private void setEditUserMenuData(Uzivatel uzivatel) throws SQLException {
        editUserController.updateViews(uzivatel, userIsAdmin);
        editUserController.updateRole(getRole());
        editUserController.updateStudijniObory(getStudijniObory());
        editUserController.updateStudijniPlany(getStudijniPlany());

        editUserController.setCurrentRole(getRoleUzivatele(uzivatel.getIdUzivatele()));
        List<StudijniPlan> planyUzivatele = getStudijniPlanyUzivatele(uzivatel.getIdUzivatele());
                editUserController.setCurrentPlany(planyUzivatele);
        if (planyUzivatele.size() > 0) {
            StudijniObor obor = dbHelper.selectStudijniObor(planyUzivatele.get(0).getIdOboru());
            editUserController.setCurrentObor(obor);
        }
        
        editUserController.setUserEditingSelf(uzivatel.getIdUzivatele() == dbHelper.getCurrentUser().getIdUzivatele());
        
    }

    /**
     * Načte menu úprav uživatele
     *
     * @param uzivatel uživatel pro úpravu
     */
    private void loadEditUserMenu(Uzivatel uzivatel) {
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

    // ShowCurriculum menu section start
    // AccCurriculum menu section start
    /**
     * Nastaví akce, které se mají provést v menu přidání studijního plánu
     */
    private void setShowCurriculumMenuActions() {
        showCurriculumController.setAddToContactsAction((t) -> {
            addUzivateleDoKontaktu(t);
        });

        showCurriculumController.setShowProfileAction((t) -> {
            loadAccountMenu(t);
        });

        showCurriculumController.setBtnUpravitAction((t) -> {
            try {
                loadAddCurriculumMenu(getStudijniObory(), t);
            } catch (SQLException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        showCurriculumController.setShowSubjectDetailAction((t) -> {
            loadShowSubjectMenu(t);
        });

        showCurriculumController.setDeleteAction((t) -> {
            try {
                dbHelper.deletePredmet(t.getIdPredmetu());
                showDialog("Předmět úspěšně smazán");
                loadCurriculumListMenu(getStudijniPlany());
            } catch (SQLException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        showCurriculumController.setLblOborPressedAction((t) -> {
            loadShowFieldMenu(t);
        });
    }

    private void setShowCurriculumMenuData(StudijniPlan plan) throws SQLException {
        StudijniObor obor = getOborStudijnihoPlanu(plan.getIdOboru());
        List<Uzivatel> clenove = getClenoveSkupiny(plan.getIdPlanu());
        List<Predmet> predmety = getPredmetyPlanu(plan.getIdPlanu());
        showCurriculumController.setDataset(predmety, clenove, plan, obor, userIsAdmin);
    }

    /**
     * Zobrazí menu pro přidání studijního plánu
     *
     * @param obory seznam oborů
     */
    private void loadShowCurriculumMenu(StudijniPlan plan) {
        if (showCurriculumMenu == null) {
            loader = new FXMLLoader();
            try {
                // Načtení layoutu start
                loader.setLocation(getClass().getResource("FXMLShowCurriculum.fxml"));
                showCurriculumMenu = loader.load();
                showCurriculumController = loader.getController();
                // Načtení layoutu end

                setAutoResizeCenter(showCurriculumMenu);

                setShowCurriculumMenuActions();

            } catch (IOException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        loadCurrentMenu(showCurriculumMenu);

        try {
            // Aktualizuje seznam oborů pro zobrazení
            setShowCurriculumMenuData(plan);
        } catch (SQLException ex) {
            Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // ShowCurriculum menu section end

    // ShowField menu section start
    private void setShowFieldMenuActions() {
        showFieldController.setDeleteCurriculumAction((t) -> {
            try {
                deletePlan(t.getIdPlanu());
                showDialog("Studijní plán úspěšně smazán");
            } catch (SQLException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        showFieldController.setShowCurriculumDetailAction((t) -> {
            loadShowCurriculumMenu(t);
        });

        showFieldController.setBtnUpravitAction((t) -> {
            loadAddFieldMenu(t);
        });
    }

    private void setShowFieldMenuData(StudijniObor obor) throws SQLException {
        List<StudijniPlan> plany = getStudijniPlanyOboru(obor.getIdOboru());
        showFieldController.setDataset(plany, obor, userIsAdmin);
    }

    /**
     * Zobrazí menu pro přidání studijního plánu
     *
     * @param obory seznam oborů
     */
    private void loadShowFieldMenu(StudijniObor obor) {
        if (showFiledMenu == null) {
            loader = new FXMLLoader();
            try {
                // Načtení layoutu start
                loader.setLocation(getClass().getResource("FXMLShowField.fxml"));
                showFiledMenu = loader.load();
                showFieldController = loader.getController();
                // Načtení layoutu end

                setAutoResizeCenter(showFiledMenu);

                setShowFieldMenuActions();
            } catch (IOException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        loadCurrentMenu(showFiledMenu);

        try {
            // Aktualizuje seznam oborů pro zobrazení
            setShowFieldMenuData(obor);
        } catch (SQLException ex) {
            Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // ShowField menu section end

    // ShowSubjectMenu section start
    private void setShowSubjectMenuActions() {
        showSubjectController.setDeleteCurriculumAction((t) -> {
            try {
                deletePlan(t.getIdPlanu());
                showDialog("Studijní plán úspěšně smazán");
            } catch (SQLException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        showSubjectController.setShowCurriculumDetailAction((t) -> {
            loadShowCurriculumMenu(t);
        });

        showSubjectController.setBtnUpravitAction((t) -> {
            List<StudijniPlan> plany;
            try {
                plany = getStudijniPlany();
                loadAddSubjectMenu(plany, t);
            } catch (SQLException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void setShowSubjectMenuData(Predmet currPredmet) throws SQLException {
        List<StudijniPlan> plany = getStudijniPlanyPredmetu(currPredmet.getIdPredmetu());
        showSubjectController.setDataset(plany, currPredmet, userIsAdmin);
    }

    /**
     * Zobrazí menu pro přidání studijního plánu
     *
     * @param obory seznam oborů
     */
    private void loadShowSubjectMenu(Predmet currPredmet) {
        if (showSubjectMenu == null) {
            loader = new FXMLLoader();
            try {
                // Načtení layoutu start
                loader.setLocation(getClass().getResource("FXMLShowSubject.fxml"));
                showSubjectMenu = loader.load();
                showSubjectController = loader.getController();
                // Načtení layoutu end

                setAutoResizeCenter(showSubjectMenu);

                setShowSubjectMenuActions();

            } catch (IOException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        loadCurrentMenu(showSubjectMenu);

        try {
            // Aktualizuje seznam oborů pro zobrazení
            setShowSubjectMenuData(currPredmet);
        } catch (SQLException ex) {
            Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // ShowSubjectMenu section end
    // EditPost section start
    private void setEditPostMenuActions() {
        editPostController.setBtnCancelAction((t) -> {
            paneCenter.getChildren().clear();
        });

        editPostController.setBtnSaveAction((t) -> {
            try {
                dbHelper.updatePrispevek(t.getNazev(), t.getObsahPrispevku(), t.getBlokace(), t.getIdPrispevku(), t.getPriorita());
                showDialog("Příspěvěk úspěšně aktualizován");
                paneCenter.getChildren().clear();
            } catch (SQLException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void setEditPostMenuData(Prispevek currPrispevek) throws SQLException {
        editPostController.updateDataSet(currPrispevek,
                getListOfPriorities(getRoleUzivatele(dbHelper.getCurrentUser().getIdUzivatele())),
                currPrispevek.getPriorita());
    }

    /**
     * Zobrazí menu pro přidání studijního plánu
     *
     * @param obory seznam oborů
     */
    private void loadEditPostMenu(Prispevek currPrispevek) {
        if (editPost == null) {
            loader = new FXMLLoader();
            try {
                // Načtení layoutu start
                loader.setLocation(getClass().getResource("FXMLEditPost.fxml"));
                editPost = loader.load();
                editPostController = loader.getController();
                // Načtení layoutu end

                setAutoResizeCenter(editPost);

                setEditPostMenuActions();

            } catch (IOException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        loadCurrentMenu(editPost);

        try {
            // Aktualizuje seznam oborů pro zobrazení
            setEditPostMenuData(currPrispevek);
        } catch (SQLException ex) {
            Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    // EditPost section end
    private List<StudijniPlan> getStudijniPlany() throws SQLException {
        return dbHelper.selectStudijniPlany();
    }

    private List<StudijniPlan> getStudijniPlanyOboru(int idOboru) throws SQLException {
        return dbHelper.selectStudijniPlanyOboru(idOboru);
    }

    private List<Role> getRole() throws SQLException {
        return dbHelper.selectRole();
    }

    private List<Role> getRoleUzivatele(int idUzivatele) throws SQLException {
        return dbHelper.selectRoleUzivatele(idUzivatele);
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

    private List<StudijniPlan> getStudijniPlanyPredmetu(int idPredmetu) throws SQLException {
        return dbHelper.selectStudijniPlanyPredmetu(idPredmetu);
    }

    private List<Predmet> getPredmetyUzivatele(int idUzivatele) throws SQLException {
        return dbHelper.selectPredmetyUzivatele(idUzivatele);
    }

    private List<Predmet> getPredmetyPlanu(int idPlanu) throws SQLException {
        return dbHelper.selectPredmetyStudijnihoPlanu(idPlanu);
    }

    private List<Predmet> getPredmety() throws SQLException {
        return dbHelper.selectPredmety();
    }

    private List<KontaktVypis> getKontaktyUzivatele(int idUzivatele) throws SQLException {
        return dbHelper.selectKontaktyUzivatele(idUzivatele);
    }

    private void deletePredmet(int idPredmetu) throws SQLException {
        dbHelper.deletePredmet(idPredmetu);
    }

    private void deletePlan(int idPlanu) throws SQLException {
        dbHelper.deleteStudijniPlan(idPlanu);
    }

    private void deleteObor(int idOboru) throws SQLException {
        dbHelper.deleteObor(idOboru);
    }

    private void deletePrispevek(int idPrispevku) throws SQLException {
        dbHelper.deletePrispevek(idPrispevku);
    }

    private void deleteUser(int idUzivatele) throws SQLException {
        dbHelper.deleteUser(idUzivatele);
    }

    private List<Integer> getListOfPriorities(List<Role> role) {
        List<Integer> priority = new ArrayList<>();
        priority.add(0);
        priority.add(1);
        boolean admin = role.stream().anyMatch((t) -> {
            return t.getIdRole() == 1;
        });
        boolean teacher = role.stream().anyMatch((t) -> {
            return t.getIdRole() == 2;
        });

        if (admin) {
            priority.add(2);
            priority.add(3);
        } else if (teacher) {
            priority.add(2);
        }

        return priority;
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

    private StudijniObor getOborStudijnihoPlanu(int idOboru) throws SQLException {
        return dbHelper.selectStudijniObor(idOboru);
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
            loadAddSubjectMenu(getStudijniPlany(), null);
        } catch (SQLException ex) {
            Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleMenuItemPridatStudijniOborAction(ActionEvent event) {
        loadAddFieldMenu(null);
    }

    @FXML
    private void handleMenuItemPridatStudijniPlanAction(ActionEvent event) {
        try {
            loadAddCurriculumMenu(getStudijniObory(), null);
        } catch (SQLException ex) {
            Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
