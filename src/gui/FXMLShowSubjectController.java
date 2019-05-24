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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import model.Predmet;
import model.StudijniPlan;

/**
 * FXML Controller class
 *
 * @author Lukas
 */
public class FXMLShowSubjectController implements Initializable {

    private ObservableList<StudijniPlan> plany = FXCollections.observableArrayList();
    private Consumer<StudijniPlan> showCurriculumDetailAction;
    private Consumer<StudijniPlan> deleteCurriculumAction;
    private Consumer<Predmet> btnUpravitAction;
    
    private Predmet currPredmet;

    @FXML
    private Label lblZkratka;
    @FXML
    private Label lblNazev;
    @FXML
    private TextArea tfPopis;
    @FXML
    private ListView<StudijniPlan> listViewPlany;
    @FXML
    private Button btnUpravit;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listViewPlany.setItems(plany);
        listViewPlany.setCellFactory((param) -> {
            return new GroupListCell(addContextMenuCurriculum());
        });
    }

    @FXML
    private void handleBtnUpravitAction(ActionEvent event) {
        if (btnUpravitAction != null) {
            btnUpravitAction.accept(currPredmet);
        }
    }

    public void setShowCurriculumDetailAction(Consumer<StudijniPlan> showDetailAction) {
        this.showCurriculumDetailAction = showDetailAction;
    }

    public void setDeleteCurriculumAction(Consumer<StudijniPlan> deleteCurriculumAction) {
        this.deleteCurriculumAction = deleteCurriculumAction;
    }

    public void setBtnUpravitAction(Consumer<Predmet> btnUpravitAction) {
        this.btnUpravitAction = btnUpravitAction;
    }

    private ContextMenu addContextMenuCurriculum() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem showDetail = new MenuItem("Zobrazit detail");
        showDetail.setOnAction(event -> {
            if (showCurriculumDetailAction != null) { // TODO: Fix bug of wrong selection
                showCurriculumDetailAction.accept(listViewPlany.getSelectionModel().getSelectedItem());
            }
        });

        MenuItem delete = new MenuItem("Odstranit studijní plán");
        delete.setOnAction((event) -> {
            if (deleteCurriculumAction != null) { // TODO: Fix bug of wrong selection
                deleteCurriculumAction.accept(listViewPlany.getSelectionModel().getSelectedItem());
            }
        });
        contextMenu.getItems().addAll(showDetail, delete);
        return contextMenu;
    }
    
    public void setDataset(List<StudijniPlan> plany, Predmet currPredmet){
        this.currPredmet = currPredmet;
        this.plany.clear();
        this.plany.addAll(plany);
        updateViews();
    }
    
    private void updateViews(){
        lblNazev.setText(currPredmet.getNazevPredmetu());
        lblZkratka.setText(currPredmet.getZkratkaPredmetu());
        tfPopis.setText(currPredmet.getPopis());
    }

}
