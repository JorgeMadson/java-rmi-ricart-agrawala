package pacote;

// Todas as interfaces e classes para o sistema de RMI estão no pacote java.rmi
import java.rmi.Remote;
import java.rmi.RemoteException;

//Essa interface funciona como um contrato nela mostramos quais funções estamos disponibilizando
public interface InterfaceJavaRMI extends Remote {

    String estadoDoRecurso(int idDoRecurso) throws RemoteException;

    //isso aqui é o certo:
    // boolean solicitarRecurso //praticamente um  estadoDoRecurso
    // void liberarRecurso //muda o estado do recuso pra released
    // boolean notificarLiberacaoDeRecurso //esse aqui não entendi muito bem ainda
}

//O que é o?
//Java RMI, ou seja, Remote Method Invocation, é uma API Java usada para implementar chamadas de procedimento remoto (RPC) (chamada de procedimento remoto),
//que pode transmitir diretamente objetos Java serializados e coleta de lixo distribuída. Sua implementação depende da Java Virtual Machine (JVM),
//portanto, ela só oferece suporte a chamadas de uma JVM para outra.
