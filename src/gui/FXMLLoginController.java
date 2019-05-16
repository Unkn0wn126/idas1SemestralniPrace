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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Lukas
 */
public class FXMLLoginController implements Initializable {
    
    @FXML
    private TextField tfJmeno;
    @FXML
    private PasswordField tfHeslo;
    
    private Consumer<ActionEvent> btnPrihlasitAction;
    private Consumer<WindowEvent> closeAction;
    
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
        
            this.stage.setOnCloseRequest((event) -> {
                if (closeAction != null) {
                    closeAction.accept(event);
                }
        });
    }

    public void setBtnPrihlasitAction(Consumer<ActionEvent> btnPrihlasitAction) {
        this.btnPrihlasitAction = btnPrihlasitAction;
    }
    
    public void setCloseAction(Consumer<WindowEvent> closeAction){
        this.closeAction = closeAction;
    }
    
    public String getJmeno(){
        return tfJmeno.getText();
    }
    
    public String getHeslo(){
        return tfHeslo.getText();
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }    

    @FXML
    private void handleBtnPrihlasitAction(ActionEvent event) {
        if (btnPrihlasitAction != null) {
            btnPrihlasitAction.accept(event);
        }
    }

    @FXML
    private void handleBtnZrusitAction(ActionEvent event) {
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }
}
