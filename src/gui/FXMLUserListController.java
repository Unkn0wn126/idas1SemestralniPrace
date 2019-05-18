/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import gui.customcells.KontaktListCell;
import gui.customcells.UzivatelListCell;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import model.Uzivatel;

/**
 * FXML Controller class
 *
 * @author Lukas
 */
public class FXMLUserListController implements Initializable {

    @FXML
    private ListView<Uzivatel> listViewUzivatele;
    @FXML
    private Label lblTitle;

    private ObservableList<Uzivatel> dataset = FXCollections.observableArrayList();

    private Consumer<Uzivatel> addToContactsAction;
    private Consumer<Uzivatel> showProfileAction;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listViewUzivatele.setItems(dataset);
        listViewUzivatele.setCellFactory((param) -> {
            return new UzivatelListCell(addContextMenuContact());
        });
    }

    public void setDataSet(List<Uzivatel> uzivatele) {
        dataset.clear();
        dataset.addAll(uzivatele);
    }

    public void setAddToContactsAction(Consumer<Uzivatel> addToContactsAction) {
        this.addToContactsAction = addToContactsAction;
    }

    public Uzivatel getSelected() {
        return listViewUzivatele.getSelectionModel().getSelectedItem();
    }

    public void setContextMenuShowProfileAction(Consumer<Uzivatel> showProfileAction) {
        this.showProfileAction = showProfileAction;
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
}
