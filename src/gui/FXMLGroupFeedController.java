/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Lukas
 */
public class FXMLGroupFeedController implements Initializable {

    @FXML
    private ScrollPane scrollPaneMain;
    @FXML
    private VBox vBoxMain;
    @FXML
    private ScrollPane scrollPanePin;
    @FXML
    private VBox vBoxPin;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
