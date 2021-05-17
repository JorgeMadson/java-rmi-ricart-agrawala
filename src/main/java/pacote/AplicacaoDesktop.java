package pacote;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class AplicacaoDesktop {

    private static AlgoritmoRicartAgrawala peer1;
    private static AlgoritmoRicartAgrawala peer2;
    private static AlgoritmoRicartAgrawala peer3;

    // Textos
    static JLabel titulo;
    static JLabel pedirAcessoSessaoCritica;
    static JLabel liberarSessaoCritica;

    // Botoes
    static JButton levantarPeer1;
    static JButton levantarPeer2;
    static JButton levantarPeer3;
    static JButton peer1PedirSCRecurso1;
    static JButton peer2PedirSCRecurso1;
    static JButton peer3PedirSCReCurso1;

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

        // Disponibilizando porta
        try {
            LocateRegistry.createRegistry(1099);

            Naming.rebind("Peer1", new ClasseDeImplementacoes());
            Naming.rebind("Peer3", new ClasseDeImplementacoes());
            Naming.rebind("Peer2", new ClasseDeImplementacoes());
        } catch (MalformedURLException | RemoteException e2) {
            System.out.println("AplicacaoDesktop -> Disponibilizando porta");

            e2.printStackTrace();
        }
        
        // Levantando peers
        try {
            AplicacaoDesktop.peer1 = new AlgoritmoRicartAgrawala(1, 1);
            AplicacaoDesktop.peer2 = new AlgoritmoRicartAgrawala(2, 2);
            AplicacaoDesktop.peer3 = new AlgoritmoRicartAgrawala(3, 3);
        } catch (MalformedURLException e1) {

            System.out.println("AplicacaoDesktop -> MalformedURLException");
            e1.printStackTrace();
        }

        // Ações dos botões-------------------------------------------//

        // PedirSC
        peer1PedirSCRecurso1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    AplicacaoDesktop.peer1.perguntarSePossoEntrarNaSC();
                } catch (MalformedURLException | NotBoundException | RemoteException ex) {
                    System.out.println(ex);
                    Logger.getLogger(AplicacaoDesktop.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        peer2PedirSCRecurso1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    AplicacaoDesktop.peer2.perguntarSePossoEntrarNaSC();
                } catch (MalformedURLException | NotBoundException | RemoteException ex) {
                    System.out.println(ex);
                    Logger.getLogger(AplicacaoDesktop.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        peer3PedirSCReCurso1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    AplicacaoDesktop.peer3.perguntarSePossoEntrarNaSC();
                } catch (MalformedURLException | NotBoundException | RemoteException ex) {
                    System.out.println(ex);
                }
            }
        });
        peer1PedirSCRecurso2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    AplicacaoDesktop.peer1.perguntarSePossoEntrarNaSC();
                } catch (MalformedURLException | NotBoundException | RemoteException ex) {
                    System.out.println(ex);
                    Logger.getLogger(AplicacaoDesktop.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        peer2PedirSCRecurso2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    AplicacaoDesktop.peer2.perguntarSePossoEntrarNaSC();
                } catch (MalformedURLException | NotBoundException | RemoteException ex) {
                    System.out.println(ex);
                    Logger.getLogger(AplicacaoDesktop.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        peer3PedirSCReCurso2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    AplicacaoDesktop.peer3.perguntarSePossoEntrarNaSC();
                } catch (MalformedURLException | NotBoundException | RemoteException ex) {
                    System.out.println(ex);
                }
            }
        });

        // LiberarSC
        peer1LiberarSCRecurso1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    AplicacaoDesktop.peer1.liberarSC();
                } catch (MalformedURLException | NotBoundException | RemoteException ex) {
                    System.out.println(ex);
                }
            }
        });
        peer2LiberarSCRecurso1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    AplicacaoDesktop.peer2.liberarSC();
                } catch (MalformedURLException | NotBoundException | RemoteException ex) {
                    System.out.println(ex);
                }
            }
        });
        peer3LiberarSCRecurso1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    AplicacaoDesktop.peer3.liberarSC();
                } catch (MalformedURLException | NotBoundException | RemoteException ex) {
                    System.out.println(ex);
                }
            }
        });

    }

    public static void executarParteVisual() {
        JFrame f = new JFrame();

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

        // Botões-------------------------------------------//
        // Botões levantar peers
        levantarPeer1 = new JButton("Levantar Peer1");
        levantarPeer1.setBounds(50, 50, 200, 20);// eixo x, eixo y, largura, altura

        levantarPeer2 = new JButton("Levantar Peer2");
        levantarPeer2.setBounds(50, 75, 200, 20);// eixo x, eixo y, largura, altura

        levantarPeer3 = new JButton("Levantar Peer3");
        levantarPeer3.setBounds(50, 100, 200, 20);// eixo x, eixo y, largura, altura

        // Botões pedir acesso a Sessão Crítica Recurso 1
        peer1PedirSCRecurso1 = new JButton("► 1");
        peer1PedirSCRecurso1.setBounds(275, 50, 60, 20);// eixo x, eixo y, largura, altura
        peer2PedirSCRecurso1 = new JButton("► 1");
        peer2PedirSCRecurso1.setBounds(275, 75, 60, 20);// eixo x, eixo y, largura, altura
        peer3PedirSCReCurso1 = new JButton("► 1");
        peer3PedirSCReCurso1.setBounds(275, 100, 60, 20);// eixo x, eixo y, largura, altura

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
        f.add(levantarPeer1);
        f.add(levantarPeer2);
        f.add(levantarPeer3);

        f.add(peer1PedirSCRecurso1);
        f.add(peer2PedirSCRecurso1);
        f.add(peer3PedirSCReCurso1);
        f.add(peer1PedirSCRecurso1);
        f.add(peer2PedirSCRecurso1);
        f.add(peer3PedirSCReCurso1);

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
