/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import gui.customcells.MessageListCell;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import model.UzivatelManager;
import model.Zprava;

/**
 * FXML Controller class
 *
 * @author Lukas
 */
public class FXMLConversationController implements Initializable {

    @FXML
    private TextArea tAMessage;

    private Random rand = new Random();
    
    private UzivatelManager uzivManager;
    @FXML
    private ListView<Zprava> listView;
    
    private ObservableList<Zprava> zpravy = FXCollections.observableArrayList();
    @FXML
    private Label lblName;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listView.setItems(zpravy);
        listView.setCellFactory((param) -> {
            return new MessageListCell();
        });
        listView.setSelectionModel(null);
    }

    @FXML
    private void handleBtnOdeslatAction(ActionEvent event) {
        if (tAMessage.getText() != "" && tAMessage.getText().length() > 0) {
            Zprava zprava = new Zprava(1, tAMessage.getText(), LocalDateTime.now(), uzivManager.getCurrentUser().getJmeno());
            tAMessage.clear();
            zpravy.add(zprava);
            listView.scrollTo(zpravy.size()-1);
        }
    }
    
    public void setUzivatelManager(UzivatelManager uzivManager){
        this.uzivManager = uzivManager;
    }

    public void generateTexts() {
        zpravy.clear();
        int r = rand.nextInt((30 - 10 + 1) + 10);

        for (int i = 0; i < r; i++) {
            
            Zprava zprava = new Zprava(i, "Message", LocalDateTime.now(), "Test");
            
            zpravy.add(zprava);
        }
        listView.scrollTo(zpravy.size()-1);
    }

}
