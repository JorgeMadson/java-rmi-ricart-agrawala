package pacote;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

//Essa classe server apenas para testar o servidor Java RMI
//Ou seja, para checar se o servidor Java RMi está funcionando como deveria
public class ClienteJavaRMI {

    public static String respostaServidor;

    public static void main(String[] args) throws MalformedURLException {
        
        int carimboDeTempo = 0;
        int numSeq = 1;
        int numeroDaPorta = 1099;
        AlgoritmoRicartAgrawala peer1 = new AlgoritmoRicartAgrawala(carimboDeTempo, numSeq, numeroDaPorta);
        AlgoritmoRicartAgrawala peer2 = new AlgoritmoRicartAgrawala(1, 2, 1100);
        AlgoritmoRicartAgrawala peer3 = new AlgoritmoRicartAgrawala(2, 3, 1101);

        String[] todosOsPeersConectados;
        try {
            todosOsPeersConectados = peer1.receberNomePeersNaRede();
            
            System.out.println(concatenarListaDeString(todosOsPeersConectados));

        } catch (RemoteException ex) {
            System.out.println(ex);
            Logger.getLogger(ClienteJavaRMI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static String concatenarListaDeString(String[] lista) {
        String resposta = "";
         for (String umaString : lista) {
            resposta = resposta + umaString + "\n";
        }
         return resposta;
    }

    //Executa a interface remota
    public static void executarInterfaceRemota(int carimboDeTempo, int numSeq, int numeroDaPorta) {
        try {
            verQuemSaoOsProcessosAtivos();

            InterfaceJavaRMI ola = (InterfaceJavaRMI) Naming.lookup("Peer"+Integer.toString(numSeq));
//            respostaServidor = "Resposta do servidor: " + ola.alerta(1, "Teste");
            
        } catch (MalformedURLException | NotBoundException | RemoteException e) {
            System.out.println(e);
        }
    }

    public static void verQuemSaoOsProcessosAtivos() throws RemoteException, MalformedURLException {
        //Observando nome dos servidores
        String[] lista = Naming.list("");
        for (String nome : lista) {
            System.out.println(nome);
        }
    }
}
