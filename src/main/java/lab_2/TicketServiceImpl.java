package lab_2;

import java.util.ArrayList;
import java.util.List;

public class TicketServiceImpl implements TickerService {
    int sum = 0;

    @Override
    public int count(int n) {
        System.out.println("Running " + n + " threads.");

        List<Thread> threads = new ArrayList<>();
        sum  = 0;
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

        System.out.println(sum);
        return sum;
    }

    @Override
    public void exit() {
        XmlServer.stop();
    }

    // Создать несколько потоков исполнения
    class Worker implements Runnable {
        private int a;
        private int b;

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
            synchronized (TicketServiceImpl.this) {
                TicketServiceImpl.this.sum += sum;
            }
        }
    }
}