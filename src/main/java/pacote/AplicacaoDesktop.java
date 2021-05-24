package pacote;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import javax.swing.*;

public class AplicacaoDesktop {

    private static AlgoritmoRicartAgrawala[] peer = new AlgoritmoRicartAgrawala[3];

    // JFrame para criar a janela
    static JFrame f;

    // Textos
    static JLabel titulo;
    static JLabel pedirAcessoSessaoCritica;
    static JLabel liberarSessaoCritica;
    static JLabel levantarPeer1;
    static JLabel levantarPeer2;
    static JLabel levantarPeer3;
    static JLabel estadoRecurso1;
    static JLabel estadoRecurso2;

    // Botoes
    static JButton peer1PedirSCRecurso1;
    static JButton peer2PedirSCRecurso1;
    static JButton peer3PedirSCRecurso1;

    static JButton peer1PedirSCRecurso2;
    static JButton peer2PedirSCRecurso2;
    static JButton peer3PedirSCReCurso2;

    static JButton peer1LiberarSCRecurso1;
    static JButton peer2LiberarSCRecurso1;
    static JButton peer3LiberarSCRecurso1;

    static JButton peer1LiberarSCRecurso2;
    static JButton peer2LiberarSCRecurso2;
    static JButton peer3LiberarSCRecurso2;

    public static void main(String[] args) throws AlreadyBoundException {

        // Parte visual-------------------------------------------//
        executarParteVisual();
        // Lógica
        // 1- Ligar o JavaRMI
        // 2- Ligar os peers
        // 3- Cada peer poder pedir pra entrar na SC
        // 3.1- Se um peer tiver pedido pra entrar ver quem pediu primeiro
        // 3.2- Se ninguém pediu os outros respondem ok
        // 3.3- Todo mundo respondendo entrar na SC
        // 4- Um peer que esteja na SC pede pra liberar

        // Levantando peers
        try {
            AplicacaoDesktop.peer[0] = new AlgoritmoRicartAgrawala("Peer1");
            AplicacaoDesktop.peer[1] = new AlgoritmoRicartAgrawala("Peer2");
            AplicacaoDesktop.peer[2] = new AlgoritmoRicartAgrawala("Peer3");
        } catch (Exception e1) {

            System.out.println("AplicacaoDesktop -> Levantando peers");
            e1.printStackTrace();
        }

        // Servidor de nomes
        try {
            LocateRegistry.createRegistry(1099);

            Naming.rebind("Peer1", AplicacaoDesktop.peer[0]);
            Naming.rebind("Peer2", AplicacaoDesktop.peer[1]);
            Naming.rebind("Peer3", AplicacaoDesktop.peer[2]);
        } catch (MalformedURLException | RemoteException ex) {
            System.out.println("AplicacaoDesktop -> Disponibilizando porta");
            ex.printStackTrace();
        }

        // Ações dos botões-------------------------------------------//

        // PedirSC
        peer1PedirSCRecurso1.addActionListener(peerPedindoSCNoRecurso(1,1));
        peer2PedirSCRecurso1.addActionListener(peerPedindoSCNoRecurso(2,1));
        peer3PedirSCRecurso1.addActionListener(peerPedindoSCNoRecurso(3,1));

        peer1PedirSCRecurso2.addActionListener(peerPedindoSCNoRecurso(1,2));
        peer2PedirSCRecurso2.addActionListener(peerPedindoSCNoRecurso(2,2));
        peer3PedirSCReCurso2.addActionListener(peerPedindoSCNoRecurso(3,2));

        // LiberarSC
        // Recurso 1
        peer1LiberarSCRecurso1.addActionListener(peerLiberandoRecurso(1,1));
        peer2LiberarSCRecurso1.addActionListener(peerLiberandoRecurso(2,1));
        peer3LiberarSCRecurso1.addActionListener(peerLiberandoRecurso(3,1));

        // Recurso 2
        peer1LiberarSCRecurso2.addActionListener(peerLiberandoRecurso(1,2));
        peer2LiberarSCRecurso2.addActionListener(peerLiberandoRecurso(2,2));
        peer3LiberarSCRecurso2.addActionListener(peerLiberandoRecurso(3,2));

    }

    static ActionListener peerPedindoSCNoRecurso(int peer ,int recurso) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    boolean recursoEstaSendoUsado = AplicacaoDesktop.peer[peer-1].perguntarSePossoEntrarNaSC(recurso);
                    if (recursoEstaSendoUsado == false) {
                        AplicacaoDesktop.peer[peer-1].recurso1EstaSendoUtilizado = AlgoritmoRicartAgrawala.held;
                        estadoRecurso1.setText("Recurso " +recurso+": Peer "+peer+" usando");
                    }
                    
                } catch (MalformedURLException | NotBoundException | RemoteException ex) {
                    System.out.println("peer"+peer+"PedirSCRecurso"+recurso+ ex);
                }
            }
        };
    }

    static ActionListener peerLiberandoRecurso(int peer, int recurso) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    System.out.println("\nPeer1 quer liberar o recurso 1");
                    if (AplicacaoDesktop.peer[peer-1].recurso1EstaSendoUtilizado == AlgoritmoRicartAgrawala.held) {
                        AplicacaoDesktop.peer[peer-1].liberarSC(recurso);
                        estadoRecurso1.setText("Recurso 1: Livre");
                    } else {
                        System.out.println("Não é esse peer que está usando");
                    }
                } catch (MalformedURLException | NotBoundException | RemoteException ex) {
                    System.out.println("peer1LiberarSCRecurso1" + ex);
                }
            }
        };
    }

    static void executarParteVisual() {
        f = new JFrame();

        // Encerra a aplicação quando fechado
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Label-------------------------------------------//
        // Labels nome do algoritmo
        titulo = new JLabel("Algoritmo Ricart Agrawala");
        titulo.setBounds(50, 30, 250, 15);// eixo x, eixo y, largura, altura
        f.add(titulo);

        pedirAcessoSessaoCritica = new JLabel("Pedir Acesso Sessão Crítica");
        pedirAcessoSessaoCritica.setBounds(275, 30, 250, 15);// eixo x, eixo y, largura, altura
        f.add(pedirAcessoSessaoCritica);

        liberarSessaoCritica = new JLabel("Liberar Sessão Crítica");
        liberarSessaoCritica.setBounds(500, 30, 250, 15);// eixo x, eixo y, largura, altura
        f.add(liberarSessaoCritica);

        // Label peers
        levantarPeer1 = new JLabel("| Peer1:");
        levantarPeer1.setBounds(200, 50, 200, 20);// eixo x, eixo y, largura, altura
        levantarPeer2 = new JLabel("| Peer2:");
        levantarPeer2.setBounds(200, 75, 200, 20);// eixo x, eixo y, largura, altura
        levantarPeer3 = new JLabel("| Peer3:");
        levantarPeer3.setBounds(200, 100, 200, 20);// eixo x, eixo y, largura, altura
        f.add(levantarPeer1);
        f.add(levantarPeer2);
        f.add(levantarPeer3);

        estadoRecurso1 = new JLabel("Recurso 1: Livre");
        estadoRecurso1.setBounds(25, 50, 200, 30);// eixo x, eixo y, largura, altura
        f.add(estadoRecurso1);

        estadoRecurso2 = new JLabel("Recurso 2: Livre");
        estadoRecurso2.setBounds(25, 75, 200, 30);// eixo x, eixo y, largura, altura
        f.add(estadoRecurso2);

        // Botões-------------------------------------------//

        // Botões pedir acesso a Sessão Crítica Recurso 1
        peer1PedirSCRecurso1 = new JButton("► 1");
        peer1PedirSCRecurso1.setBounds(275, 50, 60, 20);// eixo x, eixo y, largura, altura
        peer2PedirSCRecurso1 = new JButton("► 1");
        peer2PedirSCRecurso1.setBounds(275, 75, 60, 20);// eixo x, eixo y, largura, altura
        peer3PedirSCRecurso1 = new JButton("► 1");
        peer3PedirSCRecurso1.setBounds(275, 100, 60, 20);// eixo x, eixo y, largura, altura

        // Botões pedir acesso a Sessão Crítica Recurso 2
        peer1PedirSCRecurso2 = new JButton("► 2");
        peer1PedirSCRecurso2.setBounds(350, 50, 60, 20);// eixo x, eixo y, largura, altura
        peer2PedirSCRecurso2 = new JButton("► 2");
        peer1PedirSCRecurso2.setBounds(350, 50, 60, 20);// eixo x, eixo y, largura, altura
        peer2PedirSCRecurso2 = new JButton("► 2");
        peer2PedirSCRecurso2.setBounds(350, 75, 60, 20);// eixo x, eixo y, largura, altura
        peer3PedirSCReCurso2 = new JButton("► 2");
        peer3PedirSCReCurso2.setBounds(350, 100, 60, 20);// eixo x, eixo y, largura, altura

        // Botões pedir acesso a Sessão Crítica Recurso 1
        peer1LiberarSCRecurso1 = new JButton("◄ 1");
        peer1LiberarSCRecurso1.setBounds(500, 50, 60, 20);// eixo x, eixo y, largura, altura
        peer2LiberarSCRecurso1 = new JButton("◄ 1");
        peer2LiberarSCRecurso1.setBounds(500, 75, 60, 20);// eixo x, eixo y, largura, altura
        peer3LiberarSCRecurso1 = new JButton("◄ 1");
        peer3LiberarSCRecurso1.setBounds(500, 100, 60, 20);// eixo x, eixo y, largura, altura

        // Botões pedir acesso a Sessão Crítica Recurso 2
        peer1LiberarSCRecurso2 = new JButton("◄ 2");
        peer1LiberarSCRecurso2.setBounds(560, 50, 60, 20);// eixo x, eixo y, largura, altura
        peer2LiberarSCRecurso2 = new JButton("◄ 2");
        peer2LiberarSCRecurso2.setBounds(560, 75, 60, 20);// eixo x, eixo y, largura, altura
        peer3LiberarSCRecurso2 = new JButton("◄ 2");
        peer3LiberarSCRecurso2.setBounds(560, 100, 60, 20);// eixo x, eixo y, largura, altura

        // adcionando botões no JFrame-------------------------------------------//

        f.add(peer1PedirSCRecurso1);
        f.add(peer2PedirSCRecurso1);
        f.add(peer3PedirSCRecurso1);
        f.add(peer1PedirSCRecurso1);
        f.add(peer2PedirSCRecurso1);
        f.add(peer3PedirSCRecurso1);

        f.add(peer1PedirSCRecurso2);
        f.add(peer2PedirSCRecurso2);
        f.add(peer3PedirSCReCurso2);
        f.add(peer1PedirSCRecurso2);
        f.add(peer2PedirSCRecurso2);
        f.add(peer3PedirSCReCurso2);

        f.add(peer1LiberarSCRecurso1);
        f.add(peer2LiberarSCRecurso1);
        f.add(peer3LiberarSCRecurso1);
        f.add(peer1LiberarSCRecurso2);
        f.add(peer2LiberarSCRecurso2);
        f.add(peer3LiberarSCRecurso2);

        // Janela-------------------------------------------//
        f.setSize(800, 400);// largura, altura
        f.setLayout(null);// usando o gerenciador de layout
        f.setVisible(true);// fazendo o frame visivel

    }

}
