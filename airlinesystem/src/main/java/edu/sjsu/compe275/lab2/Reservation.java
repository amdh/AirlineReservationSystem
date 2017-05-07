package edu.sjsu.compe275.lab2;


import org.hibernate.annotations.GenericGenerator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import javax.persistence.*;

import java.util.List;
/**
 * Created by Amruta on 4/15/2017.
 */
@Entity
@Table(name = "reservation")
//@JsonIgnoreProperties({"hibernateLazyInitializer", "passanger"})
public class Reservation {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "passenger_id")
      private Passanger passanger;

    private int price; // sum of each flight’s price.


    @ManyToMany
    @JoinTable(
            name="reservation_flight",
            joinColumns=@JoinColumn(name="reservation_id", referencedColumnName="orderNumber"),
            inverseJoinColumns=@JoinColumn(name="flight_id", referencedColumnName="flight_number"))
     private List<Flight> flights;

    

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

 
    public Passanger getPassenger() {
        return passanger;
    }

    public void setPassenger(Passanger passenger) {
        this.passanger = passenger;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }
    
    public JSONObject getFullJSON() throws JSONException
    {
        JSONObject result = new JSONObject();
        JSONObject reserv=new JSONObject();
        reserv.put("orderNumber",this.getOrderNumber());
        reserv.put("price",this.getPrice());
        JSONArray flights = new JSONArray();
        for (Flight flight:this.getFlights())
        {
            flights.put(flight.getJSON());
        }
        reserv.put("flights",new JSONObject().put("flight",flights));
        reserv.put("passenger",this.getPassenger().getJSON());
        result.put("reservation",reserv);
        return result;
    }

    /**
     * XML representation of Reservation JSONObject
     * @return String
     * @throws JSONException 
     */
    public String getXML() throws JSONException
    {
        return XML.toString(getFullJSON());
    }
    
}

