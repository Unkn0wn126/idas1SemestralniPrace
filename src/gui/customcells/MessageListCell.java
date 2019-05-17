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
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import model.Zprava;

/**
 *
 * @author Lukas
 */
public class MessageListCell extends ListCell<Zprava> {

    @FXML
    private Label lblJmeno;
    @FXML
    private Label lblTime;
    @FXML
    private Label lblText;
    @FXML
    private ImageView imageView;
    @FXML
    private GridPane gridPane;

    private FXMLLoader loader;

    private Zprava zprava;

    @Override
    protected void updateItem(Zprava item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (loader == null) {
                loader = new FXMLLoader(getClass().getResource("ListCellMessage.fxml"));
                loader.setController(this);

                try {
                    loader.load();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (zprava == null) {
                zprava = item;
            }

            lblJmeno.setText(item.getAutor());
            lblText.setText(item.getObsahZpravy());
            String casOdeslani = item.getCasOdeslani().format(DateTimeFormatter.ISO_DATE_TIME);
            lblTime.setText(casOdeslani);

            setGraphic(gridPane);
            setPrefHeight(gridPane.getPrefHeight());
        }
    }
}
