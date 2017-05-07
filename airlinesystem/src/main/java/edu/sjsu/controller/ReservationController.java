package edu.sjsu.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
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
public class ReservationController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();
	public static final Logger logger = LoggerFactory.getLogger(ReservationController.class);
	Response rm = new Response();
	ModelMap model = new ModelMap();
	ModelMap model2 = new ModelMap();

	@Autowired
	ReservationRepository resRepository;

	@Autowired
	FlightRepository fRepository;

	@Autowired
	PassengerRepository pRepository;

	//ObjectMapper mapper = new ObjectMapper().registerModule(new JsonViewModule());

	// ------------------get a reservation as a
	// json------------------------------------

	@RequestMapping(value = "/reservation/{order_number}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getReservationJSON(@PathVariable("order_number") String order_number) throws JSONException {
		Reservation p = resRepository.findOne(order_number);
		if (p == null) {
			model.clear();
			model2.clear();
			model.addAttribute("BadRequest", model2);
			model2.addAttribute("code", "404");
			String st = "Reservation with id " + order_number + " doesnot exist.";
			model2.addAttribute("msg", st);

			return new ResponseEntity(model, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(p.getFullJSON().toString(), HttpStatus.OK);
	}

	// ----------------------- search a reservation
	// ------------------------------------------

	@RequestMapping(value = "/reservation?", method = RequestMethod.GET, produces = { MediaType.APPLICATION_XML_VALUE })

	public ResponseEntity<?> getReservation(@RequestParam(value = "passengerId", required = true) String passengerId,
			@RequestParam(value = "from", required = true) String from_source,
			@RequestParam(value = "to", required = true) String to_dest,
			@RequestParam(value = "flightNumber", required = true) String flightNo) {

		List<Reservation> reservations = null;
		JSONObject result = new JSONObject();
		JSONArray reservationArray = new JSONArray();
		Reservation p = resRepository.findOne(passengerId);
		return ResponseEntity.ok(p);
	}

	// -------------------Create a
	// reservation-------------------------------------------

	@RequestMapping(value = "/reservation", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public ResponseEntity<?> createReservation(@RequestParam(value = "passengerId", required = true) String passengerId,
			@RequestParam List<String> flightLists, HttpServletResponse response) {

		logger.info("Creating reservation : {}", passengerId);

		Reservation reservation = new Reservation();
		Passanger p = pRepository.findOne(passengerId);

		if (p == null) {
			model.clear();
			model2.clear();
			model.addAttribute("BadRequest", model2);
			model2.addAttribute("code", "404");
			String st = "Passenger with id " + passengerId + " does not exist";
			model2.addAttribute("msg", st);
			return new ResponseEntity(model, HttpStatus.NOT_FOUND);
		}
		List pList = (List) new ArrayList<Passanger>();
		List flights = (List) new ArrayList<Flight>();

		int price = 0;

		try {
			// save the reservation with flights
			// update flight with passenger and seats

			if (flightLists.size() > 0) {
				for (String flight : flightLists) {
					Flight f = fRepository.findOne(flight);
					flights.add(f);
					// check time overlap
					Date dTime = f.getDepartureTime();
					for (Reservation r : p.getReservations())
						for (Flight pf : r.getFlights()) {
							if (pf.getDepartureTime().compareTo(dTime) == 0) {
								model.addAttribute("BadRequest", model2);
								model2.addAttribute("code", "404");
								String st = "Reservation timeoverlap. .Reservation cannot be created";
								model2.addAttribute("msg", st);

								return ResponseEntity.ok(model);
							}
						}
					/*
					 * //update passangers flights List<Flight> passengerFlights
					 * = p.getFlights(); passengerFlights.addAll(flights);
					 * p.setFlights(passengerFlights); pRepository.save(p);
					 */// check seats
					if (f.getSeatsLeft() > 0) {
						pList = f.getPassengers();
						pList.add(p);
						price += f.getPrice();
						f.setSeatsLeft(f.getSeatsLeft() - 1);
						f.setPassengers(pList);
						fRepository.save(f);
					} else {
						model.addAttribute("BadRequest", model2);
						model2.addAttribute("code", "404");
						String st = "No seasts avaialble.Reservation cannot be created";
						model2.addAttribute("msg", st);

						return ResponseEntity.ok(model);
					}
				}
			}

			// p.setReservations(null);
			reservation.setPassenger(p);
			reservation.setPrice(price);
			reservation.setFlights(flights);
			System.out.println("post reservation");
			System.out.println(reservation.getOrderNumber() + "|" + reservation.getPrice() + "|"
					+ reservation.getFlights().size());

			reservation = resRepository.save(reservation);
			System.out.println(reservation.getFullJSON().toString());
			//reservation = resRepository.findOne(reservation.getOrderNumber());
			String jsonRes = reservation.getFullJSON().toString();
			logger.debug(jsonRes);
			System.out.println(jsonRes);
			JSONObject json_test = new JSONObject(jsonRes);
			String xml_test = XML.toString(reservation.getFullJSON());
			System.out.println(xml_test);
			return new ResponseEntity(xml_test, HttpStatus.OK);
		
		} catch (Exception ex) {
			model.clear();
			model2.clear();
			model.addAttribute("BadRequest", model2);
			model2.addAttribute("code", "404");
			String st = "Reservation cannot be created";
			model2.addAttribute("msg", st);

			return new ResponseEntity(model, HttpStatus.BAD_REQUEST);

		}

	}

	// ------------------- Delete a
	// reservation-----------------------------------------

	@RequestMapping(value = "/reservation/{id}", method = RequestMethod.DELETE, produces = {
			MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(value = HttpStatus.ACCEPTED)
	public ResponseEntity<?> deleteReservation(@PathVariable("id") String id) throws JSONException {

		logger.info("Fetching & Deleting reservation with number {}", id);

		Reservation p = resRepository.findOne(id);
		if (p == null) {
			logger.error("Unable to delete reservation with id {} not found.", id);
			model.clear();
			model2.clear();
			model.addAttribute("BadRequest", model2);
			model2.addAttribute("code", "404");
			String st = "Reservation not found";
			model2.addAttribute("msg", st);

			return new ResponseEntity(model, HttpStatus.NOT_FOUND);

		} else {
			List<Flight> f_list = p.getFlights();
			for (Flight f : f_list) {
				int cnt = f.getSeatsLeft();
				if (cnt > 0)
					f.setSeatsLeft(++cnt);
				fRepository.save(f);
			}

			try {
				Reservation reservation = resRepository.findOne(id);
				Passanger passenger = reservation.getPassenger();
				//remove passenger from flight and udpate seats
				for(Flight flight: reservation.getFlights()){
					flight.setSeatsLeft(flight.getSeatsLeft()+1);
					flight.getPassangers().remove(passenger);
					fRepository.save(flight);
				}
				resRepository.delete(id);
				model.clear();
				model2.clear();
				String numb = "200";
				model.addAttribute("Response", model2);
				model2.addAttribute("code", "200");
				String st = "Reservation with id " + id + " is deleted successfully";
				model2.addAttribute("msg", st);
				JSONObject json_test = new JSONObject(model);
				String xml_test = XML.toString(json_test);

				return new ResponseEntity(xml_test,HttpStatus.OK);

			} catch (Exception e) {
				model.clear();
				model2.clear();
				model.addAttribute("BadRequest", model2);
				model2.addAttribute("code", "404");
				String st = "Error in Reservation cancellation";
				model2.addAttribute("msg", st);

				JSONObject json_test = new JSONObject(model);
				String xml_test = XML.toString(json_test);
				return new ResponseEntity(xml_test, HttpStatus.BAD_REQUEST);
			}
		}

	}

	@ExceptionHandler(BadHttpRequest.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public Map<String, Object> handleUnsupportedMediaTypeException(HttpMediaTypeNotSupportedException ex)
			throws IOException {
		Map<String, Object> map = new HashMap();
		map.put("code", "404");
		// map.put("cause", ex.getLocalizedMessage());
		map.put("msg", "Not found");
		return map;
	}
}
