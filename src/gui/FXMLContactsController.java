/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import gui.customcells.GroupListCell;
import gui.customcells.KontaktListCell;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.GridPane;
import model.KontaktVypis;
import model.StudijniPlan;

/**
 * FXML Controller class
 *
 * @author Lukas
 */
public class FXMLContactsController implements Initializable {

    // Contacts variables start
    private Consumer<KontaktVypis> showProfileAction;
    private Consumer<List<KontaktVypis>> selectedContactChangedAction;
    private Consumer<KontaktVypis> removeContactAction;
    private ObservableList<KontaktVypis> kontakty = FXCollections.observableArrayList();
    // Contacts variables end

    // Group variables start
    private Consumer<StudijniPlan> showGroupMembersAction;
    private Consumer<StudijniPlan> showDetailAction;
    private Consumer<StudijniPlan> selectedGroupChangedAction;
    private ObservableList<StudijniPlan> skupiny = FXCollections.observableArrayList();
    private int selectedGroupId;
    // Group variables end

    @FXML
    private GridPane gridPane;
    @FXML
    private ListView<KontaktVypis> listViewKontakty;
    @FXML
    private ListView<StudijniPlan> listViewGroups;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listViewKontakty.setItems(kontakty);

        listViewKontakty.setCellFactory((param) -> {
            return new KontaktListCell(addContextMenuContact());
        });

        listViewKontakty.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        listViewKontakty.setOnMousePressed((event) -> {
            switch (event.getButton()) {
                case PRIMARY:
                    if (selectedContactChangedAction != null) {
                        List<KontaktVypis> kontakt = listViewKontakty.getSelectionModel().getSelectedItems();
                        if (kontakt != null) {
                            selectedContactChangedAction.accept(listViewKontakty.getSelectionModel().getSelectedItems());
                        }

                    }
                    break;
            }

        });

        listViewGroups.setOnMousePressed((event) -> {
            switch (event.getButton()) {
                case PRIMARY:
                    if (selectedGroupChangedAction != null) {
                        listViewKontakty.getSelectionModel().select(null);
                        StudijniPlan skupina = listViewGroups.getSelectionModel().getSelectedItem();
                        if (skupina != null) {
                            selectedGroupId = skupina.getIdPlanu();
                            selectedGroupChangedAction.accept(skupina);
                        }
                    }
                    break;
            }
        });

        listViewGroups.setItems(skupiny);

        listViewGroups.setCellFactory((param) -> {
            return new GroupListCell(addContextMenuGroup());
        });

    }

    // Contacts operations start
    /**
     * Vytvo???? kontextov?? menu pro dan?? panel kontaktu
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

        MenuItem removeContact = new MenuItem("Odebrat kontakt");
        removeContact.setOnAction((event) -> {
            if (removeContactAction != null) {
                removeContactAction.accept(listViewKontakty.getSelectionModel().getSelectedItem());
            }
        });

        contextMenu.getItems().addAll(showDetails, removeContact);
        return contextMenu;
    }

    /**
     * Nastav??, co se m?? st??t p??i ud??losti kliknut?? na kontakt
     *
     * @param selectedContactChangedAction akce, kter?? se m?? prov??st
     */
    public void setSelectedContactChangedAction(Consumer<List<KontaktVypis>> selectedContactChangedAction) {
        this.selectedContactChangedAction = selectedContactChangedAction;
    }

    /**
     * Nastav??, co se m?? st??t p??i kliknut?? na kontextov?? menu "zobrazit profil"
     *
     * @param showProfileAction
     */
    public void setContextMenuShowProfileAction(Consumer<KontaktVypis> showProfileAction) {
        this.showProfileAction = showProfileAction;
    }

    /**
     * Nastav??, co se m?? st??t p??i kliknut?? na kontextov?? menu "odebrat kontakt"
     *
     * @param removeContactAction
     */
    public void setRemoveContactAction(Consumer<KontaktVypis> removeContactAction) {
        this.removeContactAction = removeContactAction;
    }

    /**
     * Vr??t?? seznam vybran??ch kontakt??
     *
     * @return seznam vybran??ch kontakt??
     */
    public List<KontaktVypis> getSelectedUsers() {
        return listViewKontakty.getSelectionModel().getSelectedItems();
    }

    /**
     * Nastav?? data pro zobrazen??
     *
     * @param data seznam kontakt?? u??ivatele
     * @throws SQLException
     */
    public void setContactDataSet(List<KontaktVypis> data) throws SQLException {
        kontakty.clear();
        kontakty.addAll(data);
    }

    // Contacts operations end
    // Group operations start
    /**
     * Vytvo???? kontextov?? menu pro dan?? panel skupiny
     *
     * @return
     */
    private ContextMenu addContextMenuGroup() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem showMembers = new MenuItem("Zobrazit ??leny skupiny");
        showMembers.setOnAction((event) -> {
            if (showGroupMembersAction != null) {
                showGroupMembersAction.accept(listViewGroups.getSelectionModel().getSelectedItem());
            }
        });
        MenuItem showDetail = new MenuItem("Zobrazit detail skupiny");
        showDetail.setOnAction((event) -> {
            if (showDetailAction != null) {
                showDetailAction.accept(listViewGroups.getSelectionModel().getSelectedItem());
            }
        });
        contextMenu.getItems().addAll(showMembers, showDetail);
        return contextMenu;
    }

    public int getSelectedGroupId() {
        return selectedGroupId;
    }

    /**
     * Nastav??, co se m?? st??t p??i ud??losti kliknut?? na skupinu
     *
     * @param selectedGroupChangedAction akce, kter?? se m?? prov??st
     */
    public void setSelectedGroupChangedAction(Consumer<StudijniPlan> selectedGroupChangedAction) {
        this.selectedGroupChangedAction = selectedGroupChangedAction;
    }

    /**
     * Nastav??, co se m?? st??t p??i kliknut?? na kontextov?? menu "zobrazit ??leny
     * skupiny"
     *
     * @param showGroupMembersAction akce, kter?? se m?? prov??st
     */
    public void setShowGroupMembersAction(Consumer<StudijniPlan> showGroupMembersAction) {
        this.showGroupMembersAction = showGroupMembersAction;
    }

    /**
     * Nastav??, co se m?? st??t p??i kliknut?? na kontextov?? menu "zobrazit detail
     * skupiny"
     *
     * @param showDetailAction
     */
    public void setShowGroupDetailAction(Consumer<StudijniPlan> showDetailAction) {
        this.showDetailAction = showDetailAction;
    }

    /**
     * Nastav?? data pro zobrazen??
     *
     * @param data seznam skupin u??ivatele
     * @throws SQLException
     */
    public void setGroupsDataSet(List<StudijniPlan> data) throws SQLException {
        skupiny.clear();
        skupiny.addAll(data);
    }

    // Group operations end
}
