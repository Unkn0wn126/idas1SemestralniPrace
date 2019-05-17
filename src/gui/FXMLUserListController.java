/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import gui.customcells.KontaktListCell;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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
    
    private Consumer<ActionEvent> addToContactsAction;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listViewUzivatele.setItems(dataset);
        listViewUzivatele.setCellFactory((param) -> {
            return new KontaktListCell();
        });
    }
    
    public void setDataSet(List<Uzivatel> uzivatele) {
        dataset.clear();
        dataset.addAll(uzivatele);
    }
    
    public void setAddToContactsAction(Consumer<ActionEvent> addToContactsAction){
        this.addToContactsAction = addToContactsAction;
    }
    
    public Uzivatel getSelected(){
        return listViewUzivatele.getSelectionModel().getSelectedItem();
    }

    @FXML
    private void handleBtnPridatDoKontaktuAction(ActionEvent event) {
        if (addToContactsAction != null) {
            addToContactsAction.accept(event);
        }
    }
}
