/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.customcells;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import model.StudijniPlan;

/**
 *
 * @author Lukas
 */
public class PickCurriculumListCell extends ListCell<StudijniPlan> {

    @FXML
    private Label lblName;
    @FXML
    private CheckBox cbAddCurriculum;
    @FXML
    private GridPane gridPane;

    private FXMLLoader loader;

    private StudijniPlan skupina;

    @Override
    protected void updateItem(StudijniPlan item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (loader == null) {
                loader = new FXMLLoader(getClass().getResource("ListPickCellCurriculum.fxml"));
                loader.setController(this);

                try {
                    loader.load();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            skupina = item;

            lblName.setText(skupina.getNazev());

            setGraphic(gridPane);
            setPrefHeight(gridPane.getPrefHeight());
        }
    }
}
