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
import model.StudijniObor;

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

    private Consumer<StudijniObor> addFieldAction;
    private Consumer<StudijniObor> updateFieldAction;
    private Consumer<ActionEvent> btnCancelAction;

    private StudijniObor obor;
    private boolean updating;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void setAddFieldAction(Consumer<StudijniObor> addFieldAction) {
        this.addFieldAction = addFieldAction;
    }

    public void setUpdateFieldAction(Consumer<StudijniObor> updateFieldAction) {
        this.updateFieldAction = updateFieldAction;
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

    public void setDataset(StudijniObor obor) {
        this.obor = obor;
        updating = true;
        updateViews();
    }

    private void updateViews() {
        tfNazev.setText(obor.getNazev());
        tfPopis.setText(obor.getPopis());
        tfZkratkaOboru.setText(obor.getZkratka());
        dpAkreditace.setValue(obor.getAkreditaceDo());
    }

    public void setDataset() {
        this.obor = null;
        updating = false;
        clearInputs();
    }

    @FXML
    private void handleBtnUlozitAction(ActionEvent event) {
        if (updating) { // TODO: Dodělat
            if (updateFieldAction != null) {
                updateFieldAction.accept(obor);
            }
        }else{
            if (addFieldAction != null) {
                addFieldAction.accept(obor);
            }
        }
    }

    @FXML
    private void handleBtnZrusitAction(ActionEvent event) {
        if (btnCancelAction != null) {
            btnCancelAction.accept(event);
        }
    }

}
