/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.customcells;

import java.util.function.Predicate;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;
import model.Role;

/**
 *
 * @author Lukas
 */
public class PickRoleListCell extends ListCell<Role> {

    @FXML
    private Label lblName;

    @FXML
    private Label lblOpravneni;

    @FXML
    private Label lblPoznamka;
    @FXML
    private CheckBox cbAddRole;
    @FXML
    private GridPane gridPane;

    private FXMLLoader loader;

    private Role role;
    private SimpleBooleanProperty admin;

    public PickRoleListCell(SimpleBooleanProperty admin) {
        this.admin = admin;
    }

    public PickRoleListCell() {
        this.admin = new SimpleBooleanProperty(false);
    }

    @Override
    protected void updateItem(Role item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (loader == null) {
                loader = new FXMLLoader(getClass().getResource("ListPickCellRole.fxml"));
                loader.setController(this);

                try {
                    loader.load();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            role = item;

            cbAddRole.setDisable(false);

            cbAddRole.setSelected(role.isSelected());

            cbAddRole.setDisable(item.getIdRole() == 1 && admin.getValue());

            cbAddRole.selectedProperty().addListener((observable, oldValue, newValue) -> {
                role.setSelected(newValue);
            });

            lblName.setText(role.getJmenoRole());
            lblOpravneni.setText(role.getOpravneni());
            lblPoznamka.setText(role.getPoznamka());

            setGraphic(gridPane);
            setPrefHeight(gridPane.getPrefHeight());
        }
    }
}
