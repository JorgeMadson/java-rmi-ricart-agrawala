package pacote;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import sun.util.locale.provider.LocaleResources;

// O serve a nossa classe, preferi fazer os atributos e funções de forma estática
public class ServerJavaRMI {

    static String NOME_SERVIDOR = "Peer";
    static String resposta;
    static String servidorEstaAtivo = "Não ativo...";

    public static void main(String[] args) throws AlreadyBoundException, MalformedURLException, RemoteException, NotBoundException {

        //Criou o registro, agora dá pra 
        LocateRegistry.createRegistry(1099);
        
        iniciarServidor("Peer1");
        iniciarServidor("Peer2");
        iniciarServidor("Peer3");
        iniciarServidor("1");
        Remote registros = Naming.lookup("Peer1");
        Remote todosOsRegistros = Naming.lookup("");
        
        String soPraSegurarOBreak = "chegou";
        soPraSegurarOBreak+=" no fim";
    }

    public static String listarServidores(String[] listaServerLigados) {

        //Vê se existe algum servidor ativo
        resposta = "Numero de servidores ativos na porta: " + listaServerLigados.length + "\n";

        //Adiciona os servidores ativos a lista
        for (String nomeServer : listaServerLigados) {

            resposta = resposta + "Servidores:" + nomeServer + "\n";
        }
        return resposta;
    }

    public static void iniciarServidor(String identificador) throws AlreadyBoundException, MalformedURLException {
        try {

            //Dá pra usar o Naming e Registry
            //Caso registry não funcione, use o naming:
            Naming.bind(NOME_SERVIDOR + identificador, new ClasseDeImplementacoes());

            servidorEstaAtivo = "Servindo classe ClasseDeImplementacoes";
            System.out.println(servidorEstaAtivo);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
