package pacote;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class AplicacaoDesktop {

    public static void main(String[] args) {
        
        //Iniciando o servidor RMI
        
        
        JFrame f = new JFrame();

        //Encerra a aplicação quando fechado
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton b = new JButton("Rodar Algoritmo Ricart Agrawala");
       
        b.setBounds(50, 50, 250, 40);//eixo x, eixo y, largura, altura

        JLabel lblName = new JLabel("Esperando...");
        lblName.setBounds(65, 31, 46, 14);
        f.add(lblName);

         b.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                lblName.setText("Teste");
            }
        });
        
        f.add(b);//adding button in JFrame  

        f.setSize(400, 500);//400 largura e 500 altura
        f.setLayout(null);//usando o gerenciador de layout 
        f.setVisible(true);//fazendo o frame visivel
    }
}
