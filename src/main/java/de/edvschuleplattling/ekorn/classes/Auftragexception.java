package de.edvschuleplattling.ekorn.classes;

public class Auftragexception {
    // Fehlerprüfung für Pflichtfelder
    public static void istBlank(String typ, String text) {
        if (text.isBlank()) {
            throw new IllegalArgumentException(typ + " Pflichtfeld!");
        }
    }

    // Fehlerprüfung für Felder, die nur Buchstaben erlauben
    public static void istNichtBuchstabe(String typ, String text) {
        for (int i = 0; i < text.length(); i++) {
            if (!Character.isAlphabetic(text.codePointAt(i))) {
                throw new IllegalArgumentException(typ + " darf nur Buchstaben enthalten!");
            }
        }
    }

    // Fehlerprüfung für Felder, die nur Zahlen erlauben
    public static void istNichtZahl(String typ, String text) {
        for (int i = 0; i < text.length(); i++) {
            if (!Character.isDigit(text.codePointAt(i))) {
                throw new IllegalArgumentException(typ + " darf nur Zahlen enthalten!");
            }
        }
    }
}
