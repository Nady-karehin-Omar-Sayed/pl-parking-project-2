package models;

public class Ticket {
 
    private int ticketId;
    private String plateNumber;
    private int spotId;
    private long entryTimeMillis; 
    private boolean isPaid;
 
    public Ticket(int ticketId, String plateNumber, int spotId) {
        this.ticketId = ticketId;
        this.plateNumber = plateNumber;
        this.spotId = spotId;
        this.entryTimeMillis = System.currentTimeMillis(); 
        this.isPaid = false;
    }
 

    public Ticket(int ticketId, String plateNumber, int spotId,
                  long entryTimeMillis, boolean isPaid) {
        this.ticketId = ticketId;
        this.plateNumber = plateNumber;
        this.spotId = spotId;
        this.entryTimeMillis = entryTimeMillis;
        this.isPaid = isPaid;
    }
 
    public int getTicketId()         { return ticketId; }
    public String getPlateNumber()   { return plateNumber; }
    public int getSpotId()           { return spotId; }
    public long getEntryTimeMillis() { return entryTimeMillis; }
    public boolean isPaid()          { return isPaid; }
 
    public void setPaid(boolean paid) { this.isPaid = paid; }
 

    public String toCSV() {
        return ticketId + "," + plateNumber + "," + spotId
             + "," + entryTimeMillis + "," + isPaid;
    }
 
    public void display() {
        System.out.println("---- TICKET ----");
        System.out.println("Ticket ID  : " + ticketId);
        System.out.println("Plate      : " + plateNumber);
        System.out.println("Spot       : " + spotId);

        System.out.println("Entry Time : " + new java.util.Date(entryTimeMillis));
        System.out.println("Paid       : " + (isPaid ? "YES" : "NO"));
        System.out.println("----------------");
    }
}
