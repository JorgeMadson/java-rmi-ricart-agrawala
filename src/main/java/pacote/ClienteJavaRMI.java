package pacote;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

//Esse é o cliente onde a gente tem acesso a classe que foi compartilhada e podemos executar os métodos dela
public class ClienteJavaRMI {

    public static String respostaServidor;

    public static void main(String[] args) {
        abrirUmProcessoCliente(1,1);
        abrirUmProcessoCliente(2,2);
        abrirUmProcessoCliente(3,3);
    }

    public static void abrirUmProcessoCliente(int carimboDeTempo, int numSeq) {
        try {
            //Observando nome dos servidores
            String[] lista = Naming.list("");
            for (String nome : lista) {
                System.out.println(nome);
            }

            InterfaceJavaRMI ola = (InterfaceJavaRMI) Naming.lookup("Ola");
            AlgoritmoRicartAgrawala processo = new AlgoritmoRicartAgrawala(carimboDeTempo, numSeq);
            processo.invocacao();
            respostaServidor = "Resposta do servidor: " + ola.alerta(1, "Teste");
        } catch (MalformedURLException | NotBoundException | RemoteException e) {
            System.out.println(e);
        }
    }
}
