/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

/**
 *
 * @author Lukas
 */
public class MessageBox extends FlowPane {

    private String author;
    private String text;
    private Label lbl;
    private Label lblAuthor;

    public MessageBox(String text, String author, Pos position) {
        this.author = author;
        this.text = text;
        this.setMinWidth(0);

        this.setAlignment(position);
        this.setVgap(0);
        lbl = new Label(text);
        lblAuthor = new Label(author);
        lblAuthor.setWrapText(true);

        lbl.setWrapText(true);
        
        lblAuthor.setStyle("-fx-text-fill: #777");
        
        this.widthProperty().addListener((observable, oldValue, newValue) -> {
            lbl.setMaxWidth((Double) newValue / 2);
        });

        this.setMargin(lbl, new Insets(30));
        this.getChildren().addAll(lbl);
        if (position == Pos.CENTER_LEFT) {
            this.getChildren().add(lblAuthor);
            lbl.setStyle("-fx-background-color: #e5e5e5; -fx-padding: 10; -fx-background-radius: 30");
        } else if (position == Pos.CENTER_RIGHT) {
            this.getChildren().add(0, lblAuthor);
            lbl.setStyle("-fx-background-color: #0084ff; -fx-padding: 10; -fx-background-radius: 30; -fx-text-fill: #f4f4f4");
        }

    }
}
