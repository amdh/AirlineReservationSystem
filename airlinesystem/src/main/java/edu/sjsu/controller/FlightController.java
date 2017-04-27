package edu.sjsu.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.ui.ModelMap;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpHeaders;
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
import edu.sjsu.dao.FlightRepository;
import javassist.tools.web.BadHttpRequest;

/**
 * Created by Amruta on 4/15/2017.
 */
@RestController
@EnableAutoConfiguration
public class FlightController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    public static final Logger logger = LoggerFactory.getLogger(FlightController.class);
    Response rm = new Response();
    ModelMap model = new ModelMap();
    ModelMap model2 = new ModelMap();
    
    @Autowired
    FlightRepository flightRepository;
    
    @RequestMapping(params = "xml", value = "/flight/{number}", method = RequestMethod.GET,  produces={MediaType.APPLICATION_XML_VALUE})
    public  ResponseEntity<?> getPassengerXML(@PathVariable("number") String number, @RequestParam boolean xml) {
        Flight f = flightRepository.findOne(number);

        return ResponseEntity.ok(f);

     //   return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(params = "json", value = "/flight/{number}", method = RequestMethod.GET, produces ={MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getPassengerJSON(@PathVariable("number") String number, @RequestParam boolean json) {
        Flight f = flightRepository.findOne(number);
   //     return new ResponseEntity<>(p, HttpStatus.OK);
   //     return p;
        
        return ResponseEntity.ok(f);
    }
    
    // ------------------- Delete a flight-----------------------------------------

    @RequestMapping(value = "/airline/{number}", method = RequestMethod.DELETE/*,  produces = {MediaType.APPLICATION_XML_VALUE}*/)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public ResponseEntity<?> deleteFlight(@PathVariable("number") String number) throws JSONException {
        logger.info("Fetching & Deleting flight with number {}", number);

        Flight f = flightRepository.findOne(number);
        if(f == null){
        	 logger.error("Unable to update. Passenger with number {} not found.", number);
        	 model.addAttribute("BadRequest", model2);   
        	 model2.addAttribute("code", "404");
        	 model2.addAttribute("msg",number);
        	 String num = "200";
        	 rm.setCode(num);
        	 rm.setMsg("Flight with Number " + number + " is deleted successfully");
        	 return ResponseEntity.ok(model);
        	 //return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        	 rm.setCode(Integer.toString(200));
//         	 rm.setMsg(number);
//         	 flightRepository.delete(number);
//         	 return ResponseEntity.ok(rm);
        }else{
        	String numb = "200";
        	rm.setCode(numb);
       	    rm.setMsg("Flight with Number " + number + " is deleted successfully");
       	    model.addAttribute("BadRequest", model2);   
    	    model2.addAttribute("code", "200");
    	    String st = "Flight with Number " + number + " is deleted successfully";
    	    model2.addAttribute("msg",st);
       	    JSONObject json_test = new JSONObject(model);
       	    String xml_test = XML.toString(json_test);
        	flightRepository.delete(number);
        	return ResponseEntity.ok(xml_test);
        }        
    }
//    // -------------------Create a passenger-------------------------------------------
//
//    @RequestMapping(value = "/passenger",  method = RequestMethod.POST,  produces ={MediaType.APPLICATION_JSON_VALUE})
//    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
//    public ResponseEntity<Flight> createFlight(@RequestParam(value="firstname", required=true) String firstname,@RequestParam(value="lastname", required=true) String lastname,
//    		@RequestParam(value="age", required=true) int age,@RequestParam(value="gender", required=true) String gender,@RequestParam(value="phone", required=true) String phone) {
//        logger.info("Creating passenger : {}", firstname);
//        Flight passenger;
//        try{
//        	passenger = flightRepository.save(new Flight(firstname, lastname, gender, age, phone));
//        }catch (Exception ex) {
//        	 String errorCode = "400 - Bad Request";
//             String errorMsg = "Requested URL doesn't exist";
//
//             return new ResponseEntity<Flight>(HttpStatus.BAD_REQUEST);
//        
//        }
//        return ResponseEntity.ok(passenger);
//    }
//
//    // ------------------- Update a User ------------------------------------------------
//
//    @RequestMapping(value = "/passenger/{id}", method = RequestMethod.PUT)
//    public ResponseEntity<Flight> updateFlight(@PathVariable("id") String id , @RequestParam(value="firstname", required=true) String firstname,@RequestParam(value="lastname", required=true) String lastname,
//    		@RequestParam(value="age", required=true) int age,@RequestParam(value="gender", required=true) String gender,@RequestParam(value="phone", required=true) String phone) {
//        logger.info("Updating passenger with id {}", id);
//
//        Flight p = flightRepository.findById(id);
//        if(p == null){
//        	 logger.error("Unable to update. Flight with id {} not found.", id);
//        	 return new ResponseEntity<Flight>(HttpStatus.NOT_FOUND);
//        }
//        
//        p.setAge(age);
//        p.setFirstname(firstname);
//        p.setGender(gender);
//        p.setLastname(lastname);
//        p.setPhone(phone);
//        
//        p = flightRepository.save(p);
//        return ResponseEntity.ok(p);
//        User currentUser = userService.findById(id);
//         * 
//
//        if (currentUser == null) {
//            logger.error("Unable to update. User with id {} not found.", id);
//            return new ResponseEntity(new CustomErrorType("Unable to upate. User with id " + id + " not found."),
//                    HttpStatus.NOT_FOUND);
//        }
//
//        currentUser.setName(user.getName());
//        currentUser.setAge(user.getAge());
//        currentUser.setSalary(user.getSalary());
//
//        userService.updateUser(currentUser);
//        return new ResponseEntity<User>(currentUser, HttpStatus.OK);
//    }
//
//    // ------------------- Delete a passenger-----------------------------------------
//
//    @RequestMapping(value = "/passenger/{id}", method = RequestMethod.DELETE,  produces ={MediaType.APPLICATION_XML_VALUE})
//    @ResponseStatus(value = HttpStatus.ACCEPTED)
//    public ResponseEntity<Object> deleteFlight(@PathVariable("id") String id) {
//        logger.info("Fetching & Deleting passenger with id {}", id);
//
//        Flight p = flightRepository.findById(id);
//        if(p == null){
//        	 logger.error("Unable to update. Flight with id {} not found.", id);
//        	 return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        
//        flightRepository.delete(id);
//        
//        return (ResponseEntity<Object>) ResponseEntity.ok();
//        User user = userService.findById(id);
//        if (user == null) {
//            logger.error("Unable to delete. User with id {} not found.", id);
//            return new ResponseEntity(new CustomErrorType("Unable to delete. User with id " + id + " not found."),
//                    HttpStatus.NOT_FOUND);
//        }
//        userService.deleteUserById(id);
//        return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
//    }
//    
//    @ExceptionHandler(value = BadHttpRequest.class)  
//    public String badRequestHandler(BadHttpRequest e){  
//        return e.getMessage();  
//    } 
//    
//    @ExceptionHandler(BadHttpRequest.class)
//    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
//    public Map<String, Object> handleUnsupportedMediaTypeException(
//        HttpMediaTypeNotSupportedException ex) throws IOException {
//        Map<String, Object> map =new HashMap();
//        map.put("code", "404");
//       // map.put("cause", ex.getLocalizedMessage());
//        map.put("msg", "Not found");
//        return map;
    //}
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
