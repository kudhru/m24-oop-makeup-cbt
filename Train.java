import java.util.*;

public class Train {
    private String trainNumber;
    private String trainName;
    private List<Station> stops;
    private Map<String, Double> classBaseFares; // class -> base fare
    private Map<String, Set<String>> availableSeats; // class -> set of available seats
    private Set<DayOfWeek> runningDays;

    public Train(String trainNumber, String trainName, Map<String, Integer> classCapacity) {
        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.stops = new ArrayList<>();
        this.classBaseFares = new HashMap<>();
        this.availableSeats = new HashMap<>();
        this.runningDays = new HashSet<>();
        initializeSeats(classCapacity);
    }

    private void initializeSeats(Map<String, Integer> classCapacity) {
       /* Question 1
        - Initializes available seats for each class (1A, 2A, 3A, SL) based on the provided capacity
        - For each class, creates seat numbers in format "1", "2", "3", etc. 
        - Seat numbers start from 1 and increment by 1 for each new seat. 
        - If a class has a capacity of 52, it will have seats from "1" to "52".
        - Stores the created seats in the availableSeats map with the class type as the key
       */ 
    }
    
    public List<String> assignSeats(String travelClass, int count) {
        /* Question 2
        - Assigns the requested number of seats for given travel class
        - Returns empty list if insufficient seats available
        - Removes the assigned seats from the set of available seats for the given travel class
        - Returns list of assigned seat numbers
       */ 
    }

    public void releaseSeats(String travelClass, List<String> seats) {
        /* Question 3
        - Adds the released seats back to the set of available seats for the given travel class
       */ 
    }

    public boolean serves(String sourceCode, String destinationCode) {
        /* Question 4
        - Checks if train serves the given source-destination pair
        - Returns true only if source comes before destination in train's route
       */ 
    }

    public double getFare(String travelClass, String sourceCode, String destinationCode) {
        /* Question 5
        - Calculates fare based on:
            - Base fare for the travel class (retrieved from classBaseFares map)
            - Distance between source and destination (retrieved from Station objects in stops list)
        - Formula: baseFare * (destinationDistance - sourceDistance) / 100.0
       */ 
    }

    public void addStop(Station station) {
        stops.add(station);
    }

    public void setClassBaseFares(Map<String, Double> fares) {
        this.classBaseFares.putAll(fares);
    }

    public void setRunningDays(Set<DayOfWeek> days) {
        this.runningDays.addAll(days);
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public boolean runsOn(DayOfWeek day) {
        return runningDays.contains(day);
    }

    public List<Station> getStops() {
        return new ArrayList<>(stops);
    }

    public String getTrainName() {
        return trainName;
    }
} 