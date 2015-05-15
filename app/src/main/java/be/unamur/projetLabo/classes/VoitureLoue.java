package be.unamur.projetLabo.classes;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VoitureLoue extends Voiture {
    Date start;
    Date end;

    public VoitureLoue(JSONObject voitObj) {
        super(voitObj);
        try {
            //a tester :D
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
            this.start = formatter.parse(voitObj.getString("Start"));
            this.end = formatter.parse(voitObj.getString("End"));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }
}
