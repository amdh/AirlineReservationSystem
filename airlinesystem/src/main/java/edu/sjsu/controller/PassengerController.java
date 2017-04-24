package edu.sjsu.controller;

import java.io.IOException;
import java.util.HashMap;
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

import edu.sjsu.compe275.lab2.Passenger;
import edu.sjsu.dao.PassengerRepository;
import javassist.tools.web.BadHttpRequest;

/**
 * Created by Amruta on 4/15/2017.
 */
@RestController
@EnableAutoConfiguration
public class PassengerController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    public static final Logger logger = LoggerFactory.getLogger(PassengerController.class);

    @Autowired
    PassengerRepository passengerRepository;

    //---------------get a passenger ------------------------------------

    @RequestMapping(value = "/passenger/{id}?xml=true", method = RequestMethod.GET,  produces={MediaType.APPLICATION_XML_VALUE})
    public Passenger getPassengerXML(@PathVariable("id") String id) {
        Passenger p = passengerRepository.findById(id);

        return p;

     //   return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/passenger/{id}?json=true", method = RequestMethod.GET, produces ={MediaType.APPLICATION_JSON_VALUE})
    public Passenger getPassengerJSON(@PathVariable("id") String id) {
        Passenger p = passengerRepository.findById(id);

        return p;
    }

    // -------------------Create a passenger-------------------------------------------

    @RequestMapping(value = "/passenger",  method = RequestMethod.POST,  produces ={MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Passenger> createPassenger(@RequestParam(value="firstname", required=true) String firstname,@RequestParam(value="lastname", required=true) String lastname,
    		@RequestParam(value="age", required=true) int age,@RequestParam(value="gender", required=true) String gender,@RequestParam(value="phone", required=true) String phone) {
        logger.info("Creating passenger : {}", firstname);
        Passenger passenger;
        try{
        	passenger = passengerRepository.save(new Passenger(firstname, lastname, gender, age, phone));
        }catch (Exception ex) {
        	 String errorCode = "400 - Bad Request";
             String errorMsg = "Requested URL doesn't exist";

             return new ResponseEntity<Passenger>(HttpStatus.BAD_REQUEST);
        
        }
        return ResponseEntity.ok(passenger);
    }

    // ------------------- Update a User ------------------------------------------------

    @RequestMapping(value = "/passenger/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Passenger> updatePassenger(@PathVariable("id") String id , @RequestParam(value="firstname", required=true) String firstname,@RequestParam(value="lastname", required=true) String lastname,
    		@RequestParam(value="age", required=true) int age,@RequestParam(value="gender", required=true) String gender,@RequestParam(value="phone", required=true) String phone) {
        logger.info("Updating passenger with id {}", id);

        Passenger p = passengerRepository.findById(id);
        if(p == null){
        	 logger.error("Unable to update. Passenger with id {} not found.", id);
        	 return new ResponseEntity<Passenger>(HttpStatus.NOT_FOUND);
        }
        
        p.setAge(age);
        p.setFirstname(firstname);
        p.setGender(gender);
        p.setLastname(lastname);
        p.setPhone(phone);
        
        p = passengerRepository.save(p);
        return ResponseEntity.ok(p);
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

    // ------------------- Delete a passenger-----------------------------------------

    @RequestMapping(value = "/passenger/{id}", method = RequestMethod.DELETE,  produces ={MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public ResponseEntity<Object> deletePassenger(@PathVariable("id") String id) {
        logger.info("Fetching & Deleting passenger with id {}", id);

        Passenger p = passengerRepository.findById(id);
        if(p == null){
        	 logger.error("Unable to update. Passenger with id {} not found.", id);
        	 return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        passengerRepository.delete(id);
        
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