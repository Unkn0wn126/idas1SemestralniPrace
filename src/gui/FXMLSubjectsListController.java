/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import gui.customcells.SubjectListCell;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import model.Predmet;

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

    private Consumer<Predmet> showSubjectDetailAction;
    private Consumer<Predmet> deleteSubjectAction;

    private SimpleBooleanProperty giveAdminPermissions = new SimpleBooleanProperty();

    private ContextMenu contextMenuSubject;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        giveAdminPermissions.setValue(false);
        contextMenuSubject = addContextMenu();

        listViewPredmety.setItems(dataset);
        listViewPredmety.setCellFactory((param) -> {
            return new SubjectListCell(contextMenuSubject);
        });

        giveAdminPermissions.addListener((observable, oldValue, newValue) -> {
            if (newValue && contextMenuSubject.getItems().size() < 2) {
                contextMenuSubject.getItems().add(createMenuDelete());
            } else if (!newValue && contextMenuSubject.getItems().size() > 1) {
                contextMenuSubject.getItems().remove(1);
            }
        });
    }

    public void setDataSet(List<Predmet> predmety, boolean isAdmin) {
        dataset.clear();
        dataset.addAll(predmety);
        
        giveAdminPermissions.setValue(isAdmin);
    }

    private ContextMenu addContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem showDetail = new MenuItem("Zobrazit detail předmětu");
        showDetail.setOnAction((event) -> {
            if (showSubjectDetailAction != null) {
                showSubjectDetailAction.accept(listViewPredmety.getSelectionModel().getSelectedItem());
            }
        });

        contextMenu.getItems().addAll(showDetail);
        return contextMenu;
    }

    public MenuItem createMenuDelete() {
        MenuItem delete = new MenuItem("Odstranit předmět");
        delete.setOnAction((event) -> {
            if (deleteSubjectAction != null) {
                deleteSubjectAction.accept(listViewPredmety.getSelectionModel().getSelectedItem());
            }
        });

        return delete;
    }

    public void setShowSubjectDetailAction(Consumer<Predmet> showSubjectDetailAction) {
        this.showSubjectDetailAction = showSubjectDetailAction;
    }

    public void setDeleteAction(Consumer<Predmet> deleteAction) {
        this.deleteSubjectAction = deleteAction;
    }

}
