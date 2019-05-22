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

/**
 * FXML Controller class
 *
 * @author Lukas
 */
public class FXMLShowFieldController implements Initializable {

    @FXML
    private Label lblNazev;
    @FXML
    private Label lblZkratka;
    @FXML
    private Label lblAkreditaceDo;
    @FXML
    private TextArea taPopis;
    @FXML
    private ListView<?> listViewPlany;
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
