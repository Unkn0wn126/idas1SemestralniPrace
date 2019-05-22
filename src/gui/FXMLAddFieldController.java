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
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.StudijniPlan;

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
    @FXML
    private ListView<StudijniPlan> listViewPlany;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void setBtnSaveAction(Consumer<ActionEvent> btnSaveAction) {
        this.btnSaveAction = btnSaveAction;
    }

    public void setBtnCancelAction(Consumer<ActionEvent> btnCancelAction) {
        this.btnCancelAction = btnCancelAction;
    }

    public String getNazev() throws IllegalArgumentException {
        String nazev = tfNazev.getText();
        if (nazev.length() > 50 || nazev.length() == 0) {
            throw new IllegalArgumentException("Název musí být maximálně 50 "
                    + "znaků dlouhý a nesmí být prázdný");
        }
        return tfNazev.getText();
    }

    public String getZkratka() throws IllegalArgumentException {
        String zkratka = tfZkratkaOboru.getText();
        if (zkratka.length() > 5 || zkratka.length() == 0) {
            throw new IllegalArgumentException("Zkratka musí být maximálně 5 "
                    + "znaků dlouhý a nesmí být prázdná");
        }
        return tfZkratkaOboru.getText();
    }

    public String getPopis() throws IllegalArgumentException {
        String popis = tfPopis.getText();
        if (popis.length() > 300) {
            throw new IllegalArgumentException("Popis musí být maximálně 300 "
                    + "znaků dlouhý");
        }
        return tfPopis.getText();
    }

    public LocalDate getAkreditaceDo() throws IllegalArgumentException {
        if (dpAkreditace.getValue() == null) {
            throw new IllegalArgumentException("Neplatné datum");
        }
        return dpAkreditace.getValue();
    }

    public void clearInputs() {
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
