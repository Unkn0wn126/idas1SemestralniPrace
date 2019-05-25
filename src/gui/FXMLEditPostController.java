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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Prispevek;

/**
 * FXML Controller class
 *
 * @author Lukas
 */
public class FXMLEditPostController implements Initializable {

    @FXML
    private ComboBox<Integer> cbPriorita;
    @FXML
    private TextField tfNazev;
    @FXML
    private TextArea tfObsah;

    private ObservableList<Integer> priority = FXCollections.observableArrayList();

    private Consumer<Prispevek> btnSaveAction;
    private Consumer<ActionEvent> btnCancelAction;

    private Prispevek currPrispevek;

    private int numOfErrors;
    private boolean isComment;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        numOfErrors = 0;
        isComment = false;
        cbPriorita.setItems(priority);
    }

    public void updateDataSet(Prispevek currPrispevek, List<Integer> priority, int currPriority) {
        this.currPrispevek = currPrispevek;
        tfNazev.setText(currPrispevek.getNazev());
        this.isComment = currPrispevek.getIdRodice() > 0;
        
        tfNazev.setDisable(isComment);
        cbPriorita.setDisable(isComment);
        
        tfObsah.setText(currPrispevek.getObsahPrispevku());
        this.priority.clear();
        this.priority.addAll(priority);
        List<Integer> selectPriority = this.priority.stream().filter((t) -> {
            return t == currPriority;
        }).collect(Collectors.toList());

        cbPriorita.getSelectionModel().select(selectPriority.get(0));
    }

    public String getNazev() {
        String nazev = tfNazev.getText();
        if (nazev == null) {
            showAlert("Název může být maximálně 50 znaků dlouhý a nesmí být prázdný");
            numOfErrors++;
        }
        if (nazev.length() > 50 || nazev.length() < 1) {
            showAlert("Název může být maximálně 50 znaků dlouhý a nesmí být prázdný");
            numOfErrors++;
        }

        return nazev;
    }

    public String getObsah() {
        String obsah = tfObsah.getText();
        if (obsah == null) {
            showAlert("Obsah může být maximálně 1000 znaků dlouhý a nesmí být prázdný");
            numOfErrors++;
        }
        if (obsah.length() > 50 || obsah.length() < 1) {
            showAlert("Obsah může být maximálně 1000 znaků dlouhý a nesmí být prázdný");
            numOfErrors++;
        }

        return obsah;
    }

    public int getPriorita() {
        if (cbPriorita.getValue() == null) {
            showAlert("Neplatná priorita");
            numOfErrors++;
            return -1;
        }

        return cbPriorita.getValue();
    }

    public void setBtnSaveAction(Consumer<Prispevek> btnSaveAction) {
        this.btnSaveAction = btnSaveAction;
    }

    public void setBtnCancelAction(Consumer<ActionEvent> btnCancelAction) {
        this.btnCancelAction = btnCancelAction;
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

    @FXML
    private void handleBtnUlozitAction(ActionEvent event) {
        numOfErrors = 0;
        String nazev = null;
        int priorita = 0;

        if (!isComment) {
            nazev = getNazev();
            priorita = getPriorita();
        }
        String obsah = getObsah();

        if (numOfErrors == 0) {
            if (btnSaveAction != null) {
                this.currPrispevek.setNazev(nazev);
                this.currPrispevek.setObsahPrispevku(obsah);
                this.currPrispevek.setPriorita(priorita);

                btnSaveAction.accept(currPrispevek);
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
