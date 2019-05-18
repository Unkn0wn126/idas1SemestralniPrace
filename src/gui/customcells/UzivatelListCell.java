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
import javafx.scene.paint.Color;
import model.Uzivatel;

/**
 *
 * @author Lukas
 */
public class UzivatelListCell extends ListCell<Uzivatel> {

    @FXML
    private Label lblName;
    @FXML
    private Label lblSurname;
    @FXML
    private Label lblRocnik;
    @FXML
    private Label lblBan;
    @FXML
    private Label lblRole;
    @FXML
    private ImageView imageView;
    @FXML
    private GridPane gridPane;

    private FXMLLoader loader;

    private Uzivatel uzivatel;

    private ContextMenu contextMenu;

    public UzivatelListCell(ContextMenu contextMenu) {
        super();

        this.contextMenu = contextMenu;
        this.setContextMenu(contextMenu);
    }

    @Override
    protected void updateItem(Uzivatel item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (loader == null) {
                loader = new FXMLLoader(getClass().getResource("ListCellUzivatel.fxml"));
                loader.setController(this);

                try {
                    loader.load();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (uzivatel == null) {
                uzivatel = item;
            }

            lblName.setText(item.getJmeno());
            lblSurname.setText(item.getPrijmeni());
            lblRocnik.setText(Integer.toString(item.getRokStudia()));
            lblBan.setText("0"); // TODO: napojit na skutečná data
            lblRole.setText("Uzivatel"); // TODO: napojit na skutečná data
            setGraphic(gridPane);
            setPrefHeight(gridPane.getPrefHeight());
        }
    }

    /**
     * Získá instanci uživatele této buňky
     * @return uživatel
     */
    public Uzivatel getUzivatel() {
        return this.uzivatel;
    }

}
