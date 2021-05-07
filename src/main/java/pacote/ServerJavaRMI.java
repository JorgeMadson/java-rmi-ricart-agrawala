package pacote;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

// O serve a nossa classe, preferi fazer os atributos e funções de forma estática
public class ServerJavaRMI {

    static String NOME_SERVIDOR = "Peer";
    static String resposta;
    static String servidorEstaAtivo = "Não ativo...";

    public static void main(String[] args) throws AlreadyBoundException, MalformedURLException {
        iniciarServidor("Teste");
    }

    public static String listarServidores(String[] listaServerLigados) {

        resposta = "Numero de servidores ativos: " + listaServerLigados.length + "\n";

        for (String nomeServer : listaServerLigados) {

            resposta = resposta + "Servidores:" + nomeServer + "\n";
        }
        return resposta;
    }

    public static void iniciarServidor(String identificador) throws AlreadyBoundException {
        try {

            InterfaceJavaRMI implementacao = new ClasseDeImplementacoes();

            //Dá pra usar o Naming e Registry
            //Caso registry não funcione, use o naming:
            //Naming.bind(NOME_SERVIDOR, ola);

            //Caso queria mudar a porta em que vamos conectar, a padrão é 1099
            Registry registry = LocateRegistry.createRegistry(1099);

            String[] listaServerLigados = registry.list();

            System.out.println(listarServidores(listaServerLigados));

            if (listaServerLigados.length == 0) {
                registry.bind(ServerJavaRMI.NOME_SERVIDOR  + identificador, implementacao);
            } else {
                //Recebe o servidor ativo da lista
                registry = LocateRegistry.getRegistry(ServerJavaRMI.NOME_SERVIDOR + identificador);
            }

            servidorEstaAtivo = "Servindo classe ClasseDeImplementacoes";
            System.out.println(servidorEstaAtivo);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
