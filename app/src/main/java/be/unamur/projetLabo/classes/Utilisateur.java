package be.unamur.projetLabo.classes;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;

import be.unamur.projetLabo.ProjetLabo;
import be.unamur.projetLabo.exception.UserConnectionException;

public class Utilisateur {


    private String login;
    private String password;
    private boolean fidele, contrated;
    private int id;
    private VoitureLoue voiture;
    private int capital;



    public Utilisateur(JSONObject userJSON)throws UserConnectionException{
        try{
            this.id = userJSON.getInt("id");
            this.login = userJSON.getString("login");
            this.password = userJSON.getString("password");
            this.fidele = userJSON.getBoolean("fidele");
            this.capital = userJSON.getInt("capital");
            if(userJSON.has("voiture")){
                this.voiture = new VoitureLoue(userJSON.getJSONObject("voiture"));
            }
        } catch (JSONException e) {
            throw new UserConnectionException("Utilisateur(JSONObject userJSON)");
        }
        try{
            this.contrated = userJSON.getBoolean("contrat");
        } catch (JSONException e) {}
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
