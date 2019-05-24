/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Role;
import model.StudijniObor;
import model.StudijniPlan;
/**
 * FXML Controller class
 *
 * @author Lukas
 */
public class FXMLRegisterController implements Initializable {
    
    private Consumer<ActionEvent> registerAction;
    private Consumer<ActionEvent> cancelAction;
    @FXML
    private TextField tfJmeno;
    @FXML
    private TextField tfPrijmeni;
    private TextField tfLogin;
    @FXML
    private PasswordField tfHeslo;
    @FXML
    private PasswordField tfHesloConfirm;
    @FXML
    private ComboBox<Integer> cbRocnik;
    @FXML
    private TextField tfEmail;
    @FXML
    private TextArea taPoznamka;
    
    private ObservableList<StudijniPlan> plany = FXCollections.observableArrayList();
    private ObservableList<StudijniObor> obory = FXCollections.observableArrayList();
    private ObservableList<Role> role = FXCollections.observableArrayList();
    private ObservableList<Integer> rocniky = FXCollections.observableArrayList();
    @FXML
    private ListView<Role> listViewRole;
    @FXML
    private ListView<StudijniPlan> listViewStudijniPlany;
    @FXML
    private ComboBox<StudijniObor> cbObor;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbRocnik.setItems(rocniky);
        rocniky.addAll(0, 1, 2, 3, 4, 5, 6);
        
        listViewStudijniPlany.setItems(plany);
        
        listViewRole.setItems(role);
        
        cbObor.setItems(obory);
    }

    public void setRegisterAction(Consumer<ActionEvent> registerAction) {
        this.registerAction = registerAction;
    }

    public void setCancelAction(Consumer<ActionEvent> cancelAction) {
        this.cancelAction = cancelAction;
    }

    public void setPlanyDataset(List<StudijniPlan> data){
        plany.clear();
        plany.addAll(data);
    }

    public void setOboryDataset(List<StudijniObor> data){
        obory.clear();
        obory.addAll(data);
    }

    public void setRoleDataset(List<Role> data) {
        role.clear();
        role.addAll(data);
    }
    
    public String getJmeno(){
        return tfJmeno.getText();
    }
    
    public String getPrijmeni(){
        return tfPrijmeni.getText();
    }
    
    public String getLogin(){
        return tfLogin.getText();
    }
    
    public String getHeslo(){
        return tfHeslo.getText();
    }
    
    public String getHesloConfirm(){
        return tfHesloConfirm.getText();
    }
    
    public Integer getRocnik(){
        return cbRocnik.getValue();
    }
    
    public String getEmail(){
        return tfEmail.getText();
    }
    
    public String getPoznamka(){
        return taPoznamka.getText();
    }
    
    public List<StudijniPlan> getStudijniPlan(){
        return listViewStudijniPlany.getItems();
    }
    
    
    @FXML
    private void handleBtnZaregistrovatAction(ActionEvent event) {
        if (registerAction != null) {
            registerAction.accept(event);
        }
    }

    @FXML
    private void handleBtnZrusitAction(ActionEvent event) {
        if (cancelAction != null) {
            cancelAction.accept(event);
        }
    }
    
}
