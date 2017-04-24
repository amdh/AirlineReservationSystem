package edu.sjsu.dao;

import edu.sjsu.compe275.lab2.Passenger;
import edu.sjsu.compe275.lab2.Reservation;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Amruta on 4/17/2017.
 */


public interface ReservationRepository extends CrudRepository<Reservation, String> {

  
}