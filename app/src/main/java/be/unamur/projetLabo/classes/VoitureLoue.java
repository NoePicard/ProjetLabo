package be.unamur.projetLabo.classes;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class VoitureLoue extends Voiture {
    private Calendar start;
    private Calendar  end;
    private int idLocation;

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
        this.start = start;
        this.end = end;
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
        this.start = start;
    }

    public Calendar getEnd() {
        return end;
    }

    public void setEnd(Calendar end) {
        this.end = end;
    }


}
