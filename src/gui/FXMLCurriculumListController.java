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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
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
    private Consumer<StudijniPlan> showDetailAction;

    private ObservableList<StudijniPlan> dataset = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listViewCurriculum.setItems(dataset);
        listViewCurriculum.setCellFactory(lv -> {
            ListCell<StudijniPlan> cell = new ListCell<>();

            ContextMenu contextMenu = new ContextMenu();

            MenuItem showDetail = new MenuItem("Zobrazit detail");
            showDetail.setOnAction(event -> {
                if (showDetailAction != null) {
                    showDetailAction.accept(listViewCurriculum.getSelectionModel().getSelectedItem());
                }
            });
            
            MenuItem delete = new MenuItem("Odstranit");
            contextMenu.getItems().addAll(showDetail, delete);

            cell.textProperty().bind(cell.itemProperty().asString());

            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                } else {
                    cell.setContextMenu(contextMenu);
                }
            });
            return cell;
        });
    }

    public void setDataset(List<StudijniPlan> data) {
        dataset.clear();
        dataset.addAll(data);
    }

    public void setShowDetailAction(Consumer<StudijniPlan> showDetailAction) {
        this.showDetailAction = showDetailAction;
    }

}
