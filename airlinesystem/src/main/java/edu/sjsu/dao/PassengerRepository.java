package edu.sjsu.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.compe275.lab2.Passanger;

import java.util.List;

/**
 * Created by Amruta on 4/17/2017.
 */


public interface PassengerRepository extends CrudRepository<Passanger, String> {

    List<Passanger> findByFirstname(String firstname);
    
    Passanger findById(String id);

	void delete(String id);
	
	/*@Query("select p from flight_passenger where passenger_id=")*/
}
