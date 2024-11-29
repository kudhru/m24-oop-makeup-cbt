public class Passenger {
    private String name;
    private int age;
    private String gender;
    private String seatNumber;

    public Passenger(String name, int age, String gender, String seatNumber) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.seatNumber = seatNumber;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }
} 
