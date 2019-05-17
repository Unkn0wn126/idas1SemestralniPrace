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
import javafx.scene.layout.GridPane;
import model.Kontakt;
import model.Skupina;
import model.Uzivatel;
import model.UzivatelManager;

/**
 * FXML Controller class
 *
 * @author Lukas
 */
public class FXMLContactsController implements Initializable {

    private Consumer<ActionEvent> showProfileAction;
    private Consumer<ActionEvent> showGroupMembersAction;
    private Consumer<ActionEvent> selectedContactChangedAction;
    private Consumer<ActionEvent> selectedGroupChangedAction;
    private ContactView lastRightMouseSelected;

    private ObservableList<Kontakt> kontakty = FXCollections.observableArrayList();
    private ObservableList<Uzivatel> uzivatele = FXCollections.observableArrayList();
    private ObservableList<Skupina> skupiny = FXCollections.observableArrayList();

    private UzivatelManager uzivatelManager;

    @FXML
    private GridPane gridPane;
    @FXML
    private ListView<Uzivatel> listViewKontakty;
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
        listViewKontakty.setItems(uzivatele);

        listViewKontakty.setCellFactory((param) -> {
            return new KontaktListCell();
        });
        
        listViewKontakty.getSelectionModel().selectedItemProperty().addListener((observable) -> {
            if (selectedContactChangedAction != null) {
                selectedContactChangedAction.accept(null);
            }
        });
        
        listViewGroups.getSelectionModel().selectedItemProperty().addListener((observable) -> {
            if (selectedGroupChangedAction != null) {
                selectedGroupChangedAction.accept(null);
            }
        });
        
        listViewGroups.setItems(skupiny);
        
        listViewGroups.setCellFactory((param) -> {
            return new GroupListCell();
        });

        try {
            fillContactSection();
        } catch (SQLException ex) {
            Logger.getLogger(FXMLContactsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        fillGroupSection();

    }

    public void setSelectedContactChangedAction(Consumer<ActionEvent> selectedContactChangedAction) {
        this.selectedContactChangedAction = selectedContactChangedAction;
    }

    public void setSelectedGroupChangedAction(Consumer<ActionEvent> selectedGroupChangedAction) {
        this.selectedGroupChangedAction = selectedGroupChangedAction;
    }

    /**
     * Vytvoří kontextové menu pro daný panel kontaktu
     *
     * @return kontextové menu
     */
    private ContextMenu addContextMenuContact() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem showDetails = new MenuItem("Zobrazit profil");
        showDetails.setOnAction((event) -> {
            if (showProfileAction != null) {
                showProfileAction.accept(event);
            }
        });
        contextMenu.getItems().addAll(showDetails);
        return contextMenu;
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
                showGroupMembersAction.accept(event);
            }
        });
        contextMenu.getItems().addAll(showMembers);
        return contextMenu;
    }

    public void setContextMenuShowProfileAction(Consumer<ActionEvent> showProfileAction) {
        this.showProfileAction = showProfileAction;
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
    private void fillContactSection() throws SQLException {
        if (uzivatelManager != null) {
//            uzivatele.clear();
            for (int i = 0; i < kontakty.size(); i++) {
                Uzivatel uzivatel = uzivatelManager.selectUzivatelById(kontakty.get(i).getIdKontaktu());
                uzivatele.add(uzivatel);
            }

        }
    }

    public ContactView getLastRightMouseSelected() {
        return lastRightMouseSelected;
    }

    public void setUzivatelManager(UzivatelManager uzivatelManager) {
        this.uzivatelManager = uzivatelManager;
    }

    public void setContactDataSet(List<Kontakt> data) throws SQLException {
        kontakty.clear();
        kontakty.addAll(data);
        fillContactSection();
    }

}
