/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.customcells;

import java.time.format.DateTimeFormatter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import model.Prispevek;

/**
 *
 * @author Lukas
 */
public class PinnedPostListCell extends ListCell<Prispevek> {

    @FXML
    private Label lblPostName;
    @FXML
    private Label lblName;
    @FXML
    private Label lblTime;
    @FXML
    private TextArea taMessage;

    @FXML
    private ImageView imageView;
    @FXML
    private GridPane gridPane;

    private FXMLLoader loader;

    private Prispevek prispevek;

    @Override
    protected void updateItem(Prispevek item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (loader == null) {
                loader = new FXMLLoader(getClass().getResource("ListCellPinnedPost.fxml"));
                loader.setController(this);

                try {
                    loader.load();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (prispevek == null) {
                prispevek = item;
            }
            lblName.setText(item.getJmenoAutora());
            lblPostName.setText(item.getNazev());
            String casOdeslani = item.getCasOdeslani().format(DateTimeFormatter.ISO_DATE_TIME);
            lblTime.setText(casOdeslani);
            taMessage.setText(item.getObsahPrispevku());

            setGraphic(gridPane);
            setPrefHeight(gridPane.getPrefHeight());
        }
    }
}
