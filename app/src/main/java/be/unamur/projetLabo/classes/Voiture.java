package be.unamur.projetLabo.classes;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Voiture  implements Serializable {
    private int id;
    private String name; //modèle, nom voiture.
    private int nbSeat;
    private int nbDoor;
    private boolean manualTransmission;
    private String path;
    private float price; //Per day
    private boolean openDoor;
    private boolean openEtui;
    private boolean keyInEtui;
    private float fuelQuantity;

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

}
