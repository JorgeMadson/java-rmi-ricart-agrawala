package pacote;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

//Essa classe server apenas para testar o servidor Java RMI
//Ou seja, para checar se o servidor Java RMi está funcionando como deveria
public class ClasseParaTestesJavaRMI {

    public static String respostaServidor;

    public static void main(String[] args) throws MalformedURLException {
        
        int meuId = 1;
        int numSeq = 1;
        int numeroDaPorta = 1099;
        AlgoritmoRicartAgrawala peer1 = new AlgoritmoRicartAgrawala(meuId, numSeq, numeroDaPorta);
        AlgoritmoRicartAgrawala peer2 = new AlgoritmoRicartAgrawala(2, 2, 1100);
        AlgoritmoRicartAgrawala peer3 = new AlgoritmoRicartAgrawala(3, 3, 1101);

        String[] todosOsPeersConectados;
    }
    
    public static String concatenarListaDeString(String[] lista) {
        String resposta = "";
         for (String umaString : lista) {
            resposta = resposta + umaString + "\n";
        }
         return resposta;
    }

    //Executa a interface remota
    public static void executarInterfaceRemota(int meuId, int numSeq, int numeroDaPorta) {
        try {
            verQuemSaoOsProcessosAtivos();

            InterfaceJavaRMI ola = (InterfaceJavaRMI) Naming.lookup("Peer"+Integer.toString(numSeq));
//        respostaServidor = "Resposta do servidor: " + ola.alerta(1, "Teste");
            
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
