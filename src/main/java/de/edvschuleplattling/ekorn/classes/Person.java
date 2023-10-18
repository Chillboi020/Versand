package de.edvschuleplattling.ekorn.classes;
import static de.edvschuleplattling.ekorn.classes.Auftragexception.*;

public class Person {
    //region ATTRIBUTES
    private String vorname;
    private String nachname;
    private String strasse;
    private String hausNr;
    private String plz;
    private String ort;
    private final char PERSONART;
    //endregion

    //region CONSTRUCTORS
    public Person(char persArt, String vorname, String nachname, String strasse, String hausNr, String plz, String ort) {
        this.PERSONART = persArt;
        if (vorname.isBlank() && nachname.isBlank() && strasse.isBlank() && hausNr.isBlank() && plz.isBlank() && ort.isBlank()) {
            throw new IllegalArgumentException(PERSONART + ": Leere Felder");
        }
        setVorname(vorname);
        setNachname(nachname);
        setStrasse(strasse);
        setHausNr(hausNr);
        setPlz(plz);
        setOrt(ort);
    }
    //endregion

    //region GETTER/SETTER
    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        tryPerson("Vorname", vorname);
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        tryPerson("Nachname", nachname);
        this.nachname = nachname;
    }

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        tryPerson("Straße", strasse);
        this.strasse = strasse;
    }

    public String getHausNr() { return hausNr; }

    public void setHausNr(String hausNr) {
        try {
            istBlank("HausNr", hausNr);
        } catch (Exception e) {
            throw new IllegalArgumentException(PERSONART + ": " + e.getMessage());
        }
        this.hausNr = hausNr;
    }

    public String getPlz() {
        return plz;
    }

    public void setPlz(String plz) {
        try {
            istBlank("PLZ", plz);
            istNichtZahl("PLZ", plz);
        } catch (Exception e) {
            throw new IllegalArgumentException(PERSONART + ": " + e.getMessage());
        }
        if (plz.length() != 5) {
            throw new IllegalArgumentException(PERSONART + ": PLZ muss 5 Zeichen lang sein!");
        }
        this.plz = plz;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        tryPerson("Ort", ort);
        this.ort = ort;
    }
    //endregion

    // Fehlerprüfungen für einige Felder
    public void tryPerson(String type, String txt) {
        try {
            istBlank(type, txt);
            istNichtBuchstabe(type, txt);
        } catch (Exception e) {
            throw new IllegalArgumentException(PERSONART + ": " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return vorname + ", " + nachname + ", " + strasse + " " + hausNr + ", " + plz + ", " + ort;
    }
}
