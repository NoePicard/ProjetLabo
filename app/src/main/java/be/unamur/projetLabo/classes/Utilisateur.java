package be.unamur.projetLabo.classes;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;

import be.unamur.projetLabo.ProjetLabo;
import be.unamur.projetLabo.exception.UserConnectionException;
import be.unamur.projetLabo.request.OkHttpStack;
import be.unamur.projetLabo.request.PostRequest;

public class Utilisateur {


    private String login;
    private String password;
    private boolean fidele, contrated;
    private int id;
    private VoitureLoue voiture;
    private float capital;



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
    public void setCapital (float capital){
        this.capital = capital;
    }
    public void addCapital(float capital){
        this.capital += capital;
    }
    public void subCapital(float capital){
        this.capital -= capital;
    }
    public int getCapital(){
        return (int) Math.floor(this.capital);
    }

    public VoitureLoue getVoiture() {
        return voiture;
    }
    public void setVoiture(VoitureLoue voiture) {
        this.voiture = voiture;
    }

    public void setToApi(final Context context) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", Integer.toString(this.id));
        params.put("login", this.login);
        params.put("password", this.password);
        params.put("contracted", Boolean.toString(this.contrated));
        params.put("capital", Float.toString(this.capital));

        String URL = ProjetLabo.API_BASE_URL + "/objects/users.json";

        PostRequest requestAddUser = new PostRequest(URL, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject userJSON = new JSONObject(s);
                    if (userJSON.has("response")) {
                        if (userJSON.getBoolean("response")) {

                        } else {
                            Toast.makeText(context, "Impossible de mettre à jour votre compte utilisateur", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(context, "Impossible de mettre à jour votre compte utilisateur", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "Impossible de mettre à jour votre compte utilisateur", Toast.LENGTH_LONG).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(context, "Une erreur réseau est survenue !", Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(context, new OkHttpStack());
        queue.add(requestAddUser);

        if (this.voiture != null) {
            this.voiture.setToApi(context);
        }
    }
}
