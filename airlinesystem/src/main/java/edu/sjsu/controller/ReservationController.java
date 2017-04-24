package edu.sjsu.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import edu.sjsu.compe275.lab2.Flight;
import edu.sjsu.compe275.lab2.Passenger;
import edu.sjsu.compe275.lab2.Reservation;
import edu.sjsu.dao.ReservationRepository;
import javassist.tools.web.BadHttpRequest;

/**
 * Created by Amruta on 4/15/2017.
 */
@RestController
@EnableAutoConfiguration
public class ReservationController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    public static final Logger logger = LoggerFactory.getLogger(ReservationController.class);

    @Autowired
   ReservationRepository resRepository;

    //---------------get a reservation ------------------------------------

  

    @RequestMapping(value = "/reservation/{id}?json=true", method = RequestMethod.GET, produces ={MediaType.APPLICATION_JSON_VALUE})
    public Reservation getReservationJSON(@PathVariable("id") String id) {
        Reservation p = resRepository.findOne(id);

        return p;
    }

    // -------------------Create a reservation-------------------------------------------

    @RequestMapping(value = "/reservation /number",  method = RequestMethod.POST,  produces ={MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Reservation> createReservation(@RequestParam(value="passengerId", required=true) String passengerId,@RequestParam(value="flightLists", required=true) String flightLists) {
        logger.info("Creating reservation : {}", passengerId);
        
        Reservation reservation = new Reservation();
        reservation.setPassenger(new Passenger(passengerId));
        List flights = (List) new ArrayList<>();
        for (String flight : flightLists.split(",")) {
        	flights.add(new Flight(flight));
		}
        
        reservation.setFlights(flights);
        try{
        	reservation = resRepository.save(reservation);
        }catch (Exception ex) {
        	 String errorCode = "400 - Bad Request";
             String errorMsg = "Requested URL doesn't exist";

             return new ResponseEntity<Reservation>(HttpStatus.BAD_REQUEST);
        
        }
        return ResponseEntity.ok(reservation);
    }

    // ------------------- Update a User ------------------------------------------------

    @RequestMapping(value = "/reservation/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Reservation> updateReservation(@PathVariable("id") String id , @RequestParam(value="firstname", required=true) String firstname,@RequestParam(value="lastname", required=true) String lastname,
    		@RequestParam(value="age", required=true) int age,@RequestParam(value="gender", required=true) String gender,@RequestParam(value="phone", required=true) String phone) {
        logger.info("Updating reservation with id {}", id);

        Reservation r = resRepository.findOne(id);
        if(r == null){
        	 logger.error("Unable to update. Reservation with id {} not found.", id);
        	 return new ResponseEntity<Reservation>(HttpStatus.NOT_FOUND);
        }
        
      
        
        r = resRepository.save(r);
        return ResponseEntity.ok(r);
        /*User currentUser = userService.findById(id);
         * 

        if (currentUser == null) {
            logger.error("Unable to update. User with id {} not found.", id);
            return new ResponseEntity(new CustomErrorType("Unable to upate. User with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);
        }

        currentUser.setName(user.getName());
        currentUser.setAge(user.getAge());
        currentUser.setSalary(user.getSalary());

        userService.updateUser(currentUser);
        return new ResponseEntity<User>(currentUser, HttpStatus.OK);*/
    }

    // ------------------- Delete a reservation-----------------------------------------

    @RequestMapping(value = "/reservation/{id}", method = RequestMethod.DELETE,  produces ={MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public ResponseEntity<Object> deleteReservation(@PathVariable("id") String id) {
        logger.info("Fetching & Deleting reservation with id {}", id);

        Reservation p = resRepository.findOne(id);
        if(p == null){
        	 logger.error("Unable to update. Reservation with id {} not found.", id);
        	 return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        resRepository.delete(id);
        
        return (ResponseEntity<Object>) ResponseEntity.ok();
       /* User user = userService.findById(id);
        if (user == null) {
            logger.error("Unable to delete. User with id {} not found.", id);
            return new ResponseEntity(new CustomErrorType("Unable to delete. User with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);
        }
        userService.deleteUserById(id);
        return new ResponseEntity<User>(HttpStatus.NO_CONTENT);*/
    }
    
   /* @ExceptionHandler(value = BadHttpRequest.class)  
    public String badRequestHandler(BadHttpRequest e){  
        return e.getMessage();  
    } */
    
    @ExceptionHandler(BadHttpRequest.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleUnsupportedMediaTypeException(
        HttpMediaTypeNotSupportedException ex) throws IOException {
        Map<String, Object> map =new HashMap();
        map.put("code", "404");
       // map.put("cause", ex.getLocalizedMessage());
        map.put("msg", "Not found");
        return map;
    }
}
