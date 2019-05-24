/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Role;
import model.StudijniObor;
import model.StudijniPlan;
import model.Uzivatel;

/**
 * FXML Controller class
 *
 * @author Lukas
 */
public class FXMLEditUserController implements Initializable {

    private Consumer<Uzivatel> btnCancelEvent;
    private Consumer<ActionEvent> btnSaveEvent;
    private Uzivatel uzivatel;

    @FXML
    private TextField tfJmeno;
    @FXML
    private TextField tfPrijmeni;
    @FXML
    private PasswordField tfHeslo1;
    @FXML
    private PasswordField tfHeslo2;
    @FXML
    private ComboBox<Integer> cbRocnik;
    @FXML
    private TextField tfEmail;

    private ObservableList<StudijniPlan> studijniPlany = FXCollections.observableArrayList();
    private ObservableList<StudijniObor> obory = FXCollections.observableArrayList();
    private ObservableList<Role> role = FXCollections.observableArrayList();
    private ObservableList<Integer> rocniky = FXCollections.observableArrayList();
    private ObservableList<Integer> blokace = FXCollections.observableArrayList();

    private SimpleBooleanProperty giveAdminPermissions = new SimpleBooleanProperty();

    private Predicate<Uzivatel> canEdit;

    @FXML
    private TextField tfStareHeslo;
    @FXML
    private TextArea taPoznamka;
    @FXML
    private ListView<Role> listViewRole;
    @FXML
    private ListView<StudijniPlan> listViewPlany;
    @FXML
    private ComboBox<StudijniObor> cbObor;
    @FXML
    private Tab tabRole;
    @FXML
    private ComboBox<Integer> cbBlokace;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        giveAdminPermissions.setValue(false);
        tabRole.setDisable(true);

        rocniky.addAll(0, 1, 2, 3, 4, 5, 6);
        cbRocnik.setItems(rocniky);
        cbObor.setItems(obory);

        blokace.addAll(0, 1);
        cbBlokace.setItems(blokace);

        listViewPlany.setItems(studijniPlany);
        listViewRole.setItems(role);

        giveAdminPermissions.addListener((observable, oldValue, newValue) -> {
            tabRole.setDisable(!newValue);
        });
    }

    public void updateViews(Uzivatel uzivatel, boolean isAdmin) {
        this.uzivatel = uzivatel;
        tfEmail.setText(uzivatel.getEmail());
        tfJmeno.setText(uzivatel.getJmeno());
        tfPrijmeni.setText(uzivatel.getPrijmeni());
        taPoznamka.setText(uzivatel.getPoznamka());
        cbRocnik.getSelectionModel().select(uzivatel.getRokStudia());

        giveAdminPermissions.setValue(isAdmin);

        if (canEdit != null) {
            cbBlokace.setDisable(!canEdit.test(uzivatel));
        }
    }

    @FXML
    private void handleBtnZrusitAction(ActionEvent event) {
        if (btnCancelEvent != null) {
            btnCancelEvent.accept(uzivatel);
        }
    }

    public void updateStudijniPlany(List<StudijniPlan> data) {
        studijniPlany.clear();
        studijniPlany.addAll(data);
    }

    public void updateStudijniObory(List<StudijniObor> data) {
        obory.clear();
        obory.addAll(data);
    }

    public void updateRole(List<Role> data) {
        role.clear();
        role.addAll(data);
    }

    public void setCurrentObor(StudijniObor obor) {
        
    }

    public void setCurrentRole(List<Role> role) {
        
    }

    public void setCurrentPlany(List<StudijniPlan> plany) {

    }

    /**
     * Nastaví akci, podle které se určí, jestli má daný uživatel právo určitých
     * úprav
     *
     * @param canEdit akce, která se má provést
     */
    public void setCanEditAction(Predicate<Uzivatel> canEdit) {
        this.canEdit = canEdit;
    }

    public void setBtnCancelEvent(Consumer<Uzivatel> btnCancelEvent) {
        this.btnCancelEvent = btnCancelEvent;
    }

    public void setBtnSaveEvent(Consumer<ActionEvent> btnSaveEvent) {
        this.btnSaveEvent = btnSaveEvent;
    }

    @FXML
    private void handleBtnUlozitZmenyAction(ActionEvent event) {
        if (btnSaveEvent != null) {
            btnSaveEvent.accept(event);
        }
    }

}
