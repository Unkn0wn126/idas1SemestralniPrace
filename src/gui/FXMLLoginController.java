/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Lukas
 */
public class FXMLLoginController implements Initializable {

    @FXML
    private TextField tfJmeno;
    @FXML
    private PasswordField tfHeslo;

    // Akce pro přihlášení
    private Consumer<ActionEvent> btnPrihlasitAction;
    // Akce při zavření okna
    private Consumer<WindowEvent> closeAction;

    private Stage stage;

    /**
     * Nastaví stage tomuto layoutu
     *
     * @param stage stage pro layout
     */
    public void setStage(Stage stage) {
        this.stage = stage;

        // Zajistí provedení dané akce při zavření okna
        this.stage.setOnCloseRequest((event) -> {
            if (closeAction != null) {
                closeAction.accept(event);
            }
        });
    }

    /**
     * Nastaví akci, která se má provést při kliknutí na tlačítko přihlásit
     * @param btnPrihlasitAction akce, která se má provést
     */
    public void setBtnPrihlasitAction(Consumer<ActionEvent> btnPrihlasitAction) {
        this.btnPrihlasitAction = btnPrihlasitAction;
    }

    /**
     * Nastaví akci, která se má provést při pokusu o zavření aplikace
     * @param closeAction akce, která se má provést
     */
    public void setCloseAction(Consumer<WindowEvent> closeAction) {
        this.closeAction = closeAction;
    }

    /**
     * Vrátí text v textFieldu pro jméno
     * @return text v textFieldu pro jméno
     */
    public String getJmeno() {
        return tfJmeno.getText();
    }

    /**
     * Vrátí text v passwordFieldu pro heslo
     * @return text v passwordFieldu pro heslo
     */
    public String getHeslo() {
        return tfHeslo.getText();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void handleBtnPrihlasitAction(ActionEvent event) {
        if (btnPrihlasitAction != null) { // Ošetření nullPointerException
            btnPrihlasitAction.accept(event);
        }
    }

    @FXML
    private void handleBtnZrusitAction(ActionEvent event) {
        // Spustí event pokoušející se zavřít okno
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }
}
