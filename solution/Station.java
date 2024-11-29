public class Station {
    private String stationCode;
    private String stationName;
    private Time arrivalTime;
    private Time departureTime;
    private int distance;

    public Station(String stationCode, String stationName, 
                  Time arrivalTime, Time departureTime, int distance) {
        this.stationCode = stationCode;
        this.stationName = stationName;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.distance = distance;
    }

    public String getStationCode() {
        return stationCode;
    }

    public String getStationName() {
        return stationName;
    }

    public Time getArrivalTime() {
        return arrivalTime;
    }

    public Time getDepartureTime() {
        return departureTime;
    }

    public int getDistance() {
        return distance;
    }
} 