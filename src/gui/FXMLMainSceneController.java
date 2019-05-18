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
import javafx.scene.Parent;
import javafx.scene.SubScene;
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

    private DatabaseHelper db; // pro předávání dat z databáze

    // Controllers
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

    // Sub-scene nodes
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

    @FXML
    private FlowPane paneLeft;
    @FXML
    private FlowPane paneCenter;

    private Consumer<ActionEvent> logoutAction;
    @FXML
    private Menu menuPridat;

    /**
     * Nastaví akci, která se má provést při pokusu o odhlášení uživatele
     *
     * @param logoutAction akce, která se má provést
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

    public void loadContactsMenu() {
        loader = new FXMLLoader();
        try {
            // Načtení panelu s kontakty a skupinami
            loader.setLocation(getClass().getResource("FXMLContacts.fxml"));
            contactsMenu = loader.load();
            contactsController = loader.getController();

            // automatická změna velikosti při změně velikosti okna start
            paneLeft.widthProperty().addListener((observable, oldValue, newValue) -> {
                contactsMenu.setPrefWidth((double) newValue);
            });
            
            paneLeft.heightProperty().addListener((observable, oldValue, newValue) -> {
                contactsMenu.setPrefHeight((double) newValue);
            });
            // automatická změna velikosti při změně velikosti okna end
            
            paneLeft.getChildren().add(contactsMenu);
            
            contactsController.setSelectedContactChangedAction((t) -> {
                loadConversationMenu();
                conversationController.updateMessages();
            });
            
            contactsController.setSelectedGroupChangedAction((t) -> {
                loadGroupFeedMenu();
            });

            contactsController.setContextMenuShowProfileAction((t) -> {
                loadAccountMenu(contactsController.getSelectedUsers().get(0));
            });

            contactsController.setContextMenuSendMessageAction((t) -> {
                List<Uzivatel> users = contactsController.getSelectedUsers();
                for (Object user : users) {
                    System.out.println(user);
                }
            });

            contactsController.setUzivatelManager(db.getUzivatelManager());
            contactsController.setContactDataSet(db.getKontaktManager().selectKontakty(db.getUzivatelManager().getCurrentUser().getIdUzivatele()));

            // Načtení konverzace
        } catch (IOException ex) {
            Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleMenuItemLogoutAction(ActionEvent event) {
        if (logoutAction != null) {
            logoutAction.accept(event);
        }
    }

    /**
     * Načte menu s konverzací s kontaktem
     */
    private void loadConversationMenu() {
        if (conversationMenu == null) {
            loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("FXMLConversation.fxml"));
            try {
                conversationMenu = loader.load();
                conversationController = loader.getController();
                conversationController.setUzivatelManager(db.getUzivatelManager());
                conversationController.setZpravaManager(db.getZpravaManager());

                // automatická změna velikosti při změně velikosti okna start
                paneCenter.widthProperty().addListener((observable, oldValue, newValue) -> {
                    conversationMenu.setPrefWidth((double) newValue);
                });
                
                paneCenter.heightProperty().addListener((observable, oldValue, newValue) -> {
                    conversationMenu.setPrefHeight((double) newValue);
                });
                // automatická změna velikosti při změně velikosti okna end
            } catch (IOException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (!paneCenter.getChildren().contains(conversationMenu)) {
            paneCenter.getChildren().remove(0, paneCenter.getChildren().size());
            paneCenter.getChildren().add(conversationMenu);

            conversationMenu.setPrefWidth(paneCenter.getWidth());
            conversationMenu.setPrefHeight(paneCenter.getHeight());
        }
    }

    /**
     * Načte menu se zprávami skupiny
     */
    private void loadGroupFeedMenu() {
        if (groupFeedMenu == null) {
            loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("FXMLGroupFeed.fxml"));
            try {
                groupFeedMenu = loader.load();
                groupFeedController = loader.getController();

                // automatická změna velikosti při změně velikosti okna start
                paneCenter.widthProperty().addListener((observable, oldValue, newValue) -> {
                    groupFeedMenu.setPrefWidth((double) newValue);
                });
                
                paneCenter.heightProperty().addListener((observable, oldValue, newValue) -> {
                    groupFeedMenu.setPrefHeight((double) newValue);
                });
                // automatická změna velikosti při změně velikosti okna end

            } catch (IOException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (!paneCenter.getChildren().contains(groupFeedMenu)) {
            paneCenter.getChildren().remove(0, paneCenter.getChildren().size());
            paneCenter.getChildren().add(groupFeedMenu);
            groupFeedController.updateFeed();

            groupFeedMenu.setPrefWidth(paneCenter.getWidth());
            groupFeedMenu.setPrefHeight(paneCenter.getHeight());
        }

    }

    /**
     * Nastaví pomocnou třídu pro získání dat z databáze
     *
     * @param db
     */
    public void setDatabaseHelper(DatabaseHelper db) {
        this.db = db;
    }

    /**
     * Načte menu s údaji o profilu uživatele
     */
    private void loadAccountMenu(Uzivatel uzivatel) {
        if (accountMenu == null) {
            loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("FXMLAccountView.fxml"));
            try {
                accountMenu = loader.load();
                accountController = loader.getController();
                paneCenter.widthProperty().addListener((observable, oldValue, newValue) -> {
                    accountMenu.setPrefWidth((double) newValue);
                });
                paneCenter.heightProperty().addListener((observable, oldValue, newValue) -> {
                    accountMenu.setPrefHeight((double) newValue);
                });
            } catch (IOException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (!paneCenter.getChildren().contains(accountMenu)) {
            paneCenter.getChildren().remove(0, paneCenter.getChildren().size());
            paneCenter.getChildren().add(accountMenu);

            accountMenu.setPrefWidth(paneCenter.getWidth());
            accountMenu.setPrefHeight(paneCenter.getHeight());
        }

        accountController.setEnableButton(uzivatel.getIdUzivatele().equals(db.getUzivatelManager().getCurrentUser().getIdUzivatele()));
        accountController.setKontaktManager(db.getKontaktManager());
        accountController.setUzivatelManager(db.getUzivatelManager());
        accountController.updateView(uzivatel);

        accountController.setEditButtonAction((t) -> {
            loadEditUserMenu(uzivatel);
        });
    }

    /**
     * Načte menu se seznamem uživatelů
     */
    private void loadUserListMenu(List<Uzivatel> uzivatele) {
        if (userListMenu == null) {
            loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("FXMLUserList.fxml"));
            try {
                userListMenu = loader.load();
                userListController = loader.getController();
                paneCenter.widthProperty().addListener((observable, oldValue, newValue) -> {
                    userListMenu.setPrefWidth((double) newValue);
                });
                paneCenter.heightProperty().addListener((observable, oldValue, newValue) -> {
                    userListMenu.setPrefHeight((double) newValue);
                });

                userListController.setAddToContactsAction((t) -> { // TODO: better
                    Uzivatel uzivatel = db.getUzivatelManager().getCurrentUser();
                    if (t != null && !t.getIdUzivatele().equals(uzivatel.getIdUzivatele())) {
                        try {
                            db.getKontaktManager().insertKontakt(t.getIdUzivatele(),
                                    uzivatel.getIdUzivatele(), LocalDate.now(),
                                    LocalDate.now().plusYears(3), "Kontakt");
                            db.getKontaktManager().insertKontakt(uzivatel.getIdUzivatele(),
                                    t.getIdUzivatele(), LocalDate.now(),
                                    LocalDate.now().plusYears(3), "Kontakt");
                            
                            contactsController.setContactDataSet(db.getKontaktManager().selectKontakty(uzivatel.getIdUzivatele()));
                        } catch (SQLException ex) {
                            showAlert(ex.getLocalizedMessage());
                        }
                    }else if (t.getIdUzivatele().equals(uzivatel.getIdUzivatele())) {
                        showAlert("Sebe sama nelze přidat do kontaktů!");
                    }
                });
            } catch (IOException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (!paneCenter.getChildren().contains(userListMenu)) {
            paneCenter.getChildren().remove(0, paneCenter.getChildren().size());
            paneCenter.getChildren().add(userListMenu);

            userListMenu.setPrefWidth(paneCenter.getWidth());
            userListMenu.setPrefHeight(paneCenter.getHeight());
        }

        userListController.setDataSet(uzivatele);
    }

    /**
     * Načte menu se seznamem předmětů
     */
    private void loadSubjectListMenu(List<Predmet> predmety) {
        if (subjectsListMenu == null) {
            loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("FXMLSubjectList.fxml"));
            try {
                subjectsListMenu = loader.load();
                subjectsListController = loader.getController();
                paneCenter.widthProperty().addListener((observable, oldValue, newValue) -> {
                    subjectsListMenu.setPrefWidth((double) newValue);
                });
                paneCenter.heightProperty().addListener((observable, oldValue, newValue) -> {
                    subjectsListMenu.setPrefHeight((double) newValue);
                });
            } catch (IOException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (!paneCenter.getChildren().contains(subjectsListMenu)) {
            paneCenter.getChildren().remove(0, paneCenter.getChildren().size());
            paneCenter.getChildren().add(subjectsListMenu);

            subjectsListMenu.setPrefWidth(paneCenter.getWidth());
            subjectsListMenu.setPrefHeight(paneCenter.getHeight());
        }

        subjectsListController.setDataSet(predmety);
    }

    /**
     * Načte menu se seznamem oborů
     */
    private void loadFieldListMenu(List<StudijniObor> obory) {
        if (fieldListMenu == null) {
            loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("FXMLFieldList.fxml"));
            try {
                fieldListMenu = loader.load();
                fieldListController = loader.getController();
                paneCenter.widthProperty().addListener((observable, oldValue, newValue) -> {
                    fieldListMenu.setPrefWidth((double) newValue);
                });
                paneCenter.heightProperty().addListener((observable, oldValue, newValue) -> {
                    fieldListMenu.setPrefHeight((double) newValue);
                });
            } catch (IOException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (!paneCenter.getChildren().contains(fieldListMenu)) {
            paneCenter.getChildren().remove(0, paneCenter.getChildren().size());
            paneCenter.getChildren().add(fieldListMenu);

            fieldListMenu.setPrefWidth(paneCenter.getWidth());
            fieldListMenu.setPrefHeight(paneCenter.getHeight());
        }

        fieldListController.setDataset(obory);
    }

    /**
     * Načte menu se seznamem studijních plánů
     */
    private void loadCurriculumListMenu(List<StudijniPlan> plany) {
        if (curriculumListMenu == null) {
            loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("FXMLCurriculumList.fxml"));
            try {
                curriculumListMenu = loader.load();
                curriculumListController = loader.getController();
                paneCenter.widthProperty().addListener((observable, oldValue, newValue) -> {
                    curriculumListMenu.setPrefWidth((double) newValue);
                });
                paneCenter.heightProperty().addListener((observable, oldValue, newValue) -> {
                    curriculumListMenu.setPrefHeight((double) newValue);
                });
            } catch (IOException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (!paneCenter.getChildren().contains(curriculumListMenu)) {
            paneCenter.getChildren().remove(0, paneCenter.getChildren().size());
            paneCenter.getChildren().add(curriculumListMenu);

            curriculumListMenu.setPrefWidth(paneCenter.getWidth());
            curriculumListMenu.setPrefHeight(paneCenter.getHeight());
        }

        curriculumListController.setDataset(plany);
    }

    /**
     * Načte menu registrace uživatele
     */
    private void loadRegisterMenu(List<StudijniObor> obory) {
        if (registerMenu == null) {
            loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("FXMLRegister.fxml"));
            try {
                registerMenu = loader.load();
                registerController = loader.getController();
                paneCenter.widthProperty().addListener((observable, oldValue, newValue) -> {
                    registerMenu.setPrefWidth((double) newValue);
                });
                paneCenter.heightProperty().addListener((observable, oldValue, newValue) -> {
                    registerMenu.setPrefHeight((double) newValue);
                });

                registerController.setRegisterAction((t) -> {
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
                            db.getUzivatelManager().registerUser(jmeno, prijmeni, login, heslo1, email, poznamka, rok);
                        } catch (SQLException ex) {
                            Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        showDialog("Uživatel úspěšně nahrán do databáze");
                    }
                });
            } catch (IOException ex) {
                showAlert("Neplatné vstupy pro uživatele");
            }
        }

        if (!paneCenter.getChildren().contains(registerMenu)) {
            paneCenter.getChildren().remove(0, paneCenter.getChildren().size());
            paneCenter.getChildren().add(registerMenu);

            registerMenu.setPrefWidth(paneCenter.getWidth());
            registerMenu.setPrefHeight(paneCenter.getHeight());
        }

        registerController.setOboryDataset(obory);
    }

    /**
     * Načte menu úprav uživatele
     *
     * @param uzivatel uživatel pro úpravu
     */
    private void loadEditUserMenu(Uzivatel uzivatel) {
        if (editUserMenu == null) {
            loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("FXMLEditUser.fxml"));
            try {
                editUserMenu = loader.load();
                editUserController = loader.getController();
                paneCenter.widthProperty().addListener((observable, oldValue, newValue) -> {
                    editUserMenu.setPrefWidth((double) newValue);
                });
                paneCenter.heightProperty().addListener((observable, oldValue, newValue) -> {
                    editUserMenu.setPrefHeight((double) newValue);
                });

                editUserController.setBtnCancelEvent((t) -> {
                    loadAccountMenu(uzivatel);
                });
            } catch (IOException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (!paneCenter.getChildren().contains(editUserMenu)) {
            paneCenter.getChildren().remove(0, paneCenter.getChildren().size());
            paneCenter.getChildren().add(editUserMenu);

            editUserMenu.setPrefWidth(paneCenter.getWidth());
            editUserMenu.setPrefHeight(paneCenter.getHeight());
        }
        
        try {
            editUserController.updateViews(uzivatel);
        } catch (SQLException ex) {
            Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Zobrazí menu pro přidání předmětu
     */
    private void loadAddSubjectMenu() {
        if (addSubjectMenu == null) {
            loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("FXMLAddSubject.fxml"));
            try {
                addSubjectMenu = loader.load();
                addSubjectController = loader.getController();
                paneCenter.widthProperty().addListener((observable, oldValue, newValue) -> {
                    addSubjectMenu.setPrefWidth((double) newValue);
                });
                paneCenter.heightProperty().addListener((observable, oldValue, newValue) -> {
                    addSubjectMenu.setPrefHeight((double) newValue);
                });

                addSubjectController.setBtnCancelAction((t) -> {
                    paneCenter.getChildren().clear();
                });
            } catch (IOException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (!paneCenter.getChildren().contains(addSubjectMenu)) {
            paneCenter.getChildren().remove(0, paneCenter.getChildren().size());
            paneCenter.getChildren().add(addSubjectMenu);

            addSubjectMenu.setPrefWidth(paneCenter.getWidth());
            addSubjectMenu.setPrefHeight(paneCenter.getHeight());
        }
    }

    /**
     * Zobrazí menu pro přidání studijního plánu
     */
    private void loadAddCurriculumMenu(List<StudijniObor> obory) {
        if (addCurriculumMenu == null) {
            loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("FXMLAddCurriculum.fxml"));
            try {
                addCurriculumMenu = loader.load();
                addCurriculumController = loader.getController();
                paneCenter.widthProperty().addListener((observable, oldValue, newValue) -> {
                    addCurriculumMenu.setPrefWidth((double) newValue);
                });
                paneCenter.heightProperty().addListener((observable, oldValue, newValue) -> {
                    addCurriculumMenu.setPrefHeight((double) newValue);
                });

                addCurriculumController.setBtnCancelAction((t) -> {
                    paneCenter.getChildren().clear();
                });

                addCurriculumController.setBtnSaveAction((t) -> {
                    String nazev = addCurriculumController.getNazev();
                    String popis = addCurriculumController.getPopis();
                    StudijniObor obor = addCurriculumController.getObor();

                    if (nazev.length() > 0 && popis.length() > 0 && obor != null) {
                        try {
                            db.getStudijniPlanManager().insertStudijniPlan(nazev, obor, popis);
                            showDialog("Studijní plán úspěšně nahrán");
                        } catch (SQLException ex) {
                            showAlert("Neplatné záznamy pro studijní plán");
                        }
                    }
                });
            } catch (IOException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (!paneCenter.getChildren().contains(addCurriculumMenu)) {
            paneCenter.getChildren().remove(0, paneCenter.getChildren().size());
            paneCenter.getChildren().add(addCurriculumMenu);

            addCurriculumMenu.setPrefWidth(paneCenter.getWidth());
            addCurriculumMenu.setPrefHeight(paneCenter.getHeight());
        }

        addCurriculumController.setDataset(obory);
    }

    /**
     * Zobrazí menu pro přidání oboru
     */
    private void loadAddFieldMenu() {
        if (addFieldMenu == null) {
            loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("FXMLAddField.fxml"));
            try {
                addFieldMenu = loader.load();
                addFieldController = loader.getController();
                paneCenter.widthProperty().addListener((observable, oldValue, newValue) -> {
                    addFieldMenu.setPrefWidth((double) newValue);
                });
                paneCenter.heightProperty().addListener((observable, oldValue, newValue) -> {
                    addFieldMenu.setPrefHeight((double) newValue);
                });

                addFieldController.setBtnCancelAction((t) -> {
                    paneCenter.getChildren().clear();
                });

                addFieldController.setBtnSaveAction((t) -> {
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
            } catch (IOException ex) {
                Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (!paneCenter.getChildren().contains(addFieldMenu)) {
            paneCenter.getChildren().remove(0, paneCenter.getChildren().size());
            paneCenter.getChildren().add(addFieldMenu);

            addFieldMenu.setPrefWidth(paneCenter.getWidth());
            addFieldMenu.setPrefHeight(paneCenter.getHeight());
        }
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
        });
    }

    @FXML
    private void handleMenuItemVyhledatPredmetAction(ActionEvent event) {
        showSearchMenu((t) -> {
            try {
                loadSubjectListMenu(db.getPredmetManager().selectPredmetyByAttribute(t));
            } catch (SQLException ex) {
                showAlert("Předmět se nepodařilo nalézt");
            }
        });
    }

    @FXML
    private void handleMenuItemVyhledatStudijniOborAction(ActionEvent event) {
        showSearchMenu((t) -> {
            try {
                loadFieldListMenu(db.getStudijniOborManager().selectStudijniOboryByAttribute(t));
            } catch (SQLException ex) {
                showAlert("Předmět se nepodařilo nalézt");
            }
        });
    }

    @FXML
    private void handleMenuItemVyhledatStudijniPlanAction(ActionEvent event) {
        showSearchMenu((t) -> {
            System.out.println(t); //TODO: vyhodnotit hledání a vrátit výsledek
        });
    }

    @FXML
    private void handleMenuItemPridatUzivateleAction(ActionEvent event) {
        try {
            loadRegisterMenu(db.getStudijniOborManager().selectStudijniObory());
        } catch (SQLException ex) {
            Logger.getLogger(FXMLMainSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
        registerController.setCancelAction((t) -> {
            paneCenter.getChildren().clear();
        });
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
    private void showSearchMenu(Consumer<String> searchAction) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText(null);
        dialog.setContentText("Zadejte id nebo jméno:");
        dialog.setTitle("Vyhledávání");
        Optional<String> result = dialog.showAndWait();
        if (result.get().length() > 0) {
            searchAction.accept(result.get());
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
