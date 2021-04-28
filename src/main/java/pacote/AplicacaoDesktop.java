package pacote;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.AlreadyBoundException;
import javax.swing.*;

public class AplicacaoDesktop {

    public static void main(String[] args) throws AlreadyBoundException {

        JFrame f = new JFrame();

        //Encerra a aplicação quando fechado
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton botaoRodarRicartAgrawala = new JButton("Rodar Algoritmo Ricart Agrawala");

        botaoRodarRicartAgrawala.setBounds(50, 50, 250, 40);//eixo x, eixo y, largura, altura

        JLabel labelStatusServidor = new JLabel(ServerJavaRMI.servidorEstaAtivo);
        labelStatusServidor.setBounds(50, 30, 250, 15);//eixo x, eixo y, largura, altura
        f.add(labelStatusServidor);

        //Iniciando o servidor RMI
        ServerJavaRMI.iniciarServidor();
        labelStatusServidor.setText(ServerJavaRMI.servidorEstaAtivo);

        //Label mostrando os processos
        JLabel labelProcessosExecutando = new JLabel(ServerJavaRMI.resposta);
        labelProcessosExecutando.setBounds(50, 100, 250, 15);//eixo x, eixo y, largura, altura
        f.add(labelProcessosExecutando);

        botaoRodarRicartAgrawala.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ClienteJavaRMI.abrirUmProcessoCliente(1, 1);
                labelProcessosExecutando.setText(ClienteJavaRMI.respostaServidor);
            }
        });

        f.add(botaoRodarRicartAgrawala);//adding button in JFrame  

        f.setSize(400, 500);//400 largura e 500 altura
        f.setLayout(null);//usando o gerenciador de layout 
        f.setVisible(true);//fazendo o frame visivel
    }
}
