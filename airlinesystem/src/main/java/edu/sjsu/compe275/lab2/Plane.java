package edu.sjsu.compe275.lab2;

import javax.persistence.Embeddable;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * Created by Amruta on 4/15/2017.
 */
@Embeddable
public class Plane {

    private int capacity;


     private String model;

    private String manufacturer;

    private int yearOfManufacture;

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getYearOfManufacture() {
        return yearOfManufacture;
    }

    public void setYearOfManufacture(int yearOfManufacture) {
        this.yearOfManufacture = yearOfManufacture;
    }
    
    public JSONObject getJSON() throws JSONException
    {
        JSONObject plane=new JSONObject();
        plane.put("capacity",this.getCapacity());
        plane.put("model",this.getModel());
        plane.put("manufacturer",this.getManufacturer());
        plane.put("yearOfManufacture",this.getYearOfManufacture());
        return plane;
    }
}
