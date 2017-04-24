package edu.sjsu.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import edu.sjsu.compe275.lab2.Flight;
import edu.sjsu.compe275.lab2.Passenger;

/**
 * Created by Amruta on 4/17/2017.
 */


public interface FlightRepository extends CrudRepository<Flight, Integer> {

	void updateBySeatsLeft(String number, int seatsLeft);
}