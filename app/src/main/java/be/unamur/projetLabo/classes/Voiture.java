package be.unamur.projetLabo.classes;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Voiture  implements Serializable {
    private int id;
    private String name;
    private int nbSeat;
    private int nbDoor;
    private boolean manualTransmission;
    private String path;

    public Voiture( int id, String name, int nbSeat, int nbDoor, boolean manualTransmission) {
        this.path = "";
        this.id = id;
        this.name = name;
        this.nbSeat = nbSeat;
        this.nbDoor = nbDoor;
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

    public Voiture(JSONObject voitObj){
        try {
            this.id = voitObj.getInt("Id");
            this.name = voitObj.getString("Name");
            this.nbSeat = voitObj.getInt("NbSeat");
            this.nbDoor = voitObj.getInt("NbDoor");
            this.manualTransmission = voitObj.getBoolean("ManualTransmission");
            this.path = voitObj.getString("Path");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
