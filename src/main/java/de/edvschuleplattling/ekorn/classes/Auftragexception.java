package de.edvschuleplattling.ekorn.classes;

public class Auftragexception {
    public static void istBlank(String typ, String text) {
        if (text.isBlank()) {
            throw new IllegalArgumentException(typ + " Pflichtfeld!");
        }
    }

    public static void istNichtBuchstabe(String typ, String text) {
        for (int i = 0; i < text.length(); i++) {
            if (!Character.isAlphabetic(text.codePointAt(i))) {
                throw new IllegalArgumentException(typ + " darf nur Buchstaben enthalten!");
            }
        }
    }

    public static void istNichtZahl(String typ, String text) {
        for (int i = 0; i < text.length(); i++) {
            if (!Character.isDigit(text.codePointAt(i))) {
                throw new IllegalArgumentException(typ + " darf nur Zahlen enthalten!");
            }
        }
    }
}
