/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

/**
 *
 * @author Lukas
 */
public class PostBox extends FlowPane{
    private Label lbl;
    private Label lblAuthor;
    private String text;
    private String author;

    public PostBox(String text, String author) {
        lbl = new Label(text);
        lblAuthor = new Label(author);
        this.author = author;
        this.text = text;
        this.setAlignment(Pos.CENTER);
        lblAuthor.setStyle("-fx-font-fill: blue");
        
        this.getChildren().addAll(lblAuthor, lbl);
    }
    
}
