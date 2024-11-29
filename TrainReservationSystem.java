import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TrainReservationSystem {
    private Map<String, Train> trains; // trainNumber -> Train
    private Map<String, List<Booking>> userBookings; // userId -> List of bookings
    private final Object bookingLock = new Object();
    
    public TrainReservationSystem() {
        this.trains = new ConcurrentHashMap<>();
        this.userBookings = new ConcurrentHashMap<>();
        readTrainsInformation();
    }

    private void readTrainsInformation() {
        /* Question 6
        - Reads train data from trains.csv
        - Parses each line of the CSV file to extract train details
        - Each line contains: trainNumber, trainName, classBaseFares, classCapacity, runningDays, stops
        - Example format of classBaseFares: "1A::3000.0;2A::2000.0;3A::1500.0;SL::1000.0"
        - Example format of classCapacity: "1A::10;2A::20;3A::30;SL::40"
        - Example format of runningDays: "MON;WED;FRI"
        - Example format of stops: "CODE::Name::HH:mm::HH:mm::distance;..."
        - Uses parseClassFares, parseClassCapacity, parseRunningDays, and parseStops methods to parse the respective fields
        - Creates Train objects with parsed information
        - Adds the created Train objects to the trains map with trainNumber as key
        - Catches IOException if encountered
       */ 
    }

    private Map<String, Double> parseClassFares(String input) {
        Map<String, Double> fares = new HashMap<>();
        String[] pairs = input.split(";");
        for (String pair : pairs) {
            String[] keyValue = pair.split("::");
            fares.put(keyValue[0], Double.parseDouble(keyValue[1]));
        }
        return fares;
    }

    private Map<String, Integer> parseClassCapacity(String input) {
        Map<String, Integer> capacity = new HashMap<>();
        String[] pairs = input.split(";");
        for (String pair : pairs) {
            String[] keyValue = pair.split("::");
            capacity.put(keyValue[0], Integer.parseInt(keyValue[1]));
        }
        return capacity;
    }

    private Set<DayOfWeek> parseRunningDays(String input) {
        Set<DayOfWeek> days = new HashSet<>();
        String[] dayStrings = input.split(";");
        for (String day : dayStrings) {
            days.add(stringToDayOfWeek(day));
        }
        return days;
    }

    private DayOfWeek stringToDayOfWeek(String day) {
        switch (day.toUpperCase()) {
            case "MON": return DayOfWeek.MONDAY;
            case "TUE": return DayOfWeek.TUESDAY;
            case "WED": return DayOfWeek.WEDNESDAY;
            case "THU": return DayOfWeek.THURSDAY;
            case "FRI": return DayOfWeek.FRIDAY;
            case "SAT": return DayOfWeek.SATURDAY;
            case "SUN": return DayOfWeek.SUNDAY;
            default: throw new IllegalArgumentException("Invalid day: " + day);
        }
    }

    private List<Station> parseStops(String input) {
        /* Question 7
        - Parses stops string (format: "CODE::Name::HH:mm::HH:mm::distance;...")
        - Example format: "CODE::Name::HH:mm::HH:mm::distance;..."
        - Each stop is separated from the next stop by a semicolon (;)
        - Each stop contains: station code (CODE), station name (Name), arrival time (HH:mm), departure time (HH:mm), and distance from the starting station (distance)
        - station code, station name, arrival time, departure time and distance within each stop are separated by double colons (::)
        - Creates and returns a list of Station objects with parsed information
       */ 
    }

    public List<Train> searchTrains(String sourceCode, String destinationCode, 
                                               Date date, String travelClass) {
        /* Question 8
        - Finds all trains serving the given route (sourceCode to destinationCode) on specified date
        - Returns list of matching trains
       */ 
    }

    public Booking bookTickets(String trainNumber, String userId, 
                                          List<Passenger> passengers, String travelClass, 
                                          String sourceCode, String destinationCode, 
                                          Date travelDate, boolean isTatkal) 
            throws IllegalArgumentException {
        /* Question 9
        - Books tickets for given passengers
        - Throws IllegalArgumentException if train is not found
        - Throws IllegalArgumentException if tatkal is not allowed at the current time
            - Tatkal is allowed only between 10 AM and 12 PM. Current time needs to be compared with 10 AM and 12 PM to check whether tatkal is allowed or not.
        - Uses a lock object (bookingLock) to handle concurrent bookings / cancellations
        - Assigns seats using assignSeats method
            - If no seats are available, throws IllegalArgumentException
        - Calculates fare using getFare method in the Train class
            - If isTatkal is true, calculates tatkal fare by increasing the computed fare by 30%
        - Assigns seats to passengers (updates the seatNumber field in the Passenger objects)
        - Creates a booking object and adds it to userBookings map
        - Returns the created booking object
       */ 
    }

    
    public boolean cancelBooking(String userId, String bookingId, 
                               List<String> passengerNames) {
        /* Question 10
        - Uses bookingId to find the booking in userBookings map
        - Returns false if booking is not found
        - Uses a lock object (bookingLock) to handle concurrent cancellations / bookings
        - Cancels entire booking if passengerNames is null or empty
        - Cancels specific passengers by removing them from the booking object (using removePassengers method in the Booking class)
        - Releases seats back to availableSeats map in the Train class (using releaseSeats method in the Train class)
        - Returns true if booking is successfully cancelled
       */ 
    }

    /**
     * Retrieves all bookings for a user
     */
    public List<Booking> getBookings(String userId) {
        return userBookings.getOrDefault(userId, new ArrayList<>());
    }

    /**
     * Retrieves train schedule with all stops
     */
    public List<Station> getTrainSchedule(String trainNumber) {
        Train train = trains.get(trainNumber);
        return train != null ? train.getStops() : new ArrayList<>();
    }

    
    public void sortTrainsByDepartureTime(List<Train> trains, boolean ascending) {
        /* Question 11
        - Sorts trains based on departure time from source station (starting station)
        - Ascending/descending based on ascending parameter
        - Uses the sort method in the List class with a custom comparator
        - The comparator should be implemented as a lambda function
       */ 
    }

    
    public void sortTrainsByArrivalTime(List<Train> trains, boolean ascending) {
        /* Question 12
        - Sorts trains based on arrival time at destination station (ending station)
        - Ascending/descending based on ascending parameter
        - Uses the sort method in the List class with a custom comparator
        - The comparator should be implemented as a lambda function
       */ 
    }

    // Main method to demonstrate functionality
    public static void main(String[] args) {
        TrainReservationSystem system = new TrainReservationSystem();

        // Create multiple user threads to simulate concurrent operations
        Thread user1 = new Thread(new UserSimulation(system, "USER1"));   // Delhi-Mumbai route
        Thread user2 = new Thread(new UserSimulation2(system, "USER2"));  // Bangalore-Trivandrum route
        Thread user3 = new Thread(new UserSimulation3(system, "USER3"));  // Puri-Delhi route

        // Additional users for concurrent load
        Thread user4 = new Thread(new UserSimulation(system, "USER4"));   // Another Delhi-Mumbai booking
        Thread user5 = new Thread(new UserSimulation2(system, "USER5"));  // Another Bangalore-Trivandrum booking
        Thread user6 = new Thread(new UserSimulation3(system, "USER6"));  // Another Puri-Delhi booking

        // Start all threads
        user1.start();
        user2.start();
        user3.start();
        user4.start();
        user5.start();
        user6.start();

        try {
            // Wait for all threads to complete
            user1.join();
            user2.join();
            user3.join();
            user4.join();
            user5.join();
            user6.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
