/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import database.DatabaseHelper;
import database.OracleConnector;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.UzivatelManager;

/**
 *
 * @author Lukas
 */
public class SemestralniPrace extends Application {

    public static Stage primaryStage;
    private DatabaseHelper dbHelper;
    private UzivatelManager uzivManager;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        setUpConnection();
        loadLoginScene(stage);
        stage.show();
    }

    /**
     * Naváže spojení s databází
     */
    private void setUpConnection() {
        try {
            // Credentials pro navázání spojení
            OracleConnector.setUpConnection("fei-sql1.upceucebny.cz", 1521,
                    "IDAS", "st55419", "Salem3l139");
            
            // Vytvoření instance třídy pro ovládání databáze
            // Ve zbytku aplikace řešeno dependency injection
            dbHelper = new DatabaseHelper(OracleConnector.getConnection());
            uzivManager = dbHelper.getUzivatelManager();
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Nepodařilo se navázat spojení k databázi");

            alert.showAndWait();
        }

    }

    /**
     * Načte menu pro přihlášení
     * @param stage okno, ve kterém se má zobrazit
     * @throws IOException když se menu nepodaří načíst ze souboru
     */
    private void loadLoginScene(Stage stage) throws IOException {
        // Načtení layoutu přihlášení start
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLLogin.fxml"));
        Parent root = (Parent) loader.load();
        FXMLLoginController controller = loader.getController();
        controller.setStage(stage);
        // Načtení layoutu přihlášení end

        // Nastavení akce kliknutí na tlačítko přihlásit tak,
        // aby se aplikace pokusila o přihlášení uživatele
        // a následné zobrazení hlavní scény aplikace
        controller.setBtnPrihlasitAction((t) -> {
            String login = controller.getJmeno();
            String heslo = controller.getHeslo();
            login(login, heslo, stage);
            stage.setResizable(true);
        });
        
        // Nastavení akce pokusu o zavření okna tak, 
        // aby byl současný uživatel odhlášen a spojení s databází bylo ukončeno
        controller.setCloseAction((t) -> {
            logout(t);
        });

        Scene scene = new Scene(root);

        stage.setScene(scene);
    }

    /**
     * Pokusí se přihlásit uživatele
     * @param login id/login/e-mail uživatele
     * @param heslo heslo uživatele
     * @param stage okno, ve kterém se otevře hlavní scéna
     */
    private void login(String login, String heslo, Stage stage) {
        try {
            if (uzivManager.prihlasUzivatele(login, heslo)) {
                loadMainScene(stage);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Nesprávné jméno nebo heslo");

                alert.showAndWait();
            }
        } catch (IOException | SQLException ex) {
            Logger.getLogger(SemestralniPrace.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Odhlásí uživatele, a ukončí spojení s databází a poté i aplikaci
     * @param t event zavření okna
     */
    private void logout(WindowEvent t) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Ukončit aplikaci?");
        alert.setHeaderText("Skutečně chcete ukončit aplikaci?");
        alert.setContentText(null);
        
        Optional<ButtonType> result = alert.showAndWait();
        if (!result.get().equals(ButtonType.OK)) {
            t.consume();
        } else {
            try {
                uzivManager.unsetCurrentUser();
                OracleConnector.closeConnection(true);
            } catch (SQLException ex) {
                Logger.getLogger(SemestralniPrace.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Načte hlavní scénu
     *
     * @param stage okno, ve kterém bude zobrazena
     * @throws IOException
     */
    private void loadMainScene(Stage stage) throws IOException {
        // Načtení layoutu hlavní scény start
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLMainScene.fxml"));
        Parent root = loader.load();
        FXMLMainSceneController controller = loader.getController();
        // Načtení layoutu hlavní scény end
        
        // Předání database helperu do hlavní scény pomocí dependency injection
        controller.setDatabaseHelper(dbHelper);
        
        // Načte do levého panelu kontakty lokálního uživatele
        controller.loadContactsMenu();
        
        // Nastavení akce menu odhlásit tak, aby byl současný uživatel odhlášen
        // a aplikace se vrátila na přihlašovací obrazovku
        controller.setLogoutAction((t) -> {
            try {
                uzivManager.unsetCurrentUser();
                loadLoginScene(stage);
                stage.setResizable(false);
            } catch (IOException ex) {
                Logger.getLogger(SemestralniPrace.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        Scene scene = new Scene(root);

        stage.setScene(scene);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
