public class Time implements Comparable<Time> {
    private int hours;    // 0-23
    private int minutes;  // 0-59
    
    public Time(int hours, int minutes) {
        if (hours < 0 || hours > 23 || minutes < 0 || minutes > 59) {
            throw new IllegalArgumentException("Invalid time values");
        }
        this.hours = hours;
        this.minutes = minutes;
    }
    
    public static Time of(int hours, int minutes) {
        return new Time(hours, minutes);
    }
    
    public boolean isBefore(Time other) {
        if (this.hours < other.hours) return true;
        if (this.hours > other.hours) return false;
        return this.minutes < other.minutes;
    }
    
    public boolean isAfter(Time other) {
        return !isBefore(other) && !equals(other);
    }
    
    @Override
    public int compareTo(Time other) {
        if (this.hours != other.hours) {
            return this.hours - other.hours;
        }
        return this.minutes - other.minutes;
    }
    
    public static Time now() {
        java.time.LocalTime now = java.time.LocalTime.now();
        return new Time(now.getHour(), now.getMinute());
    }
    
    @Override
    public String toString() {
        return String.format("%02d:%02d", hours, minutes);
    }
} 