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
        for (Map.Entry<String, Integer> entry : classCapacity.entrySet()) {
            Set<String> seats = new HashSet<>();
            for (int i = 1; i <= entry.getValue(); i++) {
                seats.add(String.format("%d", i));
            }
            availableSeats.put(entry.getKey(), seats);
        }
    }
    
    public List<String> assignSeats(String travelClass, int count) {
        Set<String> available = availableSeats.get(travelClass);
        if (available == null || available.size() < count) {
            return new ArrayList<>();
        }

        List<String> assigned = new ArrayList<>();
        Iterator<String> iterator = available.iterator();
        
        for (int i = 0; i < count; i++) {
            String seat = iterator.next();
            assigned.add(seat);
            iterator.remove();
        }
        
        return assigned;
    }

    public void releaseSeats(String travelClass, List<String> seats) {
        availableSeats.get(travelClass).addAll(seats);
    }

    public boolean serves(String sourceCode, String destinationCode) {
        int sourceIndex = -1;
        int destIndex = -1;
        
        for (int i = 0; i < stops.size(); i++) {
            if (stops.get(i).getStationCode().equals(sourceCode)) sourceIndex = i;
            if (stops.get(i).getStationCode().equals(destinationCode)) destIndex = i;
        }
        
        return sourceIndex != -1 && destIndex != -1 && sourceIndex < destIndex;
    }

    public double getFare(String travelClass, String sourceCode, String destinationCode) {
        double baseFare = classBaseFares.get(travelClass);
        int sourceDistance = 0;
        int destDistance = 0;
        
        for (Station station : stops) {
            if (station.getStationCode().equals(sourceCode)) {
                sourceDistance = station.getDistance();
            }
            if (station.getStationCode().equals(destinationCode)) {
                destDistance = station.getDistance();
            }
        }
        
        return baseFare * (destDistance - sourceDistance) / 100.0;
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