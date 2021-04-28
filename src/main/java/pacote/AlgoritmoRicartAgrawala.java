package pacote;

import java.io.*;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;

public class AlgoritmoRicartAgrawala {

    public boolean solicitandoCS;
    public int respostasPendentes;
    public int maiorNumSeq;
    public int numSeq;
    public int carimboDeTempo;
    //Comunicação pelo Java RMi

    //Serão 3 nós por agora
    public int nosNoCanal = 3;

    public boolean[] respostaAdiada;

    public AlgoritmoRicartAgrawala(int carimboDeTempo, int numSeq) {
        solicitandoCS = false;

        respostasPendentes = nosNoCanal;

        maiorNumSeq = 0;
        this.numSeq = numSeq;

        //Comunicação pelo Java RMi
        // 
        //Comunicação pelo Java RMi
        // Numeros do nó também é usado para prioridade (baixo nó = maior prioridade no esquema RicartAgrawala)
        // Numeros do nó são [1, nosNoCanal]; já que estamos começando em 1, então tem que ficar atento com erros ao tentar acessar o nó '0'.
        this.carimboDeTempo = carimboDeTempo;

        respostaAdiada = new boolean[nosNoCanal];
    }

    /**
     * invocacao (begun in driver module with request CS)
     */
    public boolean invocacao() throws NotBoundException, MalformedURLException, RemoteException {

        solicitandoCS = true;
        numSeq = maiorNumSeq + 1;

        respostasPendentes = nosNoCanal;

        for (int i = 1; i <= nosNoCanal + 1; i++) {
            if (i != carimboDeTempo) {
                pedirAo(numSeq, carimboDeTempo, i);
                respostasPendentes--;
            }
        }

        while (respostasPendentes > 0) {
            try {
                Thread.sleep(5);

            } catch (Exception e) {

            }
            /* espere até termos respostas de todos os outros processos */
        }

        //Retornamos quando estivermos prontos para entrar no CS
        return true;

    }

    //A outra metade da invocação
    public void liberarCS() throws NotBoundException, MalformedURLException, RemoteException {
        solicitandoCS = false;

        for (int i = 0; i < nosNoCanal; i++) {
            if (respostaAdiada[i]) {
                respostaAdiada[i] = false;
                if (i < (carimboDeTempo - 1)) {
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
        bDefer = solicitandoCS && ((seqNumRecebido > numSeq) || (seqNumRecebido == numSeq && numNoRecebido > carimboDeTempo));
        if (bDefer) {
            System.out.println("Envio diferido de mensagem para" + numNoRecebido);
            if (numNoRecebido > carimboDeTempo) {
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
        if (numNoRecebido > carimboDeTempo) {
            //Comunicação pelo Java RMi
            InterfaceJavaRMI interfaceRemota = (InterfaceJavaRMI) Naming.lookup("Ola");
            try {
                interfaceRemota.alerta(numNoRecebido - 2, ("RESPOSTA," + numNoRecebido));
            } catch (RemoteException ex) {
                Logger.getLogger(AlgoritmoRicartAgrawala.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            //Comunicação pelo Java RMi
            InterfaceJavaRMI interfaceRemota = (InterfaceJavaRMI) Naming.lookup("Ola");
            try {
                interfaceRemota.alerta(numNoRecebido - 1, ("RESPOSTA," + numNoRecebido));
            } catch (RemoteException ex) {
                Logger.getLogger(AlgoritmoRicartAgrawala.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void pedirAo(int numSeq, int carimboDeTempo, int i) throws NotBoundException, MalformedURLException, RemoteException {
        System.out.println("enviando PEDIDO ao no" + (((i))));
        if (i > carimboDeTempo) {
            //Comunicação pelo Java RMi
            InterfaceJavaRMI interfaceRemota = (InterfaceJavaRMI) Naming.lookup("Ola");
            try {
                interfaceRemota.alerta(i - 2, ("PEDIDO," + numSeq + "," + carimboDeTempo));
            } catch (RemoteException ex) {
                Logger.getLogger(AlgoritmoRicartAgrawala.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            //Comunicação pelo Java RMi
            InterfaceJavaRMI interfaceRemota = (InterfaceJavaRMI) Naming.lookup("Ola");
            try {
                interfaceRemota.alerta(i - 1, ("PEDIDO," + numSeq + "," + carimboDeTempo));
            } catch (RemoteException ex) {
                Logger.getLogger(AlgoritmoRicartAgrawala.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
