package edu.sjsu.controller;

import static com.monitorjbl.json.Match.match;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.catalina.ssi.ResponseIncludeWrapper;
import org.json.HTTP;
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
import org.springframework.ui.Model;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monitorjbl.json.JsonView;
import com.monitorjbl.json.JsonViewModule;

import edu.sjsu.compe275.lab2.Flight;
import edu.sjsu.compe275.lab2.Passanger;
import edu.sjsu.compe275.lab2.Reservation;
import edu.sjsu.dao.FlightRepository;
import edu.sjsu.dao.PassengerRepository;
import edu.sjsu.dao.ReservationRepository;
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

	@Autowired
	PassengerRepository passengerRepository;

	@Autowired
	ReservationRepository reservationRepository;

	ObjectMapper mapper = new ObjectMapper().registerModule(new JsonViewModule());

	@RequestMapping(params = "xml", value = "/flight/{number}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<?> getFlightXML(@PathVariable("number") String number, @RequestParam boolean xml)
			throws JSONException, JsonProcessingException {
		Flight f = flightRepository.findOne(number);
		if (f == null) {
			logger.error("Unable to update. Passenger with id {} not found.", number);
			model.clear();
			model2.clear();
			model.addAttribute("BadRequest", model2);
			model2.addAttribute("code", "404");
			String st = "Flight with Number " + number + " does not exist";
			model2.addAttribute("msg", st);

			/* return ResponseEntity.ok(model); */return new ResponseEntity(model, HttpStatus.NOT_FOUND);

		} else {
			/*String jsonF = mapper
					.writeValueAsString(JsonView.with(f).onClass(Flight.class, match().exclude("reservations")));
*/			
			JSONObject json_test = new JSONObject(f.getFullJson().toString());
			String xml_test = XML.toString(json_test);
			return ResponseEntity.ok(xml_test);
		}
	}

	@RequestMapping(params = "json", value = "/flight/{number}", method = RequestMethod.GET,  produces = "application/json")
	@ResponseBody
	public ResponseEntity<?> getFlightJSON(@PathVariable("number") String number, @RequestParam boolean json)
			throws JsonProcessingException, JSONException {
		Flight f = flightRepository.findOne(number);
		if (f == null) {
			logger.error("Unable to update. Passenger with id {} not found.", number);
			model.clear();
			model2.clear();
			model.addAttribute("BadRequest", model2);
			model2.addAttribute("code", "404");
			String st = "Flight with Number " + number + " does not exist";
			model2.addAttribute("msg", st);

			/* return ResponseEntity.ok(model); */return new ResponseEntity(model, HttpStatus.NOT_FOUND);

		} else {
			logger.debug(f.getJSON().toString());
			System.out.println("got flilght");
			/*String jsonF = mapper
					.writeValueAsString(JsonView.with(f).onClass(Flight.class, match().exclude("reservations")));
			*/
			//return new ResponseEntity(f, HttpStatus.OK);
			//return new ResponseEntity(f.getFullJson(),HttpStatus.OK);
			return ResponseEntity.ok(f.getFullJson().toString());
		}
	}

	// --------------------- POST Flight --------------------------------------

	@RequestMapping(value = "/flight/{flightNumber}", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public ResponseEntity<?> createOrUpdateFlight(@PathVariable("flightNumber") String flightNumber,
			@RequestParam(value = "price", required = true) int price,
			@RequestParam(value = "from", required = true) String from_source,
			@RequestParam(value = "to", required = true) String to_dest,
			@RequestParam(value = "departureTime", required = true) String departureTime,
			@RequestParam(value = "description", required = true) String description,
			@RequestParam(value = "arrivalTime", required = true) String arrivalTime,
			@RequestParam(value = "capacity", required = true) int capacity,
			@RequestParam(value = "model", required = true) String model,
			@RequestParam(value = "manufacturer", required = true) String manufacturer,
			@RequestParam(value = "yearOfManufacture", required = true) int yearOfManufacture) {

		logger.info("Creating flight : {}", model);
		Flight newFlight = null;
		try {

			DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH");
			Date dDate = df.parse(departureTime);
			Date aTime = df.parse(arrivalTime);
			System.out.println(dDate);
			if (!flightRepository.exists(flightNumber)) {
				Flight f = new Flight(flightNumber, from_source, to_dest, price, capacity, description, dDate, aTime,
						manufacturer, model, yearOfManufacture);
				newFlight = flightRepository.save(f);
				String xml = XML.toString(newFlight.getFullJson());
				System.out.println(xml);
				return new ResponseEntity(xml,HttpStatus.OK);

			} else {
				// flight already exists, so update details
				Flight existingFlight = flightRepository.findOne(flightNumber);
				if (!checkFlightCapacity(existingFlight, capacity)) {
					ModelMap mpdelmap = new ModelMap();
					mpdelmap.clear();
					model2.clear();
					mpdelmap.addAttribute("BadRequest", model2);
					model2.addAttribute("code", "400");
					String st = "Cannot reduce capacity,active reservation count for this flight is higher than the target capacity ";
					model2.addAttribute("msg", st);

					return ResponseEntity.ok(mpdelmap);

				}
			/*	List<Passanger> passengers = existingFlight.getPassengers();
				if (!passengers.isEmpty()) {
					for (Passanger passenger : passengers) {
						List<Flight> temp = new ArrayList<Flight>();
						temp.add(newFlight);
						if (checkOverlap(passenger, temp)) {
							ModelMap mpdelmap = new ModelMap();
							mpdelmap.clear();
							model2.clear();
							mpdelmap.addAttribute("BadRequest", model2);
							model2.addAttribute("code", "400");
							String st = "Flight Overlap";
							model2.addAttribute("msg", st);

							return ResponseEntity.ok(mpdelmap);
						}
						List<Flight> passengerFlights = passenger.getFlights();
						passengerFlights.remove(existingFlight);
						passengerFlights.add(newFlight);
						passenger.setFlights(passengerFlights);
						passengerRepository.save(passenger);
					}
				}*/
				List<Reservation> reservations = existingFlight.getReservations();
				if (!reservations.isEmpty()) {
					for (Reservation reservation : reservations) {
						List<Flight> reservedFlights = reservation.getFlights();
						reservedFlights.remove(existingFlight);
						reservedFlights.add(newFlight);
						reservation.setFlights(reservedFlights);
						reservationRepository.save(reservation);
					}
				}
				int oldPlaneCap = existingFlight.getPlane().getCapacity();
				int newPlaneCap = newFlight.getPlane().getCapacity();
				int changeInCapacity = Math.abs(oldPlaneCap - newPlaneCap);
				if (oldPlaneCap > newPlaneCap)
					newFlight.setSeatsLeft(existingFlight.getSeatsLeft() - changeInCapacity);
				else
					newFlight.setSeatsLeft(existingFlight.getSeatsLeft() + changeInCapacity);
				newFlight = flightRepository.save(newFlight);
				
				//return ResponseEntity.ok(newFlight.getFullJson().toString());
				String xml = XML.toString(newFlight.getFullJson());
				return new ResponseEntity(xml,HttpStatus.OK);
			}

		} catch (Exception ex) {
			ModelMap mpdelmap = new ModelMap();
			mpdelmap.clear();
			mpdelmap.addAttribute("BadRequest", model2);
			model2.addAttribute("code", "404");
			String st = "Flight cannot be created";
			model2.addAttribute("msg", st);

			return ResponseEntity.ok(mpdelmap);
		}

	}

	// ------------------- Delete a
	// flight-----------------------------------------

	@RequestMapping(value = "/airline/{number}", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<?> deleteFlight(@PathVariable("number") String number) throws JSONException {
		logger.info("Fetching & Deleting flight with number {}", number);

		Flight f = flightRepository.findOne(number);

		if (f == null) {
			logger.error("Unable to update. Passenger with number {} not found.", number);
			model.clear();
			model2.clear();
			model.addAttribute("BadRequest", model2);
			model2.addAttribute("code", "404");
			model2.addAttribute("msg", "Flight with Number " + number + " does not exist");

			JSONObject json_test = new JSONObject(model);
			String xml_test = XML.toString(json_test);
			flightRepository.delete(number);
			//return ResponseEntity.ok(xml_test);
			return new ResponseEntity(xml_test,HttpStatus.OK);
		} else {
			if (f.getReservations().size() > 0) {
				model.clear();
				model2.clear();
				model.addAttribute("BadRequest", model2);
				model2.addAttribute("code", "400");
				model2.addAttribute("msg",
						"Flight with Number " + number + " cannot be deleted , has one or more reservations");

				JSONObject json_test = new JSONObject(model);
				String xml_test = XML.toString(json_test);
				flightRepository.delete(number);
				//return ResponseEntity.ok(xml_test);
				return new ResponseEntity(xml_test,HttpStatus.OK);
			}
			model.clear();
			model2.clear();
			model.addAttribute("Response", model2);
			model2.addAttribute("code", "200");
			model2.addAttribute("msg", "Flight with Number " + number + " is deleted successfully");

			JSONObject json_test = new JSONObject(model);
			String xml_test = XML.toString(json_test);
			flightRepository.delete(number);
			//return ResponseEntity.ok(xml_test);
			return new ResponseEntity(xml_test,HttpStatus.OK);
		}
	}

	private boolean checkFlightCapacity(Flight flight, int newCap) {
		int currCap = flight.getPlane().getCapacity();

		if (currCap < newCap)
			return true;
		else {
			if (newCap < (currCap - flight.getSeatsLeft()))
				return false;
		}
		return true;
	}

	private boolean checkOverlap(Passanger passenger, List<Flight> temp) {

		for (Flight f : temp) {
			Date dTime = f.getDepartureTime();
			for (Reservation r : passenger.getReservations()) {
				for (Flight booked : r.getFlights()) {
					if (!booked.equals(f)) {
						if (dTime.compareTo(booked.getDepartureTime()) == 0)
							return true;
					}
				}
			}
		}
		return false;
	}
}
