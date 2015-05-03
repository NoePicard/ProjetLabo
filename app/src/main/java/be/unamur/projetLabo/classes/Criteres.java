package be.unamur.projetLabo.classes;

import org.json.JSONException;
import org.json.JSONObject;

import be.unamur.projetLabo.ProjetLabo;

public class Criteres {
    private String name;
    private String type;

    public Criteres(String name, String type){
        this.name = name;
        this.type = type;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getType(){
        return type;
    }
    public void setType(String type){
        this.type = type;
    }
    public String toString(){
        return name + "\n" + type;
    }

    public void hydrate(JSONObject criObj){
        try{
            this.setName(criObj.getString("Name"));
            this.setType(criObj.getString("Type"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
