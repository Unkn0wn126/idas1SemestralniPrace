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
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Lukas
 */
public class FXMLAddSubjectController implements Initializable {

    @FXML
    private TextField tfPopis;
    @FXML
    private TextField tfZkratkaPredmetu;
    @FXML
    private TextField tfNazevPredmetu;
    
    private Consumer<ActionEvent> btnSaveAction;
    private Consumer<ActionEvent> btnCancelAction;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public String getPopis() {
        return tfPopis.getText();
    }

    public String getZkratkaPredmetu() {
        return tfZkratkaPredmetu.getText();
    }

    public String getNazevPredmetu() {
        return tfNazevPredmetu.getText();
    }

    public void setBtnSaveAction(Consumer<ActionEvent> btnSaveAction) {
        this.btnSaveAction = btnSaveAction;
    }

    public void setBtnCancelAction(Consumer<ActionEvent> btnCancelAction) {
        this.btnCancelAction = btnCancelAction;
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
