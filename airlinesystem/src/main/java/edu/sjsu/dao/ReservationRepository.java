package edu.sjsu.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import edu.sjsu.compe275.lab2.Reservation;

/**
 * Created by Amruta on 4/17/2017.
 */


public interface ReservationRepository extends CrudRepository<Reservation, String> {

//	@Query("")
//	List<Reservation> findByPassengerIDFromToFlightNo(String passengerId , String from_source , String to_source , String flightNumber);
//  
}
