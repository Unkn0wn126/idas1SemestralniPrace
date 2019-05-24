/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import gui.customcells.SubjectListCell;
import gui.customcells.UzivatelListCell;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
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
import javafx.scene.control.TextArea;
import model.Predmet;
import model.StudijniObor;
import model.StudijniPlan;
import model.Uzivatel;

/**
 * FXML Controller class
 *
 * @author Lukas
 */
public class FXMLShowCurriculumController implements Initializable {

    private ObservableList<Predmet> predmety = FXCollections.observableArrayList();
    private ObservableList<Uzivatel> uzivatele = FXCollections.observableArrayList();

    private StudijniPlan currPlan;
    private StudijniObor currObor;

    private Consumer<StudijniPlan> btnUpravitAction;
    private Consumer<Uzivatel> showProfileAction;
    private Consumer<Uzivatel> addToContactsAction;

    private Consumer<Predmet> showSubjectDetailAction;
    private Consumer<Predmet> deleteSubjectAction;
    private Consumer<StudijniObor> lblOborPressedAction;

    private SimpleBooleanProperty giveAdminPermissions = new SimpleBooleanProperty();

    private ContextMenu contextMenuPredmet;

    @FXML
    private Label lblNazev;
    @FXML
    private Label lblObor;
    @FXML
    private TextArea taPopis;
    @FXML
    private ListView<Predmet> listViewPredmety;
    @FXML
    private ListView<Uzivatel> listViewUzivatele;
    @FXML
    private Button btnUpravit;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        contextMenuPredmet = addContextMenuSubjects();
        giveAdminPermissions.setValue(false);
        
        setBtnUpravitUsable(false);

        listViewPredmety.setItems(predmety);
        listViewPredmety.setCellFactory((param) -> {
            return new SubjectListCell(contextMenuPredmet);
        });

        listViewUzivatele.setItems(uzivatele);
        listViewUzivatele.setCellFactory((param) -> {
            return new UzivatelListCell(addContextMenuUsers());
        });

        lblObor.setOnMousePressed((event) -> {
            if (lblOborPressedAction != null) {
                lblOborPressedAction.accept(currObor);
            }
        });

        giveAdminPermissions.addListener((observable, oldValue, newValue) -> {
            setBtnUpravitUsable(newValue);
            
            if (newValue && contextMenuPredmet.getItems().size() < 2) {
                contextMenuPredmet.getItems().add(createMenuDelete());
            } else if (!newValue && contextMenuPredmet.getItems().size() > 1) {
                contextMenuPredmet.getItems().remove(1);
            }
        });
    }

    @FXML
    private void handleBtnUpravitAction(ActionEvent event) {
        if (btnUpravitAction != null) {
            btnUpravitAction.accept(currPlan);
        }
    }

    /**
     * Vytvoří kontextové menu pro daný panel kontaktu
     *
     * @return
     */
    private ContextMenu addContextMenuUsers() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem showDetails = new MenuItem("Zobrazit profil");
        showDetails.setOnAction((event) -> {
            if (showProfileAction != null) {
                showProfileAction.accept(listViewUzivatele.getSelectionModel().getSelectedItem());
            }
        });

        MenuItem addToContacts = new MenuItem("Přidat do kontaktů");
        addToContacts.setOnAction((event) -> {
            if (addToContactsAction != null) {
                addToContactsAction.accept(listViewUzivatele.getSelectionModel().getSelectedItem());
            }
        });

        contextMenu.getItems().addAll(showDetails, addToContacts);
        return contextMenu;
    }

    public void setDataset(List<Predmet> predmety, List<Uzivatel> uzivatele, StudijniPlan currPlan, StudijniObor currObor, boolean isAdmin) {
        this.currPlan = currPlan;
        this.currObor = currObor;
        this.predmety.clear();
        this.uzivatele.clear();

        this.predmety.addAll(predmety);
        this.uzivatele.addAll(uzivatele);

        giveAdminPermissions.set(isAdmin);

        updateViews();
    }

    private void updateViews() {
        lblObor.setText(currObor.getNazev());
        lblNazev.setText(currPlan.getNazev());
        taPopis.setText(currPlan.getPopis());
    }

    public void setBtnUpravitAction(Consumer<StudijniPlan> btnUpravitAction) {
        this.btnUpravitAction = btnUpravitAction;
    }

    public void setShowProfileAction(Consumer<Uzivatel> showProfileAction) {
        this.showProfileAction = showProfileAction;
    }

    public void setAddToContactsAction(Consumer<Uzivatel> addToContactsAction) {
        this.addToContactsAction = addToContactsAction;
    }

    public void setLblOborPressedAction(Consumer<StudijniObor> lblOborPressedAction) {
        this.lblOborPressedAction = lblOborPressedAction;
    }

    private void setBtnUpravitUsable(boolean allow) {
        btnUpravit.setVisible(allow);
        btnUpravit.setDisable(!allow);
    }

    private ContextMenu addContextMenuSubjects() {
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

    private MenuItem createMenuDelete() {
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

    public void setDeleteAction(Consumer<Predmet> deleteAction) {
        this.deleteSubjectAction = deleteAction;
    }
}
