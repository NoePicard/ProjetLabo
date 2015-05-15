package be.unamur.projetLabo.classes;

import org.json.JSONException;
import org.json.JSONObject;

import be.unamur.projetLabo.ProjetLabo;

public class Utilisateur {


    private String login;
    private String password;
    private boolean fidele, connected, contrated;
    private int id;
    private VoitureLoue voiture;
    private int capital;



    public Utilisateur() {
        this.login = "";
        this.password = "";
        this.fidele = false;
        this.connected = false;
    }
    public Utilisateur(String login, String password) {
        this.login = login;
        this.password = password;
        this.fidele = false;
        this.connected = false;
    }
    public Utilisateur(String login, String password, boolean fidele) {
        this.login = login;
        this.password = password;
        this.fidele = fidele;
        this.fidele = false;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public boolean isFidele() {
        return fidele;
    }
    public void setFidele(boolean fidele) {
        this.fidele = fidele;
    }
    public boolean isContrated() {
        return contrated;
    }
    public void setContrated(boolean contrated) {
        this.contrated = contrated;
    }
    public boolean isConnected() {
        return connected;
    }
    public void connexion() {
        this.connected = true;
    }
    public void deconnexion() {
        this.connected = false;
    }
    public boolean hydrate(JSONObject userJSON){
        try{
            ProjetLabo.user.setId(userJSON.getInt("id"));
            ProjetLabo.user.setLogin(userJSON.getString("login"));
            ProjetLabo.user.setPassword(userJSON.getString("password"));
            ProjetLabo.user.setFidele(userJSON.getBoolean("fidele"));
            ProjetLabo.user.setCapital(userJSON.getInt("capital"));
            if(userJSON.has("voiture")){
                this.voiture = new VoitureLoue(userJSON.getJSONObject("voiture"));
            }
        } catch (JSONException e) {
            return false;
        }
        try{
        ProjetLabo.user.setContrated(userJSON.getBoolean("contrat"));
        } catch (JSONException e) {}

        return true;
    }
    public void setCapital (int capital){
        this.capital = capital;
    }
    public int getCapital(){return this.capital;}

    public VoitureLoue getVoiture() {
        return voiture;
    }
    public void setVoiture(VoitureLoue voiture) {
        this.voiture = voiture;
    }
}
