package com.conteus.adventure.companion.controller.settings;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.conteus.adventure.companion.controller.MainActivity;


public class Settings {


    private String keyCaller;     // Liste der Telefonnummern, die via SMS steuern dürfen
    private String keyNotfallNummer;    // Telefonnummer die eine SMS bekommt, wenn etwas ganz schief geht
    private String smsSignature;           // Zeichenfolge die in der SMS am Anfang stehen muss
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext());

    // Eine (versteckte) Klassenvariable vom Typ der eigenen Klasse
    private static Settings instance;
    // Verhindere die Erzeugung des Objektes über andere Methoden
    private Settings () {}
    // Eine Zugriffsmethode auf Klassenebene, welches dir '''einmal''' ein konkretes
    // Objekt erzeugt und dieses zurückliefert.
    public static Settings getInstance () {
        if (Settings.instance == null) {
            Settings.instance = new Settings ();
        }
        return Settings.instance;
    }

    private void storeToPref(String key, String value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value); // value to store
        editor.commit();
    }

    public String getKeyCaller() {
        return prefs.getString("key_caller", "+41795432109");
    }

    public void setKeyCaller(String keyCaller) {
        this.keyCaller = keyCaller;
        storeToPref("key_caller", keyCaller);
    }

    public String getKeyNotfallNummer() {
        return prefs.getString("key_notfall_nummer", "+41795432109");
    }
    public void setKeyNotfallNummer(String keyNotfallNummer) {
        this.keyNotfallNummer = keyNotfallNummer;
        storeToPref("key_notfall_nummer", keyNotfallNummer);
    }
}
