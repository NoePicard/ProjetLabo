package be.unamur.projetLabo.classes;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import be.unamur.projetLabo.ProjetLabo;
import be.unamur.projetLabo.request.OkHttpStack;
import be.unamur.projetLabo.request.PostRequest;

public class VoitureLoue extends Voiture {
    private Calendar start;
    private Calendar  end;
    private int idLocation;
    private boolean gps, rendu;

    public VoitureLoue(Voiture voiture, int idLocation, Calendar start, Calendar end){
        this.id = voiture.id;
        this.name = voiture.name;
        this.nbSeat = voiture.nbSeat;
        this.nbDoor = voiture.nbDoor;
        this.manualTransmission = voiture.manualTransmission;
        this.path = voiture.path;
        this.price = voiture.price;
        this.openEtui = voiture.openEtui;
        this.keyInEtui = voiture.keyInEtui;
        this.fuelQuantity = voiture.fuelQuantity;

        this.idLocation = idLocation;
        this.setStart(start);
        this.setEnd(end);
        this.rendu = false;
    }

    public VoitureLoue(JSONObject voitObj) {
        super(voitObj);
        try {
            //a tester :D
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-d");
            start = Calendar.getInstance();
            end = Calendar.getInstance();
            this.start.setTime(formatter.parse(voitObj.getString("Start")));
            this.end.setTime(formatter.parse(voitObj.getString("End")));
            this.idLocation = voitObj.getInt("IdLocation");
            this.rendu = voitObj.getBoolean("Rendu");
            this.fuelQuantity = (float) voitObj.getDouble("FuelQuantity");
            this.gps = voitObj.getBoolean("Gps");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int getIdLocation() {
        return idLocation;
    }

    public void setIdLocation(int idLocation) {
        this.idLocation = idLocation;
    }

    public Calendar getStart() {
        return start;
    }

    public void setStart(Calendar start) {
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);
        this.start = start;
    }

    public Calendar getEnd() {
        return end;
    }

    public void setEnd(Calendar end) {
        end.set(Calendar.HOUR_OF_DAY, 0);
        end.set(Calendar.MINUTE, 0);
        end.set(Calendar.SECOND, 0);
        end.set(Calendar.MILLISECOND, 0);
        this.end = end;
    }

    public boolean isGps() {
        return gps;
    }

    public void setGps(boolean gps) {
        this.gps = gps;
    }

    public boolean isRendu() {
        return rendu;
    }

    public void setRendu(boolean rendu) {
        this.rendu = rendu;
    }

    public boolean setToApi(final Context context){
        super.setToApi(context);

        Map<String, String> params = new HashMap<String, String>();
        params.put("id", Integer.toString(this.idLocation));
        params.put("idUser", Integer.toString(ProjetLabo.user.getId()));
        params.put("idVoiture", Integer.toString(this.id));
        params.put("start", Long.toString(this.start.getTimeInMillis()));
        params.put("end", Long.toString(this.end.getTimeInMillis()));
        params.put("gps", Boolean.toString(this.gps));
        params.put("rendu", Boolean.toString(this.rendu));

        String URL = ProjetLabo.API_BASE_URL + "/objects/locations.json";

        PostRequest requestAddUser = new PostRequest(URL, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject userJSON = new JSONObject(s);
                    if (userJSON.has("response")) {
                        if(userJSON.getBoolean("response")) {

                        }else{
                            Toast.makeText(context, "Impossible de mettre à jour votre location", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(context, "Impossible de mettre à jour votre location", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "Impossible de mettre à jour votre location", Toast.LENGTH_LONG).show();
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
