package be.unamur.projetLabo.classes;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import be.unamur.projetLabo.ProjetLabo;
import be.unamur.projetLabo.exception.SetToAPIException;
import be.unamur.projetLabo.request.OkHttpStack;
import be.unamur.projetLabo.request.PostRequest;

public class Voiture implements Serializable {
    protected int id;
    protected String name; //modèle, nom voiture.
    protected int nbSeat;
    protected int nbDoor;
    protected boolean manualTransmission;
    protected String path;
    protected float price; //Per day
    protected boolean openDoor;
    protected boolean openEtui;
    protected boolean keyInEtui;
    protected float fuelQuantity;

    public Voiture(){

    }

    public Voiture(JSONObject voitObj){
        try {
            this.id = voitObj.getInt("Id");
            this.name = voitObj.getString("Name");
            this.nbSeat = voitObj.getInt("NbSeat");
            this.nbDoor = voitObj.getInt("NbDoor");
            this.manualTransmission = voitObj.getBoolean("ManualTransmission");
            this.path = voitObj.getString("Path");
            this.price = voitObj.getLong("Price");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public Voiture( int id, String name, int nbSeat, int nbDoor, boolean manualTransmission, float price) {
        this.path = "";
        this.id = id;
        this.name = name;
        this.nbSeat = nbSeat;
        this.nbDoor = nbDoor;
        this.manualTransmission = manualTransmission;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNbSeat() {
        return nbSeat;
    }

    public void setNbSeat(int nbSeat) {
        this.nbSeat = nbSeat;
    }

    public int getNbDoor() {
        return nbDoor;
    }

    public void setNbDoor(int nbDoor) {
        this.nbDoor = nbDoor;
    }

    public boolean isManualTransmission() {
        return manualTransmission;
    }

    public void setManualTransmission(boolean manualTransmission) {
        this.manualTransmission = manualTransmission;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public boolean isOpenDoor() {
        return openDoor;
    }

    public void setOpenDoor(boolean openDoor) {
        this.openDoor = openDoor;
    }

    public boolean isOpenEtui() {
        return openEtui;
    }

    public void setOpenEtui(boolean openEtui) {
        this.openEtui = openEtui;
    }

    public boolean isKeyInEtui() {
        return keyInEtui;
    }

    public void setKeyInEtui(boolean keyInEtui) {
        this.keyInEtui = keyInEtui;
    }

    public float getFuelQuantity() {
        return fuelQuantity;
    }

    public void setFuelQuantity(float fuelQuantity) {
        this.fuelQuantity = fuelQuantity;
    }

    public boolean setToApi(final Context context){
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", Integer.toString(this.id));
        params.put("name", this.name);
        params.put("nbSeat", Integer.toString(this.nbSeat));
        params.put("nbDoor", Integer.toString(this.nbDoor));
        params.put("manualTransmission", Boolean.toString(this.manualTransmission));
        params.put("path", this.path);
        params.put("price", Float.toString(this.price));
        params.put("openDoor", Boolean.toString(this.openDoor));
        params.put("openEtui", Boolean.toString(this.openEtui));
        params.put("keyInEtui", Boolean.toString(this.keyInEtui));
        params.put("fuelQuantity", Float.toString(this.fuelQuantity));

        String URL = ProjetLabo.API_BASE_URL + "/objects/voitures.json";

        PostRequest requestAddUser = new PostRequest(URL, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject userJSON = new JSONObject(s);
                    if (userJSON.has("response")) {
                        if(userJSON.getBoolean("response")) {

                        }else{
                            Toast.makeText(context, "Impossible de mettre à jour votre voiture", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(context, "Impossible de mettre à jour votre voiture", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "Impossible de mettre à jour votre voiture", Toast.LENGTH_LONG).show();
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

        return true;
    }
}
