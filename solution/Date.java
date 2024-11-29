import java.util.Calendar;

public class Date implements Comparable<Date> {
    private int day;    // 1-31
    private int month;  // 1-12
    private int year;   // e.g., 2024
    
    public Date(int day, int month, int year) {
        if (!isValid(day, month, year)) {
            throw new IllegalArgumentException("Invalid date values");
        }
        this.day = day;
        this.month = month;
        this.year = year;
    }
    
    private boolean isValid(int day, int month, int year) {
        if (month < 1 || month > 12) return false;
        if (day < 1) return false;
        
        int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (isLeapYear(year)) {
            daysInMonth[1] = 29;
        }
        
        return day <= daysInMonth[month - 1];
    }
    
    private boolean isLeapYear(int year) {
        return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
    }
    
    public DayOfWeek getDayOfWeek() {
        // Simplified implementation of Zeller's congruence
        int m = month;
        int y = year;
        if (m <= 2) {
            m += 12;
            y -= 1;
        }
        int h = (day + (13 * (m + 1)) / 5 + y + y/4 - y/100 + y/400) % 7;
        return DayOfWeek.values()[(h + 5) % 7]; // Adjusting to make Monday=0
    }
    
    public static Date now() {
        Calendar cal = Calendar.getInstance();
        return new Date(cal.get(Calendar.DAY_OF_MONTH), 
                       cal.get(Calendar.MONTH) + 1, 
                       cal.get(Calendar.YEAR));
    }
    
    @Override
    public int compareTo(Date other) {
        if (this.year != other.year) return this.year - other.year;
        if (this.month != other.month) return this.month - other.month;
        return this.day - other.day;
    }
    
    @Override
    public String toString() {
        return String.format("%02d/%02d/%04d", day, month, year);
    }
} 