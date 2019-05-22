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
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.StudijniObor;
import model.StudijniPlan;

/**
 * FXML Controller class
 *
 * @author Lukas
 */
public class FXMLAddSubjectController implements Initializable {

    @FXML
    private TextArea tfPopis;
    @FXML
    private TextField tfZkratkaPredmetu;
    @FXML
    private TextField tfNazevPredmetu;

    private ObservableList<StudijniPlan> plany = FXCollections.observableArrayList();
    
    private Consumer<ActionEvent> btnSaveAction;
    private Consumer<ActionEvent> btnCancelAction;
    @FXML
    private ListView<StudijniPlan> listViewPlany;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listViewPlany.setItems(plany);
    }

    public void setPlanyData(List<StudijniPlan> data) {
        this.plany.clear();
        this.plany.addAll(data);
    }

    public String getPopis() throws IllegalArgumentException {
        String popis = tfPopis.getText();
        if (popis.length() > 300) {
            throw new IllegalArgumentException("Popis může být dlouhý "
                    + "maximálně 300 znaků včetně");
        }
        return popis;
    }

    public String getZkratkaPredmetu() throws IllegalArgumentException {
        String zkratka = tfZkratkaPredmetu.getText();
        if (zkratka.length() > 5 || zkratka.length() == 0) {
            throw new IllegalArgumentException("Zkratka může být dlouhá "
                    + "maximálně 5 znaků včetně a nesmí být prázdná");
        }
        return zkratka;
    }

    public String getNazevPredmetu() throws IllegalArgumentException {
        String nazev = tfNazevPredmetu.getText();
        if (nazev.length() > 100 || nazev.length() == 0) {
            throw new IllegalArgumentException("Název může být dlouhý "
                    + "maximálně 100 znaků včetně a nesmí být prázdný");
        }
        return nazev;
    }
    
    public void clearInputs(){
        tfNazevPredmetu.setText(null);
        tfPopis.setText(null);
        tfZkratkaPredmetu.setText(null);
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
