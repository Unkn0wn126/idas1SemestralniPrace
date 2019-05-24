/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.customcells;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;
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
import model.Uzivatel;

/**
 *
 * @author Lukas
 */
public class PostListCell extends ListCell<Prispevek> {

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
    private Uzivatel currentUser;

    private ObservableList<Prispevek> komentare = FXCollections.observableArrayList();
    private Consumer<Prispevek> btnOdeslatAction;

    public PostListCell(Consumer<Prispevek> btnOdeslatAction, Uzivatel currentUser) {
        this.btnOdeslatAction = btnOdeslatAction;
        this.currentUser = currentUser;
    }

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
                return new PostListCell(this.btnOdeslatAction, currentUser);
            });
            komentare.clear();
            if (item.getKomentare() != null) {
                komentare.addAll(item.getKomentare());
            }
            lblName.setText(item.getJmenoAutora());
            if (item.getNazev() != null) {
                lblPostName.setText(item.getNazev());
            }
            String casOdeslani = item.getCasOdeslani().format(DateTimeFormatter.ISO_DATE_TIME);
            lblTime.setText(casOdeslani);
            taMessage.setText(item.getObsahPrispevku());

            setGraphic(gridPane);
            setPrefHeight(gridPane.getPrefHeight());
        }
    }

    @FXML
    private void handleBtnOdeslatAction(ActionEvent event) {
        if (taReply.getText().length() > 0) { // TODO: Přidat kontrolu vstupu
            if (btnOdeslatAction != null) {
                Prispevek pr = new Prispevek(taReply.getText(), LocalDateTime.now(), currentUser.getIdUzivatele(), prispevek.getIdPrispevku());
                btnOdeslatAction.accept(pr);
                taReply.clear();
            }
        }

    }

}
