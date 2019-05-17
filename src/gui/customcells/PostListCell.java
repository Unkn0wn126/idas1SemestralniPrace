/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.customcells;

import java.time.format.DateTimeFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import model.Prispevek;
import model.Zprava;

/**
 *
 * @author Lukas
 */
public class PostListCell extends ListCell<Prispevek>{
        @FXML
    private Label lblPostName;
    @FXML
    private Label lblName;
    @FXML
    private Label lblTime;
    @FXML
    private TextArea taMessage;
    @FXML
    private TextArea taReply;
    @FXML
    private ListView listViewReplies;
    
    @FXML
    private ImageView imageView;
    @FXML
    private GridPane gridPane;

    private FXMLLoader loader;

    private Prispevek prispevek;
    
    private ObservableList<Prispevek> komentare = FXCollections.observableArrayList();

    @Override
    protected void updateItem(Prispevek item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (loader == null) {
                loader = new FXMLLoader(getClass().getResource("ListCellPost.fxml"));
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
            listViewReplies.setItems(komentare);
            listViewReplies.setCellFactory((param) -> {
                return new PostListCell();
            });
            komentare.clear();
            komentare.addAll(item.getKomentare());
            lblName.setText("Place Holder");
            lblPostName.setText(item.getNazev());
            String casOdeslani = item.getCasOdeslani().format(DateTimeFormatter.ISO_DATE_TIME);
            lblTime.setText(casOdeslani);
            taMessage.setText(item.getObsahPrispevku());

            setGraphic(gridPane);
            setPrefHeight(gridPane.getPrefHeight());
        }
    }
    
    @FXML
    private void handleBtnOdeslatAction(ActionEvent event){
        System.out.println("Click!");
    }
}