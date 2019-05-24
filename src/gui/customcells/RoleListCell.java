/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.customcells;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import model.Role;

/**
 *
 * @author Lukas
 */
public class RoleListCell extends ListCell<Role> {

    @FXML
    private Label lblName;

    @FXML
    private Label lblOpravneni;
    
    @FXML
    private Label lblPoznamka;
    @FXML
    private ImageView imageView;
    @FXML
    private GridPane gridPane;

    private FXMLLoader loader;

    private Role role;

    @Override
    protected void updateItem(Role item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (loader == null) {
                loader = new FXMLLoader(getClass().getResource("ListCellRole.fxml"));
                loader.setController(this);

                try {
                    loader.load();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            role = item;

            lblName.setText(role.getJmenoRole());
            lblOpravneni.setText(role.getOpravneni());
            lblPoznamka.setText(role.getPoznamka());

            setGraphic(gridPane);
            setPrefHeight(gridPane.getPrefHeight());
        }
    }
}
