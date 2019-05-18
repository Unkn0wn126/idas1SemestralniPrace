/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import gui.customcells.MessageListCell;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import model.UzivatelManager;
import model.Zprava;
import model.ZpravaManager;

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
    private ZpravaManager zpravaManager;
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
    private void handleBtnOdeslatAction(ActionEvent event) throws SQLException {
        if (tAMessage.getText() != "" && tAMessage.getText().length() > 0) {
            Zprava zprava = new Zprava(1, tAMessage.getText(), LocalDateTime.now(), uzivManager.getCurrentUser().getJmeno());
            tAMessage.clear();
            zpravy.add(zprava);
            if (zpravaManager != null) {
                zpravaManager.insertZprava("Název", zprava.getObsahZpravy(), zprava.getAutor());
            }
            listView.scrollTo(zpravy.size()-1);
        }
    }
    
    public void setUzivatelManager(UzivatelManager uzivManager){
        this.uzivManager = uzivManager;
    }
    
    public void setZpravaManager(ZpravaManager zpravaManager){
        this.zpravaManager = zpravaManager;
    }
    
    public void updateMessages(){ // TODO: napojit na databázi tak, aby se zobrazovaly zprávy s daným kontaktem
        zpravy.clear();
        if (zpravaManager != null) {
            try {
                List<Zprava> zpr = zpravaManager.selectZpravy();
                zpravy.addAll(zpr);
            } catch (SQLException ex) {
                Logger.getLogger(FXMLConversationController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        listView.scrollTo(zpravy.size()-1);
    }

}
