/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import model.Predmet;
import model.Uzivatel;

/**
 * FXML Controller class
 *
 * @author Lukas
 */
public class FXMLShowCurriculumController implements Initializable {

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
        // TODO
    }    

    @FXML
    private void handleBtnUpravitAction(ActionEvent event) {
    }
    
}
