package be.unamur.projetLabo.classes;

import org.json.JSONException;
import org.json.JSONObject;

public class Voitures {
    private int id;
    private String name;
    private int nbSeat;
    private int nbDoor;
    private boolean manualTransmission;
    private String path;

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

    public Voitures(JSONObject voitObj){
        try {
            this.setId(voitObj.getInt("Id"));
            this.setName(voitObj.getString("Name"));
            this.setNbSeat(voitObj.getInt("NbSeat"));
            this.setNbDoor(voitObj.getInt("NbDoor"));
            this.setManualTransmission(voitObj.getBoolean("manualTransmission"));
            this.setPath(voitObj.getString("Path"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
