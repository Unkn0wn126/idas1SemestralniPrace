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
import model.Skupina;

/**
 *
 * @author Lukas
 */
public class GroupListCell extends ListCell<Skupina>{
        @FXML
    private Label lblName;
    @FXML
    private ImageView imageView;
    @FXML
    private GridPane gridPane;
    
    private FXMLLoader loader;
    
    private Skupina skupina;

    @Override
    protected void updateItem(Skupina item, boolean empty) {
        super.updateItem(item, empty);
        
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        }else{
            if(loader == null){
                loader = new FXMLLoader(getClass().getResource("ListCellGroup.fxml"));
                loader.setController(this);
                
                try {
                    loader.load();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            if (skupina == null) {
                skupina = item;
            }
            
            lblName.setText(skupina.toString());
            
            setGraphic(gridPane);
            setPrefHeight(gridPane.getPrefHeight());
        }
    }
}
