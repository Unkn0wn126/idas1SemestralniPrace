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
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import model.KontaktVypis;
import model.Predmet;
import model.Uzivatel;
import model.Zprava;

/**
 * FXML Controller class
 *
 * @author Lukas
 */
public class FXMLConversationController implements Initializable {

    // Zpravy variables start
    private ObservableList<Zprava> zpravy = FXCollections.observableArrayList();
    private Consumer<Zprava> poslatZpravuAction;
    // Zpravy variables end

    // Jmena variables start
    private String jmenoLocalUzivatele;
    private List<KontaktVypis> prijemci = new ArrayList<>();
    // Jmena variables end

    private int idLokalnihoUzivatele;

    @FXML
    private TextArea tAMessage;

    @FXML
    private ListView<Zprava> listView;

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

        listView.addEventFilter(MouseEvent.MOUSE_PRESSED, (event) -> {
            event.consume();
        });
    }

    @FXML
    private void handleBtnOdeslatAction(ActionEvent event) throws SQLException {
        if (!tAMessage.getText().isEmpty() && tAMessage.getText().length() > 0 && tAMessage.getLength() <= 500) {
            if (poslatZpravuAction != null) {
                poslatZpravuAction.accept(new Zprava(tAMessage.getText(),
                        LocalDateTime.now(), jmenoLocalUzivatele));
            }
            tAMessage.clear();
            listView.scrollTo(zpravy.size() - 1);
        } else {
            showAlert("Zpráva nesmí být prázdná a nesmí být delší než 500 znaků");
        }
    }

    /**
     * Nastaví, co se má provést při odeslání zprávy
     *
     * @param poslatZpravuAction akce, která se má provést
     */
    public void setPoslatZpravuAction(Consumer<Zprava> poslatZpravuAction) {
        this.poslatZpravuAction = poslatZpravuAction;
    }

    /**
     * Nastaví zprávy pro zobrazení
     *
     * @param zprava seznam zpráv pro zobrazení
     * @param localUzivatel lokální uživatel
     * @param prijemci seznam příjemců zprávy
     */
    public void updateMessages(List<Zprava> zprava, Uzivatel localUzivatel, List<KontaktVypis> prijemci) {
        this.jmenoLocalUzivatele = localUzivatel.getJmeno();
        this.idLokalnihoUzivatele = localUzivatel.getIdUzivatele();

        for (Zprava zpr : zprava) {
            zpr.setMessageIsFromHere(zpr.getIdAutora() == idLokalnihoUzivatele);
        }

        zpravy.clear();
        zpravy.addAll(zprava);
        this.prijemci.clear();
        this.prijemci.addAll(prijemci);
        updateLabelNames();

        listView.scrollTo(zpravy.size() - 1);
    }

    /**
     * Vrátí seznam příjemců zprávy
     *
     * @return seznam příjemců
     */
    public List<KontaktVypis> getPrijemci() {
        return prijemci;
    }

    /**
     * Updatuje popisek zobrazující jména uživatelů, ke kterým konverzace patří
     */
    private void updateLabelNames() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < prijemci.size() - 1; i++) {
            builder.append(prijemci.get(i).getJmeno());
            builder.append(", ");

        }

        builder.append(prijemci.get(prijemci.size() - 1).getJmeno());

        lblName.setText(builder.toString());
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
