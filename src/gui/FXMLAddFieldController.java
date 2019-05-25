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
import javafx.scene.control.Alert;
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
    private int numOfErrors;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        numOfErrors = 0;
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

    public String getNazev() {
        String nazev = tfNazev.getText();
        if (nazev == null) {
            showAlert("Název musí být maximálně 50 "
                    + "znaků dlouhý a nesmí být prázdný");
            numOfErrors++;
            return null;
        }
        if (nazev.length() > 50 || nazev.length() == 0) {
            showAlert("Název musí být maximálně 50 "
                    + "znaků dlouhý a nesmí být prázdný");
            numOfErrors++;
        }
        return tfNazev.getText();
    }

    public String getZkratka() {
        String zkratka = tfZkratkaOboru.getText();
        if (zkratka == null) {
            showAlert("Zkratka musí být maximálně 5 "
                    + "znaků dlouhá a nesmí být prázdná");
            numOfErrors++;
            return null;
        }
        if (zkratka.length() > 5 || zkratka.length() == 0) {
            showAlert("Zkratka musí být maximálně 5 "
                    + "znaků dlouhá a nesmí být prázdná");
            numOfErrors++;
        }

        return tfZkratkaOboru.getText();
    }

    public String getPopis() {
        String popis = tfPopis.getText();
        if (popis == null) {
            showAlert("Popis musí být maximálně 300 "
                    + "znaků dlouhý a nesmí být prázdný");
            numOfErrors++;
            return null;
        }
        if (popis.length() > 300) {
            showAlert("Popis musí být maximálně 300 "
                    + "znaků dlouhý a nesmí být prázdný");
            numOfErrors++;
        }
        return tfPopis.getText();
    }

    public LocalDate getAkreditaceDo() {
        if (dpAkreditace.getValue() == null) {
            showAlert("Neplatné datum");
            numOfErrors++;
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
        numOfErrors = 0;
        String nazev = getNazev();
        String zkratka = getZkratka();
        String popis = getPopis();
        LocalDate akreditaceDo = getAkreditaceDo();

        if (numOfErrors == 0) {
            if (updating) {
                if (updateFieldAction != null) {
                    obor.setAkreditaceDo(akreditaceDo);
                    obor.setNazev(nazev);
                    obor.setPopis(popis);
                    obor.setZkratka(zkratka);
                    updateFieldAction.accept(obor);
                }
            } else {
                if (addFieldAction != null) {
                    StudijniObor o = new StudijniObor(nazev, zkratka, popis, akreditaceDo);
                    addFieldAction.accept(o);
                }
            }
        }
    }

    @FXML
    private void handleBtnZrusitAction(ActionEvent event) {
        if (btnCancelAction != null) {
            btnCancelAction.accept(event);
        }
    }

    /**
     * Zobrazí dialog s errorem
     *
     * @param text text erroru
     */
    private void showAlert(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(text);
        alert.setHeaderText(null);

        alert.showAndWait();
    }

}
