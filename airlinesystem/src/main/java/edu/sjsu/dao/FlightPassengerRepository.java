package edu.sjsu.dao;

import org.springframework.data.repository.CrudRepository;

import edu.sjsu.compe275.lab2.Passenger;

public interface FlightPassengerRepository  extends CrudRepository<Passenger, String>{

}
