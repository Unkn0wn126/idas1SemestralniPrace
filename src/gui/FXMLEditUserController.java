/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author Lukas
 */
public class FXMLEditUserController implements Initializable {
    
    private Consumer<ActionEvent> btnCancelEvent;
    private Consumer<ActionEvent> btnSaveEvent;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    


    @FXML
    private void handleBtnZrusitAction(ActionEvent event) {
        if (btnCancelEvent != null) {
            btnCancelEvent.accept(event);
        }
    }
    
    public void setBtnCancelEvent(Consumer<ActionEvent> btnCancelEvent){
        this.btnCancelEvent = btnCancelEvent;
    }

    public void setBtnSaveEvent(Consumer<ActionEvent> btnSaveEvent) {
        this.btnSaveEvent = btnSaveEvent;
    }

    @FXML
    private void handleBtnUlozitZmenyAction(ActionEvent event) {
        if (btnSaveEvent != null) {
            btnSaveEvent.accept(event);
        }
    }
    
}
