/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import gui.customcells.UzivatelListCell;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Predicate;
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

    private Consumer<Uzivatel> editButtonAction;

    private Consumer<Uzivatel> showProfileAction;
    private Consumer<Uzivatel> addToContactsAction;

    private Predicate<Uzivatel> canEdit;

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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listViewKontakty.setItems(kontakty);

        listViewKontakty.setCellFactory((param) -> {
            return new UzivatelListCell(addContextMenuContact());
        });
        
        listViewRole.setItems(role);// TODO: udělat custom cell
        
        listViewStudijniPlany.setItems(studijniPlany); // TODO: udělat custom cell
    }

    @FXML
    private void handleBtnUpravitAction(ActionEvent event) {
        if (editButtonAction != null) { // Ošetření nullPointerException
            editButtonAction.accept(uzivatel);
        }
    }

    /**
     * Nastaví akci, která se má provést při stisknutí tlačítka editovat
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
     * @param showProfileAction akce, která se má provést
     */
    public void setShowProfileAction(Consumer<Uzivatel> showProfileAction) {
        this.showProfileAction = showProfileAction;
    }

    /**
     * Nastaví akci, která se má provést při události přidání uživatele
     * do kontaktů
     * @param addToContactsAction akce, která se má provést
     */
    public void setAddToContactsAction(Consumer<Uzivatel> addToContactsAction) {
        this.addToContactsAction = addToContactsAction;
    }

    /**
     * Updatuje data pro zobrazení podle uživatele
     * @param uzivatel uživatel, jehož data se mají zobrazit
     */
    public void updateView(Uzivatel uzivatel, List<Role> role, List<StudijniPlan> studijniPlany) {
        setUzivatel(uzivatel);
        this.role.clear();
        this.studijniPlany.clear();
        
        this.role.addAll(role);
        this.studijniPlany.addAll(studijniPlany);

        if (uzivatel != null && canEdit != null) {
            btnUpravit.setVisible(canEdit.test(uzivatel));
        }

        lblUzivatel.setText(uzivatel.getJmeno() + " " + uzivatel.getPrijmeni());
        lblJmeno.setText(uzivatel.getJmeno());
        lblPrijmeni.setText(uzivatel.getPrijmeni());
        lblEmail.setText(uzivatel.getEmail());
        lblRokStudia.setText(Integer.toString(uzivatel.getRokStudia()));
        lblPoznamka.setText(uzivatel.getPoznamka());
        String blokace = uzivatel.getBlokace() == 0 ? "Ne": "Ano";
        lblBan.setText(blokace);
        
        tabPane.getSelectionModel().selectFirst();
    }

    /**
     * Nastaví seznam kontaktů uživatele, který se má zobrazit
     * @param kontakty seznam uživatelů pro zobrazení
     */
    public void setContactsDataSet(List<Uzivatel> kontakty) {
        this.kontakty.clear();
        this.kontakty.addAll(kontakty);
    }

    /**
     * Nastaví uživatele, kterému patří profil
     * @param uzivatel uživatel, kterému patří profil
     */
    public void setUzivatel(Uzivatel uzivatel) {
        this.uzivatel = uzivatel;
    }

    /**
     * Nastaví akci, podle které se určí, jestli má daný uživatel právo úprav
     * @param canEdit akce, která se má provést
     */
    public void setCanEditAction(Predicate<Uzivatel> canEdit) {
        this.canEdit = canEdit;
    }

}
