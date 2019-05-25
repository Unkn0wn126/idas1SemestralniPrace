/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import gui.customcells.PickCurriculumListCell;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Predmet;
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

    private Consumer<Predmet> updateAction;
    private Consumer<Predmet> addAction;
    private Consumer<ActionEvent> btnCancelAction;

    private Predmet currPredmet;

    private boolean updating;
    private int numOfErrors;
    private boolean inputInserted;

    @FXML
    private ListView<StudijniPlan> listViewPlany;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inputInserted = false;
        numOfErrors = 0;

        listViewPlany.setItems(plany);
        listViewPlany.setCellFactory((param) -> {
            return new PickCurriculumListCell();
        });
    }

    public void setPlanyData(List<StudijniPlan> planyAll) {
        this.plany.clear();
        this.plany.addAll(planyAll);
        this.currPredmet = null;
        updating = false;
        clearInputs();
    }

    public void setPlanyData(List<StudijniPlan> planyAll,
            List<StudijniPlan> planySelected, Predmet currPredmet) {

        for (StudijniPlan plan : planyAll) {
            plan.setSelected(planySelected.stream().anyMatch((t) -> {
                return plan.getIdPlanu() == t.getIdPlanu();
            }));
        }

        this.plany.clear();
        this.plany.addAll(planyAll);

        this.currPredmet = currPredmet;
        updating = true;
        updateViews();
    }

    private void updateViews() {
        tfPopis.setText(currPredmet.getPopis());
        tfNazevPredmetu.setText(currPredmet.getNazevPredmetu());
        tfZkratkaPredmetu.setText(currPredmet.getZkratkaPredmetu());
    }

    public String getPopis() throws IllegalArgumentException {
        String popis = tfPopis.getText();
        if (popis == null) {
            return null;
        }
        if (popis.length() > 300) {
            showAlert("Popis může být dlouhý "
                    + "maximálně 300 znaků včetně");
            numOfErrors++;
        }
        return popis;
    }

    public String getZkratkaPredmetu() throws IllegalArgumentException {
        String zkratka = tfZkratkaPredmetu.getText();
        if (zkratka == null) {
            showAlert("Zkratka může být dlouhá "
                    + "maximálně 5 znaků včetně a nesmí být prázdná");
            numOfErrors++;
            return null;
        }
        if (zkratka.length() > 5 || zkratka.length() == 0) {
            showAlert("Zkratka může být dlouhá "
                    + "maximálně 5 znaků včetně a nesmí být prázdná");
            numOfErrors++;
        }
        return zkratka;
    }

    public String getNazevPredmetu() throws IllegalArgumentException {
        String nazev = tfNazevPredmetu.getText();
        if (nazev == null) {
            showAlert("Název může být dlouhý "
                    + "maximálně 100 znaků včetně a nesmí být prázdný");
            numOfErrors++;
            return null;
        }
        if (nazev.length() > 100 || nazev.length() == 0) {
            showAlert("Název může být dlouhý "
                    + "maximálně 100 znaků včetně a nesmí být prázdný");
            numOfErrors++;
        }
        return nazev;
    }

    public List<StudijniPlan> getSeznamPlanu() {
        List<StudijniPlan> selectedPlany = new ArrayList<>();
        for (StudijniPlan studijniPlan : plany) {
            if (studijniPlan.isSelected()) {
                selectedPlany.add(studijniPlan);
            }
        }
        if (selectedPlany.size() < 1) {
            showAlert("Musíte vybrat alespoň jeden příslušný studijní plán");
            numOfErrors++;
        }

        return selectedPlany;
    }

    public void clearInputs() {
        tfNazevPredmetu.setText(null);
        tfPopis.setText(null);
        tfZkratkaPredmetu.setText(null);
    }

    public void setUpdateAction(Consumer<Predmet> updateAction) {
        this.updateAction = updateAction;
    }

    public void setAddAction(Consumer<Predmet> addAction) {
        this.addAction = addAction;
    }

    public void setBtnCancelAction(Consumer<ActionEvent> btnCancelAction) {
        this.btnCancelAction = btnCancelAction;
    }

    public boolean isInputInserted() {
        return inputInserted;
    }

    @FXML
    private void handleBtnUlozitAction(ActionEvent event) {
        inputInserted = false;
        numOfErrors = 0;
        String nazev = getNazevPredmetu();
        String zkratka = getZkratkaPredmetu();
        String popis = getPopis();
        getSeznamPlanu();

        if (numOfErrors == 0) {
            if (updating) {
                if (updateAction != null) {
                    this.currPredmet.setNazevPredmetu(nazev);
                    this.currPredmet.setPopis(popis);
                    this.currPredmet.setZkratkaPredmetu(zkratka);
                    updateAction.accept(this.currPredmet);
                    inputInserted = true;
                }
            } else {
                if (addAction != null) {
                    Predmet pr = new Predmet(nazev, zkratka, popis);
                    addAction.accept(pr);
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
