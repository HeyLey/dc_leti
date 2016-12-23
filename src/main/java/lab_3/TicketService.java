package lab_3;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TicketService extends Remote {
    public int count(int n) throws RemoteException;
    public void exit() throws RemoteException;

}
