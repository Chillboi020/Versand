package de.edvschuleplattling.ekorn.classes;

public class Person {
    //region ATTRIBUTES
    private String vorname;
    private String nachname;
    private String strasse;
    private String hausNr;
    private String plz;
    private String ort;
    //endregion

    //region CONSTRUCTORS
    public Person(String vorname, String nachname, String strasse, String hausNr, String plz, String ort) {
        setVorname(vorname);
        setNachname(nachname);
        setStrasse(strasse);
        setHausNr(hausNr);
        setPlz(plz);
        setOrt(ort);
    }

    public Person() {
        this("Vorname", "Nachname", "Straße", "0", "00000", "Ort");
    }
    //endregion

    //region GETTER/SETTER
    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        istBlank("Vorname", vorname);
        istNummer("Vorname", vorname);
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        istBlank("Nachname", nachname);
        istNummer("Nachname", nachname);
        this.nachname = nachname;
    }

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        istBlank("Straße", strasse);
        istNummer("Straße", strasse);
        this.strasse = strasse;
    }

    public String getHausNr() { return hausNr; }

    public void setHausNr(String hausNr) {
        istBlank("HausNr", hausNr);
        if (hausNr.length() > 3) {
            throw new IllegalArgumentException("HausNr darf nur 3 Zeichen lang sein!");
        }
        this.hausNr = hausNr;
    }

    public String getPlz() {
        return plz;
    }

    public void setPlz(String plz) {
        istBlank("PLZ", plz);
        for (int i = 0; i < plz.length(); i++) {
            if (!Character.isDigit(plz.codePointAt(i))) {
                throw new IllegalArgumentException("PLZ darf nur Zahlen enthalten!");
            }
        }
        if (plz.length() != 5) {
            throw new IllegalArgumentException("PLZ muss 5 Zeichen lang sein!");
        }
        this.plz = plz;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        istBlank("Ort", ort);
        istNummer("Ort", ort);
        this.ort = ort;
    }

    private void istBlank(String typ, String text) {
        if (text.isBlank()) {
            throw new IllegalArgumentException(typ + " Pflichtfeld!");
        }
    }

    private void istNummer(String typ, String text) {
        for (int i = 0; i < text.length(); i++) {
            if (Character.isDigit(text.codePointAt(i))) {
                throw new IllegalArgumentException(typ + " darf keine Zahlen haben!");
            }
        }
    }
    //endregion


    @Override
    public String toString() {
        return "Person{" +
                "vorname='" + vorname + '\'' +
                ", nachname='" + nachname + '\'' +
                ", strasse='" + strasse + '\'' +
                ", hausNr='" + hausNr + '\'' +
                ", plz='" + plz + '\'' +
                ", ort='" + ort + '\'' +
                '}';
    }
}
