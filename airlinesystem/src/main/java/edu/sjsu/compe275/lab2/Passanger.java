package edu.sjsu.compe275.lab2;


import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Created by Amruta on 4/15/2017.
 */

@Entity
@JsonInclude(Include.NON_EMPTY)
@JsonAutoDetect(getterVisibility=Visibility.NONE)
public class Passanger {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="passanger_id")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String firstname;

    private String lastname;

    private int age;

    private String gender;

    @Column(unique=true)
    private String phone; // Phone numbers must be unique
    

 

    @OneToMany(mappedBy = "passanger", cascade = CascadeType.REMOVE)
     private List<Reservation> reservations;
    
    @ManyToMany(mappedBy="passengers")
    private List<Flight> flights;

    public Passanger(){
    	
    }
    
    public Passanger(String id){
    	this.id = id;
    }
    
    public Passanger(String firstname,String lastname,String gender,int age, String phone){
        this.firstname = firstname;
        this.lastname = lastname;
        this.gender = gender;
        this.phone = phone;
        this.age = age;
    }

    //-----------setter getter-----------------
    
    
    public String getFirstname() {
        return firstname;
    }

    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

public List<Flight> getFlights() {
		return flights;
	}

	public void setFlights(List<Flight> flights) {
		this.flights = flights;
	}
    
    public JSONObject getFullJSON() throws JSONException
    {
        JSONObject result=new JSONObject();
        JSONObject passenger=this.getJSON();
        JSONArray reservationArray=new JSONArray();
        for(Reservation res:this.getReservations())
        {
            JSONObject reservation=res.getFullJSON();
            JSONObject reserv=reservation.getJSONObject("reservation");
            reserv.remove("passenger");
            reservation.put("reservation",reserv);
            reservationArray.put(reservation);
        }
        passenger.put("reservations",reservationArray);
        result.put("passenger",passenger);
        return result;
    }

   
    public JSONObject getJSON() throws JSONException
    {
        JSONObject passenger = new JSONObject();
        passenger.put("id",this.getId());
        passenger.put("firstname",this.getFirstname());
        passenger.put("lastname",this.getLastname());
        passenger.put("age",this.getAge());
        passenger.put("gender",this.getGender());
        passenger.put("phone",this.getPhone());
        return passenger;
    }
}
