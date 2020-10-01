package com.telecominfraproject.wlan.profile.passpoint.venue.models;

import static java.util.Map.entry;

import java.util.List;
import java.util.Map;

public class VenueInfo {

    public static final Map<String, List<Integer>> venueMap = Map.ofEntries(
            
            entry("Unspecified", List.of(0, 0)),
            entry("Unspecified Assembly", List.of(1, 0)), entry("Arena", List.of(1, 1)),
            entry("Stadium", List.of(1, 2)),
            entry("Passenger Terminal (e.g., airport, bus, ferry, train station)", List.of(1, 3)),
            entry("Amphitheater", List.of(1, 4)), entry("Amusement Park", List.of(1, 5)),
            entry("Place of Worship", List.of(1, 6)), entry("Convention Center", List.of(1, 7)),
            entry("Library", List.of(1, 8)), entry("Museum", List.of(1, 9)), entry("Restaurant", List.of(1, 10)),
            entry("Theater", List.of(1, 11)), entry("Bar", List.of(1, 12)), entry("Coffee Shop", List.of(1, 13)),
            entry("Zoo or Aquarium", List.of(1, 14)), 
            
            entry("Unspecified Business", List.of(2, 0)),
            entry("Doctor or Dentist office", List.of(2, 1)), entry("Bank", List.of(2, 2)),
            entry("Fire Station", List.of(2, 3)), entry("Police Station", List.of(2, 4)),
            entry("Post Office", List.of(2, 6)), entry("Professional Office", List.of(2, 7)),
            entry("Research and Development Facility", List.of(2, 8)), entry("Attorney Office", List.of(2, 9)),

            entry("Unspecified Educational", List.of(3, 0)), entry("School, Primary", List.of(3, 1)),
            entry("School, Secondary", List.of(3, 2)), entry("University or College", List.of(3, 3)),

            entry("Unspecified Factory and Industrial", List.of(4, 0)), entry("Factory", List.of(4, 1)),

            entry("Unspecified Institutional", List.of(5, 0)), entry("Hospital", List.of(5, 1)),
            entry("Long-Term Care Facility (e.g., Nursing home, Hospice, etc.)", List.of(5, 2)),
            entry("Alcohol and Drug Rehabilitation Center", List.of(5, 3)), entry("Group Home", List.of(5, 4)),
            entry("Prison or Jail", List.of(5, 5)),

            entry("Unspecified Mercantile", List.of(6, 0)), entry("Retail Store", List.of(6, 1)),
            entry("Grocery Market", List.of(6, 2)), entry("Automotive Service Station", List.of(6, 3)),
            entry("Shopping Mall", List.of(6, 4)), entry("Gas Station", List.of(6, 5)),

            entry("Unspecified Residential", List.of(7, 0)), entry("Private Residence", List.of(7, 1)),
            entry("Hotel or Motel", List.of(7, 2)), entry("Dormitory", List.of(7, 3)),
            entry("Boarding House", List.of(7, 4)),

            entry("Unspecified Storage", List.of(8, 0)), entry("Unspecified Utility and Miscellaneous", List.of(9, 0)),

            entry("Unspecified Vehicular", List.of(10, 0)), entry("Automobile or Truck", List.of(10, 1)),
            entry("Airplane", List.of(10, 2)), entry("Bus", List.of(10, 3)), entry("Ferry", List.of(10, 4)),
            entry("Ship or Boat", List.of(10, 5)), entry("Train", List.of(10, 6)), entry("Motor Bike", List.of(10, 7)),

            entry("Unspecified Outdoor", List.of(11, 0)), entry("Muni-mesh Network", List.of(11, 1)),
            entry("City Park", List.of(11, 2)), entry("Rest Area", List.of(11, 3)),
            entry("Traffic Control", List.of(11, 4)), entry("Bus Stop", List.of(11, 5)), entry("Kiosk", List.of(11, 6))

    );
    

    
    

}
