package de.edvschuleplattling.ekorn.classes;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class Datenverarbeitung {
    // Ein Auftrag wird in eine .csv Datei eingeschrieben, oder auch überschrieben,
    // wenn der übergebene Auftrag eine existierende ID hat
    public static String toCSV(String path, Auftrag auftrag) {
        ArrayList<Auftrag> auftraege = new ArrayList<>();
        String msg = "Neuer Auftrag gespeichert: " + path;
        try {
            // Zuerst werden alle Aufträge geholt
            boolean neuerAuftrag = true;
            FileReader fr = new FileReader(path);
            Scanner sc = new Scanner(fr);

            while (sc.hasNextLine()) {
                String zeile = sc.nextLine();
                auftraege.add(buildFromLine(zeile));
            }

            // Prüfen, ob der zu speichernde Auftrag bereits existiert
            for (int i = 0; i < auftraege.size(); i++) {
                // Wenn true, dann wird der Eintrag überschrieben.
                if (auftraege.get(i).getId().equals(auftrag.getId())) {
                    neuerAuftrag = false;
                    auftraege.remove(i);
                    auftraege.add(i, auftrag);
                }
                System.out.println(auftraege.get(i));
            }

            String line;
            if (neuerAuftrag) {
                FileWriter fw = new FileWriter(path, true);
                line = buildLine(auftrag);
                fw.append(line).append("\n");
                fw.close();
            } else {
                FileWriter fw = new FileWriter(path);
                for (Auftrag a : auftraege) {
                    System.out.println("Auftrag");
                    line = buildLine(a);
                    fw.write(line + "\n");
                }
                fw.close();
                msg = "Auftrag ("+ auftrag.getId() + ") wurde überschrieben";
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return msg;
    }

    // Ein Auftrag wird ausgehend von der ID gelesen, sofern sie existiert
    public static Auftrag fromCSV(String path, String id) {
        Auftrag a = null;
        try {
            FileReader fr = new FileReader(path);
            Scanner sc = new Scanner(fr);

            while (sc.hasNextLine()) {
                String zeile = sc.nextLine();
                Auftrag aktuell = buildFromLine(zeile);
                if (aktuell.getId().equals(id)) {
                    a = aktuell;
                    break;
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Die Datei existiert nicht!");
        }
        return a;
    }

    // Auftrag wird zum Eintrag verarbeitet
    private static String buildLine(Auftrag auftrag) {
        StringBuilder sb = new StringBuilder();
        sb.append(auftrag.getId()).append(";");
        sb.append(auftrag.getAufgegeben()).append(";");

        sb.append(auftrag.getAbsender().getVorname()).append(";");
        sb.append(auftrag.getAbsender().getNachname()).append(";");
        sb.append(auftrag.getAbsender().getStrasse()).append(";");
        sb.append(auftrag.getAbsender().getHausNr()).append(";");
        sb.append(auftrag.getAbsender().getPlz()).append(";");
        sb.append(auftrag.getAbsender().getOrt()).append(";");

        sb.append(auftrag.getEmpfaenger().getVorname()).append(";");
        sb.append(auftrag.getEmpfaenger().getNachname()).append(";");
        sb.append(auftrag.getEmpfaenger().getStrasse()).append(";");
        sb.append(auftrag.getEmpfaenger().getHausNr()).append(";");
        sb.append(auftrag.getEmpfaenger().getPlz()).append(";");
        sb.append(auftrag.getEmpfaenger().getOrt()).append(";");

        sb.append(auftrag.getBeschreibung()).append(";");

        sb.append(auftrag.isExpress()).append(";");
        sb.append(auftrag.getVersandoption().toString()).append(";");
        sb.append(auftrag.getWunschtermin()).append(";");
        sb.append(auftrag.getAltAblageOrt()).append(";");

        sb.append(auftrag.isVersichert()).append(";");
        if (auftrag.getVersicherung() != null)  {
            sb.append(auftrag.getVersicherung().toString());
        } else {
            sb.append("null");
        }
        sb.append(";");
        sb.append(auftrag.getBetrag()).append(";");

        sb.append(auftrag.getRabatt()).append(";");
        sb.append(auftrag.getPreis());
        return sb.toString();
    }

    // Gelesener Eintrag wird zum Auftrag verarbeitet
    private static Auftrag buildFromLine(String line) {
        String[] daten = line.split(";");
        Auftrag a = new Auftrag();

        // 0 ID
        a.setId(daten[0]);
        // 1 Aufgabedatum
        a.setAufgegeben(LocalDate.parse(daten[1]));

        // 2, 3, 4, 5, 6, 7 Absender
        a.setAbsender(new Person('A', daten[2], daten[3], daten[4], daten[5], daten[6], daten[7]));
        // 8, 9, 10, 11, 12, 13 Empfänger
        a.setEmpfaenger(new Person('E', daten[8], daten[9], daten[10], daten[11], daten[12], daten[13]));

        // 14 Beschreibung
        a.setBeschreibung(daten[14]);

        // 15 Express
        a.setExpress(Boolean.parseBoolean(daten[15]));
        // 16 Versandtyp
        a.setVersandoption(Auftrag.zTyp.valueOf(daten[16]));
        // 17 Wunschtermin
        if (!daten[17].equals("null")) a.setWunschtermin(LocalDate.parse(daten[17]));
        // 18 Alt. Ablageort
        if (!daten[18].isEmpty()) a.setAltAblageOrt(daten[18]);

        // 19 Versichert
        a.setVersichert(Boolean.parseBoolean(daten[19]));
        // 20 Versicherung
        if (!daten[20].equals("null")) a.setVersicherung(Auftrag.vTyp.valueOf(daten[20]));
        // 21 Betrag
        a.setBetrag(daten[21]);

        // 22 Rabatt
        a.setRabatt(Double.parseDouble(daten[22]));

        // 23 Preis
        a.setPreis(Double.parseDouble(daten[23]));

        return a;
    }
}
