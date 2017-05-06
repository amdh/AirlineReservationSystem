package edu.sjsu.dao;

import edu.sjsu.compe275.lab2.Passenger;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Amruta on 4/17/2017.
 */


public interface PassengerRepository extends CrudRepository<Passenger, String> {

    List<Passenger> findByFirstname(String firstname);
    
    Passenger findById(String id);

	void delete(String id);
	
	/*@Query("select p from flight_passenger where passenger_id=")*/
}
