package edu.sjsu.compe275.lab2;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import javax.persistence.*;

import java.util.Date;
import java.util.List;

/**
 * Created by Amruta on 4/15/2017.
 */
@Entity
public class Flight {

    @Id
    @Column(name="flight_number",unique = true)
    private String number; // Each flight has a unique flight number.

    private int price;

    @Column(name="from_source")
    private String from;

    @Column(name="to_destination")
    private String to;

    /*  Date format: yy-mm-dd-hh, do not include minutes and sceonds.
    ** Example: 2017-03-22-19
    The system only needs to supports PST. You can ignore other time zones.*/

  //  @JsonFormat(pattern="yyyy-MM-dd-HH")
    private Date departureTime;

    //@JsonFormat(pattern="yyyy-MM-dd-HH")
    private Date arrivalTime;

    private int seatsLeft;

    private String description;

   @Embedded
    private Plane plane;  // Embedded

  
   @ManyToMany
   @JoinTable(name="flight_passenger",
   joinColumns= { @JoinColumn(name = "flight_umber", referencedColumnName ="flight_number")},
   inverseJoinColumns={@JoinColumn(name="passenger_id" , referencedColumnName="passanger_id")}) 
    private List<Passanger> passengers;
  
   @JsonIgnore
   @ManyToMany(mappedBy="flights")
   private List<Reservation> reservations;
    
    public Flight(){
    	
    }

    public Flight(String number){
    	this.number = number;
    }

    
    public Flight(String flightNumber,String from , String to , int price , int capacity , String description, Date aTime , Date  dTime, String manufacturer, String model, int yearsofM ){
    	this.arrivalTime = aTime;
    	this.number = flightNumber;
    	this.departureTime = dTime;
    	this.from = from;
    	this.to = to;
    	this.price = price;
    	this.seatsLeft = capacity;
    	this.description = description;
    	this.plane = new Plane();
    	this.plane.setModel(model);
    	this.plane.setCapacity(capacity);
    	this.plane.setManufacturer(manufacturer);
    	this.plane.setYearOfManufacture(yearsofM);
        	
    }
    
    public Flight(String from , String to , int price , int seatsLeft , String description, Date aTime , Date  dTime, Plane p, List<Passanger> pList){
    	this.arrivalTime = aTime;
    	this.departureTime = dTime;
    	this.from = from;
    	this.to = to;
    	this.price = price;
    	this.seatsLeft = seatsLeft;
    	this.description = description;
    	this.plane = p;
    	this.passengers = pList;
    }
    //---------------setter getter----------------

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getSeatsLeft() {
        return seatsLeft;
    }

    public void setSeatsLeft(int seatsLeft) {
        this.seatsLeft = seatsLeft;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Plane getPlane() {
        return plane;
    }

    public void setPlane(Plane plane) {
        this.plane = plane;
    }

  
    public List<Passanger> getPassangers() {
        return passengers;
    }

    public void setPassangers(List<Passanger> passengers) {
        this.passengers = passengers;
    }

	public List<Passanger> getPassengers() {
		return passengers;
	}

	public void setPassengers(List<Passanger> passengers) {
		this.passengers = passengers;
	}

	public List<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}
	
	public JSONObject getJSON() throws JSONException
    {
        JSONObject flightJson=new JSONObject();
        flightJson.put("number",this.getNumber());
        flightJson.put("price",this.getPrice());
        flightJson.put("from",this.getFrom());
        flightJson.put("to",this.getTo());
        flightJson.put("departureTime",this.getDepartureTime());
        flightJson.put("arrivalTime",this.getArrivalTime());
        flightJson.put("seatsLeft",this.getSeatsLeft());
        flightJson.put("description",this.getDescription());
        flightJson.put("plane",this.getPlane().getJSON());
        return flightJson;
    }

    /**
     * Flight Data as JSONObject inclusive of all Passengers details
     * @return JSONObject
     * @throws JSONException 
     */
    public JSONObject getFullJson() throws JSONException
    {
        JSONObject resultObject=new JSONObject();
        JSONObject flight=this.getJSON();
        JSONObject passengers=new JSONObject();
        JSONArray passengerArray=new JSONArray();
        for(Passanger passenger:this.getPassengers())
        {
            JSONObject pass=passenger.getJSON();
            passengerArray.put(pass);
        }
        passengers.put("passenger",passengerArray);
        flight.put("passengers",passengers);
        resultObject.put("flight",flight);
        return resultObject;
    }
}

