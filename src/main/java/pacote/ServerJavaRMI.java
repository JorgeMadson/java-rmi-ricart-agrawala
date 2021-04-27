package pacote;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

// O serve a nossa classe, preferi fazer os atributos e funções de forma estática

public class ServerJavaRMI {
    static String NOME_SERVIDOR = "Ola";
    public static void main(String[] args) throws AlreadyBoundException, MalformedURLException {
        try {

            InterfaceJavaRMI implementacao = new ClasseDeImplementacoes();

            //Caso queria mudar a porta em que vamos conectar, a padão é 1099
            Registry registry = LocateRegistry.createRegistry(1099);
            
            String[] listaServerLigados = registry.list();
            
            listarServidores(listaServerLigados);
            
            if (listaServerLigados.length == 0) {
                registry.bind(NOME_SERVIDOR, implementacao);
            }
            else {
                //Recebe o servidor ativo da lista
                registry = LocateRegistry.getRegistry(NOME_SERVIDOR);
            }

            //ToDo: Entender o Naming x Registry
            //Caso registry não funcione, use o naming:
            //Naming.bind(NOME_SERVIDOR, ola);

            
            System.out.println("Servindo a classe OlaMundoImplmentacao");
        }
        catch (RemoteException e){
            e.printStackTrace();
        }
    }

    public static void listarServidores(String[] listaServerLigados) {
        System.out.println("Numero de servidores ligados: " + listaServerLigados.length);
                
            for (String nomeServer : listaServerLigados) {
                
                System.out.println("Servidores:" + nomeServer);
            }
    }
}
