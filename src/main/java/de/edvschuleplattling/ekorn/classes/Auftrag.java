package de.edvschuleplattling.ekorn.classes;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static de.edvschuleplattling.ekorn.classes.Auftragexception.*;

public class Auftrag {
    //region ATTRIBUTES
    private String id;
    private LocalDate aufgegeben;
    private Person absender;
    private Person empfaenger;
    private String beschreibung;
    private boolean express;
    private zTyp versandoption;
    private LocalDate wunschtermin;
    private String altAblageOrt;
    private boolean versichert;
    private vTyp versicherung;
    private double betrag;
    private double rabatt;
    private double preis;

    private static final double[] BASISPREIS = {0.60, 3.20, 5.50};
    private static final double[] EXPRESSAUFSCHLAG = {4.00, 6.00, 6.00};
    private static final double[] VERSICHERUNGEN = {1.20, 2.00, 0.005};

    //endregion

    //region ENUMS
    public enum zTyp {BRIEF, PAECKCHEN, PAKET}

    public enum vTyp {B100, B500, UE500}
    //endregion

    //region CONSTRUCTORS
    public Auftrag(String id, LocalDate aufgegeben, Person absender, Person empfaenger, String beschreibung,
                   boolean express, zTyp versandoption, LocalDate wunschtermin, String altAblageOrt,
                   boolean versichert, vTyp versicherung, double betrag, double rabatt) {
        setId(id);
        setAufgegeben(aufgegeben);
        setAbsender(absender);
        setEmpfaenger(empfaenger);
        setBeschreibung(beschreibung);
        setExpress(express);
        setVersandoption(versandoption);
        setWunschtermin(wunschtermin);
        setAltAblageOrt(altAblageOrt);
        setVersichert(versichert);
        setVersicherung(versicherung);
        setBetrag(betrag);
        setRabatt(rabatt);
    }

    public Auftrag() {
        this("000000000000000", LocalDate.now(), null, null,
                null, false, zTyp.BRIEF, null,
                null, false, null, 0, 0);
    }
    //endregion

    //region GETTER/SETTER
    public String getId() {
        return id;
    }

    public void setId(String id) {
        istBlank("ID", id);
        istNichtZahl("ID", id);
        if (id.length() != 15) {
            throw new IllegalArgumentException("ID muss 15 Zeichen lang sein!");
        }
        this.id = id;
    }

    public LocalDate getAufgegeben() {
        return aufgegeben;
    }

    public void setAufgegeben(LocalDate aufgegeben) {
        istBlank("Aufgabedatum", aufgegeben.format(DateTimeFormatter.ISO_LOCAL_DATE));
        this.aufgegeben = aufgegeben;
    }

    public Person getAbsender() {
        return absender;
    }

    public void setAbsender(Person absender) {
        this.absender = absender;
    }

    public Person getEmpfaenger() {
        return empfaenger;
    }

    public void setEmpfaenger(Person empfaenger) {
        this.empfaenger = empfaenger;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public boolean isExpress() {
        return express;
    }

    public void setExpress(boolean express) {
        this.express = express;
    }

    public zTyp getVersandoption() {
        return versandoption;
    }

    public void setVersandoption(zTyp versandoption) {
        this.versandoption = versandoption;
    }

    public LocalDate getWunschtermin() {
        return wunschtermin;
    }

    public void setWunschtermin(LocalDate wunschtermin) {
        this.wunschtermin = wunschtermin;
    }

    public String getAltAblageOrt() {
        return altAblageOrt;
    }

    public void setAltAblageOrt(String altAblageOrt) {
        this.altAblageOrt = altAblageOrt;
    }

    public boolean isVersichert() {
        return versichert;
    }

    public void setVersichert(boolean versichert) {
        this.versichert = versichert;
    }

    public vTyp getVersicherung() {
        return versicherung;
    }

    public void setVersicherung(vTyp versicherung) {
        if (versicherung != vTyp.UE500) {
            setBetrag(0);
        }
        if (versandoption != zTyp.BRIEF && versichert) {
            this.versicherung = versicherung;
        } else {
            this.versicherung = null;
        }
    }

    public double getBetrag() {
        return betrag;
    }

    public void setBetrag(double betrag) {
        this.betrag = betrag;
    }

    public void setBetrag(String betrag) {
        istBlank("Betrag", betrag);
        double btrg = Double.parseDouble(betrag);
        if (versicherung == vTyp.UE500 && btrg <= 500.00) {
            throw new IllegalArgumentException("Betrag muss größer als 500€ sein!");
        }
        try {
            this.betrag = btrg;
        } catch (Exception e) {
            throw new IllegalArgumentException("Betrag darf nur Zahlen enthalten");
        }
    }

    public double getRabatt() {
        return rabatt;
    }

    public void setRabatt(double rabatt) {
        this.rabatt = rabatt;
    }

    public double getPreis() {
        return preis;
    }

    public void setPreis(double preis) {
        this.preis = preis;
    }
    //endregion

    public String berechne() {
        // Zustellpreis
        int zTypIndex = getVersandoption().ordinal();
        double zPreis = BASISPREIS[zTypIndex];
        if (express) {
            zPreis += EXPRESSAUFSCHLAG[zTypIndex];
        }
        if (wunschtermin != null) {
            zPreis += 0.50;
        }

        // versicherungspreis
        double vPreis = 0;
        if (versichert) {
            int vTypIndex = getVersicherung().ordinal();
            if (vTypIndex != 2) {
                vPreis = VERSICHERUNGEN[vTypIndex];
            } else {
                vPreis = getBetrag() * VERSICHERUNGEN[vTypIndex];
            }
        }

        // Gesamtpreis
        double preis = (zPreis + vPreis) * ((100 - getRabatt()) / 100);
        String preisString = String.format("%.2f", preis).replace(',', '.');
        preis = Double.parseDouble(preisString);
        setPreis(preis);
        return preisString;
    }

    @Override
    public String toString() {
        return "Auftrag '" + id + "' (aufgegeben am: " + aufgegeben + ')' +
                "\n==================================================" +
                "\nAbsender: " + absender +
                "\nEmpfänger: " + empfaenger +
                "\nBeschreibung: " + beschreibung +
                "\n--------------------------------------------------" +
                "\nExpress: " + express + ", Versandoption: " + versandoption +
                "\nWunschtermin: " + '\''+ wunschtermin + '\'' +
                ", alternativer Ablageort: " + altAblageOrt +
                "\n--------------------------------------------------" +
                "\nVersichert: " + versichert + " (" + versicherung + ") " +
                ", Betrag: " + betrag + "€" +
                "\n--------------------------------------------------" +
                "\nRabatt: " + rabatt + "%" +
                ", Preis: " + preis + "€" +
                "\n==================================================";
    }
}

