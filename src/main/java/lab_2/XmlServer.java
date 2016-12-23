package lab_2;

import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;

public class XmlServer {
    private static volatile boolean work = true;
    private static final Object lock = new Object();


    private static final int port = 8080;

    public static void main(String[] args) throws Exception {
        WebServer webServer = new WebServer(8080);
        XmlRpcServer xmlRpcServer = webServer.getXmlRpcServer();
        PropertyHandlerMapping phm = new PropertyHandlerMapping();
        phm.setVoidMethodEnabled(true);
        phm.addHandler(TickerService.class.getName(), TicketServiceImpl.class);
        xmlRpcServer.setHandlerMapping(phm);

        XmlRpcServerConfigImpl serverConfig = (XmlRpcServerConfigImpl)
                xmlRpcServer.getConfig();

        serverConfig.setEnabledForExtensions(true);
        serverConfig.setContentLengthOptional(false);
        webServer.start();

        while (work) {
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            Thread.sleep(1);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        webServer.shutdown();
    }

    public static void stop() {
        work = false;
        synchronized (lock) {
            try {
                lock.notify();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
