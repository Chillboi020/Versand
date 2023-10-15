package de.edwin.versand;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

public class VersandController {

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
    @FXML
    private Button btn_A_Pruefe;
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
    @FXML
    private Button btn_E_Pruefe;
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
    //endregion
    //endregion


}