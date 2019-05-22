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
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
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
    
    private Consumer<Prispevek> btnOdeslatAction;
    
    private Uzivatel currentUser;
    
    @FXML
    private TextArea taNovyPrispevek;
    @FXML
    private ComboBox<Integer> cbPriorita;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listViewPrispevkyRegular.setItems(prispevkyRegular);

        listViewPrispevkyRegular.setCellFactory((param) -> {
            return new PostListCell();
        });

        listViewPrispevkyPinned.setItems(prispevkyPinned);

        listViewPrispevkyPinned.setCellFactory((param) -> {
            return new PinnedPostListCell();
        });
    }

    public void updateFeed(List<Prispevek> prispevky, Uzivatel currentUser) {
        this.currentUser = currentUser;
        prispevkyRegular.clear();
        prispevkyRegular.addAll(prispevky);

        prispevkyPinned.clear();
        
        List<Prispevek> pinned = prispevky.stream().filter((t) -> {
            return t.getPriorita() > 0;
        }).collect(Collectors.toList());
        
        pinned.sort((t, t1) -> {
            return (t.getPriorita() < t1.getPriorita())? 1:-1;
        });
        
        prispevkyPinned.addAll(pinned);
    }

    public void setBtnOdeslatAction(Consumer<Prispevek> btnOdeslatAction) {
        this.btnOdeslatAction = btnOdeslatAction;
    }

    @FXML
    private void handleBtnOdeslatAction(ActionEvent event) {
        if (btnOdeslatAction != null) {
            if (!taNovyPrispevek.getText().isEmpty() && taNovyPrispevek.getText().length() > 0) { // TODO: Přidat kontrolu na délku vstupu
                String jmeno = currentUser.getJmeno() + " " + currentUser.getPrijmeni();
                Prispevek prispevek = new Prispevek(taNovyPrispevek.getText(), LocalDateTime.now(), 0, 0, currentUser.getIdUzivatele(), "Název", jmeno);
                btnOdeslatAction.accept(prispevek);
                taNovyPrispevek.clear();
            }
            
        }
    }

}
