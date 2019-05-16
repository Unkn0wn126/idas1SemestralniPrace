/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.Kontakt;
import model.Uzivatel;
import model.UzivatelManager;

/**
 * FXML Controller class
 *
 * @author Lukas
 */
public class FXMLContactsController implements Initializable {

    private ContactView selected;
    private final SimpleBooleanProperty selectedContactChanged = new SimpleBooleanProperty(false);
    private final SimpleBooleanProperty selectedGroupChanged = new SimpleBooleanProperty(false);
    private Consumer onContactChanged;
    private Consumer onGroupChanged;
    private Consumer<ActionEvent> showProfileAction;
    private Consumer<ActionEvent> showGroupMembersAction;
    private ContactView lastRightMouseSelected;
    @FXML
    private VBox vBox;
    @FXML
    private ScrollPane scrollPane;

    private ObservableList<Kontakt> kontakty = FXCollections.observableArrayList();

    private UzivatelManager uzivatelManager;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        scrollPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            vBox.setPrefWidth((Double) newValue);
        });

        vBox.getChildren().add(new Label("---Kontakty---"));
        try {
            fillContactSection();
        } catch (SQLException ex) {
            Logger.getLogger(FXMLContactsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        vBox.getChildren().add(new Label("---Skupiny---"));
        fillGroupSection();

        selectedContactChanged.addListener((observable) -> {
            if (onContactChanged != null) {
                onContactChanged.accept(observable);
            }
        });

        selectedGroupChanged.addListener((observable) -> {
            if (onGroupChanged != null) {
                onGroupChanged.accept(observable);
            }
        });

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

    /**
     * Nastaví akci, která se provede při překliknutí na jiný kontakt
     *
     * @param onContactChanged akce, která se má provést
     */
    public void setContactChangedAction(Consumer onContactChanged) {
        this.onContactChanged = onContactChanged;
    }

    public void setContextMenuShowProfileAction(Consumer<ActionEvent> showProfileAction) {
        this.showProfileAction = showProfileAction;
    }

    /**
     * Nastaví akci, která se provede při překliknutí na jinou skupiny
     *
     * @param onGroupChanged akce, která se má provést
     */
    public void setGroupChangedAction(Consumer onGroupChanged) {
        this.onGroupChanged = onGroupChanged;
    }

    /**
     * Aktualizuje, který panel je současně vybrán
     *
     * @param panel panel, který bude nastaven jako vybraný
     * @param isGroup určuje, jestli se jedná o panel skupiny
     */
    private void replaceSelected(ContactView panel, boolean isGroup) {
        if (selected != null) {
            selected.setStyle("-fx-background-color: #f4f4f4");
        }
        selected = panel;
        if (isGroup) {
            selectedGroupChanged.set(!selectedGroupChanged.getValue());
        } else {
            selectedContactChanged.set(!selectedContactChanged.getValue());
        }

        selected.setStyle("-fx-background-color: #c6c6c6");
    }

    private void fillGroupSection() {
        for (int i = 0; i < 10; i++) {
            ContactView group = new ContactView("Skupina", "" + (i + 1), addContextMenuGroup());
            group.setLeftMouseAction((t) -> {
                replaceSelected(group, true);
            });

            group.setRightMouseAction((t) -> {
                group.showContextMenu(t);
            });

            vBox.getChildren().add(group);
        }
    }

    /**
     * Naplní sekci s kontakty kontakty
     * @throws SQLException 
     */
    private void fillContactSection() throws SQLException {
        if (uzivatelManager != null) {
            for (int i = 0; i < kontakty.size(); i++) {
                Uzivatel uzivatel = uzivatelManager.selectUzivatelById(kontakty.get(i).getIdKontaktu());
                ContactView contact = new ContactView(uzivatel.getJmeno(), uzivatel.getPrijmeni(), addContextMenuContact());
                contact.setLeftMouseAction((t) -> {
                    replaceSelected(contact, false);
                });
                
                contact.setUzivatel(uzivatel);

                contact.setRightMouseAction((t) -> {
                    lastRightMouseSelected = contact;
                    contact.showContextMenu(t);
                });
                vBox.getChildren().add(1, contact);
            }
        }

    }
    
    public ContactView getLastRightMouseSelected(){
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
