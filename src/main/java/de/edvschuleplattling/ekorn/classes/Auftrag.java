package de.edvschuleplattling.ekorn.classes;

import java.time.LocalDate;

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
    //endregion

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

    //region GETTER/SETTER
    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id.isBlank()) {
            throw new IllegalArgumentException("ID: darf nicht leer sein!");
        }

        try {
            Integer.parseInt(id);
        } catch (Exception e) {
            throw new IllegalArgumentException("ID: keine Buchstaben!");
        }
        this.id = id;
    }

    public LocalDate getAufgegeben() {
        return aufgegeben;
    }

    public void setAufgegeben(LocalDate aufgegeben) {
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
        this.versicherung = versicherung;
    }

    public double getBetrag() {
        return betrag;
    }

    public void setBetrag(double betrag) {
        this.betrag = betrag;
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

    private void setPreis(double preis) {
        this.preis = preis;
    }
    //endregion

    public double berechne() {
        return 0;
    }

    // ENUMS
    public enum zTyp {BRIEF, PAECKCHEN, PAKET}

    public enum vTyp {B100, B500, UE500}
}

