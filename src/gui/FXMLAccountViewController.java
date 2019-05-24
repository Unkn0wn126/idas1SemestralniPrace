/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import gui.customcells.GroupListCell;
import gui.customcells.RoleListCell;
import gui.customcells.SubjectListCell;
import gui.customcells.UzivatelListCell;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import model.Predmet;
import model.Role;
import model.StudijniPlan;
import model.Uzivatel;

/**
 * FXML Controller class
 *
 * @author Lukas
 */
public class FXMLAccountViewController implements Initializable {

    @FXML
    private Label lblJmeno;
    @FXML
    private Label lblPrijmeni;
    @FXML
    private Label lblEmail;
    @FXML
    private Label lblRokStudia;
    @FXML
    private ListView<Uzivatel> listViewKontakty;
    @FXML
    private Label lblPoznamka;

    private Uzivatel uzivatel;

    private ObservableList<Uzivatel> kontakty = FXCollections.observableArrayList();
    private ObservableList<Role> role = FXCollections.observableArrayList();
    private ObservableList<StudijniPlan> studijniPlany = FXCollections.observableArrayList();
    private ObservableList<Predmet> predmety = FXCollections.observableArrayList();

    private Consumer<Uzivatel> editButtonAction;

    private Consumer<Uzivatel> showProfileAction;
    private Consumer<Uzivatel> addToContactsAction;

    private Predicate<Uzivatel> canEdit;

    private Consumer<Predmet> showSubjectDetailAction;
    private Consumer<Predmet> deleteSubjectAction;

    private Consumer<StudijniPlan> showCurriculumDetailAction;
    private Consumer<StudijniPlan> deleteCurriculumAction;

    private SimpleBooleanProperty giveAdminPermissions = new SimpleBooleanProperty();

    private ContextMenu contextMenuCurriculum;
    private ContextMenu contextMenuSubject;

    @FXML
    private Button btnUpravit;
    @FXML
    private Label lblUzivatel;
    @FXML
    private TabPane tabPane;
    @FXML
    private Label lblBan;
    @FXML
    private ListView<Role> listViewRole;
    @FXML
    private ListView<StudijniPlan> listViewStudijniPlany;
    @FXML
    private ListView<Predmet> listViewPredmety;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        giveAdminPermissions.setValue(false);
        contextMenuCurriculum = addContextMenuCurriculum();
        contextMenuSubject = addContextMenuPredmety();

        listViewKontakty.setItems(kontakty);
        listViewKontakty.setCellFactory((param) -> {
            return new UzivatelListCell(addContextMenuContact());
        });

        listViewRole.setItems(role);
        listViewRole.setCellFactory((param) -> {
            return new RoleListCell();
        });

        listViewStudijniPlany.setItems(studijniPlany);
        listViewStudijniPlany.setCellFactory((param) -> {
            return new GroupListCell(contextMenuCurriculum);
        });

        listViewPredmety.setItems(predmety);
        listViewPredmety.setCellFactory((param) -> {
            return new SubjectListCell(contextMenuSubject);
        });

        giveAdminPermissions.addListener((observable, oldValue, newValue) -> {
            btnUpravit.setVisible(newValue);
            btnUpravit.setDisable(!newValue);
            
            if (newValue && contextMenuCurriculum.getItems().size() < 2) {
                contextMenuCurriculum.getItems().add(createMenuDeleteCurriculum());
            } else if (!newValue && contextMenuCurriculum.getItems().size() > 1) {
                contextMenuCurriculum.getItems().remove(1);
            }
            if (newValue && contextMenuSubject.getItems().size() < 2) {
                contextMenuSubject.getItems().add(createMenuDeletePredmet());
            } else if (!newValue && contextMenuSubject.getItems().size() > 1) {
                contextMenuSubject.getItems().remove(1);
            }
        });
    }

    @FXML
    private void handleBtnUpravitAction(ActionEvent event) {
        if (editButtonAction != null) { // Ošetření nullPointerException
            editButtonAction.accept(uzivatel);
        }
    }

    /**
     * Nastaví akci, která se má provést při stisknutí tlačítka editovat
     *
     * @param editButtonAction akce, která se má provést
     */
    public void setEditButtonAction(Consumer<Uzivatel> editButtonAction) {
        this.editButtonAction = editButtonAction;
    }

    /**
     * Vytvoří kontextové menu pro daný panel kontaktu
     *
     * @return
     */
    private ContextMenu addContextMenuContact() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem showDetails = new MenuItem("Zobrazit profil");
        showDetails.setOnAction((event) -> {
            if (showProfileAction != null) {
                showProfileAction.accept(listViewKontakty.getSelectionModel().getSelectedItem());
            }
        });
        MenuItem addToContacts = new MenuItem("Přidat do kontaktů");
        addToContacts.setOnAction((event) -> {
            if (addToContactsAction != null) {
                addToContactsAction.accept(listViewKontakty.getSelectionModel().getSelectedItem());
            }
        });
        contextMenu.getItems().addAll(showDetails, addToContacts);
        return contextMenu;
    }

    /**
     * Nastaví akci, která se má provést při události zobrazení profilu
     * vybraného uživatele
     *
     * @param showProfileAction akce, která se má provést
     */
    public void setShowProfileAction(Consumer<Uzivatel> showProfileAction) {
        this.showProfileAction = showProfileAction;
    }

    /**
     * Nastaví akci, která se má provést při události přidání uživatele do
     * kontaktů
     *
     * @param addToContactsAction akce, která se má provést
     */
    public void setAddToContactsAction(Consumer<Uzivatel> addToContactsAction) {
        this.addToContactsAction = addToContactsAction;
    }

    /**
     * Updatuje data pro zobrazení podle uživatele
     *
     * @param uzivatel uživatel, jehož data se mají zobrazit
     * @param role
     * @param studijniPlany
     * @param predmety
     */
    public void updateView(Uzivatel uzivatel, List<Role> role, List<StudijniPlan> studijniPlany, List<Predmet> predmety, boolean isAdmin) {
        setUzivatel(uzivatel);
        this.role.clear();
        this.studijniPlany.clear();
        this.predmety.clear();

        this.role.addAll(role);
        this.studijniPlany.addAll(studijniPlany);
        this.predmety.addAll(predmety);

        if (uzivatel != null && canEdit != null) {
            btnUpravit.setVisible(canEdit.test(uzivatel));
        }
        
        giveAdminPermissions.setValue(isAdmin);

        lblUzivatel.setText(uzivatel.getJmeno() + " " + uzivatel.getPrijmeni());
        lblJmeno.setText(uzivatel.getJmeno());
        lblPrijmeni.setText(uzivatel.getPrijmeni());
        lblEmail.setText(uzivatel.getEmail());
        lblRokStudia.setText(Integer.toString(uzivatel.getRokStudia()));
        lblPoznamka.setText(uzivatel.getPoznamka());
        String blokace = uzivatel.getBlokace() == 0 ? "Ne" : "Ano";
        lblBan.setText(blokace);

        tabPane.getSelectionModel().selectFirst();
    }

    /**
     * Nastaví seznam kontaktů uživatele, který se má zobrazit
     *
     * @param kontakty seznam uživatelů pro zobrazení
     */
    public void setContactsDataSet(List<Uzivatel> kontakty) {
        this.kontakty.clear();
        this.kontakty.addAll(kontakty);
    }

    /**
     * Nastaví uživatele, kterému patří profil
     *
     * @param uzivatel uživatel, kterému patří profil
     */
    public void setUzivatel(Uzivatel uzivatel) {
        this.uzivatel = uzivatel;
    }

    /**
     * Nastaví akci, podle které se určí, jestli má daný uživatel právo úprav
     *
     * @param canEdit akce, která se má provést
     */
    public void setCanEditAction(Predicate<Uzivatel> canEdit) {
        this.canEdit = canEdit;
    }

    private ContextMenu addContextMenuPredmety() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem showDetail = new MenuItem("Zobrazit detail předmětu");
        showDetail.setOnAction((event) -> {
            if (showSubjectDetailAction != null) {
                showSubjectDetailAction.accept(listViewPredmety.getSelectionModel().getSelectedItem());
            }
        });

        contextMenu.getItems().addAll(showDetail);
        return contextMenu;
    }

    private MenuItem createMenuDeletePredmet() {
        MenuItem delete = new MenuItem("Odstranit předmět");
        delete.setOnAction((event) -> {
            if (deleteSubjectAction != null) {
                deleteSubjectAction.accept(listViewPredmety.getSelectionModel().getSelectedItem());
            }
        });

        return delete;
    }

    public void setShowSubjectDetailAction(Consumer<Predmet> showSubjectDetailAction) {
        this.showSubjectDetailAction = showSubjectDetailAction;
    }

    public void setDeleteSubjectAction(Consumer<Predmet> deleteAction) {
        this.deleteSubjectAction = deleteAction;
    }

    public void setShowCurriculumDetailAction(Consumer<StudijniPlan> showDetailAction) {
        this.showCurriculumDetailAction = showDetailAction;
    }

    public void setDeleteCurriculumAction(Consumer<StudijniPlan> deleteCurriculumAction) {
        this.deleteCurriculumAction = deleteCurriculumAction;
    }

    private ContextMenu addContextMenuCurriculum() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem showDetail = new MenuItem("Zobrazit detail");
        showDetail.setOnAction(event -> {
            if (showCurriculumDetailAction != null) { // TODO: Fix bug of wrong selection
                showCurriculumDetailAction.accept(listViewStudijniPlany.getSelectionModel().getSelectedItem());
            }
        });

        contextMenu.getItems().addAll(showDetail);
        return contextMenu;
    }

    private MenuItem createMenuDeleteCurriculum() {
        MenuItem delete = new MenuItem("Odstranit studijní plán");
        delete.setOnAction((event) -> {
            if (deleteCurriculumAction != null) { // TODO: Fix bug of wrong selection
                deleteCurriculumAction.accept(listViewStudijniPlany.getSelectionModel().getSelectedItem());
            }
        });

        return delete;
    }

}
