/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import model.Predmet;
import model.Uzivatel;

/**
 * FXML Controller class
 *
 * @author Lukas
 */
public class FXMLSubjectsListController implements Initializable {

    @FXML
    private Label lblTitle;
    @FXML
    private ListView<Predmet> listViewPredmety;

    private ObservableList<Predmet> dataset = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listViewPredmety.setItems(dataset);
    }

    public void setDataSet(List<Predmet> predmety) {
        dataset.clear();
        dataset.addAll(predmety);
    }

}
