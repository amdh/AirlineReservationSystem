package edu.sjsu.compe275.lab2;

/**
 * Created by Amruta on 4/15/2017.
 */
@Entity
public class Plane {

    private int capacity;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(type="text")
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
}
