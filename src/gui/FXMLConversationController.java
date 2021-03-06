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
            showAlert("Zpr??va nesm?? b??t pr??zdn?? a nesm?? b??t del???? ne?? 500 znak??");
        }
    }

    /**
     * Nastav??, co se m?? prov??st p??i odesl??n?? zpr??vy
     *
     * @param poslatZpravuAction akce, kter?? se m?? prov??st
     */
    public void setPoslatZpravuAction(Consumer<Zprava> poslatZpravuAction) {
        this.poslatZpravuAction = poslatZpravuAction;
    }

    /**
     * Nastav?? zpr??vy pro zobrazen??
     *
     * @param zprava seznam zpr??v pro zobrazen??
     * @param localUzivatel lok??ln?? u??ivatel
     * @param prijemci seznam p????jemc?? zpr??vy
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
     * Vr??t?? seznam p????jemc?? zpr??vy
     *
     * @return seznam p????jemc??
     */
    public List<KontaktVypis> getPrijemci() {
        return prijemci;
    }

    /**
     * Updatuje popisek zobrazuj??c?? jm??na u??ivatel??, ke kter??m konverzace pat????
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
     * Zobraz?? dialog s errorem
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
