/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import gui.customcells.PickSubjectListCell;
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
import javafx.scene.control.TextField;
import model.Predmet;
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
    ObservableList<Predmet> predmety = FXCollections.observableArrayList();
    
    @FXML
    private ListView<Predmet> listViewPredmety;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbObor.setItems(studijniObory);
        
        listViewPredmety.setItems(predmety);
        listViewPredmety.setCellFactory((param) -> {
            return new PickSubjectListCell();
        });
    }
    
    public void setDataset(List<StudijniObor> obory, List<Predmet> predmety){
        studijniObory.clear();
        studijniObory.addAll(obory);
        
        this.predmety.clear();
        this.predmety.addAll(predmety);
    }

    public String getNazev() throws IllegalArgumentException{
        String nazev = tfNazev.getText();
        if (nazev.length() > 50 || nazev.length() == 0) {
            throw new IllegalArgumentException("Název nesmí být prázdný "
                    + "a musí být do 50 znaků včetně");
        }
        return tfNazev.getText();
    }

    public String getPopis() throws IllegalArgumentException{
        String popis = tfPopis.getText();
        if (popis.length() > 300) {
            throw new IllegalArgumentException("Popis musí být dlouhý"
                    + " do 300 znaků včetně");
        }
        return tfPopis.getText();
    }

    public StudijniObor getObor() throws IllegalArgumentException{
        StudijniObor obor = cbObor.getValue();
        if (obor == null) {
            throw new IllegalArgumentException("Neplatný obor");
        }
        return cbObor.getValue();
    }

    public void setBtnSaveAction(Consumer<ActionEvent> btnSaveAction) {
        this.btnSaveAction = btnSaveAction;
    }

    public void setBtnCancelAction(Consumer<ActionEvent> btnCancelAction) {
        this.btnCancelAction = btnCancelAction;
    }
    
    public void clearInputs(){
        tfNazev.setText(null);
        tfPopis.setText(null);
        cbObor.getSelectionModel().clearSelection();
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
