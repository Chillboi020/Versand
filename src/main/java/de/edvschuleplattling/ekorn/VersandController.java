package de.edvschuleplattling.ekorn;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class VersandController implements Initializable {

    //region FXML Variablen
    //region Oberer Bereich
    @FXML
    private TextField txt_ID;
    @FXML
    private DatePicker dtp_Aufgegeben;
    @FXML
    private Button btn_Speichern;
    @FXML
    private Button btn_Laden;
    @FXML
    private Button btn_Reset;
    @FXML
    private TextField txt_Meldung;
    //endregion
    //region Absender (PFLICHTFELD)
    @FXML
    private TextField txt_A_Vname;
    @FXML
    private TextField txt_A_Nname;
    @FXML
    private TextField txt_A_Str;
    @FXML
    private TextField txt_A_Str_Nr;
    @FXML
    private TextField txt_A_PLZ;
    @FXML
    private TextField txt_A_Ort;
    //endregion
    //region Empfänger (PFLICHTFELD)
    @FXML
    private TextField txt_E_Vname;
    @FXML
    private TextField txt_E_Nname;
    @FXML
    private TextField txt_E_Str;
    @FXML
    private TextField txt_E_Str_Nr;
    @FXML
    private TextField txt_E_PLZ;
    @FXML
    private TextField txt_E_Ort;
    //endregion
    //region Inhalt (OPTIONALFELD)
    @FXML
    private TextArea txta_Beschreibung;
    //endregion
    //region Zustellung (PFLICHTFELD)
    @FXML
    private CheckBox cb_Express;
    @FXML
    private RadioButton rb_Brief;
    @FXML
    private RadioButton rb_Paeckchen;
    @FXML
    private RadioButton rb_Paket;
    @FXML
    private DatePicker dtp_Wunschtermin;
    @FXML
    private CheckBox cb_AltAblage;
    @FXML
    private TextArea txta_AltAblage;
    //endregion
    //region Versicherung (BEDINGT PFLICHTFELD)
    @FXML
    private TitledPane tpane_Versicherung;
    @FXML
    private CheckBox cb_Versichert;
    @FXML
    private Pane pane_Versicherung;
    @FXML
    private RadioButton rb_bis100;
    @FXML
    private RadioButton rb_bis500;
    @FXML
    private RadioButton rb_ue500;
    @FXML
    private Pane pane_Betrag;
    @FXML
    private Label lbl_Betrag;
    @FXML
    private TextField txt_Betrag;
    //endregion
    //region Preisberechnung (PFLICHTFELD)
    @FXML
    private Slider sld_Rabatt;
    @FXML
    private Button btn_Berechne;
    @FXML
    private TextField txtPreis;
    @FXML
    private Button btn_A_Pruefung;
    @FXML
    private Button btn_E_Pruefung;
    @FXML
    private Button btn_Wunschtermin_Reset;
    //endregion
    //endregion

    //region Controller Variablen
    ToggleGroup zTypen = new ToggleGroup();
    ToggleGroup vTypen = new ToggleGroup();
    ArrayList<TextField> absender;
    ArrayList<TextField> empfaenger;
    //endregion

    //region FXML Methoden
    //region Oberer Bereich
    @FXML
    public void on_Speichern_Click() {
        System.out.println("Speichern");
    }

    @FXML
    public void on_Laden_Click() {
        System.out.println("Laden");
    }

    @FXML
    public void on_Reset_Click() {
        reset();
    }
    //endregion

    //region Personen
    @FXML
    public void on_A_Pruefung_Click() {
        System.out.println("Absender prüfen");
    }

    @FXML
    public void on_E_Pruefung_Click() {
        System.out.println("Empfänger prüfen");
    }
    //endregion

    //region Zustellung (fertig)
    @FXML
    public void on_Ztyp_Select() {
        RadioButton rb = (RadioButton) zTypen.getSelectedToggle();
        switch (rb.getText()) {
            case "Brief":
                versicherungVisibility(false);
                break;
            case "Päckchen", "Paket":
                versicherungVisibility(true);
                break;
        }
    }

    @FXML
    public void on_Wunschtermin_Picked() {
        btn_Wunschtermin_Reset.setVisible(true);
    }

    @FXML
    public void on_Wunschtermin_Reset_Click() {
        dtp_Wunschtermin.setValue(null);
        btn_Wunschtermin_Reset.setVisible(false);
    }

    @FXML
    public void on_AltAblage_Check() {
        txta_AltAblage.setDisable(!cb_AltAblage.isSelected());
        if (!cb_AltAblage.isSelected()) txta_AltAblage.clear();
    }
    //endregion


    //region Versicherung (fertig)
    @FXML
    public void on_Versichert_Check() {
        vTypenVisibility(cb_Versichert.isSelected());
    }

    @FXML
    public void on_Vtyp_Selected() {
        RadioButton rb = (RadioButton) vTypen.getSelectedToggle();
        String rbText = rb.getText();

        switch (rbText) {
            case "<= 100€", "<= 500€":
                betragVisibility(false);
                break;
            case " >  500€":
                betragVisibility(true);
        }
    }
    //endregion

    //region Preisberechnung
    @FXML
    public void on_Rabatt_Slide() {
        System.out.println("Rabatt: " + sld_Rabatt.getValue());
    }

    @FXML
    public void on_Berechne_Click() {
        System.out.println("Berechnen");
    }
    //endregion
    //endregion

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        absender = new ArrayList<>() {{
            add(txt_A_Vname); add(txt_A_Nname); add(txt_A_Str); add(txt_A_Str_Nr); add(txt_A_PLZ); add(txt_A_Ort);
        }};

        empfaenger = new ArrayList<>() {{
            add(txt_E_Vname); add(txt_E_Nname); add(txt_E_Str); add(txt_E_Str_Nr); add(txt_E_PLZ); add(txt_E_Ort);
        }};

        rb_Brief.setToggleGroup(zTypen);
        rb_Paeckchen.setToggleGroup(zTypen);
        rb_Paket.setToggleGroup(zTypen);

        rb_bis100.setToggleGroup(vTypen);
        rb_bis500.setToggleGroup(vTypen);
        rb_ue500.setToggleGroup(vTypen);

        reset();
    }

    public void reset() {
        // Oberer Bereich
        txt_ID.clear();
        // Datum auf heutigen Tag setzen
        dtp_Aufgegeben.setValue(LocalDate.now());

        // Absender und Empfänger
        for (TextField a : absender) {
            a.clear();
        }
        for (TextField e : empfaenger) {
            e.clear();
        }

        // Inhalt
        txta_Beschreibung.clear();

        // Zustellung
        cb_Express.setSelected(false);
        rb_Brief.setSelected(true);
        on_Wunschtermin_Reset_Click();
        cb_AltAblage.setSelected(false);
        on_AltAblage_Check();

        // Versicherung
        on_Ztyp_Select();
    }

    public void versicherungVisibility(boolean b) {
        tpane_Versicherung.setVisible(b);
        if (!b) {
            vTypenVisibility(true);
            cb_Versichert.setSelected(false);
            on_Versichert_Check();
        }
    }

    public void vTypenVisibility(boolean b) {
        pane_Versicherung.setVisible(b);
        if (!b) {
            rb_bis100.setSelected(true);
            on_Vtyp_Selected();
        }
    }

    public void betragVisibility(boolean b) {
        pane_Betrag.setVisible(b);
        if (!b) txt_Betrag.clear();
    }
}