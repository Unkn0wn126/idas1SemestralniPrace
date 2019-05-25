/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import gui.customcells.PickSubjectListCell;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.stream.Collectors;
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
    private ObservableList<Predmet> predmety = FXCollections.observableArrayList();
    private List<Predmet> currPredmety;

    private StudijniPlan currPlan;
    boolean updating;

    @FXML
    private ListView<Predmet> listViewPredmety;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        currPredmety = new ArrayList<>();
        
        cbObor.setItems(studijniObory);

        listViewPredmety.setItems(predmety);
        listViewPredmety.setCellFactory((param) -> {
            return new PickSubjectListCell();
        });
    }

    public void setDataset(List<StudijniObor> obory, List<Predmet> predmety) {
        this.studijniObory.clear();
        this.studijniObory.addAll(obory);

        this.predmety.clear();
        this.predmety.addAll(predmety);

        this.currPlan = null;
        updating = false;
        
        clearViews();
    }

    public void setDataset(List<StudijniObor> obory, List<Predmet> predmety, StudijniPlan currPlan, List<Predmet> currPredmety) {
        this.currPredmety.clear();
        this.currPredmety.addAll(predmety);
        
        for (Predmet predmet : predmety) {
            predmet.setSelected(currPredmety.stream().anyMatch((t) -> {
                return predmet.getIdPredmetu() == t.getIdPredmetu();
            }));
        }
        
        this.studijniObory.clear();
        this.studijniObory.addAll(obory);

        this.predmety.clear();
        this.predmety.addAll(predmety);

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
    
    private void clearViews(){
        tfNazev.clear();
        tfPopis.clear();
        cbObor.getSelectionModel().selectFirst();
    }

    public String getNazev() throws IllegalArgumentException {
        String nazev = tfNazev.getText();
        if (nazev.length() > 50 || nazev.length() == 0) {
            throw new IllegalArgumentException("Název nesmí být prázdný "
                    + "a musí být do 50 znaků včetně");
        }
        return tfNazev.getText();
    }

    public String getPopis() throws IllegalArgumentException {
        String popis = tfPopis.getText();
        if (popis.length() > 300) {
            throw new IllegalArgumentException("Popis musí být dlouhý"
                    + " do 300 znaků včetně");
        }
        return tfPopis.getText();
    }

    public StudijniObor getObor() throws IllegalArgumentException {
        StudijniObor obor = cbObor.getValue();
        if (obor == null) {
            throw new IllegalArgumentException("Neplatný obor");
        }
        return cbObor.getValue();
    }
    
    public List<Predmet> getSelectedPredmety(){
        boolean inputOk = predmety.stream().anyMatch((t) -> {
            return t.isSelected();
        });
        if (inputOk) {
            List<Predmet> outputPredmety = new ArrayList<>();
            for (Predmet predmet : predmety) {
                if (predmet.isSelected()) {
                    outputPredmety.add(predmet);
                }
            }
            
            return outputPredmety;
        }
        
        return null;
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
        if (updating) {
            if (updateAction != null) {
                updateAction.accept(currPlan);
            }
        } else {
            if (addAction != null) {
                StudijniPlan plan = new StudijniPlan(tfNazev.getText(), cbObor.getValue().getIdOboru(), tfPopis.getText());
                addAction.accept(plan);
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
