package de.edvschuleplattling.ekorn;

import de.edvschuleplattling.ekorn.classes.Auftrag;
import de.edvschuleplattling.ekorn.classes.Datenverarbeitung;
import de.edvschuleplattling.ekorn.classes.Person;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

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
    @FXML
    private Button btn_Speichern;
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
    //region Zustellung
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
    //region Preisberechnung
    @FXML
    private Slider sld_Rabatt;
    @FXML
    private TextField txt_Preis;
    //endregion
    //endregion

    //region Controller Variablen
    private final ToggleGroup ZTYPEN = new ToggleGroup();
    private final ToggleGroup VTYPEN = new ToggleGroup();
    private ArrayList<TextField> absenderList;
    private ArrayList<TextField> empfaengerList;
    private Auftrag auftrag = new Auftrag();
    private boolean istGeladen;
    private static final DecimalFormat DEC = new DecimalFormat("#0.00");
    //endregion

    //region FXML Methoden
    //region Oberer Bereich
    @FXML
    public void on_Speichern_Click() {
        speichern();
    }

    @FXML
    public void on_Laden_Click() {
        laden();
    }

    @FXML
    public void on_Reset_Click() {
        reset();
    }
    //endregion

    //region Zustellung (fertig)
    @FXML
    public void on_Ztyp_Select() {
        kannSpeichern(false);
        // Versandtypen haben einen Einfluss auf die Versicherung
        RadioButton rb = (RadioButton) ZTYPEN.getSelectedToggle();
        switch (rb.getText()) {
            case "Brief" -> versicherungVisibility(false);
            case "Päckchen", "Paket" -> versicherungVisibility(true);
        }
    }

    @FXML
    public void on_Wunschtermin_Picked() {
        kannSpeichern(false);
        btn_Wunschtermin_Reset.setVisible(true);
    }

    @FXML
    public void on_Wunschtermin_Reset_Click() {
        kannSpeichern(false);
        dtp_Wunschtermin.setValue(null);
        btn_Wunschtermin_Reset.setVisible(false);
    }

    @FXML
    public void on_AltAblage_Check() {
        kannSpeichern(false);
        txta_AltAblage.setDisable(!cb_AltAblage.isSelected());
        if (!cb_AltAblage.isSelected()) txta_AltAblage.clear();
    }
    //endregion

    //region Versicherung (fertig)
    @FXML
    public void on_Versichert_Check() {
        kannSpeichern(false);
        vTypenVisibility(cb_Versichert.isSelected());
    }

    @FXML
    public void on_Vtyp_Selected() {
        kannSpeichern(false);
        RadioButton rb = (RadioButton) VTYPEN.getSelectedToggle();
        String rbText = rb.getText();

        switch (rbText) {
            case "<= 100€", "<= 500€" -> betragVisibility(false);
            case " >  500€" -> betragVisibility(true);
        }
    }
    //endregion

    //region Preisberechnung
    @FXML
    public void on_Rabatt_Slided() {
        kannSpeichern(false);
    }

    @FXML
    public void on_Berechne_Click() {
        berechnen();
    }
    //endregion
    //endregion

    //region Controller Methoden
    @Override
    // Was beim Programmstart passieren soll
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Liste mit Textfeldern wird befüllt
        absenderList = new ArrayList<>() {{
            add(txt_A_Vname);
            add(txt_A_Nname);
            add(txt_A_Str);
            add(txt_A_Str_Nr);
            add(txt_A_PLZ);
            add(txt_A_Ort);
        }};
        empfaengerList = new ArrayList<>() {{
            add(txt_E_Vname);
            add(txt_E_Nname);
            add(txt_E_Str);
            add(txt_E_Str_Nr);
            add(txt_E_PLZ);
            add(txt_E_Ort);
        }};

        // Togglegroup Zustelltypen
        rb_Brief.setToggleGroup(ZTYPEN);
        rb_Paeckchen.setToggleGroup(ZTYPEN);
        rb_Paket.setToggleGroup(ZTYPEN);

        // Togglegroup Versicherungstypen
        rb_bis100.setToggleGroup(VTYPEN);
        rb_bis500.setToggleGroup(VTYPEN);
        rb_ue500.setToggleGroup(VTYPEN);

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

    // Wenn Reset gedrückt wird, das Laden von Daten fehlschlägt, oder beim Programmstart
    public void reset() {
        kannSpeichern(false);
        istGeladen = false;
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

    // Im Falle eines Fehlers wird eine Meldung ausgegeben
    public void setMeldung(String msg, Color clr) {
        lbl_Meldung.setText(msg);
        lbl_Meldung.setTextFill(clr);
    }

    // Bei Veränderungen muss der Preis aktualisiert werden, damit man den Auftrag speichern kann
    public void kannSpeichern(boolean b) {
        btn_Speichern.setDisable(!b);
    }

    // erfasst den Auftrag
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
            if (dtp_Aufgegeben.getValue().isBefore(LocalDate.now()) && !istGeladen) {
                throw new IllegalArgumentException("Aufgabedatum darf nicht in der Vergangenheit liegen");
            }
            auftrag.setAufgegeben(dtp_Aufgegeben.getValue());

            // Absender, Empfänger, Beschreibung
            Person absender = new Person('A', aList.get(0), aList.get(1), aList.get(2), aList.get(3), aList.get(4), aList.get(5));
            Person empfaenger = new Person('E', eList.get(0), eList.get(1), eList.get(2), eList.get(3), eList.get(4), eList.get(5));
            auftrag.setAbsender(absender);
            auftrag.setEmpfaenger(empfaenger);
            auftrag.setBeschreibung(txta_Beschreibung.getText());

            // Zustellung
            auftrag.setExpress(cb_Express.isSelected());
            RadioButton rb = (RadioButton) ZTYPEN.getSelectedToggle();
            Auftrag.zTyp zTyp = null;
            switch (rb.getText()) {
                case "Brief" -> zTyp = Auftrag.zTyp.BRIEF;
                case "Päckchen" -> zTyp = Auftrag.zTyp.PAECKCHEN;
                case "Paket" -> zTyp = Auftrag.zTyp.PAKET;
            }

            auftrag.setVersandoption(zTyp);
            auftrag.setWunschtermin(dtp_Wunschtermin.getValue());
            if (cb_AltAblage.isSelected() && txta_AltAblage.getText().isEmpty()) {
                throw new IllegalArgumentException("Alt. Ablageort Pflichtfeld!");
            }
            if (!txta_AltAblage.getText().isEmpty()) auftrag.setAltAblageOrt(txta_AltAblage.getText());

            // Versicherung
            auftrag.setVersichert(cb_Versichert.isSelected());
            rb = (RadioButton) VTYPEN.getSelectedToggle();
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
            setMeldung("Berechnung erfolgreich", Color.rgb(0, 200, 0));
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    // holt den Auftrag und setzt alle Felder mit den richtigen Werten
    public void setFelder() {
        // Oberer Bereich
        txt_ID.setText(auftrag.getId());
        dtp_Aufgegeben.setValue(auftrag.getAufgegeben());

        // Absender
        absenderList.get(0).setText(auftrag.getAbsender().getVorname());
        absenderList.get(1).setText(auftrag.getAbsender().getNachname());
        absenderList.get(2).setText(auftrag.getAbsender().getStrasse());
        absenderList.get(3).setText(auftrag.getAbsender().getHausNr());
        absenderList.get(4).setText(auftrag.getAbsender().getPlz());
        absenderList.get(5).setText(auftrag.getAbsender().getOrt());

        // Empfänger
        empfaengerList.get(0).setText(auftrag.getEmpfaenger().getVorname());
        empfaengerList.get(1).setText(auftrag.getEmpfaenger().getNachname());
        empfaengerList.get(2).setText(auftrag.getEmpfaenger().getStrasse());
        empfaengerList.get(3).setText(auftrag.getEmpfaenger().getHausNr());
        empfaengerList.get(4).setText(auftrag.getEmpfaenger().getPlz());
        empfaengerList.get(5).setText(auftrag.getEmpfaenger().getOrt());

        // Beschreibung
        txta_Beschreibung.setText(auftrag.getBeschreibung());

        // Zustellung
        cb_Express.setSelected(auftrag.isExpress());
        switch (auftrag.getVersandoption()) {
            case BRIEF -> rb_Brief.setSelected(true);
            case PAECKCHEN -> rb_Paeckchen.setSelected(true);
            case PAKET -> rb_Paket.setSelected(true);
        }
        on_Ztyp_Select();
        if (auftrag.getWunschtermin() != null) {
            dtp_Wunschtermin.setValue(auftrag.getWunschtermin());
            on_Wunschtermin_Picked();
        }
        if (auftrag.getAltAblageOrt() != null) {
            cb_AltAblage.setSelected(true);
            on_AltAblage_Check();
            txta_AltAblage.setText(auftrag.getAltAblageOrt());
        }

        // Versicherung
        cb_Versichert.setSelected(auftrag.isVersichert());
        on_Versichert_Check();
        if (auftrag.isVersichert() && auftrag.getVersandoption() != Auftrag.zTyp.BRIEF) {
            switch (auftrag.getVersicherung()) {
                case B100 -> rb_bis100.setSelected(true);
                case B500 -> rb_bis500.setSelected(true);
                case UE500 -> rb_ue500.setSelected(true);
            }
            on_Vtyp_Selected();
            if (auftrag.getVersicherung() == Auftrag.vTyp.UE500) {
                txt_Betrag.setText(DEC.format(auftrag.getBetrag()));
            }
        }

        // Preisberechnung
        sld_Rabatt.setValue(auftrag.getRabatt());
        txt_Preis.setText(DEC.format(auftrag.getPreis()));
    }

    // Der Speicherprozess
    public void speichern() {
        try {
            String file = getFile();
            String msg = Datenverarbeitung.toCSV(file, auftrag);
            reset();

            // Wenn Speichern erfolgreich war
            setMeldung(msg, Color.rgb(0, 200, 0));
        } catch (Exception e) {
            setMeldung(e.getMessage(), Color.rgb(200, 0, 0));
        }
    }

    // Der Ladeprozess
    public void laden() {
        try {
            String file = getFile();

            Auftrag a = new Auftrag();
            a.setId(txt_ID.getText());
            auftrag = Datenverarbeitung.fromCSV(file, a.getId());

            // Falls keine ID gefunden wurde
            if (auftrag == null) {
                throw new IllegalArgumentException("Keine passende ID vorhanden!");
            }

            // Falls Laden erfolgreich war
            setFelder();
            istGeladen = true;
            setMeldung("Laden erfolgreich", Color.rgb(0, 200, 0));
        } catch (Exception e) {
            reset();
            setMeldung(e.getMessage(), Color.rgb(200, 0, 0));
        }
    }

    // Der Berechnungsprozess
    public void berechnen() {
        try {
            auftragErfassung();
            txt_Preis.setText(auftrag.berechne());

            // Bei erfolgreicher Berechnung kann man speichern
            kannSpeichern(true);
        } catch (IllegalArgumentException msg) {
            setMeldung(msg.getMessage(), Color.rgb(200, 0, 0));
        }
    }

    // holt den Dateipfad und setzt den Namen der .csv Datei
    public String getFile() {
        return "files\\versand-" + dtp_Aufgegeben.getValue() + ".csv";
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
                // Sobald die Höchstgrenze überschritten wird, sorgt man dafür, dass nichts Weiteres geschrieben werden kann
                if (tf.getText().length() > maxLength) {
                    String s = tf.getText().substring(0, maxLength);
                    tf.setText(s);
                }
            }
        });
    }
    //endregion

    //region Sichtbarkeit für Versicherung
    //blendet den Versicherungsbereich aus, wenn Brief ausgewählt
    public void versicherungVisibility(boolean b) {
        tpane_Versicherung.setVisible(b);
        if (!b) {
            vTypenVisibility(true);
            cb_Versichert.setSelected(false);
            on_Versichert_Check();
        }
    }

    // Blendet die Radiobuttons aus, wenn versichert false ist
    public void vTypenVisibility(boolean b) {
        pane_Versicherung.setVisible(b);
        if (!b) {
            rb_bis100.setSelected(true);
            on_Vtyp_Selected();
        }
    }

    // Blendet den Betrag aus, wenn über 500€ nicht ausgewählt ist
    public void betragVisibility(boolean b) {
        pane_Betrag.setVisible(b);
        if (!b) {
            txt_Betrag.clear();
        } else {
            txt_Betrag.setText("500.01");
        }
    }
    //endregion
    //endregion
}