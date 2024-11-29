import java.util.Arrays;
import java.util.List;

class UserSimulation2 implements Runnable {
    private TrainReservationSystem system;
    private String userId;

    public UserSimulation2(TrainReservationSystem system, String userId) {
        this.system = system;
        this.userId = userId;
    }

    @Override
    public void run() {
        try {
            // Simulate user operations for Bangalore-Trivandrum route
            // 1. Search for trains
            List<Train> trains = system.searchTrains("BLR", "TVM", 
                new Date(16, 4, 2024), "1A");
            
            if (!trains.isEmpty()) {
                // 2. Book tickets for a family
                List<Passenger> passengers = Arrays.asList(
                    new Passenger("Raj Kumar", 45, "M", null),
                    new Passenger("Priya Kumar", 40, "F", null),
                    new Passenger("Rohan Kumar", 12, "M", null),
                    new Passenger("Riya Kumar", 8, "F", null)
                );
                
                Booking booking = system.bookTickets(
                    trains.get(0).getTrainNumber(),
                    userId,
                    passengers,
                    "1A",
                    "BLR",
                    "TVM",
                    new Date(16, 4, 2024),
                    true  // Tatkal booking
                );
                
                System.out.println("Tatkal booking successful for " + userId + 
                    " with booking ID: " + booking.getBookingId());
                
                // 3. Retrieve and sort trains by arrival time
                system.sortTrainsByArrivalTime(trains, true);
                System.out.println("Sorted trains by arrival time for " + userId);
                
                // 4. Cancel two passengers
                if (!system.getBookings(userId).isEmpty()) {
                    system.cancelBooking(userId, booking.getBookingId(), 
                        Arrays.asList("Rohan Kumar", "Riya Kumar"));
                    System.out.println("Cancelled child passengers from booking");
                }
            }
            
            // 5. Get train schedule
            List<Station> schedule = system.getTrainSchedule("16205");
            System.out.println("Mysore Express has " + schedule.size() + " stops");
            
        } catch (IllegalArgumentException e) {
            System.out.println("Operation failed for " + userId + ": " + e.getMessage());
        }
    }
} 