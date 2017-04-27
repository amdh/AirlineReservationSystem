package edu.sjsu.compe275.lab2;


import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Amruta on 4/15/2017.
 */
@Entity
public class Flight {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String number; // Each flight has a unique flight number.

    private int price;

    @Column(name="from_source")
    private String from;

    @Column(name="to_destination")
    private String to;

    /*  Date format: yy-mm-dd-hh, do not include minutes and sceonds.
    ** Example: 2017-03-22-19
    The system only needs to supports PST. You can ignore other time zones.*/

    private Date departureTime;

    private Date arrivalTime;

    private int seatsLeft;

    private String description;

   @Embedded
    private Plane plane;  // Embedded

   @OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    private List<Passenger> passengers;
    
    public Flight(){
    	
    }

    public Flight(String number){
    	this.number = number;
    }

    
    public Flight(String from , String to , int price , int seatsLeft , String description, Date aTime , Date  dTime){
    	this.arrivalTime = aTime;
    	this.departureTime = dTime;
    	this.from = from;
    	this.to = to;
    	this.price = price;
    	this.seatsLeft = seatsLeft;
    	this.description = description;
    	
    }
    
    public Flight(String from , String to , int price , int seatsLeft , String description, Date aTime , Date  dTime, Plane p, List<Passenger> pList){
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
    //---------------setter getter

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

  
    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }
}

