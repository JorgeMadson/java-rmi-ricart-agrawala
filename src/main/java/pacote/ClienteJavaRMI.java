package pacote;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

//Essa classe server apenas para testar o servidor Java RMI
//Ou seja, para checar se o servidor Java RMi está funcionando como deveria
public class ClienteJavaRMI {

    public static String respostaServidor;

    public static void main(String[] args) {
        
        
        abrirUmProcessoCliente(1,1, 1099);
//        abrirUmProcessoCliente(2,2, 1100);
//        abrirUmProcessoCliente(3,3, 1101);
    }

    public static void abrirUmProcessoCliente(int carimboDeTempo, int numSeq, int numeroDaPorta) {
        try {
            verQuemSaoOsProcessosAtivos();

            InterfaceJavaRMI ola = (InterfaceJavaRMI) Naming.lookup("Peer"+Integer.toString(numSeq));
            AlgoritmoRicartAgrawala processo = new AlgoritmoRicartAgrawala(carimboDeTempo, numSeq, numeroDaPorta);
//            processo.invocacao();
            respostaServidor = "Resposta do servidor: " + ola.alerta(1, "Teste");
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
