package pacote;

import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class AlgoritmoRicartAgrawala extends UnicastRemoteObject implements InterfaceJavaRMI {

    public int respostasPendentes;
    LocalDateTime horaDoPedido;
    public int idDoPeer;
    public String meuId;

    public class Fila {
        public Fila(String peer, LocalDateTime horaDoEvento) {
            this.peer = peer;
            this.horaDoPedido = horaDoEvento;
        }
        public String peer;
        public LocalDateTime horaDoPedido;
    }

    // Os três estados do recurso são:
    static String held = "held"; // (o processo está usando o recurso)
    static String released = "released"; // (o processo não está usando o recurso)
    static String wanted = "wanted"; // (o processo quer usar o recurso)

    // Esses são os recursos
    public String recurso1EstaSendoUtilizado = "released";
    public String recurso2EstaSendoUtilizado = "released";

    // Comunicação pelo Java RMi
    // Serão 3 peers por enquanto
    public int numeroDePeers = 3;

    public static ArrayList<Fila> filaDePeers = new ArrayList<Fila>();

    public AlgoritmoRicartAgrawala(String nomeDoPeer) throws RemoteException {

        respostasPendentes = numeroDePeers;

        // this.idDoPeer = numSeq;
        // Comunicação pelo Java RMi
        // Fazer a fila
        // Numeros do nó também é usado para prioridade (baixo nó = maior prioridade no
        // esquema RicartAgrawala)
        // Numeros do nó são [1, numeroDePeers]; já que estamos começando em 1, então
        // tem que ficar atento com erros ao tentar acessar o nó '0'.
        this.meuId = nomeDoPeer;
        // Implementar de uma forma melhor ao pedido de liberar a SC
        // Arrays.fill(filaDePeers, trunew Fila[3];
    }

    /**
     * invocacao (inicio do modulo com requisição da CS)
     */
    public boolean perguntarSePossoEntrarNaSC(int recurso) throws NotBoundException, MalformedURLException, RemoteException {

        // Declarando que eu quero usar o recurso
        mudarEstadoDoRecurso(recurso, AlgoritmoRicartAgrawala.wanted);

        System.out.println("\nO " + this.meuId + " está pedindo o recurso " + recurso);

        // fila de peers
        LocalDateTime horaDeAgora = LocalDateTime.now();

        if (meuId == "Peer1") {
            String respostaPeer2 = pedirAo("Peer2", recurso, horaDeAgora); // vai responder: held, wanted ou released
            String respostaPeer3 = pedirAo("Peer3", recurso, horaDeAgora);

            // Se os outros dois peers não estão usando nem querendo usar, eu uso.
            boolean recursoEstaLiberado = respostaPeer2 == AlgoritmoRicartAgrawala.released && respostaPeer3 == AlgoritmoRicartAgrawala.released;

            System.out.println("Recurso " + recurso + " está livre!");
            if (recursoEstaLiberado) {
                // Mudo o estado do recurso para held
                mudarEstadoDoRecurso(recurso, AlgoritmoRicartAgrawala.held);
                removerDaPrimeroDaFila(recurso);
                return true;
            }

            //Caso esteja sendo usado
            // boolean recursoEstaSendoUsado = respostaPeer2 == AlgoritmoRicartAgrawala.held || respostaPeer3 == AlgoritmoRicartAgrawala.held;
            // então vou ficar na fila esperando quando liberar
            filaDePeers.add(new Fila(meuId, horaDeAgora));
            return false;

            // boolean querUsarORecursoTambem = respostaPeer2 == AlgoritmoRicartAgrawala.wanted || respostaPeer2 == AlgoritmoRicartAgrawala.wanted;
            // ver quem pediu primeiro
        }
        if (meuId == "Peer2") {
            String respostaPeer1 = pedirAo("Peer1", recurso, horaDeAgora); // vai responder: held, wanted ou released
            String respostaPeer3 = pedirAo("Peer3", recurso, horaDeAgora);

            boolean recursoEstaLiberado = respostaPeer1 != AlgoritmoRicartAgrawala.released && respostaPeer3 != AlgoritmoRicartAgrawala.released;
            System.out.println("Recurso " + recurso + " está livre!");
            if (recursoEstaLiberado) {
                mudarEstadoDoRecurso(recurso, AlgoritmoRicartAgrawala.held);
                return true;
            }
            // boolean recursoEstaSendoUsado = respostaPeer1 != AlgoritmoRicartAgrawala.held || respostaPeer1 != AlgoritmoRicartAgrawala.held;
            // então vou ficar na fila esperando quando liberar
            // boolean querUsarORecursoTambem = respostaPeer1 != AlgoritmoRicartAgrawala.wanted || respostaPeer1 != AlgoritmoRicartAgrawala.wanted;
            // ver quem pediu primeiro
            filaDePeers.add(new Fila(meuId, horaDeAgora));
            return false;
        }
        if (meuId == "Peer3") {
            String respostaPeer1 = pedirAo("Peer1", recurso, horaDeAgora); // vai responder: held, wanted ou released
            String respostaPeer2 = pedirAo("Peer2", recurso, horaDeAgora);

            // Se os outros dois peers não estão usando nem querendo usar, eu uso.
            boolean recursoEstaLiberado = respostaPeer1 != AlgoritmoRicartAgrawala.released
                    && respostaPeer2 != AlgoritmoRicartAgrawala.released;

            System.out.println("Recurso " + recurso + " está livre!");
            if (recursoEstaLiberado) {
                mudarEstadoDoRecurso(recurso, AlgoritmoRicartAgrawala.held);
                return true;
            }
            // boolean recursoEstaSendoUsado = respostaPeer1 != AlgoritmoRicartAgrawala.held || respostaPeer1 != AlgoritmoRicartAgrawala.held;
            // então vou ficar na fila esperando quando liberar
            // boolean querUsarORecursoTambem = respostaPeer2 != AlgoritmoRicartAgrawala.wanted || respostaPeer2 != AlgoritmoRicartAgrawala.wanted;
            // ver quem pediu primeiro
            filaDePeers.add(new Fila(meuId, horaDeAgora));
            return false;
        }

        //
        // Se a resposta for FALSE nenhum peer está usando
        // Se for TRUE algum peer está usando
        // Então esse peer tem que liberar o recurso
        return false;
    }

    private void mudarEstadoDoRecurso(int recurso, String estado) {
        if (recurso == 1) {
            this.recurso1EstaSendoUtilizado = estado;
        }
        if (recurso == 2) {
            this.recurso2EstaSendoUtilizado = estado;
        }
    }

    // A outra metade da invocação
    public void liberarSC(int qualRecurso) throws NotBoundException, MalformedURLException, RemoteException {
        if (qualRecurso == 1) {
            this.recurso1EstaSendoUtilizado = AlgoritmoRicartAgrawala.released;
        }

        if (qualRecurso == 2) {
            this.recurso2EstaSendoUtilizado = AlgoritmoRicartAgrawala.released;
        }

        removerDaPrimeroDaFila(qualRecurso);

        // solicitandoCS = false;
        // for (int i = 0; i < numeroDePeers - 1; i++) {
        // if (filaDePeers[i]new Fila[3]{
        // filaDePeers[i] = falnew Fila[3];
        // // if (i < (meuId - 1)) {
        // // responderAo(i + 1);
        // // } else {
        // // responderAo(i + 2);
        // // }
        // }
        // }
    }

    private void removerDaPrimeroDaFila(int qualRecurso) {
        if (AlgoritmoRicartAgrawala.filaDePeers.size() > 0) {
            AlgoritmoRicartAgrawala.filaDePeers.get(0);
            mudarEstadoDoRecurso(qualRecurso, AlgoritmoRicartAgrawala.held);
            AlgoritmoRicartAgrawala.filaDePeers.remove(0);
        }
    }

    /**
     * Recebendo pedido
     *
     * @param seqNumRecebido O número de sequência da mensagem recebida
     * @param numNoRecebido  O número do nó da mensagem recebida
     *
     */
    // public void receberPedido(int seqNumRecebido, int numNoRecebido) throws
    // NotBoundException, MalformedURLException, RemoteException {
    // System.out.println("Pedido recebido do peer" + numNoRecebido);
    // // boolean bDefer = false;
    // maiorNumSeq = Math.max(maiorNumSeq, seqNumRecebido);
    // // bDefer = solicitandoCS && ((seqNumRecebido > idDoPeer) || (seqNumRecebido
    // == idDoPeer && numNoRecebido > meuId));
    // // if (bDefer) {
    // // System.out.println("Envio diferido de mensagem para" + numNoRecebido);
    // // if (numNoRecebido > meuId) {
    // // filaDePeers[numNoRecebido - 2] = trnew Fila[3];
    // // } else {
    // // filaDePeers[numNoRecebido - 1] = trnew Fila[3];
    // // }
    // // } else {
    // // System.out.println("Mensagem de resposta enviada para" + numNoRecebido);
    // // responderAo(numNoRecebido);
    // // }
    // }
    /**
     * Recebendo Respostas
     */
    // public void receberResposta() {
    // respostasPendentes = Math.max((respostasPendentes - 1), 0);
    // //System.out.println("Outstanding replies: " + respostasPendentes);
    // }
    // public void responderAo(int idPeerPedinte) throws NotBoundException,
    // MalformedURLException, RemoteException {
    // System.out.println("enviado RESPOSTA ao Peer " + idPeerPedinte);
    // // if (idPeerPedinte > meuId) {
    // //Comunicação pelo Java RMi
    // // InterfaceJavaRMI interfaceRemota = (InterfaceJavaRMI)
    // Naming.lookup(NOME_PEER);
    // // try {
    // // interfaceRemota.alerta(idPeerPedinte - 2, ("RESPOSTA," + idPeerPedinte));
    // // } catch (RemoteException ex) {
    // //
    // Logger.getLogger(AlgoritmoRicartAgrawala.class.getName()).log(Level.SEVERE,
    // null, ex);
    // // }
    // // } else {
    // //Comunicação pelo Java RMi
    // // InterfaceJavaRMI interfaceRemota = (InterfaceJavaRMI)
    // Naming.lookup(NOME_PEER);
    // // try {
    // // interfaceRemota.alerta(idPeerPedinte - 1, ("RESPOSTA," + idPeerPedinte));
    // // } catch (RemoteException ex) {
    // //
    // Logger.getLogger(AlgoritmoRicartAgrawala.class.getName()).log(Level.SEVERE,
    // null, ex);
    // // }
    // // }
    // }
    public String pedirAo(String nomeDoPedinte, int recurso, LocalDateTime horaDoPedido)
            throws NotBoundException, MalformedURLException, RemoteException {
        System.out.println("Pedido para utilizar o recurso " + recurso + " ao " + nomeDoPedinte);
        this.horaDoPedido = horaDoPedido;

        InterfaceJavaRMI interfaceRemota = (InterfaceJavaRMI) Naming.lookup(nomeDoPedinte);
        return interfaceRemota.estadoDoRecurso(recurso);
    }

    @Override
    public String estadoDoRecurso(int idDoRecurso) {
        if (idDoRecurso == 1) {
            return this.recurso1EstaSendoUtilizado;
        } else {
            return this.recurso2EstaSendoUtilizado;
        }
    }

}
