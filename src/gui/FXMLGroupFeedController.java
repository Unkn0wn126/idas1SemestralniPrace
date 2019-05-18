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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import model.Prispevek;

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
    @FXML
    private TextArea taNovyPrispevek;
    @FXML
    private ComboBox<?> cbPriorita;
    @FXML
    private Button btnOdeslat;

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

    public void updateFeed() {
        for (int i = 0; i < 10; i++) {
            List<Prispevek> komentare = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                Prispevek komentar = new Prispevek(i + 10, "Koment", LocalDateTime.now(), "Test koment", 0, "Koment", new ArrayList<Prispevek>());
                komentare.add(komentar);
            }
            
            
            
            Prispevek pr = new Prispevek(i, "Test message", LocalDateTime.now(), "Testovací zpráva", 0, "Test", komentare);
            prispevkyRegular.add(pr);
        }

        for (int i = 0; i < 2; i++) {
            Prispevek komentar = new Prispevek(i + 10, "Koment", LocalDateTime.now(), "Test koment", 0, "Koment", new ArrayList<Prispevek>());
            List<Prispevek> komentare = new ArrayList<>();
            komentare.add(komentar);
            Prispevek pr = new Prispevek(i, "Test message", LocalDateTime.now(), "Testovací zpráva", 0, "Test", komentare);
            prispevkyPinned.add(pr);

        }
    }

}
