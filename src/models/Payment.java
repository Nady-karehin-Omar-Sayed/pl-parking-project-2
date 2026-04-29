package models;

public class Payment {
 
    public static final double HOURLY_RATE = 5.0; // $5 per hour
 
    private int paymentId;
    private int ticketId;
    private double totalHours;
    private double totalCost;
    private long exitTimeMillis;
 
    public Payment(int paymentId, int ticketId, double totalHours, long exitTimeMillis) {
        this.paymentId = paymentId;
        this.ticketId = ticketId;
        this.totalHours = totalHours;
        this.totalCost = totalHours * HOURLY_RATE; 
        this.exitTimeMillis = exitTimeMillis;
    }
    public Payment(int paymentId, int ticketId, double totalHours,
                   double totalCost, long exitTimeMillis) {
        this.paymentId = paymentId;
        this.ticketId = ticketId;
        this.totalHours = totalHours;
        this.totalCost = totalCost;
        this.exitTimeMillis = exitTimeMillis;
    }
 
    public int getPaymentId()      { return paymentId; }
    public int getTicketId()       { return ticketId; }
    public double getTotalHours()  { return totalHours; }
    public double getTotalCost()   { return totalCost; }
    public long getExitTimeMillis(){ return exitTimeMillis; }
 

    public String toCSV() {
        return paymentId + "," + ticketId + "," + totalHours
             + "," + totalCost + "," + exitTimeMillis;
    }
 
    public void display() {
        System.out.println("---- PAYMENT RECEIPT ----");
        System.out.println("Payment ID  : " + paymentId);
        System.out.println("Ticket ID   : " + ticketId);
        System.out.println("Total Hours : " + totalHours);
        System.out.println("Total Cost  : $" + totalCost);
        System.out.println("Exit Time   : " + new java.util.Date(exitTimeMillis));
        System.out.println("-------------------------");
    }
}
