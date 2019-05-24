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
import model.Predmet;

/**
 *
 * @author Lukas
 */
public class SubjectListCell extends ListCell<Predmet> {

    @FXML
    private Label lblName;

    @FXML
    private Label lblZkratka;
    @FXML
    private ImageView imageView;
    @FXML
    private GridPane gridPane;

    private FXMLLoader loader;

    private Predmet predmet;

    private ContextMenu contextMenu;

    public SubjectListCell(ContextMenu contextMenu) {
        this.contextMenu = contextMenu;
        this.setContextMenu(contextMenu);
    }

    @Override
    protected void updateItem(Predmet item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (loader == null) {
                loader = new FXMLLoader(getClass().getResource("ListCellSubject.fxml"));
                loader.setController(this);

                try {
                    loader.load();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            predmet = item;

            lblName.setText(predmet.getNazevPredmetu());
            lblZkratka.setText(predmet.getZkratkaPredmetu());

            setGraphic(gridPane);
            setPrefHeight(gridPane.getPrefHeight());
        }
    }
}
