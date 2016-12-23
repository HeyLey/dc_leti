package lab_3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RmiClient {
    public static void main(String args[]) {
        try {
            String name = "TicketService";
            Registry registry = LocateRegistry.getRegistry();
            TicketService ticket = (TicketService) registry.lookup(name);

            System.out.println("Connected successfully...");

            BufferedReader inu = new BufferedReader(new InputStreamReader(System.in));

            String fuser;

            while ((fuser = inu.readLine())!= null) {
                if (fuser.equalsIgnoreCase("close")) {

                    break;
                } else if (fuser.equalsIgnoreCase("exit")) {
                    System.out.println("Shutting down server");

                    ticket.exit();
                    break;
                } else {
                    Integer n = Integer.parseInt(fuser);
                    System.out.println("Counting with " + n + " threads.");
                    int count = ticket.count(n);
                    System.out.println("Tickets number: " + count);
                }

            }

        } catch (Exception e) {
            System.err.println("TicketService exception:");
            e.printStackTrace();
        }

    }
}
