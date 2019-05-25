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
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.StudijniObor;
import model.StudijniPlan;

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

    private Consumer<StudijniPlan> updateAction;
    private Consumer<StudijniPlan> addAction;
    private Consumer<ActionEvent> btnCancelAction;

    private ObservableList<StudijniObor> studijniObory = FXCollections.observableArrayList();

    private StudijniPlan currPlan;
    boolean updating;
    private int numOfErrors;
    private boolean inputInserted;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        numOfErrors = 0;
        inputInserted = false;

        cbObor.setItems(studijniObory);

    }

    public void setDataset(List<StudijniObor> obory) {
        this.studijniObory.clear();
        this.studijniObory.addAll(obory);

        this.currPlan = null;
        updating = false;

        clearViews();
    }

    public void setDataset(List<StudijniObor> obory, StudijniPlan currPlan) {

        this.studijniObory.clear();
        this.studijniObory.addAll(obory);

        this.currPlan = currPlan;
        updating = true;
        updateViews();
    }

    private void updateViews() {
        tfNazev.setText(currPlan.getNazev());
        tfPopis.setText(currPlan.getPopis());
        List<StudijniObor> obor = studijniObory.stream().filter((t) -> {
            return currPlan.getIdOboru() == t.getIdOboru();
        }).collect(Collectors.toList());

        cbObor.getSelectionModel().select(obor.get(0));
    }

    private void clearViews() {
        tfNazev.clear();
        tfPopis.clear();
        cbObor.getSelectionModel().selectFirst();
    }

    public String getNazev() throws IllegalArgumentException {
        String nazev = tfNazev.getText();
        if (nazev == null) {
            showAlert("Název nesmí být prázdný "
                    + "a musí být do 50 znaků včetně");
            numOfErrors++;
            return null;
        }
        if (nazev.length() > 50 || nazev.length() == 0) {
            showAlert("Název nesmí být prázdný "
                    + "a musí být do 50 znaků včetně");
            numOfErrors++;
        }
        return nazev;
    }

    public String getPopis() throws IllegalArgumentException {
        String popis = tfPopis.getText();
        if (popis.length() > 300) {
            showAlert("Popis musí být dlouhý"
                    + " do 300 znaků včetně");
            numOfErrors++;
        }
        return popis;
    }

    public StudijniObor getObor() throws IllegalArgumentException {
        StudijniObor obor = cbObor.getValue();
        if (obor == null) {
            showAlert("Neplatný obor");
            numOfErrors++;
        }
        return obor;
    }

    public boolean isInputInserted() {
        return inputInserted;
    }

    public void setUpdateAction(Consumer<StudijniPlan> updateAction) {
        this.updateAction = updateAction;
    }

    public void setAddAction(Consumer<StudijniPlan> addAction) {
        this.addAction = addAction;
    }

    public void setBtnCancelAction(Consumer<ActionEvent> btnCancelAction) {
        this.btnCancelAction = btnCancelAction;
    }

    public void clearInputs() {
        tfNazev.setText(null);
        tfPopis.setText(null);
        cbObor.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleBtnUlozitAction(ActionEvent event) {
        inputInserted = false;
        numOfErrors = 0;
        String nazev = getNazev();
        String popis = getPopis();
        StudijniObor obor = getObor();

        if (numOfErrors == 0) {
            if (updating) {
                if (updateAction != null) {
                    this.currPlan.setIdOboru(obor.getIdOboru());
                    this.currPlan.setNazev(nazev);
                    this.currPlan.setPopis(popis);
                    updateAction.accept(currPlan);
                    inputInserted = true;
                }
            } else {
                if (addAction != null) {
                    StudijniPlan plan = new StudijniPlan(nazev, obor.getIdOboru(), popis);
                    addAction.accept(plan);
                    inputInserted = true;
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
