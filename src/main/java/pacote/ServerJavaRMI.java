package pacote;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;

// O server serve a nossa função

public class ServerJavaRMI {
    public static void main(String[] args) {
        try {
            InterfaceJavaRMI ola = new OlaMundoImplmentacao();
            Naming.bind("rmi://localhost:1109/Ola", ola);
            
            System.out.println("Servido a classe OlaMundoImplmentacao");
        }
        catch (MalformedURLException | AlreadyBoundException | RemoteException e){
            e.printStackTrace();
        }
    }   
}
