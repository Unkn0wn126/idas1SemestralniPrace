/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import gui.customcells.GroupListCell;
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
    private Consumer<StudijniPlan> showCurriculumDetailAction;
    private Consumer<StudijniPlan> deleteCurriculumAction;

    private ObservableList<StudijniPlan> dataset = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listViewCurriculum.setItems(dataset);
        listViewCurriculum.setCellFactory(lv -> {
            return new GroupListCell(addContextMenuCurriculum());
        });
    }

    public void setDataset(List<StudijniPlan> data) {
        dataset.clear();
        dataset.addAll(data);
    }

    public void setShowCurriculumDetailAction(Consumer<StudijniPlan> showDetailAction) {
        this.showCurriculumDetailAction = showDetailAction;
    }

    public void setDeleteCurriculumAction(Consumer<StudijniPlan> deleteCurriculumAction) {
        this.deleteCurriculumAction = deleteCurriculumAction;
    }

    private ContextMenu addContextMenuCurriculum() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem showDetail = new MenuItem("Zobrazit detail");
        showDetail.setOnAction(event -> {
            if (showCurriculumDetailAction != null) { // TODO: Fix bug of wrong selection
                showCurriculumDetailAction.accept(listViewCurriculum.getSelectionModel().getSelectedItem());
            }
        });

        MenuItem delete = new MenuItem("Odstranit");
        delete.setOnAction((event) -> {
            if (deleteCurriculumAction != null) { // TODO: Fix bug of wrong selection
                deleteCurriculumAction.accept(listViewCurriculum.getSelectionModel().getSelectedItem());
            }
        });
        contextMenu.getItems().addAll(showDetail, delete);
        return contextMenu;
    }

}
