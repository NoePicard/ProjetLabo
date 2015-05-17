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
    private boolean openDoor;
    private boolean openEtui;
    private boolean keyInEtui;
    private float fuelQuantity;

    public VoitureLoue(JSONObject voitObj) {
        super(voitObj);
        try {
            //a tester :D
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-d");
            start = Calendar.getInstance();
            end = Calendar.getInstance();
            this.start.setTime(formatter.parse(voitObj.getString("Start")));
            this.end.setTime(formatter.parse(voitObj.getString("End")));

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
}
