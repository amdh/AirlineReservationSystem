package edu.sjsu.controller;

import edu.sjsu.compe275.lab2.Passenger;
import edu.sjsu.dao.PassengerRepository;
import edu.sjsu.model.Greeting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

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
    public Passenger getPassenger(@PathVariable("id") String id) {
        Passenger p = passengerRepository.findById(id);

        return p;

     //   return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/passenger/{id}?json=true", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Passenger getPassenger(@PathVariable("id") String id) {
        Passenger p = passengerRepository.findById(id);

        return p;
    }

    // -------------------Create a passenger-------------------------------------------

    @RequestMapping(value = "/passenger/", method = RequestMethod.POST)
    public String createPassenger(@RequestBody Passenger passenger) {
        logger.info("Creating passenger : {}", passenger);

        try{
            passengerRepository.save(passenger);
        }catch (Exception ex) {
            return "bas request";
        }
        return "User succesfully created with id = " + passenger.getId();
    }

    // ------------------- Update a User ------------------------------------------------

    @RequestMapping(value = "/passenger/{id}", method = RequestMethod.PUT)
    public void updatePassenger(@PathVariable("id") long id, @RequestBody Passenger passenger) {
        logger.info("Updating passenger with id {}", id);

        /*User currentUser = userService.findById(id);

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

    @RequestMapping(value = "/passenger/{id}", method = RequestMethod.DELETE)
    public void deletePassenger(@PathVariable("id") long id) {
        logger.info("Fetching & Deleting passenger with id {}", id);

       /* User user = userService.findById(id);
        if (user == null) {
            logger.error("Unable to delete. User with id {} not found.", id);
            return new ResponseEntity(new CustomErrorType("Unable to delete. User with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);
        }
        userService.deleteUserById(id);
        return new ResponseEntity<User>(HttpStatus.NO_CONTENT);*/
    }
}
