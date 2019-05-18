/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.StudijniPlan;
import model.StudijniPlanManager;
import model.Uzivatel;

/**
 * FXML Controller class
 *
 * @author Lukas
 */
public class FXMLEditUserController implements Initializable {
    
    private Consumer<Uzivatel> btnCancelEvent;
    private Consumer<ActionEvent> btnSaveEvent;
    private Uzivatel uzivatel;
    
    @FXML
    private TextField tfJmeno;
    @FXML
    private TextField tfPrijmeni;
    @FXML
    private PasswordField tfHeslo1;
    @FXML
    private PasswordField tfHeslo2;
    @FXML
    private ComboBox<Integer> cbRocnik;
    @FXML
    private TextField tfEmail;
    @FXML
    private TextArea tfPoznamka;
    @FXML
    private ComboBox<StudijniPlan> cbPlan;
    
    private ObservableList<StudijniPlan> studijniPlany = FXCollections.observableArrayList();
    private ObservableList<Integer> rocniky = FXCollections.observableArrayList();
    @FXML
    private TextField tfStareHeslo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rocniky.addAll(0, 1, 2, 3, 4, 5, 6);
        cbRocnik.setItems(rocniky);
        cbPlan.setItems(studijniPlany);
    }    
    
    public void updateViews(Uzivatel uzivatel){ // TODO: dodělat zobrazení studijních plánů
        this.uzivatel = uzivatel;
        tfEmail.setText(uzivatel.getEmail());
        tfJmeno.setText(uzivatel.getJmeno());
        tfPrijmeni.setText(uzivatel.getPrijmeni());
        tfPoznamka.setText(uzivatel.getPoznamka());
        cbRocnik.getSelectionModel().select(uzivatel.getRokStudia());
    }

    @FXML
    private void handleBtnZrusitAction(ActionEvent event) {
        if (btnCancelEvent != null) {
            btnCancelEvent.accept(uzivatel);
        }
    }
    
    public void updateStudijniPlany(List<StudijniPlan> plany){
        studijniPlany.clear();
        studijniPlany.addAll(plany);
    }
    
    public void setBtnCancelEvent(Consumer<Uzivatel> btnCancelEvent){
        this.btnCancelEvent = btnCancelEvent;
    }

    public void setBtnSaveEvent(Consumer<ActionEvent> btnSaveEvent) {
        this.btnSaveEvent = btnSaveEvent;
    }

    @FXML
    private void handleBtnUlozitZmenyAction(ActionEvent event) {
        if (btnSaveEvent != null) {
            btnSaveEvent.accept(event);
        }
    }
    
}
