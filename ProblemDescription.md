# Train Reservation System - Programming Assignment

## Overview

You are required to implement a train reservation system similar to IRCTC where multiple users can concurrently search for trains, book tickets, and cancel bookings. The system handles multiple trains, each with different classes (1A, 2A, 3A, SL) and their corresponding fares and seat capacities.

## Class Descriptions

### TrainReservationSystem Class [TrainReservationSystem.java]
This is the main class that manages the entire reservation system. It maintains a collection of trains and user bookings. The class handles train searches, ticket bookings, and cancellations. It also provides functionality to sort trains based on arrival/departure times.

### Train Class [Train.java]
This class represents a train with its basic information, stops, seat availability, and fare details. It handles seat management and fare calculations. The class ensures thread-safe operations when multiple users try to book seats simultaneously.

### Helper Classes (Fully Implemented - Do not modify any of these classes)
1. **Booking**: Represents a booking with passenger details, assigned seats, and fare information [Booking.java]
2. **DayOfWeek**: Enum representing days of the week [DayOfWeek.java]
3. **Date**: Custom implementation for date handling with day-of-week calculations [Date.java]
4. **Passenger**: Contains passenger information (name, age, gender, seat) [Passenger.java]
5. **Station**: Represents a station with arrival/departure times and distance information [Station.java]
6. **Time**: Custom implementation for time handling [Time.java]
7. **UserSimulation(1/2/3)**: Test classes that simulate concurrent user operations [UserSimulation1.java, UserSimulation2.java, UserSimulation3.java]

## Methods to Implement

### Train Class Methods [to be implemented in Train.java]

1. **initializeSeats(Map<String, Integer> classCapacity)** [5 marks]
   - Initializes available seats for each class (1A, 2A, 3A, SL) based on the provided capacity
   - For each class, creates seat numbers in format "1", "2", "3", etc. Seat numbers start from 1 and increment by 1 for each new seat. If a class has a capacity of 52, it will have seats from "1" to "52".
   - Stores the created seats in the availableSeats map with the class type as the key

2. **assignSeats(String travelClass, int count)** [5 marks]
   - Assigns the requested number of seats for given travel class
   - Returns empty list if insufficient seats available
   - Removes the assigned seats from the set of available seats for the given travel class
   - Returns list of assigned seat numbers

3. **releaseSeats(String travelClass, List<String> seats)** [2 marks]
   - Adds the released seats back to the set of available seats for the given travel class

4. **serves(String sourceCode, String destinationCode)** [5 marks]
   - Checks if train serves the given source-destination pair
   - Returns true only if source comes before destination in train's route

5. **getFare(String travelClass, String sourceCode, String destinationCode)** [3 marks]
   - Calculates fare based on:
     * Base fare for the travel class (retrieved from classBaseFares map)
     * Distance between source and destination (retrieved from Station objects in stops list)
   - Formula: baseFare * (destinationDistance - sourceDistance) / 100.0

### TrainReservationSystem Class Methods [to be implemented in TrainReservationSystem.java]

6. **readTrainsInformation()** [10 marks]
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

7. **parseStops(String input)** [5 marks]
   - Parses stops string (format: "CODE::Name::HH:mm::HH:mm::distance;...")
   - Example format: "CODE::Name::HH:mm::HH:mm::distance;..."
        - Each stop is separated from the next stop by a semicolon (;)
        - Each stop contains: station code (CODE), station name (Name), arrival time (HH:mm), departure time (HH:mm), and distance from the starting station (distance)
        - station code, station name, arrival time, departure time and distance within each stop are separated by double colons (::)
   - Creates and returns a list of Station objects with parsed information

8. **searchTrains(String sourceCode, String destinationCode, Date date, String travelClass)** [5 marks]
   - Finds all trains serving the given route (sourceCode to destinationCode) on specified date
   - Returns list of matching trains

9. **bookTickets(String trainNumber, String userId, List<Passenger> passengers, String travelClass, String sourceCode, String destinationCode, Date travelDate, boolean isTatkal)** [12 marks]
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

10. **cancelBooking(String userId, String bookingId, List<String> passengerNames)** [13 marks]
   - Uses bookingId to find the booking in userBookings map
   - Returns false if booking is not found
   - Uses a lock object (bookingLock) to handle concurrent cancellations / bookings
   - Cancels entire booking if passengerNames is null or empty
   - Cancels specific passengers by removing them from the booking object (using removePassengers method in the Booking class)
   - Releases seats back to availableSeats map in the Train class (using releaseSeats method in the Train class)
   - Returns true if booking is successfully cancelled


11. **sortTrainsByDepartureTime(List<Train> trains, boolean ascending)** [5 marks]
   - Sorts trains based on departure time from source station (starting station)
   - Ascending/descending based on ascending parameter
   - Uses the sort method in the List class with a custom comparator
   - The comparator should be implemented as a lambda function

12. **sortTrainsByArrivalTime(List<Train> trains, boolean ascending)** [5 marks]
   - Sorts trains based on arrival time at destination station (ending station)
   - Ascending/descending based on ascending parameter
   - Uses the sort method in the List class with a custom comparator
   - The comparator should be implemented as a lambda function

## Important Notes

1. The system supports concurrent operations. Use proper synchronization in bookTickets and cancelBooking methods.
2. Do not modify any method signatures or add member variables.
3. You can add local variables within methods as needed.
4. All helper classes are fully implemented - do not modify them.
5. Use the provided trains.csv file for train data. 