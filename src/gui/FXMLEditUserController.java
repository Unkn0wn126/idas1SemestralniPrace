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
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Prispevek;
import model.Role;
import model.StudijniObor;
import model.StudijniPlan;
import model.Uzivatel;

/**
 * FXML Controller class
 *
 * @author Lukas
 */
public class FXMLEditUserController implements Initializable {

    private Consumer<Uzivatel> btnCancelEvent;
    private Consumer<Uzivatel> btnSaveEvent;
    private Uzivatel uzivatel;

    private int numOfErrors;
    private boolean checkForPassword;

    @FXML
    private TextField tfJmeno;
    @FXML
    private TextField tfPrijmeni;
    @FXML
    private PasswordField tfHeslo1;
    @FXML
    private PasswordField tfHeslo2;
    @FXML
    private ComboBox<Integer> cbRocnik;
    @FXML
    private TextField tfEmail;

    private ObservableList<StudijniPlan> studijniPlany = FXCollections.observableArrayList();
    private ObservableList<StudijniObor> obory = FXCollections.observableArrayList();
    private ObservableList<Role> role = FXCollections.observableArrayList();
    private ObservableList<Integer> rocniky = FXCollections.observableArrayList();
    private ObservableList<Integer> blokace = FXCollections.observableArrayList();

    private SimpleBooleanProperty giveAdminPermissions = new SimpleBooleanProperty();
    private SimpleBooleanProperty userEditingSelf = new SimpleBooleanProperty();

    private Predicate<Uzivatel> canEdit;

    private List<StudijniPlan> planyAll = new ArrayList<>();

    @FXML
    private TextField tfStareHeslo;
    @FXML
    private TextArea taPoznamka;
    @FXML
    private ListView<Role> listViewRole;
    @FXML
    private ListView<StudijniPlan> listViewPlany;
    @FXML
    private ComboBox<StudijniObor> cbObor;
    @FXML
    private Tab tabRole;
    @FXML
    private ComboBox<Integer> cbBlokace;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        checkForPassword = false;
        numOfErrors = 0;

        giveAdminPermissions.setValue(false);
        userEditingSelf.setValue(false);
        tabRole.setDisable(true);

        rocniky.addAll(0, 1, 2, 3, 4, 5, 6);
        cbRocnik.setItems(rocniky);
        cbObor.setItems(obory);

        blokace.addAll(0, 1);
        cbBlokace.setItems(blokace);

        listViewPlany.setItems(studijniPlany);
        listViewPlany.setCellFactory((param) -> {
            return new PickCurriculumListCell();
        });

        cbObor.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            studijniPlany.clear();

            List<StudijniPlan> currPlany = planyAll.stream().filter((t) -> {
                if (newValue != null) {
                    return t.getIdOboru() == newValue.getIdOboru();
                }
                return false;

            }).collect(Collectors.toList());

            for (StudijniPlan studijniPlan : planyAll) {
                if (!currPlany.contains(studijniPlan)) {
                    studijniPlan.setSelected(false);
                }
            }

            studijniPlany.addAll(currPlany);
        });

        listViewRole.setItems(role);
        listViewRole.setCellFactory((param) -> {
            return new PickRoleListCell(userEditingSelf);
        });

        giveAdminPermissions.addListener((observable, oldValue, newValue) -> {
            tabRole.setDisable(!newValue);
        });
    }

    public void updateViews(Uzivatel uzivatel, boolean isAdmin) {
        this.uzivatel = uzivatel;
        tfEmail.setText(uzivatel.getEmail());
        tfJmeno.setText(uzivatel.getJmeno());
        tfPrijmeni.setText(uzivatel.getPrijmeni());
        taPoznamka.setText(uzivatel.getPoznamka());
        cbRocnik.getSelectionModel().select(uzivatel.getRokStudia());

        giveAdminPermissions.setValue(isAdmin);

        cbBlokace.getSelectionModel().select(uzivatel.getBlokace());

        if (canEdit != null) {
            cbBlokace.setDisable(!canEdit.test(uzivatel));
        }
    }

    @FXML
    private void handleBtnZrusitAction(ActionEvent event) {
        if (btnCancelEvent != null) {
            btnCancelEvent.accept(uzivatel);
        }
    }

    public void updateStudijniPlany(List<StudijniPlan> data) {
        planyAll.clear();
        planyAll.addAll(data);
        studijniPlany.clear();
        studijniPlany.addAll(data);
    }

    public void setUserEditingSelf(boolean userEditingSelf) {
        this.userEditingSelf.setValue(userEditingSelf);
    }

    public void updateStudijniObory(List<StudijniObor> data) {
        obory.clear();
        obory.addAll(data);
    }

    public void updateRole(List<Role> data) {
        role.clear();
        role.addAll(data);
    }

    public void setCurrentObor(StudijniObor obor) {
        List<StudijniObor> oborList = obory.stream().filter((t) -> {
            return obor.getIdOboru() == t.getIdOboru();
        }).collect(Collectors.toList());

        cbObor.getSelectionModel().select(oborList.get(0));
    }

    public void setCurrentRole(List<Role> currRole) {
        for (Role role1 : this.role) {
            role1.setSelected(currRole.stream().anyMatch((t) -> {
                return role1.getIdRole() == t.getIdRole();
            }));
        }

    }

    public void setCurrentPlany(List<StudijniPlan> plany) {
        for (StudijniPlan plan : studijniPlany) {
            plan.setSelected(plany.stream().anyMatch((t) -> {
                return plan.getIdPlanu() == t.getIdPlanu();
            }));
        }

    }

    public String getJmeno() {
        String jmeno = tfJmeno.getText();
        if (jmeno == null) {
            showAlert("Jm??no nesm?? b??t pr??zdn?? a mus?? b??t maxim??ln?? 30 znak?? dlouh??");
            numOfErrors++;
            return null;
        }
        if (jmeno.length() > 30 || jmeno.length() < 1) {
            showAlert("Jm??no nesm?? b??t pr??zdn?? a mus?? b??t maxim??ln?? 30 znak?? dlouh??");
            numOfErrors++;
        }

        return jmeno;
    }

    public void oldPasswordAgrees(String oldPassword) {
        if (tfHeslo1.getText().length() > 0 || tfHeslo2.getText().length() > 0) {
            checkForPassword = true;
            String pssw = tfStareHeslo.getText();
            if (pssw == null) {
                showAlert("Heslo nesm?? b??t pr??zdn?? a mus?? b??t maxim??ln?? 16 znak?? dlouh??");
                numOfErrors++;
                return;
            }
            if (pssw.length() > 16 || pssw.length() < 1) {
                showAlert("Heslo nesm?? b??t pr??zdn?? a mus?? b??t maxim??ln?? 16 znak?? dlouh??");
                numOfErrors++;
                return;
            }
            if (!pssw.equals(oldPassword)) {
                showAlert("Nespr??vn?? star?? heslo");
                numOfErrors++;
            }
        } else {
            this.uzivatel.setHeslo(oldPassword);
            checkForPassword = false;
        }
    }

    public String getPrijmeni() {
        String prijmeni = tfPrijmeni.getText();
        if (prijmeni == null) {
            showAlert("P????jmen?? nesm?? b??t pr??zdn?? a mus?? b??t maxim??ln?? 80 znak?? dlouh??");
            numOfErrors++;
        }
        if (prijmeni.length() > 80 || prijmeni.length() < 1) {
            showAlert("P????jmen?? nesm?? b??t pr??zdn?? a mus?? b??t maxim??ln?? 80 znak?? dlouh??");
            numOfErrors++;
            return null;
        }

        return prijmeni;
    }

    public String getHesloFirst() {
        String heslo = tfHeslo1.getText();
        if (heslo == null) {
            showAlert("Heslo nesm?? b??t pr??zdn?? a mus?? b??t maxim??ln?? 16 znak?? dlouh??");
            numOfErrors++;
            return null;
        }
        if (heslo.length() > 16 || heslo.length() < 1) {
            showAlert("Heslo nesm?? b??t pr??zdn?? a mus?? b??t maxim??ln?? 16 znak?? dlouh??");
            numOfErrors++;
        }

        return heslo;
    }

    public String getHesloConfirm() {
        String heslo = tfHeslo2.getText();
        if (heslo == null) {
            showAlert("Heslo nesm?? b??t pr??zdn?? a mus?? b??t maxim??ln?? 16 znak?? dlouh??");
            numOfErrors++;
            return null;
        }
        if (heslo.length() > 16 || heslo.length() < 1) {
            showAlert("Heslo nesm?? b??t pr??zdn?? a mus?? b??t maxim??ln?? 16 znak?? dlouh??");
            numOfErrors++;
        }

        return heslo;
    }

    public String getHeslo() {
        String heslo1 = getHesloFirst();
        String heslo2 = getHesloConfirm();
        if (!heslo1.equals(heslo2)) {
            showAlert("Hesla se neshoduj??");
            numOfErrors++;
        }

        return heslo1;
    }

    public Integer getRocnik() {
        if (cbRocnik.getValue() == null) {
            showAlert("Neplatn?? ro??n??k");
            numOfErrors++;
            return -1;
        }

        return cbRocnik.getValue();
    }

    public Integer getBlokace() {
        if (cbBlokace.getValue() == null) {
            showAlert("Neplatn?? statut blokace");
            numOfErrors++;
            return -1;
        }

        return cbBlokace.getValue();
    }

    public StudijniObor getObor() {
        if (cbObor.getValue() == null) {
            showAlert("Neplatn?? obor");
            numOfErrors++;
        }

        return cbObor.getValue();
    }

    public String getEmail() {
        String email = tfEmail.getText();
        if (email == null) {
            showAlert("E-mail nesm?? b??t pr??zdn?? a mus?? b??t maxim??ln?? 30 znak?? dlouh??");
            numOfErrors++;
            return null;
        }
        if (email.length() > 30 || email.length() < 1) {
            showAlert("E-mail nesm?? b??t pr??zdn?? a mus?? b??t maxim??ln?? 30 znak?? dlouh??");
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
            showAlert("Pozn??mka mus?? b??t maxim??ln?? 30 znak?? dlouh??");
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
            showAlert("Mus??te vybrat alespo?? jeden p????slu??n?? studijn?? pl??n");
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
            showAlert("Mus??te vybrat alespo?? jednu roli");
            numOfErrors++;
        }

        return selectedRole;
    }

    public boolean isChangesInserted() {
        return checkForPassword;
    }

    /**
     * Nastav?? akci, podle kter?? se ur????, jestli m?? dan?? u??ivatel pr??vo ur??it??ch
     * ??prav
     *
     * @param canEdit akce, kter?? se m?? prov??st
     */
    public void setCanEditAction(Predicate<Uzivatel> canEdit) {
        this.canEdit = canEdit;
    }

    public void setBtnCancelEvent(Consumer<Uzivatel> btnCancelEvent) {
        this.btnCancelEvent = btnCancelEvent;
    }

    public void setBtnSaveEvent(Consumer<Uzivatel> btnSaveEvent) {
        this.btnSaveEvent = btnSaveEvent;
    }

    @FXML
    private void handleBtnUlozitZmenyAction(ActionEvent event) {
        numOfErrors = 0;
        String jmeno = getJmeno();
        String prijmeni = getPrijmeni();
        String email = getEmail();
        String poznamka = getPoznamka();
        StudijniObor obor = getObor();
        String heslo = uzivatel.getHeslo();
        if (checkForPassword) {
            heslo = getHeslo();
        }
        getRoleUzivatele();
        getStudijniPlan();
        int rocnik = getRocnik();
        int blokace = getBlokace();

        if (numOfErrors == 0) {
            if (btnSaveEvent != null) {
                this.uzivatel.setBlokace(blokace);
                this.uzivatel.setHeslo(heslo);
                this.uzivatel.setJmeno(jmeno);
                this.uzivatel.setPrijmeni(prijmeni);
                this.uzivatel.setPoznamka(poznamka);
                this.uzivatel.setEmail(email);
                this.uzivatel.setRokStudia(rocnik);

                btnSaveEvent.accept(this.uzivatel);
            }
        }

    }

    /**
     * Zobraz?? dialog s errorem
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
