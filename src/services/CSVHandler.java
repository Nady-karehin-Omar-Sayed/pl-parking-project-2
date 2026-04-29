package services;

import java.io.*;
import java.util.ArrayList;
import models.Payment;
import models.Ticket;



public class CSVHandler {

    
    private static final String TICKETS_FILE  = "data/tickets.csv";
    private static final String PAYMENTS_FILE = "data/payments.csv";

   
    public static void saveTickets(ArrayList<Ticket> tickets) {
        try {
          
            FileWriter fw = new FileWriter(TICKETS_FILE, false);
            BufferedWriter bw = new BufferedWriter(fw);

          
            bw.write("ticketId,plateNumber,spotId,entryTimeMillis,isPaid");
            bw.newLine();

         
            for (Ticket t : tickets) {
                bw.write(t.toCSV());
                bw.newLine();
            }

            bw.close();
            System.out.println("[CSV] Tickets saved successfully.");

        } catch (IOException e) {
            System.out.println("[CSV ERROR] Could not save tickets: " + e.getMessage());
        }
    }

  
    public static ArrayList<Ticket> loadTickets() {
        ArrayList<Ticket> tickets = new ArrayList<>();

        try {
            FileReader fr = new FileReader(TICKETS_FILE);
            BufferedReader br = new BufferedReader(fr);

            String line;
            boolean firstLine = true; 

            while ((line = br.readLine()) != null) {

                
                if (firstLine) { firstLine = false; continue; }

          
                String[] parts = line.split(",");

                if (parts.length == 5) {
                    int ticketId          = Integer.parseInt(parts[0].trim());
                    String plateNumber    = parts[1].trim();
                    int spotId            = Integer.parseInt(parts[2].trim());
                    long entryTimeMillis  = Long.parseLong(parts[3].trim());
                    boolean isPaid        = Boolean.parseBoolean(parts[4].trim());

                   
                    Ticket t = new Ticket(ticketId, plateNumber, spotId, entryTimeMillis, isPaid);
                    tickets.add(t);
                }
            }

            br.close();
            System.out.println("[CSV] Loaded " + tickets.size() + " ticket(s).");

        } catch (FileNotFoundException e) {
           
            System.out.println("[CSV] No tickets file found. Starting fresh.");
        } catch (IOException e) {
            System.out.println("[CSV ERROR] Could not load tickets: " + e.getMessage());
        }

        return tickets;
    }


    public static void savePayments(ArrayList<Payment> payments) {
        try {
            FileWriter fw = new FileWriter(PAYMENTS_FILE, false);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write("paymentId,ticketId,totalHours,totalCost,exitTimeMillis");
            bw.newLine();

            for (Payment p : payments) {
                bw.write(p.toCSV());
                bw.newLine();
            }

            bw.close();
            System.out.println("[CSV] Payments saved successfully.");

        } catch (IOException e) {
            System.out.println("[CSV ERROR] Could not save payments: " + e.getMessage());
        }
    }

 
    public static ArrayList<Payment> loadPayments() {
        ArrayList<Payment> payments = new ArrayList<>();

        try {
            FileReader fr = new FileReader(PAYMENTS_FILE);
            BufferedReader br = new BufferedReader(fr);

            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {

                if (firstLine) { firstLine = false; continue; }

               
                String[] parts = line.split(",");

                if (parts.length == 5) {
                    int paymentId        = Integer.parseInt(parts[0].trim());
                    int ticketId         = Integer.parseInt(parts[1].trim());
                    double totalHours    = Double.parseDouble(parts[2].trim());
                    double totalCost     = Double.parseDouble(parts[3].trim());
                    long exitTimeMillis  = Long.parseLong(parts[4].trim());

                    
                    Payment p = new Payment(paymentId, ticketId, totalHours, totalCost, exitTimeMillis);
                    payments.add(p);
                }
            }

            br.close();
            System.out.println("[CSV] Loaded " + payments.size() + " payment(s).");

        } catch (FileNotFoundException e) {
            System.out.println("[CSV] No payments file found. Starting fresh.");
        } catch (IOException e) {
            System.out.println("[CSV ERROR] Could not load payments: " + e.getMessage());
        }

        return payments;
    }

  
    public static void ensureDataFolder() {
        File folder = new File("data");
        if (!folder.exists()) {
            folder.mkdir();
            System.out.println("[CSV] Created data/ folder.");
        }
    }
}