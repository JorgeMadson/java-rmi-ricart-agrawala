package pacote;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class AplicacaoDesktop {

    private static AlgoritmoRicartAgrawala peer1;
    private static AlgoritmoRicartAgrawala peer2;
    private static AlgoritmoRicartAgrawala peer3;

    public static void main(String[] args) throws AlreadyBoundException {

        JFrame f = new JFrame();

        //Encerra a aplicação quando fechado
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Label-------------------------------------------//
        //Labels nome do algoritmo
        JLabel titulo = new JLabel("Algoritmo Ricart Agrawala");
        titulo.setBounds(50, 30, 250, 15);//eixo x, eixo y, largura, altura
        f.add(titulo);
        
        JLabel pedirAcessoSessaoCritica = new JLabel("Pedir Acesso Sessão Crítica");
        pedirAcessoSessaoCritica.setBounds(275, 30, 250, 15);//eixo x, eixo y, largura, altura
        f.add(pedirAcessoSessaoCritica);
        
        JLabel liberarSessaoCritica = new JLabel("Liberar Sessão Crítica");
        liberarSessaoCritica.setBounds(500, 30, 250, 15);//eixo x, eixo y, largura, altura
        f.add(liberarSessaoCritica);

        //Botões-------------------------------------------//
        //Botões levantar peers
        JButton levantarPeer1 = new JButton("Levantar Peer1");
        levantarPeer1.setBounds(50, 50, 200, 20);//eixo x, eixo y, largura, altura

        JButton levantarPeer2 = new JButton("Levantar Peer2");
        levantarPeer2.setBounds(50, 75, 200, 20);//eixo x, eixo y, largura, altura

        JButton levantarPeer3 = new JButton("Levantar Peer3");
        levantarPeer3.setBounds(50, 100, 200, 20);//eixo x, eixo y, largura, altura

        //Botões pedir acesso a Sessão Crítica
        JButton peer1PedirSC = new JButton("►");
        peer1PedirSC.setBounds(275, 50, 50, 20);//eixo x, eixo y, largura, altura
        JButton peer2PedirSC = new JButton("►");
        peer2PedirSC.setBounds(275, 75, 50, 20);//eixo x, eixo y, largura, altura
        JButton peer3PedirSC = new JButton("►");
        peer3PedirSC.setBounds(275, 100, 50, 20);//eixo x, eixo y, largura, altura
        
        //Botões pedir acesso a Sessão Crítica
        JButton peer1LiberarSC = new JButton("◄");
        peer1LiberarSC.setBounds(500, 50, 50, 20);//eixo x, eixo y, largura, altura
        JButton peer2LiberarSC = new JButton("◄");
        peer2LiberarSC.setBounds(500, 75, 50, 20);//eixo x, eixo y, largura, altura
        JButton peer3LiberarSC = new JButton("◄");
        peer3LiberarSC.setBounds(500, 100, 50, 20);//eixo x, eixo y, largura, altura


        //Ações dos botões-------------------------------------------//
        //Levantar peers
        levantarPeer1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AplicacaoDesktop.peer1 = new AlgoritmoRicartAgrawala(1, 1, 1099);
            }
        });
        levantarPeer2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AplicacaoDesktop.peer2 = new AlgoritmoRicartAgrawala(2, 2, 1100);
            }
        });
        levantarPeer3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AplicacaoDesktop.peer3 = new AlgoritmoRicartAgrawala(3, 3, 1101);
            }
        });
        //PedirSC
        peer1PedirSC.addActionListener(new ActionListener() {
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
        peer2PedirSC.addActionListener(new ActionListener() {
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
        peer3PedirSC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    AplicacaoDesktop.peer3.perguntarSePossoEntrarNaSC();
                } catch (MalformedURLException | NotBoundException | RemoteException ex) {
                    System.out.println(ex);
                }
            }
        });
        //LiberarSC
        peer1LiberarSC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    AplicacaoDesktop.peer1.liberarSC();
                } catch (MalformedURLException | NotBoundException | RemoteException ex) {
                    System.out.println(ex);
                }
            }
        });
        peer2LiberarSC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    AplicacaoDesktop.peer2.liberarSC();
                } catch (MalformedURLException | NotBoundException | RemoteException ex) {
                    System.out.println(ex);
                }
            }
        });
        peer3LiberarSC.addActionListener(new ActionListener() {
            @Override
           public void actionPerformed(ActionEvent e) {
                try {
                    AplicacaoDesktop.peer3.liberarSC();
                } catch (MalformedURLException | NotBoundException | RemoteException ex) {
                    System.out.println(ex);
                }
            }
        });

        //adcionando botões no JFrame-------------------------------------------//
        f.add(levantarPeer1);
        f.add(levantarPeer2);
        f.add(levantarPeer3);
        
        f.add(peer1PedirSC);
        f.add(peer2PedirSC);
        f.add(peer3PedirSC);
        
        f.add(peer1LiberarSC);
        f.add(peer2LiberarSC);
        f.add(peer3LiberarSC);

        //Janela-------------------------------------------//
        f.setSize(800, 400);// largura, altura
        f.setLayout(null);//usando o gerenciador de layout 
        f.setVisible(true);//fazendo o frame visivel
    }
}
