package pacote;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.Arrays;

public class AlgoritmoRicartAgrawala extends UnicastRemoteObject implements InterfaceJavaRMI {

    public boolean solicitandoCS;
    public int respostasPendentes;
    public int maiorNumSeq;
    public int idDoPeer;
    public String meuId;

    
    //Esses são os recursos
    public boolean recurso1EstaSendoUtilizado = false;
    public boolean recurso2EstaSendoUtilizado = false;
    
    //Comunicação pelo Java RMi

    //Serão 3 peers por enquanto
    public int numeroDePeers = 3;

    public boolean[] respostaAdiada;

    public AlgoritmoRicartAgrawala(String nomeDoPeer) throws RemoteException  {

        maiorNumSeq = 0;
        solicitandoCS = false;

        respostasPendentes = numeroDePeers;

        // this.idDoPeer = numSeq;

        //Comunicação pelo Java RMi
        // Numeros do nó também é usado para prioridade (baixo nó = maior prioridade no esquema RicartAgrawala)
        // Numeros do nó são [1, numeroDePeers]; já que estamos começando em 1, então tem que ficar atento com erros ao tentar acessar o nó '0'.
        this.meuId = nomeDoPeer;

        respostaAdiada = new boolean[numeroDePeers];
        //Implementar de uma forma melhor ao pedido de liberar a SC
        Arrays.fill(respostaAdiada, true);
    }

    /**
     * invocacao (inicio do modulo com requisição da CS)
     */
    public boolean perguntarSePossoEntrarNaSC(int recurso) throws NotBoundException, MalformedURLException, RemoteException {

        solicitandoCS = true;
        idDoPeer = maiorNumSeq + 1;
        boolean retorno = false;

        if(meuId == "Peer1") {
            
            LocalDateTime horaDeAgora = LocalDateTime.now();
            retorno = pedirAo("Peer2", horaDeAgora);
            retorno = pedirAo("Peer2", horaDeAgora);
        }
        if(meuId == "Peer2") {
            LocalDateTime horaDeAgora = LocalDateTime.now();
            retorno = pedirAo("Peer2", horaDeAgora);
            retorno = pedirAo("Peer2", horaDeAgora);
        }
        if(meuId == "Peer3") {
            LocalDateTime horaDeAgora = LocalDateTime.now();
            retorno = pedirAo("Peer2", horaDeAgora);
            retorno = pedirAo("Peer2", horaDeAgora);
        }

        //Retornamos quando estivermos prontos para entrar no CS
        return retorno;
    }

    //A outra metade da invocação
    public void liberarSC() throws NotBoundException, MalformedURLException, RemoteException {
        solicitandoCS = false;

        for (int i = 0; i < numeroDePeers - 1; i++) {
            if (respostaAdiada[i]) {
                respostaAdiada[i] = false;
                // if (i < (meuId - 1)) {
                //     responderAo(i + 1);
                // } else {
                //     responderAo(i + 2);
                // }
            }
        }
    }

    /**
     * Recebendo pedido
     *
     * @param seqNumRecebido O número de sequência da mensagem recebida
     * @param numNoRecebido O número do nó da mensagem recebida
     *
     */
    public void receberPedido(int seqNumRecebido, int numNoRecebido) throws NotBoundException, MalformedURLException, RemoteException {
        System.out.println("Pedido recebido do peer" + numNoRecebido);
        boolean bDefer = false;

        maiorNumSeq = Math.max(maiorNumSeq, seqNumRecebido);
        // bDefer = solicitandoCS && ((seqNumRecebido > idDoPeer) || (seqNumRecebido == idDoPeer && numNoRecebido > meuId));
        // if (bDefer) {
        //     System.out.println("Envio diferido de mensagem para" + numNoRecebido);
        //     if (numNoRecebido > meuId) {
        //         respostaAdiada[numNoRecebido - 2] = true;
        //     } else {
        //         respostaAdiada[numNoRecebido - 1] = true;
        //     }
        // } else {
        //     System.out.println("Mensagem de resposta enviada para" + numNoRecebido);
        //     responderAo(numNoRecebido);
        // }

    }

    /**
     * Recebendo Respostas
     */
    public void receberResposta() {
        respostasPendentes = Math.max((respostasPendentes - 1), 0);
        //System.out.println("Outstanding replies: " + respostasPendentes);
    }

    public void responderAo(int idPeerPedinte) throws NotBoundException, MalformedURLException, RemoteException {
        System.out.println("enviado RESPOSTA ao Peer " + idPeerPedinte);
        // if (idPeerPedinte > meuId) {
            //Comunicação pelo Java RMi
            // InterfaceJavaRMI interfaceRemota = (InterfaceJavaRMI) Naming.lookup(NOME_PEER);
            // try {
            //     interfaceRemota.alerta(idPeerPedinte - 2, ("RESPOSTA," + idPeerPedinte));
            // } catch (RemoteException ex) {
            //     Logger.getLogger(AlgoritmoRicartAgrawala.class.getName()).log(Level.SEVERE, null, ex);
            // }
        // } else {
            //Comunicação pelo Java RMi
            // InterfaceJavaRMI interfaceRemota = (InterfaceJavaRMI) Naming.lookup(NOME_PEER);
            // try {
            //     interfaceRemota.alerta(idPeerPedinte - 1, ("RESPOSTA," + idPeerPedinte));
            // } catch (RemoteException ex) {
            //     Logger.getLogger(AlgoritmoRicartAgrawala.class.getName()).log(Level.SEVERE, null, ex);
            // }
        // }
    }

    public boolean pedirAo(String nomeDoPedinte, LocalDateTime horaDoPedido) throws NotBoundException, MalformedURLException, RemoteException {
        System.out.println("enviando PEDIDO ao " + nomeDoPedinte);
        LocalDateTime agora = LocalDateTime.now();

        // if (i > idDoPedinte) {
        //     //Comunicação pelo Java RMi
        //     // InterfaceJavaRMI interfaceRemota = (InterfaceJavaRMI) Naming.lookup(NOME_PEER);
        //     // try {
        //     //    interfaceRemota.alerta(i - 2, ("PEDIDO," + numSeq + "," + idDoPedinte));
        //     // } catch (RemoteException ex) {
        //     //     Logger.getLogger(AlgoritmoRicartAgrawala.class.getName()).log(Level.SEVERE, null, ex);
        //     // }
        // } else {
        //     //Comunicação pelo Java RMi
        //     // InterfaceJavaRMI interfaceRemota = (InterfaceJavaRMI) Naming.lookup(NOME_PEER);
        //     // try {
        //     //     interfaceRemota.alerta(i - 1, ("PEDIDO," + numSeq + "," + idDoPedinte));
        //     // } catch (RemoteException ex) {
        //     //     Logger.getLogger(AlgoritmoRicartAgrawala.class.getName()).log(Level.SEVERE, null, ex);
        //     // }
        // }
        return false;
    }

    @Override
    public boolean estadoDoRecurso(int idDoRecurso) {
        if(idDoRecurso == 1)
            return this.recurso1EstaSendoUtilizado;
        else
            return this.recurso2EstaSendoUtilizado;
    }

}
