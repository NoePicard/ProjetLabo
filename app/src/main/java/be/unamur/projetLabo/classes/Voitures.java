package be.unamur.projetLabo.classes;

import org.json.JSONException;
import org.json.JSONObject;

public class Voitures {
    private int id;
    private String name;
    private int nbSeat;
    private int nbDoor;
    private int manualTransmission;
    private String path;

    public Voitures(int id, String name, int nbSeat, int nbDoor, String path, int manualTransmission) {
        this.id = id;
        this.name = name;
        this.nbSeat = nbSeat;
        this.nbDoor = nbDoor;
        this.path = path;
        this.manualTransmission = manualTransmission;
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

    public int isManualTransmission() {
        return manualTransmission;
    }

    public void setManualTransmission(int manualTransmission) {
        this.manualTransmission = manualTransmission;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void hydrate(JSONObject voitObj){
        try {
            this.setId(voitObj.getInt("Id"));
            this.setName(voitObj.getString("Name"));
            this.setNbSeat(voitObj.getInt("NbSeat"));
            this.setNbDoor(voitObj.getInt("NbDoor"));
            this.setManualTransmission(voitObj.getInt("manualTransmission"));
            this.setPath(voitObj.getString("Path"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
