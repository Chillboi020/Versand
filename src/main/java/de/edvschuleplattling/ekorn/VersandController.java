package de.edvschuleplattling.ekorn;

import de.edvschuleplattling.ekorn.classes.Auftrag;
import de.edvschuleplattling.ekorn.classes.Person;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.lang.reflect.Array;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
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
    private Label lbl_Meldung;
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
    private Button btn_Wunschtermin_Reset;
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
    private TextField txt_Betrag;
    //endregion
    //region Preisberechnung (PFLICHTFELD)
    @FXML
    private Slider sld_Rabatt;
    @FXML
    private Button btn_Berechne;
    @FXML
    private TextField txt_Preis;
    //endregion
    //endregion

    //region Controller Variablen
    private ToggleGroup zTypen = new ToggleGroup();
    private ToggleGroup vTypen = new ToggleGroup();
    private ArrayList<TextField> absenderList;
    private ArrayList<TextField> empfaengerList;
    private Auftrag auftrag = new Auftrag();
    private Person absender = new Person();
    private Person empfaenger = new Person();

    private final DecimalFormat dec = new DecimalFormat("#0.00");
    //endregion

    //region FXML Methoden
    //region Oberer Bereich
    @FXML
    public void on_Speichern_Click() {

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

    //region Zustellung (fertig)
    @FXML
    public void on_Ztyp_Select() {
        RadioButton rb = (RadioButton) zTypen.getSelectedToggle();
        switch (rb.getText()) {
            case "Brief" -> versicherungVisibility(false);
            case "Päckchen", "Paket" -> versicherungVisibility(true);
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
            case "<= 100€", "<= 500€" -> betragVisibility(false);
            case " >  500€" -> betragVisibility(true);
        }
    }
    //endregion

    //region Preisberechnung
    @FXML
    public void on_Rabatt_Slide() {
    }

    @FXML
    public void on_Berechne_Click() {
        try {
            auftragErfassung();
            txt_Preis.setText(String.valueOf(auftrag.berechne()));
            System.out.println(auftrag);
        } catch (IllegalArgumentException msg) {
            setMeldung(msg.getMessage(), Color.rgb(200, 0, 0));
        }
    }
    //endregion
    //endregion

    //region Controller Methoden
    @Override
    // Was beim Programmstart passieren soll
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Liste mit Textfeldern wird befüllt
        absenderList = new ArrayList<>() {{
            add(txt_A_Vname); add(txt_A_Nname); add(txt_A_Str); add(txt_A_Str_Nr); add(txt_A_PLZ); add(txt_A_Ort);
        }};
        empfaengerList = new ArrayList<>() {{
            add(txt_E_Vname); add(txt_E_Nname); add(txt_E_Str); add(txt_E_Str_Nr); add(txt_E_PLZ); add(txt_E_Ort);
        }};

        // Togglegroup Zustelltypen
        rb_Brief.setToggleGroup(zTypen);
        rb_Paeckchen.setToggleGroup(zTypen);
        rb_Paket.setToggleGroup(zTypen);

        // Togglegroup Versicherungstypen
        rb_bis100.setToggleGroup(vTypen);
        rb_bis500.setToggleGroup(vTypen);
        rb_ue500.setToggleGroup(vTypen);

        // Deaktiviert Sonntage im Wunschtermin
        dtp_Wunschtermin.setDayCellFactory(deaktiviereSonntag());

        // Textbegrenzung für einige Textfelder
        addTextLimiter(txt_ID, 15);
        addTextLimiter(txt_A_Str_Nr, 3);
        addTextLimiter(txt_E_Str_Nr, 3);
        addTextLimiter(txt_A_PLZ, 5);
        addTextLimiter(txt_E_PLZ, 5);
        reset();
    }

    public void reset() {
        // Oberer Bereich
        txt_ID.clear();
        // Datum auf heutigen Tag setzen
        dtp_Aufgegeben.setValue(LocalDate.now());
        lbl_Meldung.setText("");
        lbl_Meldung.setTextFill(Color.BLACK);

        // Absender und Empfänger
        for (TextField a : absenderList) {
            a.clear();
        }
        for (TextField e : empfaengerList) {
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

        // Preisberechnung
        sld_Rabatt.setValue(5);
        txt_Preis.clear();
    }

    public void setMeldung(String msg, Color clr) {
        lbl_Meldung.setText(msg);
        lbl_Meldung.setTextFill(clr);
    }

    public void auftragErfassung() {
        ArrayList<String> aList = new ArrayList<>();
        for (TextField a : absenderList) {
            aList.add(a.getText());
        }
        ArrayList<String> eList = new ArrayList<>();
        for (TextField e : empfaengerList) {
            eList.add(e.getText());
        }

        try {
            // Oberer Bereich
            auftrag.setId(txt_ID.getText());
            if (dtp_Aufgegeben.getValue().isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Aufgabedatum darf nicht in der Vergangenheit liegen");
            }
            auftrag.setAufgegeben(dtp_Aufgegeben.getValue());

            // Absender, Empfänger, Beschreibung
            absender = new Person('A', aList.get(0), aList.get(1), aList.get(2), aList.get(3), aList.get(4), aList.get(5));
            empfaenger = new Person('E', eList.get(0), eList.get(1), eList.get(2), eList.get(3), eList.get(4), eList.get(5));
            auftrag.setAbsender(absender);
            auftrag.setEmpfaenger(empfaenger);
            auftrag.setBeschreibung(txta_Beschreibung.getText());

            // Zustellung
            auftrag.setExpress(cb_Express.isSelected());
            RadioButton rb = (RadioButton) zTypen.getSelectedToggle();
            Auftrag.zTyp zTyp = null;
            switch (rb.getText()) {
                case "Brief" -> zTyp = Auftrag.zTyp.BRIEF;
                case "Päckchen" -> zTyp = Auftrag.zTyp.PAECKCHEN;
                case "Paket" -> zTyp = Auftrag.zTyp.PAKET;
            };
            auftrag.setVersandoption(zTyp);
            auftrag.setWunschtermin(dtp_Wunschtermin.getValue());
            if (cb_AltAblage.isSelected() && txta_AltAblage.getText().equals("")) {
                throw new IllegalArgumentException("Alt. Ablageort Pflichtfeld!");
            }
            auftrag.setAltAblageOrt(txta_AltAblage.getText());

            // Versicherung
            auftrag.setVersichert(cb_Versichert.isSelected());
            rb = (RadioButton) vTypen.getSelectedToggle();
            Auftrag.vTyp vTyp = null;
            switch (rb.getText()) {
                case "<= 100€" -> vTyp = Auftrag.vTyp.B100;
                case "<= 500€" -> vTyp = Auftrag.vTyp.B500;
                case " >  500€" -> vTyp = Auftrag.vTyp.UE500;
            }
            auftrag.setVersicherung(vTyp);
            if (rb.getText().equals(" >  500€")) {
                auftrag.setBetrag(txt_Betrag.getText());
            }

            auftrag.setRabatt(sld_Rabatt.getValue());

            setMeldung("", Color.BLACK);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    //region Filter
    // Deaktiviert den Sonntag im DatePicker
    private Callback<DatePicker, DateCell> deaktiviereSonntag() {
        return new Callback<>() {
            @Override
            public DateCell call(final DatePicker dp) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);
                        // jedes Mal, wenn man ein Datum beim Datepicker auswählt, wird es aufgerufen, und deaktiviert alle Sonntage
                        if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                            setDisable(true);
                        }
                    }
                };
            }
        };
    }

    // Fügt einen TextLimiter hinzu
    private void addTextLimiter(final TextField tf, final int maxLength) {
        tf.textProperty().addListener(new ChangeListener<>() {
            @Override
            // Wird aufgerufen, wenn etwas in das Textfeld geschrieben wird
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                // Sobald die Höchstgrenze überschritten wird, sorgt man dafür, dass nichts weiteres geschrieben werden kann
                if (tf.getText().length() > maxLength) {
                    String s = tf.getText().substring(0, maxLength);
                    tf.setText(s);
                }
            }
        });
    }
    //endregion

    //region Sichtbarkeit für Versicherung
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
        if (!b) {
            txt_Betrag.clear();
        } else {
            txt_Betrag.setText("500.01");
        }
    }
    //endregion
}