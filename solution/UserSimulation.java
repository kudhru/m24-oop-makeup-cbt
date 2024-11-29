import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class UserSimulation implements Runnable {
    private TrainReservationSystem system;
    private String userId;

    public UserSimulation(TrainReservationSystem system, String userId) {
        this.system = system;
        this.userId = userId;
    }

    @Override
    public void run() {
        try {
            // Simulate user operations
            // 1. Search for trains
            List<Train> trains = system.searchTrains("DEL", "MUM", 
                new Date(15, 4, 2024), "2A");
            
            if (!trains.isEmpty()) {
                // 2. Book tickets
                List<Passenger> passengers = Arrays.asList(
                    new Passenger("John Doe", 30, "M", null),
                    new Passenger("Jane Doe", 28, "F", null)
                );
                
                Booking booking = system.bookTickets(
                    trains.get(0).getTrainNumber(),
                    userId,
                    passengers,
                    "2A",
                    "DEL",
                    "MUM",
                    new Date(15, 4, 2024),
                    false
                );
                
                System.out.println("Booking successful for " + userId + 
                    " with booking ID: " + booking.getBookingId());
                
                // 3. Retrieve bookings
                List<Booking> userBookings = system.getBookings(userId);
                System.out.println("Number of bookings for " + userId + 
                    ": " + userBookings.size());
                
                // 4. Cancel some passengers
                if (!userBookings.isEmpty()) {
                    system.cancelBooking(userId, booking.getBookingId(), 
                        Collections.singletonList("John Doe"));
                    System.out.println("Cancelled passenger John Doe from booking");
                }
            }
            
            // 5. Get train schedule
            List<Station> schedule = system.getTrainSchedule("12345");
            System.out.println("Train 12345 has " + schedule.size() + " stops");
            
        } catch (IllegalArgumentException e) {
            System.out.println("Operation failed for " + userId + ": " + e.getMessage());
        }
    }
} 