import java.util.*;

public class Booking {
    private String bookingId;
    private String trainNumber;
    private String userId;
    private List<Passenger> passengers;
    private String travelClass;
    private String sourceCode;
    private String destinationCode;
    private Date travelDate;
    private List<String> assignedSeats;
    private double fare;
    private boolean isTatkal;

    public Booking(String bookingId, String trainNumber, String userId,
                  List<Passenger> passengers, String travelClass,
                  String sourceCode, String destinationCode, Date travelDate,
                  List<String> assignedSeats, double fare, boolean isTatkal) {
        this.bookingId = bookingId;
        this.trainNumber = trainNumber;
        this.userId = userId;
        this.passengers = new ArrayList<>(passengers);
        this.travelClass = travelClass;
        this.sourceCode = sourceCode;
        this.destinationCode = destinationCode;
        this.travelDate = travelDate;
        this.assignedSeats = new ArrayList<>(assignedSeats);
        this.fare = fare;
        this.isTatkal = isTatkal;
    }

    public void removePassengers(List<String> passengerNames) {
        Iterator<Passenger> iterator = passengers.iterator();
        while (iterator.hasNext()) {
            Passenger p = iterator.next();
            if (passengerNames.contains(p.getName())) {
                iterator.remove();
            }
        }
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public String getUserId() {
        return userId;
    }

    public List<Passenger> getPassengers() {
        return new ArrayList<>(passengers);
    }

    public String getTravelClass() {
        return travelClass;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public String getDestinationCode() {
        return destinationCode;
    }

    public Date getTravelDate() {
        return travelDate;
    }

    public List<String> getAssignedSeats() {
        return new ArrayList<>(assignedSeats);
    }

    public double getFare() {
        return fare;
    }

    public boolean isTatkal() {
        return isTatkal;
    }
} 
