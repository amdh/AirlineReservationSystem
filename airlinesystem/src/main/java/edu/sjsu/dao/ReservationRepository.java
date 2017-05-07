package edu.sjsu.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import edu.sjsu.compe275.lab2.Reservation;

/**
 * Created by Amruta on 4/17/2017.
 */


public interface ReservationRepository extends CrudRepository<Reservation, String> {

//	@Query("")
//	List<Reservation> findByPassengerIDFromToFlightNo(String passengerId , String from_source , String to_source , String flightNumber);
	
	 /*@Query("select r from Reservation r inner join r.flights f where f.to_destination=(:to_destination)" +
	            " and f.source=(:source) and f.number=(:flightNumber) and r.passenger.id=(:passengerId)")
	    List<Reservation> findByAllParam(@Param("flightNumber") String flightNumber,
	                                  @Param("destination") String destination,
	                                  @Param("source") String source ,
	                                  @Param("passengerId")String passengerId );
	 
	  @Query("select r from Reservation r where r.passenger.id=(:passenger_id)")
	    List<Reservation> findByPassengerId(@Param("passenger_id")String passengerId);

	    @Query("select r from Reservation r inner join r.flights f where f.number=(:flightNumber)")
	    List<Reservation> findByFlightNumber(@Param("flightNumber") String flightNumber);

	    @Query("select r from Reservation r inner join r.flights f where f.source=(:source)")
	    List<Reservation> findBySource(@Param("source") String source);

	    @Query("select r from Reservation r inner join r.flights f where f.destination=(:destination)")
	    List<Reservation> findByDestination(@Param("destination") String destination);*/
}
