/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import gui.customcells.PinnedPostListCell;
import gui.customcells.PostListCell;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Prispevek;
import model.Uzivatel;

/**
 * FXML Controller class
 *
 * @author Lukas
 */
public class FXMLGroupFeedController implements Initializable {

    @FXML
    private ListView<Prispevek> listViewPrispevkyRegular;
    @FXML
    private ListView<Prispevek> listViewPrispevkyPinned;

    private ObservableList<Prispevek> prispevkyRegular = FXCollections.observableArrayList();
    private ObservableList<Prispevek> prispevkyPinned = FXCollections.observableArrayList();
    private ObservableList<Integer> priority = FXCollections.observableArrayList();

    private Consumer<Prispevek> btnOdeslatAction;
    private Consumer<Prispevek> komentarBtnOdeslatAction;
    private Consumer<Prispevek> editPostAction;
    private Consumer<Prispevek> deletePostAction;
    private Consumer<Prispevek> blockPostAction;
    
    private boolean isAdmin;

    private Uzivatel currentUser;

    @FXML
    private TextArea taNovyPrispevek;
    @FXML
    private ComboBox<Integer> cbPriorita;
    @FXML
    private TextField tfNazev;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbPriorita.setItems(priority);
        isAdmin = false;

        listViewPrispevkyRegular.setItems(prispevkyRegular);

        listViewPrispevkyRegular.setCellFactory((param) -> {
            return new PostListCell(komentarBtnOdeslatAction, currentUser, isAdmin, editPostAction, blockPostAction, deletePostAction);
        });

        listViewPrispevkyPinned.setItems(prispevkyPinned);

        listViewPrispevkyPinned.setCellFactory((param) -> {
            return new PinnedPostListCell();
        });
    }

    public void updateFeed(List<Prispevek> prispevky, Uzivatel currentUser, boolean isAdmin) {
        this.currentUser = currentUser;
        prispevkyRegular.clear();
        prispevkyRegular.addAll(prispevky);
        this.isAdmin = isAdmin;

        prispevkyPinned.clear();

        List<Prispevek> pinned = prispevky.stream().filter((t) -> {
            return t.getPriorita() > 0 && t.getBlokace() == 0;
        }).collect(Collectors.toList());

        pinned.sort((t, t1) -> {
            return (t.getPriorita() < t1.getPriorita()) ? 1 : -1;
        });

        prispevkyPinned.addAll(pinned);
    }

    public void setEditPostAction(Consumer<Prispevek> editPostAction) {
        this.editPostAction = editPostAction;
    }

    public void setDeletePostAction(Consumer<Prispevek> deletePostAction) {
        this.deletePostAction = deletePostAction;
    }

    public void setBlockPostAction(Consumer<Prispevek> blockPostAction) {
        this.blockPostAction = blockPostAction;
    }

    public void updatePriorities(List<Integer> priority) {
        this.priority.clear();
        this.priority.addAll(priority);
        cbPriorita.getSelectionModel().selectFirst();
    }

    public void setBtnOdeslatAction(Consumer<Prispevek> btnOdeslatAction) {
        this.btnOdeslatAction = btnOdeslatAction;
    }

    public void setKomentarBtnOdeslatAction(Consumer<Prispevek> komentarBtnOdeslatAction) {
        this.komentarBtnOdeslatAction = komentarBtnOdeslatAction;
    }

    public boolean inputIsOk() {
        return textInputIsOk() && nameInputIsOk() && priorityIsOk();
    }

    private boolean textInputIsOk() {
        return !taNovyPrispevek.getText().isEmpty()
                && taNovyPrispevek.getText().length() > 0
                && taNovyPrispevek.getText().length() <= 1000;
    }

    private boolean nameInputIsOk() {
        return !tfNazev.getText().isEmpty()
                && tfNazev.getText().length() > 0
                && tfNazev.getText().length() <= 50;
    }

    private boolean priorityIsOk() {
        return cbPriorita.getSelectionModel().getSelectedItem() != null;
    }

    @FXML
    private void handleBtnOdeslatAction(ActionEvent event) {
        if (btnOdeslatAction != null) {
            String jmeno = currentUser.getJmeno() + " " + currentUser.getPrijmeni();
            Prispevek prispevek = new Prispevek(taNovyPrispevek.getText(),
                    LocalDateTime.now(), 0, cbPriorita.getValue(),
                    currentUser.getIdUzivatele(), tfNazev.getText(), jmeno);

            btnOdeslatAction.accept(prispevek);

            if (inputIsOk()) {
                taNovyPrispevek.clear();
                tfNazev.clear();
                cbPriorita.getSelectionModel().selectFirst();
            }
        }
    }

}
