/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import model.Kontakt;
import model.Uzivatel;

/**
 * FXML Controller class
 *
 * @author Lukas
 */
public class FXMLAccountViewController implements Initializable {

    @FXML
    private Label lblId;
    @FXML
    private Label lblJmeno;
    @FXML
    private Label lblPrijmeni;
    @FXML
    private Label lblEmail;
    @FXML
    private Label lblRokStudia;
    @FXML
    private ListView<Kontakt> listViewKontakty;
    @FXML
    private Label lblPoznamka;
    
    private Uzivatel uzivatel;
    
    private ObservableList<Kontakt> kontakty = FXCollections.observableArrayList();
    
    private SimpleBooleanProperty enableEditButton;
    
    private Consumer<ActionEvent> editButtonAction;
    
    @FXML
    private Button btnUpravit;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        enableEditButton = new SimpleBooleanProperty(false);
        
        enableEditButton.addListener((observable, oldValue, newValue) -> {
            btnUpravit.setVisible(newValue);
        });
        
        listViewKontakty.setItems(kontakty);
    }    

    @FXML
    private void handleBtnUpravitAction(ActionEvent event) {
        if (editButtonAction != null) {
            editButtonAction.accept(event);
        }
    }
    
    public void setEditButtonAction(Consumer<ActionEvent> editButtonAction){
        this.editButtonAction = editButtonAction;
    }
    
    public void updateView(Uzivatel uzivatel){
        setUzivatel(uzivatel);
        kontakty.clear();
        kontakty.addAll(uzivatel.getKontakty());
        lblId.setText(uzivatel.getIdUzivatele());
        lblJmeno.setText(uzivatel.getJmeno());
        lblPrijmeni.setText(uzivatel.getPrijmeni());
        lblEmail.setText(uzivatel.getEmail());
        lblRokStudia.setText(Integer.toString(uzivatel.getRokStudia()));
        lblPoznamka.setText(uzivatel.getPoznamka());
    }
    
    public void setUzivatel(Uzivatel uzivatel){
        this.uzivatel = uzivatel;
    }
    
    public void setEnableButton(boolean expression){
        this.enableEditButton.set(expression);
    }
    
}
