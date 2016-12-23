package lab_2;



import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.util.ClientFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.SyncFailedException;
import java.net.Socket;
import java.net.URL;


public class XmlClient {
    public static void main(String[] args) throws Exception {
        System.out.println("Welcome to Client side");

        if (args.length == 0) {
            System.out.println("use: client hostname");
            System.exit(-1);
        }
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        System.out.println("Connecting to... " + args[0]);

        config.setServerURL(new URL("http://" + args[0] + ":8080/xmlrpc"));
        config.setEnabledForExtensions(true);
        config.setConnectionTimeout(60 * 1000);
        config.setReplyTimeout(60 * 1000);
        XmlRpcClient client = new XmlRpcClient();
        client.setConfig(config);
        ClientFactory factory = new ClientFactory(client);
        TickerService ticket = (TickerService) factory.newInstance(TickerService.class);

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
    }
}