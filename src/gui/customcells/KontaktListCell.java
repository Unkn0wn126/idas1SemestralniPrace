/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.customcells;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.Uzivatel;

/**
 *
 * @author Lukas
 */
public class KontaktListCell extends ListCell<Uzivatel>{
    @FXML
    private Label lblName;
    @FXML
    private Label lblSurname;
    @FXML
    private ImageView imageView;
    @FXML
    private Circle circleOnline;
    @FXML
    private GridPane gridPane;
    
    private FXMLLoader loader;
    
    private Uzivatel uzivatel;

    @Override
    protected void updateItem(Uzivatel item, boolean empty) {
        super.updateItem(item, empty);
        
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        }else{
            if(loader == null){
                loader = new FXMLLoader(getClass().getResource("ListCellKontakt.fxml"));
                loader.setController(this);
                
                try {
                    loader.load();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            if (uzivatel == null) {
                uzivatel = item;
            }
            
            setOnlineCircleColor();
            
            lblName.setText(item.getJmeno());
            lblSurname.setText(item.getPrijmeni());
            
            setGraphic(gridPane);
            setPrefHeight(gridPane.getPrefHeight());
        }
    }
    
    public Uzivatel getUzivatel(){
        return this.uzivatel;
    }
    
    private void setOnlineCircleColor(){
        if (uzivatel.isOnline() == 0) {
            circleOnline.setFill(Color.RED);
        }else{
            circleOnline.setFill(Color.GREENYELLOW);
        }
    }
    
    
}
