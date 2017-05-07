package edu.sjsu.controller;

import static com.monitorjbl.json.Match.match;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monitorjbl.json.JsonView;
import com.monitorjbl.json.JsonViewModule;

import edu.sjsu.compe275.lab2.Flight;
import edu.sjsu.compe275.lab2.Passanger;
import edu.sjsu.compe275.lab2.Reservation;
import edu.sjsu.dao.FlightRepository;
import edu.sjsu.dao.PassengerRepository;

/**
 * Created by Amruta on 4/15/2017.
 */
@RestController
@ComponentScan(value = "edu.sjsu.cmpe275.lab2.dao")
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

	@Autowired
	FlightRepository flightRepository;

	//ObjectMapper mapper = new ObjectMapper().registerModule(new JsonViewModule());
	

	// ---------------get a passenger ------------------------------------

	@RequestMapping(params = "xml", value = "/passenger/{id}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<?> getPassangerXML(@PathVariable("id") String id, @RequestParam boolean xml)
			throws JSONException {

		if (!passengerRepository.exists(id)) {
			logger.error("Unable to update. Passanger with id {} not found.", id);
			model.clear();
			model.addAttribute("BadRequest", model2);
			model2.addAttribute("code", "404");
			String st = "Passanger with Number " + id + " does not exist";
			model2.addAttribute("msg", st);

			return new ResponseEntity(model,HttpStatus.BAD_REQUEST);

		} else {
			Passanger p = passengerRepository.findOne(id);
			
			JSONObject json_test = new JSONObject(p.getFullJSON().toString());
			String xml_test = XML.toString(json_test);
			return new ResponseEntity(xml_test,HttpStatus.OK);
		}

		// return new ResponseEntity(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(params = "json", value = "/passenger/{id}", method = RequestMethod.GET/*, produces = "application/json"*/)
	@ResponseBody	
	public ResponseEntity<Object> getPassangerJSON(@PathVariable("id") String id, @RequestParam boolean json) throws JSONException {
		
		
		Passanger p = passengerRepository.findById(id);
		if (p == null) {
			logger.error("Unable to update. Passanger with id {} not found.", id);
			// String num = "404";
			// rm.setCode(num);
			// rm.setMsg("Passanger with Number " + id + " does not exist");
			model.clear();
			model.addAttribute("BadRequest", model2);
			model2.addAttribute("code", "404");
			String st = "Passanger with Number " + id + " does not exist";
			model2.addAttribute("msg", st);

			return new ResponseEntity(model,HttpStatus.BAD_REQUEST);

		} else {
			JSONObject json_test = new JSONObject(p.getFullJSON().toString());
			System.out.println(p.getFullJSON());
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			return new ResponseEntity(p.getFullJSON().toString(),headers,HttpStatus.OK);
		}
	}

	// -------------------Create a
	// passenger-------------------------------------------

	@RequestMapping(value = "/passenger", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ResponseEntity<?> createPassanger(@RequestParam(value = "firstname", required = true) String firstname,
			@RequestParam(value = "lastname", required = true) String lastname,
			@RequestParam(value = "age", required = true) int age,
			@RequestParam(value = "gender", required = true) String gender,
			@RequestParam(value = "phone", required = true) String phone) {
		logger.info("Creating passenger : {}", firstname);

		Passanger pass = null;
		try {
			pass = new Passanger(firstname, lastname, gender, age, phone);
			passengerRepository.save(pass);
			//JSONObject json_test = new JSONObject(pass.getFullJSON().toString());
			//String xml_test = XML.toString(json_test);
			return new ResponseEntity(pass.getJSON().toString(),HttpStatus.OK);
		
		} catch (Exception ex) {
			ex.getMessage();
			model.clear();
			model.addAttribute("BadRequest", model2);
			model2.addAttribute("code", "404");
			String st = "User with phone number already exists";
			model2.addAttribute("msg", st);

			return ResponseEntity.ok(model);

		}

	}

	// ------------------- Update a User
	// ------------------------------------------------

	@RequestMapping(value = "/passenger/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updatePassanger(@PathVariable("id") String id,
			@RequestParam(value = "firstname", required = true) String firstname,
			@RequestParam(value = "lastname", required = true) String lastname,
			@RequestParam(value = "age", required = true) int age,
			@RequestParam(value = "gender", required = true) String gender,
			@RequestParam(value = "phone", required = true) String phone) throws JSONException {
		logger.info("Updating passenger with id {}", id);

		Passanger p = passengerRepository.findById(id);
		if (p == null) {
			logger.error("Unable to update. Passanger with id {} not found.", id);
			model.clear();
			model.addAttribute("BadRequest", model2);
			model2.addAttribute("code", "404");
			String st = "User with id" + id + " does not exist";
			model2.addAttribute("msg", st);

			return ResponseEntity.ok(model);
		}

		p.setAge(age);
		p.setFirstname(firstname);
		p.setGender(gender);
		p.setLastname(lastname);
		p.setPhone(phone);

		p = passengerRepository.save(p);
		JSONObject json_test = new JSONObject(p.getFullJSON().toString());
		//String xml_test = XML.toString(json_test);
		return new ResponseEntity(json_test,HttpStatus.OK);

	}

	// ------------------- Delete a
	// passenger-----------------------------------------

	@RequestMapping(value = "/passenger/{id}", method = RequestMethod.DELETE, produces = {
			MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(value = HttpStatus.ACCEPTED)
	public ResponseEntity<?> deletePassanger(@PathVariable("id") String id) throws JSONException {
		logger.info("Fetching & Deleting flight with number {}", id);

		Passanger p = passengerRepository.findById(id);
		if (p == null) {
			logger.error("Unable to delete Passanger with id {} not found.", id);
			model.clear();
			model.addAttribute("BadRequest", model2);
			model2.addAttribute("code", "404");
			String st = "Passanger with id " + id + " does not exist";
			model2.addAttribute("msg", st);
			return ResponseEntity.ok(model);
		} else {
			// List<Flight> flights = p.getFlights();
			for (Reservation r : p.getReservations()) {
				for (Flight flight : r.getFlights()) {
					flight.setSeatsLeft(flight.getSeatsLeft() + 1);
					flightRepository.save(flight);
				}
			}
			passengerRepository.delete(id);
			model = new ModelMap();
			model.clear();
			String numb = "200";
			model.addAttribute("Response", model2);
			model2.addAttribute("code", "200");
			String st = "Passanger with Number " + id + " is deleted successfully";
			model2.addAttribute("msg", st);
			JSONObject json_test = new JSONObject(model);
			String xml_test = XML.toString(json_test);

			return ResponseEntity.ok(xml_test);
		}
	}
}
