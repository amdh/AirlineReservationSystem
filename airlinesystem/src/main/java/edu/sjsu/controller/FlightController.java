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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.monitorjbl.json.JsonView;
import com.monitorjbl.json.JsonViewModule;

import edu.sjsu.compe275.lab2.Flight;
import edu.sjsu.dao.FlightRepository;
import javassist.tools.web.BadHttpRequest;
import static com.monitorjbl.json.Match.match;

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
    
    ObjectMapper mapper = new ObjectMapper().registerModule(new JsonViewModule());
    
    @RequestMapping(params = "xml", value = "/flight/{number}", method = RequestMethod.GET,  produces={MediaType.APPLICATION_XML_VALUE})
    public  ResponseEntity<?> getPassengerXML(@PathVariable("number") String number, @RequestParam boolean xml) {
        Flight f = flightRepository.findOne(number);

        return ResponseEntity.ok(f);
    }

    @RequestMapping(params = "json", value = "/flight/{number}", method = RequestMethod.GET, produces ={MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getPassengerJSON(@PathVariable("number") String number, @RequestParam boolean json) throws JsonProcessingException {
        Flight f = flightRepository.findOne(number);
        String jsonF = mapper.writeValueAsString(JsonView.with(f).onClass(Flight.class, match().exclude("reservations")));
        return ResponseEntity.ok(jsonF);
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

    @ExceptionHandler(BadHttpRequest.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleUnsupportedMediaTypeException(
        HttpMediaTypeNotSupportedException ex) throws IOException {
        Map<String, Object> map =new HashMap();
        map.put("code", "404");
        map.put("msg", "Not found");
        return map;
    }
}
