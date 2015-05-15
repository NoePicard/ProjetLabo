package be.unamur.projetLabo;

import android.app.Application;
import android.content.Intent;

import be.unamur.projetLabo.activities.InfoVehiculeActivity;
import be.unamur.projetLabo.activities.InscriptionActivity;
import be.unamur.projetLabo.classes.Utilisateur;

public class ProjetLabo extends Application {
    public static Utilisateur user = new Utilisateur();
    public static String BASE_URL = "http://labo.bewweb.be/";
    public static String API_BASE_URL = BASE_URL + "api";
    public static String CONTRAT_BASE_URL = "http://labo.bewweb.be/contrat";
}
