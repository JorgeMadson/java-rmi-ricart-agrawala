package pacote;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class AlgoritmoRicartAgrawala {

    private static final String NOME_PEER = "Peer1";
    public boolean solicitandoCS;
    public int respostasPendentes;
    public int maiorNumSeq;
    public int idDoPeer;
    public int meuId;
    //Comunicação pelo Java RMi

    //Serão 3 peers por enquanto
    public int nosNoCanal = 3;

    public boolean[] respostaAdiada;

    public AlgoritmoRicartAgrawala(int meuId, int numSeq, int numeroDaPorta) {
        solicitandoCS = false;

        respostasPendentes = nosNoCanal;

        maiorNumSeq = 0;
        this.idDoPeer = numSeq;

        //Comunicação pelo Java RMi
        // Numeros do nó também é usado para prioridade (baixo nó = maior prioridade no esquema RicartAgrawala)
        // Numeros do nó são [1, nosNoCanal]; já que estamos começando em 1, então tem que ficar atento com erros ao tentar acessar o nó '0'.
        this.meuId = meuId;

        respostaAdiada = new boolean[nosNoCanal];

        try {
            //Iniciando o servidor RMI
            ServerJavaRMI.iniciarServidor(String.valueOf(numSeq), numeroDaPorta);
            //ClasseParaTestesJavaRMI.executarInterfaceRemota(meuId, idDoPeer, numeroDaPorta);
        } catch (AlreadyBoundException ex) {
            System.out.println(ex);
            Logger.getLogger(AlgoritmoRicartAgrawala.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String[] receberNomePeersNaRede() throws RemoteException, MalformedURLException {

        //Cada porta só tem um peer então pegamos sempre o primeiro
        String peerPorta1099 = nomePeersNaPorta(1099)[0];
        String peerPorta1100 = nomePeersNaPorta(1100)[0];
        String peerPorta1101 = nomePeersNaPorta(1101)[0];

        return new String[]{peerPorta1099, peerPorta1100, peerPorta1101};
    }

    public static String[] nomePeersNaPorta(int porta) throws RemoteException {

        //Checando 
        Registry registryPorta = LocateRegistry.getRegistry(porta);
        //Obtendo nome dos peers na rede na porta específica
        String[] listaServerLigados = registryPorta.list();

        if (listaServerLigados.length == 1) {
            return listaServerLigados;
        } else {
            Exception e = new Exception("Mais de um peer conectado na porta");
            return new String[]{""};
        }
    }

    /**
     * invocacao (inicio do modulo com requisição da CS)
     */
    public boolean perguntarSePossoEntrarNaSC() throws NotBoundException, MalformedURLException, RemoteException {

        solicitandoCS = true;
        idDoPeer = maiorNumSeq + 1;

        for (int i = 1; i < nosNoCanal + 1; i++) {
            if (i != meuId) {
                pedirAo(idDoPeer, meuId, i);
            }
        }

        //Retornamos quando estivermos prontos para entrar no CS
        return true;
    }

    //A outra metade da invocação
    public void liberarSC() throws NotBoundException, MalformedURLException, RemoteException {
        solicitandoCS = false;

        for (int i = 0; i < nosNoCanal; i++) {
            if (respostaAdiada[i]) {
                respostaAdiada[i] = false;
                if (i < (meuId - 1)) {
                    responderAo(i + 1);
                } else {
                    responderAo(i + 2);
                }
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
        System.out.println("Pedido recebido do nó" + numNoRecebido);
        boolean bDefer = false;

        maiorNumSeq = Math.max(maiorNumSeq, seqNumRecebido);
        bDefer = solicitandoCS && ((seqNumRecebido > idDoPeer) || (seqNumRecebido == idDoPeer && numNoRecebido > meuId));
        if (bDefer) {
            System.out.println("Envio diferido de mensagem para" + numNoRecebido);
            if (numNoRecebido > meuId) {
                respostaAdiada[numNoRecebido - 2] = true;
            } else {
                respostaAdiada[numNoRecebido - 1] = true;
            }
        } else {
            System.out.println("Mensagem de resposta enviada para" + numNoRecebido);
            responderAo(numNoRecebido);
        }

    }

    /**
     * Recebendo Respostas
     */
    public void receberResposta() {
        respostasPendentes = Math.max((respostasPendentes - 1), 0);
        //System.out.println("Outstanding replies: " + respostasPendentes);
    }

    public void responderAo(int numNoRecebido) throws NotBoundException, MalformedURLException, RemoteException {
        System.out.println("enviado RESPOSTA ao no " + numNoRecebido);
        if (numNoRecebido > meuId) {
            //Comunicação pelo Java RMi
            InterfaceJavaRMI interfaceRemota = (InterfaceJavaRMI) Naming.lookup(NOME_PEER);
            try {
                interfaceRemota.alerta(numNoRecebido - 2, ("RESPOSTA," + numNoRecebido));
            } catch (RemoteException ex) {
                Logger.getLogger(AlgoritmoRicartAgrawala.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            //Comunicação pelo Java RMi
            InterfaceJavaRMI interfaceRemota = (InterfaceJavaRMI) Naming.lookup(NOME_PEER);
            try {
                interfaceRemota.alerta(numNoRecebido - 1, ("RESPOSTA," + numNoRecebido));
            } catch (RemoteException ex) {
                Logger.getLogger(AlgoritmoRicartAgrawala.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void pedirAo(int numSeq, int idDoPedinte, int i) throws NotBoundException, MalformedURLException, RemoteException {
        System.out.println("enviando PEDIDO ao Peer" + i);
        if (i > idDoPedinte) {
            //Comunicação pelo Java RMi
            InterfaceJavaRMI interfaceRemota = (InterfaceJavaRMI) Naming.lookup(NOME_PEER);
            try {
                interfaceRemota.alerta(i - 2, ("PEDIDO," + numSeq + "," + idDoPedinte));
            } catch (RemoteException ex) {
                Logger.getLogger(AlgoritmoRicartAgrawala.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            //Comunicação pelo Java RMi
            InterfaceJavaRMI interfaceRemota = (InterfaceJavaRMI) Naming.lookup(NOME_PEER);
            try {
                interfaceRemota.alerta(i - 1, ("PEDIDO," + numSeq + "," + idDoPedinte));
            } catch (RemoteException ex) {
                Logger.getLogger(AlgoritmoRicartAgrawala.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
