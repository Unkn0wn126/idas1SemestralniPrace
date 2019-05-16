/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Lukas
 */
public class FXMLAddFieldController implements Initializable {

    @FXML
    private TextField tfNazev;
    @FXML
    private TextField tfZkratkaOboru;
    @FXML
    private TextField tfPopis;
    @FXML
    private DatePicker dpAkreditace;

    private Consumer<ActionEvent> btnSaveAction;
    private Consumer<ActionEvent> btnCancelAction;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void setBtnSaveAction(Consumer<ActionEvent> btnSaveAction) {
        this.btnSaveAction = btnSaveAction;
    }

    public void setBtnCancelAction(Consumer<ActionEvent> btnCancelAction) {
        this.btnCancelAction = btnCancelAction;
    }

    public String getNazev() {
        return tfNazev.getText();
    }

    public String getZkratka() {
        return tfZkratkaOboru.getText();
    }

    public String getPopis() {
        return tfPopis.getText();
    }

    public LocalDate getAkreditaceDo() {
        return dpAkreditace.getValue();
    }
    
    public void clearInputs(){
        tfNazev.setText(null);
        tfPopis.setText(null);
        tfZkratkaOboru.setText(null);
        dpAkreditace.setValue(null);
    }

    @FXML
    private void handleBtnUlozitAction(ActionEvent event) {
        if (btnSaveAction != null) {
            btnSaveAction.accept(event);
        }
    }

    @FXML
    private void handleBtnZrusitAction(ActionEvent event) {
        if (btnCancelAction != null) {
            btnCancelAction.accept(event);
        }
    }

}
