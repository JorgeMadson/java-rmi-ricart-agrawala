package pacote;
import java.io.*;

public class AlgoritmoRicartAgrawala {

    public boolean solicitandoCS;
    public int respostasPendentes;
    public int maiorNumSeq;
    public int numSeq;
    public int carimboDeTempo;
    //TODO: Fazer a comunicação pelo Java RMi
    //public static String[] listaDeComunicacao;

    //Serão 3 nós pos agora
    public int nosNoCanal = 3;

    public boolean[] replyDeferred;

    public AlgoritmoRicartAgrawala(int carimboDeTempo, int numSeq) {
        solicitandoCS = false;

        respostasPendentes = nosNoCanal;

        maiorNumSeq = 0;
        this.numSeq = numSeq;

        //TODO: Fazer a comunicação pelo Java RMi
        //int quantidadeDeNosParaComunicar = listaDeComunicacao.length;
        // 
        //TODO: Fazer a comunicação pelo Java RMi
        //listaDeComunicacao[quantidadeDeNosParaComunicar - 1] = nosNoCanal;
        // Node number is also used for priority (low node # == higher priority in RicartAgrawala scheme)
        // Node numbers are [1,nosNoCanal]; since we're starting at 1 check for errors trying to access node '0'.
        this.carimboDeTempo = carimboDeTempo;

        replyDeferred = new boolean[nosNoCanal];
    }

    /**
     * Invocation (begun in driver module with request CS)
     */
    public boolean invocation() {

        solicitandoCS = true;
        numSeq = maiorNumSeq + 1;

        respostasPendentes = nosNoCanal;

        for (int i = 1; i <= nosNoCanal + 1; i++) {
            if (i != carimboDeTempo) {
                requestTo(numSeq, carimboDeTempo, i);
            }
        }

        while (respostasPendentes > 0) {
            try {
                Thread.sleep(5);

            } catch (Exception e) {

            }
            /*wait until we have replies from all other processes */
        }

        //We return when ready to enter CS
        return true;

    }

    // The other half of invocation
    public void releaseCS() {
        solicitandoCS = false;

        for (int i = 0; i < nosNoCanal; i++) {
            if (replyDeferred[i]) {
                replyDeferred[i] = false;
                if (i < (carimboDeTempo - 1)) {
                    replyTo(i + 1);
                } else {
                    replyTo(i + 2);
                }
            }
        }
    }

    /**
     * Receiving Request
     *
     * @param	recivedSeqNum	The incoming message's sequence number
     * @param	recivedNudeNum	The incoming message's node number
     *
     */
    public void receiveRequest(int recivedSeqNum, int recivedNudeNum) {
        System.out.println("Received request from node " + recivedNudeNum);
        boolean bDefer = false;

        maiorNumSeq = Math.max(maiorNumSeq, recivedSeqNum);
        bDefer = solicitandoCS && ((recivedSeqNum > numSeq) || (recivedSeqNum == numSeq && recivedNudeNum > carimboDeTempo));
        if (bDefer) {
            System.out.println("Deferred sending message to " + recivedNudeNum);
            if (recivedNudeNum > carimboDeTempo) {
                replyDeferred[recivedNudeNum - 2] = true;
            } else {
                replyDeferred[recivedNudeNum - 1] = true;
            }
        } else {
            System.out.println("Sent reply message to " + recivedNudeNum);
            replyTo(recivedNudeNum);
        }

    }

    /**
     * Receiving Replies
     */
    public void receiveReply() {
        respostasPendentes = Math.max((respostasPendentes - 1), 0);
        //System.out.println("Outstanding replies: " + respostasPendentes);
    }

    public void replyTo(int recivedNudeNum) {
        System.out.println("Sending REPLY to node " + recivedNudeNum);
        if (recivedNudeNum > carimboDeTempo) {
            //TODO: Fazer a comunicação pelo Java RMi
            //listaDeComunicacao[recivedNudeNum-2].println("REPLY," + recivedNudeNum);
        } else {
            //TODO: Fazer a comunicação pelo Java RMi
            //listaDeComunicacao[recivedNudeNum-1].println("REPLY," + recivedNudeNum);
        }
    }

    public void requestTo(int numSeq, int carimboDeTempo, int i) {
        System.out.println("Sending REQUEST to node " + (((i))));
        if (i > carimboDeTempo) {
            //TODO: Fazer a comunicação pelo Java RMi
            //listaDeComunicacao[i-2].println("REQUEST," + numSeq + "," + carimboDeTempo);
        } else {
            //TODO: Fazer a comunicação pelo Java RMi
            //listaDeComunicacao[i-1].println("REQUEST," + numSeq + "," + carimboDeTempo);
        }
    }

}
