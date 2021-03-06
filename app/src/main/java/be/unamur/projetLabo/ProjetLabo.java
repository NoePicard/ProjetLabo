package be.unamur.projetLabo;

import android.app.Application;
import android.content.Intent;

import java.util.Calendar;

import be.unamur.projetLabo.activities.InfoVehiculeActivity;
import be.unamur.projetLabo.activities.InscriptionActivity;
import be.unamur.projetLabo.classes.Utilisateur;

public class ProjetLabo extends Application {
    public static Utilisateur user = null;
    public static String BASE_URL = "http://labo.bewweb.be/";
    public static String API_BASE_URL = BASE_URL + "api";
    public static String CONTRAT_BASE_URL = "http://labo.bewweb.be/contrat";
    public static Calendar NOW(){
        Calendar date = Calendar.getInstance();
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        return date;
    }

}
