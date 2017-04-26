package edu.sjsu.compe275.lab2;

import java.util.*;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import java.util.Set;
/**
 * Created by Amruta on 4/15/2017.
 */
@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String orderNumber;

    @OneToOne(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinColumn(name = "id")
    private Passenger passenger;

    private int price; // sum of each flight’s price.


    @JoinTable(name="flight_reservation",
    joinColumns= { @JoinColumn(name = "orderNumber", referencedColumnName ="orderNumber")},
    inverseJoinColumns={@JoinColumn(name="flightNumber" , referencedColumnName="number")})
    @ManyToMany
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
