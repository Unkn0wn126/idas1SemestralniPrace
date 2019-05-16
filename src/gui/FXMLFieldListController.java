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
import model.StudijniObor;

/**
 * FXML Controller class
 *
 * @author Lukas
 */
public class FXMLFieldListController implements Initializable {

    @FXML
    private Label lblTitle;
    @FXML
    private ListView<StudijniObor> listViewObory;

    private ObservableList<StudijniObor> dataset = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listViewObory.setItems(dataset);
    }
    
    public void setDataset(List<StudijniObor> data){
        dataset.clear();
        dataset.addAll(data);
    }  
    
}
