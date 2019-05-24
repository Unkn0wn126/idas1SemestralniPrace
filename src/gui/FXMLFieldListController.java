/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import gui.customcells.FieldListCell;
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
import model.StudijniObor;

/**
 * FXML Controller class
 *
 * @author Lukas
 */
public class FXMLFieldListController implements Initializable {

    private Consumer<StudijniObor> showFieldDetailAction;
    private Consumer<StudijniObor> deleteFieldAction;

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
        listViewObory.setCellFactory((param) -> {
            return new FieldListCell(addContextMenuField());
        });
    }

    public void setDataset(List<StudijniObor> data) {
        dataset.clear();
        dataset.addAll(data);
    }

    public void setShowFieldDetailAction(Consumer<StudijniObor> showFieldDetailAction) {
        this.showFieldDetailAction = showFieldDetailAction;
    }

    public void setDeleteFieldAction(Consumer<StudijniObor> deleteFieldAction) {
        this.deleteFieldAction = deleteFieldAction;
    }

    private ContextMenu addContextMenuField() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem showDetail = new MenuItem("Zobrazit detail");
        showDetail.setOnAction(event -> {
            if (showFieldDetailAction != null) { // TODO: Fix bug of wrong selection
                showFieldDetailAction.accept(listViewObory.getSelectionModel().getSelectedItem());
            }
        });

        MenuItem delete = new MenuItem("Odstranit");
        delete.setOnAction((event) -> {
            if (deleteFieldAction != null) { // TODO: Fix bug of wrong selection
                deleteFieldAction.accept(listViewObory.getSelectionModel().getSelectedItem());
            }
        });
        contextMenu.getItems().addAll(showDetail, delete);
        return contextMenu;
    }

}
