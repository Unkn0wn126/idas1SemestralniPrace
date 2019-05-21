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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import model.KontaktVypis;
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
    private List<KontaktVypis> prijemci;
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
                List<Integer> idKontaktu = new ArrayList<>();
                for (KontaktVypis kontaktVypis : prijemci) {
                    idKontaktu.add(kontaktVypis.getIdKontaktu());
                }
                zpravaManager.poslatZpravu(zprava.getObsahZpravy(), uzivManager.getCurrentUser().getIdUzivatele(), idKontaktu);

            }
            listView.scrollTo(zpravy.size() - 1);
        }
    }

    public void setUzivatelManager(UzivatelManager uzivManager) {
        this.uzivManager = uzivManager;
    }

    public void setZpravaManager(ZpravaManager zpravaManager) {
        this.zpravaManager = zpravaManager;
    }

    public void updateMessages(List<KontaktVypis> prijemci) {
        zpravy.clear();
        this.prijemci = prijemci;
        List<Zprava> intermed = new ArrayList<>();
        if (zpravaManager != null) {
            try {
                List<Integer> idKontaktu = new ArrayList<>();
                for (KontaktVypis kontaktVypis : prijemci) {
                    idKontaktu.add(kontaktVypis.getIdKontaktu());
                }
                
                List<Zprava> zpr = zpravaManager.selectZpravyVybranychKontaktu(idKontaktu, uzivManager.getCurrentUser().getIdUzivatele());
                    intermed.addAll(zpr);
                
                Set<Integer> idSet = new HashSet<>();
                List<Zprava> a = intermed.stream().filter((t) -> idSet.add(t.getIdZpravy())).collect(Collectors.toList());
                
                a.sort((t, t1) -> {
                    return (t.getCasOdeslani().compareTo(t1.getCasOdeslani()));
                });
                
                zpravy.addAll(a);

            } catch (SQLException ex) {
                Logger.getLogger(FXMLConversationController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        listView.scrollTo(zpravy.size() - 1);
    }

}
