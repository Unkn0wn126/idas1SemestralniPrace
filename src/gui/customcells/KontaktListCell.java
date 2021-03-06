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
import javafx.scene.shape.Circle;
import model.KontaktVypis;

/**
 *
 * @author Lukas
 */
public class KontaktListCell extends ListCell<KontaktVypis> {

    @FXML
    private Label lblName;
    @FXML
    private Label lblSurname;
    @FXML
    private ImageView imageView;
    @FXML
    private Circle circleOnline;
    @FXML
    private GridPane gridPane;

    private FXMLLoader loader;

    private KontaktVypis kontaktVypis;

    public KontaktListCell(ContextMenu contextMenu) {
        super();

        this.setContextMenu(contextMenu);
    }

    @Override
    protected void updateItem(KontaktVypis item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (loader == null) {
                loader = new FXMLLoader(getClass().getResource("ListCellKontakt.fxml"));
                loader.setController(this);

                try {
                    loader.load();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            kontaktVypis = item;

            setOnlineCircleColor();

            lblName.setText(item.getJmeno());
            lblSurname.setText(item.getPrijmeni());

            setGraphic(gridPane);
            setPrefHeight(gridPane.getPrefHeight());
        }
    }

    /**
     * Získá instanci uživatele této buňky
     *
     * @return uživatel
     */
    public KontaktVypis getUzivatel() {
        return this.kontaktVypis;
    }

    /**
     * Nastaví barvu indikátoru online stavu Pokud je daný uživatel offline -
     * červená Pokud je daný uživatel online - zelená
     */
    private void setOnlineCircleColor() {
        if (kontaktVypis.getBlokace() == 0) {
            if (kontaktVypis.getPrihlasen() == 0) {
                circleOnline.setFill(Color.RED);
            } else {
                circleOnline.setFill(Color.GREENYELLOW);
            }
        } else {
            circleOnline.setFill(Color.BLACK);
        }

    }

}
