package pacote;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

// O server serve a nossa função

public class ServerJavaRMI {
    public static void main(String[] args) throws AlreadyBoundException, MalformedURLException {
        try {
            /*
            Caso queria mudar a porta em que vamos conectar, a padão é 1099
            Registry registry = LocateRegistry.createRegistry(1099);
            */
            InterfaceJavaRMI ola = new OlaMundoImplmentacao();
            /*
            Caso tenha mudado a porta          
            registry.bind("Ola", ola);
            */
            //
            Naming.bind("Ola", ola);

            
            System.out.println("Servindo a classe OlaMundoImplmentacao");
        }
        catch (RemoteException e){
            e.printStackTrace();
        }
    }   
}
