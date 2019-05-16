/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.util.function.Consumer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import model.Uzivatel;

/**
 *
 * @author Lukas
 */
public class GroupView extends FlowPane {

    private Consumer<MouseEvent> leftMouseAction;
    private Consumer<MouseEvent> rightMouseAction;
    private final String firstName;
    private final String lastName;
    private final Label lbl;
    private final ContextMenu contextMenu;

    public GroupView(String jmeno, String prijmeni, ContextMenu contextMenu) {
        this.firstName = jmeno;
        this.lastName = prijmeni;
        this.contextMenu = contextMenu;

        this.setMinHeight(100);
        this.setPadding(new Insets(20));

        this.setAlignment(Pos.CENTER_LEFT);

        lbl = new Label(firstName + " " + lastName);
        this.getChildren().add(lbl);
        this.setStyle("-fx-background-color: #f4f4f4");

        this.setOnMousePressed((event) -> {
            switch (event.getButton()) {
                case PRIMARY:
                    if (this.leftMouseAction != null) {
                        this.leftMouseAction.accept(event);
                    }
                    break;
                case SECONDARY:
                    if(this.rightMouseAction != null){
                        this.rightMouseAction.accept(event);
                    }
            }

        });
    }
    
    public void showContextMenu(MouseEvent event){
        this.contextMenu.show(this, event.getScreenX(), event.getScreenY());
    }

    public void setLeftMouseAction(Consumer<MouseEvent> leftMouseAction) {
        this.leftMouseAction = leftMouseAction;
    }
    
    public void setRightMouseAction(Consumer<MouseEvent> rightMouseAction) {
        this.rightMouseAction = rightMouseAction;
    }
    
}
