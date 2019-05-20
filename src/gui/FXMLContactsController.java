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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.GridPane;
import model.Kontakt;
import model.KontaktVypis;
import model.Skupina;
import model.Uzivatel;
import model.UzivatelManager;

/**
 * FXML Controller class
 *
 * @author Lukas
 */
public class FXMLContactsController implements Initializable {

    private Consumer<Uzivatel> showProfileAction;
    private Consumer<List<Uzivatel>> sendMessageAction;
    private Consumer<Skupina> showGroupMembersAction;
    private Consumer<Uzivatel> selectedContactChangedAction;
    private Consumer<Uzivatel> removeContactAction;
    private Consumer<Skupina> selectedGroupChangedAction;

    private ObservableList<KontaktVypis> kontakty = FXCollections.observableArrayList();
    private ObservableList<Uzivatel> uzivatele = FXCollections.observableArrayList();
    private ObservableList<Skupina> skupiny = FXCollections.observableArrayList();

    private UzivatelManager uzivatelManager;

    @FXML
    private GridPane gridPane;
    @FXML
    private ListView<KontaktVypis> listViewKontakty;
    @FXML
    private ListView<Skupina> listViewGroups;

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
//                        selectedContactChangedAction.accept(listViewKontakty.getSelectionModel().getSelectedItem());
                    }
                    break;
            }

        });

        listViewGroups.setOnMousePressed((event) -> {
            switch (event.getButton()) {
                case PRIMARY:
                    if (selectedGroupChangedAction != null) {
                        listViewKontakty.getSelectionModel().select(null);
                        selectedGroupChangedAction.accept(listViewGroups.getSelectionModel().getSelectedItem());
                    }
                    break;
            }
        });

        listViewGroups.setItems(skupiny);

        listViewGroups.setCellFactory((param) -> {
            return new GroupListCell(addContextMenuGroup());
        });

        try {
            updateContactSection();
        } catch (SQLException ex) {
            Logger.getLogger(FXMLContactsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        fillGroupSection();

    }

    public void setSelectedContactChangedAction(Consumer<Uzivatel> selectedContactChangedAction) {
        this.selectedContactChangedAction = selectedContactChangedAction;
    }

    public void setSelectedGroupChangedAction(Consumer<Skupina> selectedGroupChangedAction) {
        this.selectedGroupChangedAction = selectedGroupChangedAction;
    }

    public void setShowGroupMembersAction(Consumer<Skupina> showGroupMembersAction) {
        this.showGroupMembersAction = showGroupMembersAction;
    }

    /**
     * Vytvoří kontextové menu pro daný panel skupiny
     *
     * @return
     */
    private ContextMenu addContextMenuGroup() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem showMembers = new MenuItem("Zobrazit členy skupiny");
        showMembers.setOnAction((event) -> {
            if (showGroupMembersAction != null) {
                showGroupMembersAction.accept(listViewGroups.getSelectionModel().getSelectedItem());
            }
        });
        contextMenu.getItems().addAll(showMembers);
        return contextMenu;
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
//                showProfileAction.accept(listViewKontakty.getSelectionModel().getSelectedItem());
            }
        });

        MenuItem sendMessage = new MenuItem("Poslat zprávu");
        sendMessage.setOnAction((event) -> {
            if (sendMessageAction != null) {
//                sendMessageAction.accept(listViewKontakty.getSelectionModel().getSelectedItems());
            }
        });

        MenuItem removeContact = new MenuItem("Odebrat kontakt");
        removeContact.setOnAction((event) -> {
            if (removeContactAction != null) {
//                removeContactAction.accept(listViewKontakty.getSelectionModel().getSelectedItem());
            }
        });
        
        contextMenu.getItems().addAll(showDetails, sendMessage, removeContact);
        return contextMenu;
    }

    public void setContextMenuShowProfileAction(Consumer<Uzivatel> showProfileAction) {
        this.showProfileAction = showProfileAction;
    }

    public void setContextMenuSendMessageAction(Consumer<List<Uzivatel>> sendMessageAction) {
        this.sendMessageAction = sendMessageAction;
    }

    public void setRemoveContactAction(Consumer<Uzivatel> removeContactAction) {
        this.removeContactAction = removeContactAction;
    }

    private void fillGroupSection() {
        for (int i = 0; i < 20; i++) {
            skupiny.add(new Skupina(i, i));
        }
    }

    /**
     * Naplní sekci s kontakty kontakty
     *
     * @throws SQLException
     */
    private void updateContactSection() throws SQLException {
//        if (uzivatelManager != null) {
//            uzivatele.clear();
//            for (int i = 0; i < kontakty.size(); i++) {
//                Uzivatel uzivatel = uzivatelManager.selectUzivatelById(kontakty.get(i).());
//                uzivatele.add(uzivatel);
//            }
//
//        }
    }

    public List<KontaktVypis> getSelectedUsers() {
        return listViewKontakty.getSelectionModel().getSelectedItems();
    }

    public void setUzivatelManager(UzivatelManager uzivatelManager) {
        this.uzivatelManager = uzivatelManager;
    }

    public void setContactDataSet(List<KontaktVypis> data) throws SQLException {
        kontakty.clear();
        kontakty.addAll(data);
        updateContactSection();
    }

}
