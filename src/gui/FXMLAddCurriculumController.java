/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.StudijniObor;

/**
 * FXML Controller class
 *
 * @author Lukas
 */
public class FXMLAddCurriculumController implements Initializable {

    @FXML
    private TextField tfNazev;
    @FXML
    private ComboBox<StudijniObor> cbObor;
    @FXML
    private TextField tfPopis;
    
    private Consumer<ActionEvent> btnSaveAction;
    private Consumer<ActionEvent> btnCancelAction;
    
    ObservableList<StudijniObor> studijniObory = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbObor.setItems(studijniObory);
    }
    
    public void setDataset(List<StudijniObor> obory){
        studijniObory.clear();
        studijniObory.addAll(obory);
    }

    public String getNazev() {
        return tfNazev.getText();
    }

    public String getPopis() {
        return tfPopis.getText();
    }

    public StudijniObor getObor() {
        return cbObor.getValue();
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
