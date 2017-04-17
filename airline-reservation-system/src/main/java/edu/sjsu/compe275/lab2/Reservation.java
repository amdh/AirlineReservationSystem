package edu.sjsu.compe275.lab2;

import java.util.*;
/**
 * Created by Amruta on 4/15/2017.
 */
public class Reservation {
    private String orderNumber;
    private Passenger passenger;
    private int price; // sum of each flight’s price.
    private List<Flight> flights;


    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
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
}
