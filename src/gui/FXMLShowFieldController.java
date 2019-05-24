/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import gui.customcells.GroupListCell;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.beans.property.SimpleBooleanProperty;
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
import model.StudijniObor;
import model.StudijniPlan;

/**
 * FXML Controller class
 *
 * @author Lukas
 */
public class FXMLShowFieldController implements Initializable {

    private ObservableList<StudijniPlan> plany = FXCollections.observableArrayList();

    private Consumer<StudijniPlan> showCurriculumDetailAction;
    private Consumer<StudijniPlan> deleteCurriculumAction;
    private Consumer<StudijniObor> btnUpravitAction;

    private SimpleBooleanProperty giveAdminPermissions = new SimpleBooleanProperty();

    private ContextMenu contextMenuCurriculum;

    private StudijniObor obor;

    @FXML
    private Label lblNazev;
    @FXML
    private Label lblZkratka;
    @FXML
    private Label lblAkreditaceDo;
    @FXML
    private TextArea taPopis;
    @FXML
    private ListView<StudijniPlan> listViewPlany;
    @FXML
    private Button btnUpravit;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        giveAdminPermissions.setValue(false);
        contextMenuCurriculum = addContextMenuCurriculum();
        
        setBtnUpravitUsable(false);

        listViewPlany.setItems(plany);
        listViewPlany.setCellFactory((param) -> {
            return new GroupListCell(contextMenuCurriculum);
        });

        giveAdminPermissions.addListener((observable, oldValue, newValue) -> {
            setBtnUpravitUsable(newValue);

            if (newValue && contextMenuCurriculum.getItems().size() < 2) {
                contextMenuCurriculum.getItems().add(createMenuDelete());
            } else if (!newValue && contextMenuCurriculum.getItems().size() > 1) {
                contextMenuCurriculum.getItems().remove(1);
            }
        });
    }

    @FXML
    private void handleBtnUpravitAction(ActionEvent event) {
        if (btnUpravitAction != null) {
            btnUpravitAction.accept(this.obor);
        }
    }

    public void setDataset(List<StudijniPlan> studijniPlany, StudijniObor obor, boolean isAdmin) {
        this.obor = obor;
        this.plany.clear();
        this.plany.addAll(studijniPlany);
        giveAdminPermissions.setValue(isAdmin);
        updateViews();
    }

    private void updateViews() {
        lblNazev.setText(obor.getNazev());
        lblZkratka.setText(obor.getZkratka());
        lblAkreditaceDo.setText(obor.getAkreditaceDo().format(DateTimeFormatter.ISO_DATE));
        taPopis.setText(obor.getPopis());
    }

    public void setShowCurriculumDetailAction(Consumer<StudijniPlan> showDetailAction) {
        this.showCurriculumDetailAction = showDetailAction;
    }

    public void setDeleteCurriculumAction(Consumer<StudijniPlan> deleteCurriculumAction) {
        this.deleteCurriculumAction = deleteCurriculumAction;
    }

    public void setBtnUpravitAction(Consumer<StudijniObor> btnUpravitAction) {
        this.btnUpravitAction = btnUpravitAction;
    }

    private void setBtnUpravitUsable(boolean allow) {
        btnUpravit.setVisible(allow);
        btnUpravit.setDisable(!allow);
    }

    private ContextMenu addContextMenuCurriculum() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem showDetail = new MenuItem("Zobrazit detail");
        showDetail.setOnAction(event -> {
            if (showCurriculumDetailAction != null) { // TODO: Fix bug of wrong selection
                showCurriculumDetailAction.accept(listViewPlany.getSelectionModel().getSelectedItem());
            }
        });

        contextMenu.getItems().addAll(showDetail);
        return contextMenu;
    }

    private MenuItem createMenuDelete() {
        MenuItem delete = new MenuItem("Odstranit");
        delete.setOnAction((event) -> {
            if (deleteCurriculumAction != null) { // TODO: Fix bug of wrong selection
                deleteCurriculumAction.accept(listViewPlany.getSelectionModel().getSelectedItem());
            }
        });

        return delete;
    }

}
