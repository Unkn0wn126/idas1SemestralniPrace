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
import model.StudijniPlan;

/**
 * FXML Controller class
 *
 * @author Lukas
 */
public class FXMLCurriculumListController implements Initializable {

    @FXML
    private Label lblTitle;
    @FXML
    private ListView<StudijniPlan> listViewCurriculum;
    
    private ObservableList<StudijniPlan> dataset = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listViewCurriculum.setItems(dataset);
    }
    
    public void setDataset(List<StudijniPlan> data){
        dataset.clear();
        dataset.addAll(data);
    }
    
}
