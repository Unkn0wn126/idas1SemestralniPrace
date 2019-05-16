/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import model.UzivatelManager;

/**
 * FXML Controller class
 *
 * @author Lukas
 */
public class FXMLConversationController implements Initializable {

    @FXML
    private TextArea tAMessage;

    private Random rand = new Random();
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox vBox;
    
    private UzivatelManager uzivManager;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        scrollPane.widthProperty().addListener((observable) -> {
            vBox.setPrefWidth(scrollPane.getWidth());
        });
        
        scrollPane.heightProperty().addListener((observable) -> {
            vBox.setPrefWidth(scrollPane.getWidth());
        });
        
        vBox.heightProperty().addListener((observable, oldValue, newValue) -> {
            scrollPane.setVvalue((Double) newValue);
        });
    }

    @FXML
    private void handleBtnOdeslatAction(ActionEvent event) {
        if (tAMessage.getText() != "" && tAMessage.getText().length() > 0) {
            MessageBox msg = new MessageBox(tAMessage.getText(), uzivManager.getCurrentUser().getJmeno(), Pos.CENTER_RIGHT);
            tAMessage.clear();

            vBox.getChildren().add(msg);           
            
        }
    }
    
    public void setUzivatelManager(UzivatelManager uzivManager){
        this.uzivManager = uzivManager;
    }

    public void generateTexts() {

        vBox.getChildren().remove(0, vBox.getChildren().size());
        int r = rand.nextInt((30 - 10 + 1) + 10);

        for (int i = 0; i < r; i++) {
            int side = rand.nextInt(10);
            Pos position = (side >= 5) ? Pos.CENTER_LEFT : Pos.CENTER_RIGHT;
            MessageBox msg = new MessageBox("Message", "Test", position);

            vBox.getChildren().add(msg);

        }
    }

}
