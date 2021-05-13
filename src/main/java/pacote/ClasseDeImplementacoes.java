package pacote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

//Essa Classe implementa nossos métodos(funções) ela tem o UnicastRemoteObject pra conseguir compartilhar o método pelo Java RMI
public class ClasseDeImplementacoes extends UnicastRemoteObject implements InterfaceJavaRMI {

    //Esse construtor não é muito necessário, mas dá erro as vezes quando tiramos ele
    public ClasseDeImplementacoes() throws RemoteException {
        super();
    }

    @Override
    public String alerta(int idNo, String texto) throws RemoteException {
        String retorno = "Chamada:" + idNo + " Mensagem:" + texto;
        System.out.println(retorno);
        return retorno;
    }

}
