package edu.sjsu.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import edu.sjsu.compe275.lab2.Flight;
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
    Response rm = new Response();
    ModelMap model = new ModelMap();
    ModelMap model2 = new ModelMap();
    ModelMap model_passenger = new ModelMap();

    @Autowired
    PassengerRepository passengerRepository;

    //---------------get a passenger ------------------------------------

    @RequestMapping(params = "xml", value = "/passenger/{id}", method = RequestMethod.GET/*,  produces={MediaType.APPLICATION_XML_VALUE}*/)
    public  ResponseEntity<?> getPassengerXML(@PathVariable("id") String id, @RequestParam boolean xml) throws JSONException {
        Passenger p = passengerRepository.findById(id);
        if(p == null){
          	 logger.error("Unable to update. Passenger with id {} not found.", id);
          	// String num = "404";
//           rm.setCode(num);
//          	 rm.setMsg("Passenger with Number " + id + " does not exist");
          	 model.addAttribute("BadRequest", model2);   
       	 model2.addAttribute("code", "404");
       	String st = "Passenger with Number " + id + " does not exist";
    	    model2.addAttribute("msg",st);
    	    
          	 return ResponseEntity.ok(model);
          	 
          }else{
        	  model_passenger.addAttribute("Pasenger", p);
        	  JSONObject json_test = new JSONObject(model_passenger);
         	  String xml_test = XML.toString(json_test);
       	   return ResponseEntity.ok(xml_test);
          }
     //   return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(params = "json", value = "/passenger/{id}", method = RequestMethod.GET, produces ={MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getPassengerJSON(@PathVariable("id") String id, @RequestParam boolean json) {
        Passenger p = passengerRepository.findById(id);
        if(p == null){
       	 logger.error("Unable to update. Passenger with id {} not found.", id);
       	// String num = "404";
//       	 rm.setCode(num);
//       	 rm.setMsg("Passenger with Number " + id + " does not exist");
       	 model.addAttribute("BadRequest", model2);   
    	 model2.addAttribute("code", "404");
    	String st = "Passenger with Number " + id + " does not exist";
 	    model2.addAttribute("msg",st);
 	    
       	 return ResponseEntity.ok(model);
       	 
       }else{
    	   return ResponseEntity.ok(p);
       }
    }

    // -------------------Create a passenger-------------------------------------------

    @RequestMapping(value = "/passenger",  method = RequestMethod.POST/*,  produces ={MediaType.APPLICATION_JSON_VALUE}*/)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> createPassenger(@RequestParam(value="firstname", required=true) String firstname,@RequestParam(value="lastname", required=true) String lastname,
    		@RequestParam(value="age", required=true) int age,@RequestParam(value="gender", required=true) String gender,@RequestParam(value="phone", required=true) String phone) {
        logger.info("Creating passenger : {}", firstname);
        Passenger pass = passengerRepository.findOne(phone);
        System.out.println(pass);
        
        	try{
            	pass = passengerRepository.save(new Passenger(firstname, lastname, gender, age, phone));
            }catch (Exception ex) {
            	 model.addAttribute("BadRequest", model2);   
            	 model2.addAttribute("code", "404");
            	String st = "User with phone number already exists";
         	    model2.addAttribute("msg",st);
         	    
               	 return ResponseEntity.ok(model);             
            
            }
            return ResponseEntity.ok(pass);       
    }

    // ------------------- Update a User ------------------------------------------------

    @RequestMapping(value = "/passenger/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updatePassenger(@PathVariable("id") String id , @RequestParam(value="firstname", required=true) String firstname,@RequestParam(value="lastname", required=true) String lastname,
    		@RequestParam(value="age", required=true) int age,@RequestParam(value="gender", required=true) String gender,@RequestParam(value="phone", required=true) String phone) {
        logger.info("Updating passenger with id {}", id);

        Passenger p = passengerRepository.findById(id);
        if(p == null){
        	 logger.error("Unable to update. Passenger with id {} not found.", id);
        	 model.addAttribute("BadRequest", model2);   
        	 model2.addAttribute("code", "404");
        	String st = "User with id" + id + " does not exist";
     	    model2.addAttribute("msg",st);
     	    
           	 return ResponseEntity.ok(model); 
        }
        
        p.setAge(age);
        p.setFirstname(firstname);
        p.setGender(gender);
        p.setLastname(lastname);
        p.setPhone(phone);
        
        p = passengerRepository.save(p);
        return ResponseEntity.ok(p);
    
    }

    // ------------------- Delete a passenger-----------------------------------------

    @RequestMapping(value = "/passenger/{id}", method = RequestMethod.DELETE/*,  produces ={MediaType.APPLICATION_XML_VALUE}*/)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public ResponseEntity<?> deletePassenger(@PathVariable("id") String id) throws JSONException {
        logger.info("Fetching & Deleting flight with number {}", id);

        Passenger p = passengerRepository.findById(id);
        if(p == null){
       	 logger.error("Unable to delete Passenger with id {} not found.", id);
       	 model.addAttribute("BadRequest", model2);   
       	 model2.addAttribute("code", "404");
       	String st = "passenger with id " + id + " does not exist";
       	 model2.addAttribute("msg",st);
       	return ResponseEntity.ok(model);
       }else{
    	   model = new ModelMap();
       	String numb = "200";
       	model.addAttribute("Response", model2);   
   	    model2.addAttribute("code", "200");
   	    String st = "passenger with Number " + id + " is deleted successfully";
   	    model2.addAttribute("msg",st);
      	    JSONObject json_test = new JSONObject(model);
      	    String xml_test = XML.toString(json_test);
       	passengerRepository.delete(id);
       	return ResponseEntity.ok(xml_test);
       }
//        if(p == null){
//        	 logger.error("Unable to update. Passenger with id {} not found.", id);
//        	 String num = "200";
//        	 rm.setCode(num);
//        	 rm.setMsg("Passenger with Number " + id + " is deleted successfully");
//        	 return ResponseEntity.ok(rm);
//        	 
//        }else{
//        	String numb = "200";
//        	rm.setCode(numb);
//       	    rm.setMsg("Passenger with Number " + id + " is deleted successfully");
//        	passengerRepository.delete(id);
//        	return ResponseEntity.ok(rm);
//        }        
    
    }
    
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
