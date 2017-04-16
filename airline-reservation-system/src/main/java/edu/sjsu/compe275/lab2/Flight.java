package edu.sjsu.compe275.lab2;

import java.util.Date;
import java.util.List;

/**
 * Created by Amruta on 4/15/2017.
 */
public class Flight {
    private String number; // Each flight has a unique flight number.
    private int price;
    private String from;
    private String to;

    /*  Date format: yy-mm-dd-hh, do not include minutes and sceonds.
    ** Example: 2017-03-22-19
    The system only needs to supports PST. You can ignore other time zones.*/

    private Date departureTime;
    private Date arrivalTime;
    private int seatsLeft;
    private String description;
    private Plane plane;  // Embedded
    private List<Passenger> passengers;

}

