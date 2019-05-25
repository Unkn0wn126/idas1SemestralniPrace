/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import gui.customcells.PickCurriculumListCell;
import gui.customcells.PickRoleListCell;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Role;
import model.StudijniObor;
import model.StudijniPlan;
import model.Uzivatel;

/**
 * FXML Controller class
 *
 * @author Lukas
 */
public class FXMLRegisterController implements Initializable {

    private Consumer<Uzivatel> registerAction;
    private Consumer<ActionEvent> cancelAction;
    @FXML
    private TextField tfJmeno;
    @FXML
    private TextField tfPrijmeni;
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

    private ObservableList<StudijniPlan> studijniPlany = FXCollections.observableArrayList();
    private ObservableList<StudijniObor> obory = FXCollections.observableArrayList();
    private ObservableList<Role> role = FXCollections.observableArrayList();
    private ObservableList<Integer> rocniky = FXCollections.observableArrayList();

    private List<StudijniPlan> planyAll = new ArrayList<>();

    private int numOfErrors;

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
        numOfErrors = 0;
        cbRocnik.setItems(rocniky);
        rocniky.addAll(0, 1, 2, 3, 4, 5, 6);

        listViewStudijniPlany.setItems(studijniPlany);
        listViewStudijniPlany.setCellFactory((param) -> {
            return new PickCurriculumListCell();
        });

        listViewRole.setItems(role);
        listViewRole.setCellFactory((param) -> {
            return new PickRoleListCell();
        });

        cbObor.setItems(obory);

        cbObor.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            studijniPlany.clear();

            List<StudijniPlan> currPlany = planyAll.stream().filter((t) -> {
                return t.getIdOboru() == newValue.getIdOboru();
            }).collect(Collectors.toList());

            for (StudijniPlan studijniPlan : planyAll) {
                if (!currPlany.contains(studijniPlan)) {
                    studijniPlan.setSelected(false);
                }
            }

            studijniPlany.addAll(currPlany);
        });
    }

    public void setRegisterAction(Consumer<Uzivatel> registerAction) {
        this.registerAction = registerAction;
    }

    public void setCancelAction(Consumer<ActionEvent> cancelAction) {
        this.cancelAction = cancelAction;
    }

    public void setPlanyDataset(List<StudijniPlan> data) {
        planyAll.clear();
        planyAll.addAll(data);
        studijniPlany.clear();
        studijniPlany.addAll(data);
    }

    public void setOboryDataset(List<StudijniObor> data) {
        obory.clear();
        obory.addAll(data);
    }

    public void setRoleDataset(List<Role> data) {
        role.clear();
        role.addAll(data);
    }

    public String getJmeno() {
        String jmeno = tfJmeno.getText();
        if (jmeno == null) {
            showAlert("Jméno nesmí být prázdné a musí být maximálně 30 znaků dlouhé");
            numOfErrors++;
            return null;
        }
        if (jmeno.length() > 30 || jmeno.length() < 1) {
            showAlert("Jméno nesmí být prázdné a musí být maximálně 30 znaků dlouhé");
            numOfErrors++;
        }

        return jmeno;
    }

    public String getPrijmeni() {
        String prijmeni = tfPrijmeni.getText();
        if (prijmeni == null) {
            showAlert("Příjmení nesmí být prázdné a musí být maximálně 80 znaků dlouhé");
            numOfErrors++;
        }
        if (prijmeni.length() > 80 || prijmeni.length() < 1) {
            showAlert("Příjmení nesmí být prázdné a musí být maximálně 80 znaků dlouhé");
            numOfErrors++;
            return null;
        }

        return prijmeni;
    }

    public String getHesloFirst() {
        String heslo = tfHeslo.getText();
        if (heslo == null) {
            showAlert("Heslo nesmí být prázdné a musí být maximálně 16 znaků dlouhé");
            numOfErrors++;
            return null;
        }
        if (heslo.length() > 16 || heslo.length() < 1) {
            showAlert("Heslo nesmí být prázdné a musí být maximálně 16 znaků dlouhé");
            numOfErrors++;
        }

        return heslo;
    }

    public String getHesloConfirm() {
        String heslo = tfHesloConfirm.getText();
        if (heslo == null) {
            showAlert("Heslo nesmí být prázdné a musí být maximálně 16 znaků dlouhé");
            numOfErrors++;
            return null;
        }
        if (heslo.length() > 16 || heslo.length() < 1) {
            showAlert("Heslo nesmí být prázdné a musí být maximálně 16 znaků dlouhé");
            numOfErrors++;
        }

        return heslo;
    }

    public String getHeslo() {
        String heslo1 = getHesloFirst();
        String heslo2 = getHesloConfirm();
        if (!heslo1.equals(heslo2)) {
            showAlert("Hesla se neshodují");
            numOfErrors++;
        }

        return heslo1;
    }

    public Integer getRocnik() {
        if (cbRocnik.getValue() == null) {
            showAlert("Neplatný ročník");
            numOfErrors++;
            return -1;
        }

        return cbRocnik.getValue();
    }

    public StudijniObor getObor() {
        if (cbObor.getValue() == null) {
            showAlert("Neplatný obor");
            numOfErrors++;
        }

        return cbObor.getValue();
    }

    public String getEmail() {
        String email = tfEmail.getText();
        if (email == null) {
            showAlert("E-mail nesmí být prázdný a musí být maximálně 30 znaků dlouhý");
            numOfErrors++;
            return null;
        }
        if (email.length() > 30 || email.length() < 1) {
            showAlert("E-mail nesmí být prázdný a musí být maximálně 30 znaků dlouhý");
            numOfErrors++;
        }

        return email;
    }

    public String getPoznamka() {
        String email = taPoznamka.getText();
        if (email == null) {
            return null;
        }
        if (email.length() > 300) {
            showAlert("Poznámka musí být maximálně 30 znaků dlouhá");
            numOfErrors++;
        }

        return email;
    }

    public List<StudijniPlan> getStudijniPlan() {
        List<StudijniPlan> selectedPlany = new ArrayList<>();
        for (StudijniPlan studijniPlan : studijniPlany) {
            if (studijniPlan.isSelected()) {
                selectedPlany.add(studijniPlan);
            }
        }
        if (selectedPlany.size() < 1) {
            showAlert("Musíte vybrat alespoň jeden příslušný studijní plán");
            numOfErrors++;
        }

        return selectedPlany;
    }

    public List<Role> getRoleUzivatele() {
        List<Role> selectedRole = new ArrayList<>();
        for (Role r : role) {
            if (r.isSelected()) {
                selectedRole.add(r);
            }
        }
        if (selectedRole.size() < 1) {
            showAlert("Musíte vybrat alespoň jednu roli");
            numOfErrors++;
        }

        return selectedRole;
    }

    @FXML
    private void handleBtnZaregistrovatAction(ActionEvent event) {
        numOfErrors = 0;
        String jmeno = getJmeno();
        String prijmeni = getPrijmeni();
        String email = getEmail();
        String poznamka = getPoznamka();
        StudijniObor obor = getObor();
        String heslo = getHeslo();
        getRoleUzivatele();
        getStudijniPlan();
        int rocnik = getRocnik();
        if (numOfErrors == 0) {
            if (registerAction != null) {
                Uzivatel uziv = new Uzivatel(jmeno, prijmeni, email, rocnik, poznamka, heslo);
                registerAction.accept(uziv);
            }
        }

    }

    @FXML
    private void handleBtnZrusitAction(ActionEvent event) {
        if (cancelAction != null) {
            cancelAction.accept(event);
        }
    }

    /**
     * Zobrazí dialog s errorem
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
