import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;

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
        try (BufferedReader br = new BufferedReader(new FileReader("trains.csv"))) {
            // Skip header line
            br.readLine();
            
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String trainNumber = parts[0];
                String trainName = parts[1];
                Map<String, Double> classBaseFares = parseClassFares(parts[2]);
                Map<String, Integer> classCapacity = parseClassCapacity(parts[3]);
                Set<DayOfWeek> runningDays = parseRunningDays(parts[4]);
                List<Station> stops = parseStops(parts[5]);

                Train train = new Train(trainNumber, trainName, classCapacity);
                train.setClassBaseFares(classBaseFares);
                train.setRunningDays(runningDays);
                for (Station stop : stops) {
                    train.addStop(stop);
                }

                trains.put(trainNumber, train);
            }
        } catch (IOException e) {
            System.err.println("Error reading trains.csv: " + e.getMessage());
        }
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
        List<Station> stops = new ArrayList<>();
        String[] stations = input.split(";");
        for (String station : stations) {
            String[] parts = station.split("::");
            String code = parts[0];
            String name = parts[1];
            String[] arrTime = parts[2].split(":");
            String[] depTime = parts[3].split(":");
            int distance = Integer.parseInt(parts[4].trim());

            stops.add(new Station(
                code, 
                name,
                Time.of(Integer.parseInt(arrTime[0]), Integer.parseInt(arrTime[1])),
                Time.of(Integer.parseInt(depTime[0]), Integer.parseInt(depTime[1])),
                distance
            ));
        }
        return stops;
    }

    public List<Train> searchTrains(String sourceCode, String destinationCode, 
                                               Date date, String travelClass) {
        List<Train> availableTrains = new ArrayList<>();
        
        for (Train train : trains.values()) {
            if (train.serves(sourceCode, destinationCode) && train.runsOn(date.getDayOfWeek())) {
                availableTrains.add(train);
            }
        }
        
        return availableTrains;
    }

    public Booking bookTickets(String trainNumber, String userId, 
                                          List<Passenger> passengers, String travelClass, 
                                          String sourceCode, String destinationCode, 
                                          Date travelDate, boolean isTatkal) 
            throws IllegalArgumentException {
        
        Train train = trains.get(trainNumber);
        if (train == null) {
            throw new IllegalArgumentException("Train not found");
        }

        // Validate Tatkal booking time
        if (isTatkal) {
            Time currentTime = Time.now();
            if (currentTime.isBefore(Time.of(10, 0)) || 
                currentTime.isAfter(Time.of(12, 0))) {
                throw new IllegalArgumentException("Tatkal booking is only allowed between 10 AM and 12 PM");
            }
        }

        synchronized(bookingLock) {
            // Check availability and assign seats
            List<String> assignedSeats = train.assignSeats(travelClass, passengers.size());
            if (assignedSeats.isEmpty()) {
                throw new IllegalArgumentException("No seats available");
            }

            // Create booking
            double fare = train.getFare(travelClass, sourceCode, destinationCode);
            if (isTatkal) {
                fare *= 1.3; // 30% extra for Tatkal
            }
            // Assign seats to passengers
            for (int i = 0; i < passengers.size(); i++) {
                passengers.get(i).setSeatNumber(assignedSeats.get(i));
            }
            Booking booking = new Booking(
                UUID.randomUUID().toString(),
                trainNumber,
                userId,
                passengers,
                travelClass,
                sourceCode,
                destinationCode,
                travelDate,
                assignedSeats,
                fare,
                isTatkal
            );

            // Store booking
            if (!userBookings.containsKey(userId)) {
                userBookings.put(userId, new ArrayList<>());
            }
            userBookings.get(userId).add(booking);
            return booking;
        }
    }

    /**
     * Cancels entire booking or specific passengers in a booking
     * Releases seats back to availability pool
     */
    public boolean cancelBooking(String userId, String bookingId, 
                               List<String> passengerNames) {
        List<Booking> bookings = userBookings.get(userId);
        if (bookings == null) return false;

        synchronized(bookingLock) {
            for (Booking booking : bookings) {
                if (booking.getBookingId().equals(bookingId)) {
                    if (passengerNames == null || passengerNames.isEmpty()) {
                        // Cancel entire booking
                        bookings.remove(booking);
                        Train train = trains.get(booking.getTrainNumber());
                        train.releaseSeats(booking.getTravelClass(), booking.getAssignedSeats());
                    } else {
                        // Cancel specific passengers
                        Train train = trains.get(booking.getTrainNumber());
                        List<String> seatsToRelease = new ArrayList<>();
                        for (String passengerName : passengerNames) {
                            for (Passenger p : booking.getPassengers()) {
                                if (p.getName().equals(passengerName)) {
                                    seatsToRelease.add(p.getSeatNumber());
                                    break;
                                }
                            }
                        }
                        train.releaseSeats(booking.getTravelClass(), seatsToRelease);
                        booking.removePassengers(passengerNames);
                        if (booking.getPassengers().isEmpty()) {
                            bookings.remove(booking);
                        }
                    }
                    return true;
                }
            }
        }
        return false;
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

    /**
     * Sort trains by departure time
     */
    public void sortTrainsByDepartureTime(List<Train> trains, boolean ascending) {
        trains.sort((t1, t2) -> {
            Time time1 = t1.getStops().get(0).getDepartureTime();
            Time time2 = t2.getStops().get(0).getDepartureTime();
            return ascending ? time1.compareTo(time2) : time2.compareTo(time1);
        });
    }

    /**
     * Sort trains by arrival time
     */
    public void sortTrainsByArrivalTime(List<Train> trains, boolean ascending) {
        trains.sort((t1, t2) -> {
            Time time1 = t1.getStops().get(t1.getStops().size() - 1).getArrivalTime();
            Time time2 = t2.getStops().get(t2.getStops().size() - 1).getArrivalTime();
            return ascending ? time1.compareTo(time2) : time2.compareTo(time1);
        });
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
