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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
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
    private Button btnEditovat;
    @FXML
    private Button btnZablokovat;
    @FXML
    private Button btnSmazat;
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
    private Consumer<Prispevek> btnEditovatAction;
    private Consumer<Prispevek> btnZablokovatAction;
    private Consumer<Prispevek> btnSmazatAction;

    private boolean isAdmin;
    private boolean canEdit;

    public PostListCell(Consumer<Prispevek> btnOdeslatAction,
            Uzivatel currentUser, boolean isAdmin,
            Consumer<Prispevek> btnEditovatAction,
            Consumer<Prispevek> btnZablokovatAction,
            Consumer<Prispevek> btnSmazatAction) {
        this.btnOdeslatAction = btnOdeslatAction;
        this.btnEditovatAction = btnEditovatAction;
        this.btnZablokovatAction = btnZablokovatAction;
        this.btnSmazatAction = btnSmazatAction;
        this.currentUser = currentUser;
        this.isAdmin = isAdmin;
        this.canEdit = false;
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

            prispevek = item;

            listViewReplies.setItems(komentare);
            listViewReplies.setCellFactory((param) -> {
                return new PostListCell(this.btnOdeslatAction,
                        currentUser, isAdmin,
                        this.btnEditovatAction,
                        this.btnZablokovatAction, this.btnSmazatAction);
            });
            komentare.clear();
            if (item.getKomentare() != null) {
                komentare.addAll(item.getKomentare());
            }
            lblName.setText(item.getJmenoAutora());
            if (item.getNazev() != null) {
                lblPostName.setText(item.getNazev());
            }

            canEdit = currentUser.getIdUzivatele() == item.getIdAutora();

            btnEditovat.setDisable(!canEdit || item.getBlokace() != 0);

            btnSmazat.setDisable(!canEdit || item.getBlokace() != 0);
            btnZablokovat.setDisable(!isAdmin);

            if (item.getBlokace() != 1) {
                String casOdeslani = item.getCasOdeslani().format(DateTimeFormatter.ISO_DATE_TIME);
                lblTime.setText(casOdeslani);
                taMessage.setText(item.getObsahPrispevku());
                lblName.setText(item.getJmenoAutora());
            }else{
                lblName.setText("Zablokováno");
                lblPostName.setText("Zablokováno");
                lblTime.setText("Zablokováno");
                taMessage.setText("Zablokováno");
            }

            setGraphic(gridPane);
            setPrefHeight(gridPane.getPrefHeight());
        }
    }

    @FXML
    private void handleBtnOdeslatAction(ActionEvent event) {
        if (taReply.getText().length() > 0 && taReply.getText().length() <= 1000) {
            if (btnOdeslatAction != null) {
                Prispevek pr = new Prispevek(taReply.getText(), LocalDateTime.now(), currentUser.getIdUzivatele(), prispevek.getIdPrispevku());
                btnOdeslatAction.accept(pr);
                taReply.clear();
            }
        }else{
            showAlert("Text nesmí být prázdný a může být dlouhý maximálně 100 znaků");
        }
    }

    @FXML
    private void handleBtnEditovatAction(ActionEvent event) {
        if (btnEditovatAction != null) {
            btnEditovatAction.accept(prispevek);
        }
    }

    @FXML
    private void handleBtnZablokovatAction(ActionEvent event) {
        if (btnZablokovatAction != null) {
            btnZablokovatAction.accept(prispevek);
        }
    }

    @FXML
    private void handleBtnSmazatAction(ActionEvent event) {
        if (btnSmazatAction != null) {
            btnSmazatAction.accept(prispevek);
        }
    }
    
    /**
     * Zobrazí dialog s errorem
     *
     * @param text text erroru
     */
    private void showAlert(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(text);
        alert.setHeaderText(null);

        alert.showAndWait();
    }

}
