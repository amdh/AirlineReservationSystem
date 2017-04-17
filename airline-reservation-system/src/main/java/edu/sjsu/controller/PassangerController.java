package edu.sjsu.controller;

import edu.sjsu.compe275.lab2.Passenger;
import edu.sjsu.model.Greeting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Amruta on 4/15/2017.
 */
@RestController
public class PassangerController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    public static final Logger logger = LoggerFactory.getLogger(PassangerController.class);


    //---------------get a passenger ------------------------------------

    @RequestMapping(value = "/passenger/{id}", method = RequestMethod.GET)
    public Passenger getPassenger(@PathVariable("id") long id) {
        return new Passenger("Amz");
    }

    // -------------------Create a passenger-------------------------------------------

    @RequestMapping(value = "/passenger/", method = RequestMethod.POST)
    public void createPassenger(@RequestBody Passenger passenger) {
        logger.info("Creating passenger : {}", passenger);

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
