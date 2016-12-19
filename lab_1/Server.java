package lab_1;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Server {
    static volatile int sum = 0;

    public static void main(String[] args) throws IOException {
        System.out.println("Welcome to Server side");
        BufferedReader in;
        PrintWriter out;

        ServerSocket servers = null;
        Socket fromclient = null;

        // create server socket
        try {
            servers = new ServerSocket(4444);
        } catch (IOException e) {
            System.out.println("Couldn't listen to port 4444");
            System.exit(-1);
        }

        try {
            System.out.print("Waiting for a client...");
            fromclient = servers.accept();
            System.out.println("Client connected");
        } catch (IOException e) {
            System.out.println("Can't accept");
            System.exit(-1);
        }

        in = new BufferedReader(new InputStreamReader(fromclient.getInputStream()));
        out = new PrintWriter(fromclient.getOutputStream(), true);
        String input;

        System.out.println("Wait for messages");
        while ((input = in.readLine()) != null) {
            if (input.equalsIgnoreCase("exit")) {
                System.out.println("exiting.");
                break;
            }
            Server.sum = 0;
            Integer n = Integer.parseInt(input);

            System.out.println("Running " + n + " threads.");

            List<Thread> threads = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                Worker worker = new Worker(1000000 * i / n, 1000000 * (i + 1) / n - 1);
                Thread t = new Thread(worker);
                t.start();
                threads.add(t);
            }
            for (Thread t : threads) {
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            out.println(Server.sum);
            System.out.println(input);
        }

        out.close();
        in.close();
        fromclient.close();
        servers.close();
    }
}


// Создать несколько потоков исполнения
class Worker implements Runnable {
    int a, b;

    Worker(int a, int b) {
        this.a = a;
        this.b = b;
    }

    //точка входа в поток исполнения
    public void run() {
        int[] c = new int[6];
        int sum = 0;
        for (int i = a; i <= b; i++) {
            int current = i;
            for (int j = 0; j < 6; j++) {
                c[j] = current % 10;
                current /= 10;
            }
            if (c[0] + c[1] + c[2] == c[3] + c[4] + c[5]) {
                sum++;
            }
        }
        synchronized (Server.class) {
            Server.sum += sum;
        }
    }
}