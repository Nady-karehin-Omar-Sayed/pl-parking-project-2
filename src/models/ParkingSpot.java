package models;

public class ParkingSpot {
    private int spotId ;
    private String zone;     
    private boolean isOccupied;

    public ParkingSpot(int spotId, String zone, boolean isOccupied) {
        this.spotId = spotId;
        this.zone = zone;
        this.isOccupied = isOccupied;
    }


    public ParkingSpot(int spotId, String zone) {
        this(spotId, zone, false); 
    }

    public int GetSpotId() {
        return spotId;
    }

    public String GetZone() {
        return zone;
    }

    public void SetZone(String zone) {
        this.zone = zone;
    }

    public boolean GetisOccupied() {
        return isOccupied;
    }

    public void SetOccupied(boolean occupied) {
       this.isOccupied = occupied;
    }
    public void occupy() { this.isOccupied = true; }
 
    public void free() { this.isOccupied = false; }
 
    public void display() {
        String status = isOccupied ? "OCCUPIED" : "FREE";
        System.out.println("Spot: " + spotId + " | Zone: " + zone + " | " + status);
    }

}
//Represents one physical parking space in the lot.
//
// Each spot has:
//   - spotId  : unique number (1, 2, 3 ...)
//   - zone    : section of the parking lot ("A", "B", "C")
//   - isOccupied : true = car is parked, false = free
//
// Spots are stored in an ArrayList in ParkingManager.
// They are NOT saved to CSV — admin adds them fresh each run,
