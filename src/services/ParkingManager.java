package services;
import java.util.ArrayList;
import models.*;
public class ParkingManager {

     private ArrayList<Users> users;
        private ArrayList<Customer> customers;
        private ArrayList<ParkingSpot> spots;
        private ArrayList<Ticket>  tickets;
        private ArrayList<Payment>  payments;
        private int nextUserId;
        private int nextCustomerId;
        private int nextSpotId;
        private int nextTicketId;
        private int nextPaymentId;




    public ParkingManager() {
        CSVHandler.ensureDataFolder();
        tickets= CSVHandler.loadTickets();
        payments=CSVHandler.loadPayments();
        users = new ArrayList<>();
        customers=new ArrayList<>();
        spots=new ArrayList<>();

        seedUsers();
        seedSpots();
        calculateNextIds();
    }


    private void seedUsers(){
        users.add(new Users(1, "Admin","admin",  "admin123",  "ADMIN"));
        users.add(new Users(2, "Entry Operator", "entry",  "entry123",  "ENTRY_OPERATOR"));
        users.add(new Users(3, "Exit Operator",  "exit",   "exit123",   "EXIT_OPERATOR"));
        nextUserId = 4;
    }


    private void seedSpots(){
        for(int i=1; i<=5; i++){
            spots.add(new ParkingSpot(i,"A"));
        }
        for(int i=6; i<=10;i++){
            spots.add(new ParkingSpot(i,"B"));
        }
        
       nextSpotId = 11;
    }


    private void calculateNextIds(){
         nextTicketId = 1;
         for (Ticket t : tickets) {
                if (t.getTicketId() >= nextTicketId) {
                    nextTicketId = t.getTicketId() + 1;
                }
            }
         nextPaymentId = 1;
            for (Payment p : payments) {
                if (p.getPaymentId() >= nextPaymentId) {
                    nextPaymentId = p.getPaymentId() + 1;
                }
            }
           nextCustomerId = 1;
        }

    public Users login(String username, String password){
        for(Users u: users){
            if(u.getUsername().equals(username) && u.getPassword().equals(password)){
                return u;
            }
        }
        return null;
    }



    public void addUser(String name, String username, String password, String role){

        Users u= new Users(nextUserId, name, username, password, role);
        users.add(u);
        nextUserId++;
        System.out.println("User added: " + name + " (ID: " + (nextUserId - 1) + ")");

     }

     public boolean deleteUser(int userId){
        for(int i=0; i<=users.size();i++){
            if(users.get(i).getId() == userId){
                users.remove(i);
                System.out.println("user deleted");
                return true;
            }
        }

        System.out.println("user not found");
        return false;
     }

     
    
     public boolean updateUser(int userId, String name, String username, String password, String role) {

         for (Users u : users) {
                if (u.getId() == userId) {
                    u.setName(name);
                    u.setUsername(username);
                    u.setPassword(password);
                    u.setRole(role);
                    System.out.println("User updated.");
                    return true;
                }
            }
            System.out.println("User not found.");
            return false;
        }

     public void listUsers(){
        if(users.isEmpty()){
            System.out.println("no users found");
        }
        System.out.println("==========All Users=========");
        for(Users u:users){
            u.display();
        }

     }


    public void addSpot(String zone){
        ParkingSpot spot = new ParkingSpot(nextSpotId, zone);
        spots.add(spot);
        nextSpotId++;
        System.out.println("Spot added in zone " + zone + " (ID: " + (nextSpotId - 1) + ")");
     }


     public void listAllSpots() {
            if (spots.isEmpty()) {
                System.out.println("No spots found.");
                return;
            }
            System.out.println("======== ALL SPOTS =========");
            for (ParkingSpot s : spots) {
                s.display();
            }
        }



    public void listFreeSpots() {
            System.out.println("===== FREE SPOTS =====");
            boolean anyFree = false;
            for (ParkingSpot s : spots) {
                if (!s.GetisOccupied()) {
                    s.display();
                    anyFree = true;
                }
            }
            if (!anyFree) {
                System.out.println("No free spots available!");
            }
     } 



     private ParkingSpot getSpotById(int spotId){
         for (ParkingSpot s : spots) {
                if (s.GetSpotId()== spotId) {
                    return s;
                }
            }
            return null; 
        }



    
    public Ticket createTicket(String plateNumber, int spotId) {
    
            ParkingSpot spot = getSpotById(spotId);
            if (spot == null) {
                System.out.println("Spot not found.");
                return null;
            }
            if (spot.GetisOccupied()) {
                System.out.println("Spot " + spotId + " is already occupied. Choose another.");
                return null;
            }
    
            Customer customer = new Customer(nextCustomerId, plateNumber);
            customers.add(customer);
            nextCustomerId++;
    
            Ticket ticket = new Ticket(nextTicketId, plateNumber, spotId);
            tickets.add(ticket);
            nextTicketId++;
    
            spot.occupy();
    
            CSVHandler.saveTickets(tickets);
    
            System.out.println("Ticket created successfully!");
            ticket.display();
            return ticket;
        }

        public Ticket getTicketById(int ticketID){
            for (Ticket t: tickets){
                if(t.getTicketId() == ticketID){
                    return t;
                }

            }
            return null;
        }
        

        public void listAllTickets(){
            if(tickets.isEmpty()) {
                System.out.println("Error, No Tickets Found!!");
                return;
            }
            System.out.println("====ALL TICKETS====");
            for(Ticket t: tickets){
                t.display();
            }
        }

        public void listParkedCars(){
            System.out.println("==== Currently Parked Cars ====");
            boolean ifParked=false;
            for(Ticket t: tickets){
                if(!t.isPaid())
                t.display();
                    ifParked=true; 
            }
            if(!ifParked){
                System.out.println("No Cars Are Parked Now");
            }
        }

        public Payment processPayment(int ticketID){

            Ticket ticket = getTicketById(ticketID);
            if (ticket == null) {
                System.out.println("Ticket not found.");
                return null;
            }        
            if (ticket.isPaid()) {
                System.out.println("This ticket has already been paid.");
                return null;
            }

            long exitTimeMillis=System.currentTimeMillis();
            long durationMillis= exitTimeMillis - ticket.getEntryTimeMillis();
            double totalHours = (double) durationMillis / (1000 * 60 * 60);
            totalHours=Math.ceil(totalHours* 100.0)/ 100.0;


            Payment payment = new Payment(nextPaymentId, ticketID, totalHours, exitTimeMillis);
            payments.add(payment);
            nextPaymentId++;

            ticket.setPaid(true);

            ParkingSpot spot = getSpotById(ticket.getSpotId());
                if (spot != null) {
                spot.free();
            }
            CSVHandler.saveTickets(tickets);
            CSVHandler.savePayments(payments);
    
            System.out.println("Payment processed successfully!");
            payment.display();
    
            return payment;
            
        }
            
            public void shiftReport() {
            if (payments.isEmpty()) {
                System.out.println("No payments recorded yet.");
                return;
            }
    
            double totalCollected = 0;
    
            System.out.println("===== SHIFT PAYMENT REPORT =====");
            for (Payment p : payments) {
                p.display();
                totalCollected += p.getTotalCost();
            }
    
            System.out.println("--------------------------------");
            System.out.println("TOTAL COLLECTED: $" + totalCollected);
            System.out.println("================================");
        }
        
    // PARKED CARS REPORT
    // Same as listParkedCars() but framed as a report
    // Admin views this to see who is still in the lot
    //USED IN GUIIIIIII!!!! (YA FATHAYYYYYYY!!!)




    public void parkedCarsReport() {
        listParkedCars();
    }

        //  =========================GETTERS FOR SWING UI=========================

    
     // Returns all users — used by AdminFrame to fill the users table
    public ArrayList<Users> getUsers() {
        return users;
    }
 
    // Returns all spots — used by AdminFrame (spots tab)
    // and EntryOperatorFrame (free spots table)
    public ArrayList<ParkingSpot> getSpots() {
        return spots;
    }
 
    // Returns all tickets — used by ExitOperatorFrame
    // to show currently parked cars (filtered to unpaid)
    public ArrayList<Ticket> getTickets() {
        return tickets;
    }
 
    // Returns all payments — used by AdminFrame reports tab
    public ArrayList<Payment> getPayments() {
        return payments;
    }
 
    public String getShiftReport() {
        if (payments.isEmpty()) {
            return "No payments recorded yet.";
        }    
    
        StringBuilder sb= new StringBuilder();
        double totalCollected= 0;
        sb.append("======= SHIFT PAYMENT REPORT =======\n\n");
        for (Payment p : payments) {
            sb.append("Payment ID  : ").append(p.getPaymentId()).append("\n");
            sb.append("Ticket ID   : ").append(p.getTicketId()).append("\n");
            sb.append("Total Hours : ").append(p.getTotalHours()).append(" hrs\n");
            sb.append("Total Cost  : $").append(p.getTotalCost()).append("\n");
            sb.append("Exit Time   : ").append(new java.util.Date(p.getExitTimeMillis())).append("\n");
            sb.append("-------------------------------------\n");
            totalCollected += p.getTotalCost();
        }
 
        sb.append("\nTOTAL COLLECTED : $").append(totalCollected).append("\n");
        sb.append("=====================================");
        return sb.toString();

    }

        public String getParkedCarsReport() {
            StringBuilder sb = new StringBuilder();
            sb.append("======= CURRENTLY PARKED CARS =======\n\n");
    
            boolean anyParked = false;
            for (Ticket t : tickets) {
                if (!t.isPaid()) {
                    sb.append("Ticket ID  : ").append(t.getTicketId()).append("\n");
                    sb.append("Plate      : ").append(t.getPlateNumber()).append("\n");
                    sb.append("Spot       : ").append(t.getSpotId()).append("\n");
                    sb.append("Entry Time : ")
                    .append(new java.util.Date(t.getEntryTimeMillis())).append("\n");
                    sb.append("-------------------------------------\n");
                    anyParked = true;
                }
            }
    
            if (!anyParked) {
                sb.append("No cars currently parked.\n");
            }
    
            sb.append("=====================================");
            return sb.toString();
        }



}
