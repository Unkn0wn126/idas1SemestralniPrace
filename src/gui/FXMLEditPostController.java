/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbPriorita.setItems(priority);
    }


public void updateDataSet(Prispevek currPrispevek, List<Integer> priority, int currPriority){
    tfNazev.setText(currPrispevek.getNazev());
    tfObsah.setText(currPrispevek.getObsahPrispevku());
    this.priority.clear();
    this.priority.addAll(priority);
    List<Integer> selectPriority = this.priority.stream().filter((t) -> {
        return t == currPriority;
    }).collect(Collectors.toList());
    
    cbPriorita.getSelectionModel().select(selectPriority.get(0));
}    

    @FXML
    private void handleBtnUlozitAction(ActionEvent event) {
    }

    @FXML
    private void handleBtnZrusitAction(ActionEvent event) {
    }
    
}
