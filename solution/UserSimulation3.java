import java.util.Arrays;
import java.util.List;

class UserSimulation3 implements Runnable {
    private TrainReservationSystem system;
    private String userId;

    public UserSimulation3(TrainReservationSystem system, String userId) {
        this.system = system;
        this.userId = userId;
    }

    @Override
    public void run() {
        try {
            // Simulate user operations for Puri-Delhi route
            // 1. Search for trains and sort by departure time
            List<Train> trains = system.searchTrains("PUR", "NDLS", 
                new Date(14, 4, 2024), "3A");
            system.sortTrainsByDepartureTime(trains, false); // descending order
            
            if (!trains.isEmpty()) {
                // 2. Book tickets for a group
                List<Passenger> passengers = Arrays.asList(
                    new Passenger("Amit Shah", 25, "M", null),
                    new Passenger("Rahul Verma", 26, "M", null),
                    new Passenger("Neha Gupta", 24, "F", null),
                    new Passenger("Priya Singh", 25, "F", null),
                    new Passenger("Karan Mehra", 25, "M", null)
                );
                
                Booking booking = system.bookTickets(
                    trains.get(0).getTrainNumber(),
                    userId,
                    passengers,
                    "3A",
                    "PUR",
                    "NDLS",
                    new Date(14, 4, 2024),
                    false
                );
                
                System.out.println("Group booking successful for " + userId + 
                    " with booking ID: " + booking.getBookingId());
                
                // 3. Retrieve bookings
                List<Booking> userBookings = system.getBookings(userId);
                System.out.println("Number of bookings for " + userId + 
                    ": " + userBookings.size());
                
                // 4. Cancel entire booking
                if (!userBookings.isEmpty()) {
                    system.cancelBooking(userId, booking.getBookingId(), null);
                    System.out.println("Cancelled entire group booking");
                }
            }
            
            // 5. Get train schedule
            List<Station> schedule = system.getTrainSchedule("12802");
            System.out.println("Purushottam Express has " + schedule.size() + " stops");
            
        } catch (IllegalArgumentException e) {
            System.out.println("Operation failed for " + userId + ": " + e.getMessage());
        }
    }
} 