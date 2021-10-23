/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.Color;
import java.net.Socket;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author hrptech
 */
public class ClientLightFrame extends JFrame{
    private JPanel backgroundPanel;
    public ClientLightFrame(){
        setLayout(null);
        setBounds(0, 0, 500, 400);
        setResizable(false);
        JLabel lbl = new JLabel("Light Lumens [Background]");
        lbl.setBounds(5, 5, 485, 30);
        lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl.setOpaque(true);
        lbl.setBackground(Color.WHITE);
        add(lbl);
        backgroundPanel = new JPanel();
        backgroundPanel.setBounds(5, 40, 485, 400);
        backgroundPanel.setLayout(null);
        backgroundPanel.setBackground(Color.RED);
        add(backgroundPanel);
        setVisible(true);
    }
    public void update(int color) {
        try {
            Color c = new Color(255, 0, 0, color);
            backgroundPanel.setBackground(c);
            repaint();
        } catch (Exception exc) {
            System.out.println("" + exc);
        }
    }
    public static void main(String args[]){
        Socket socket = null;
       if (args.length > 2) {
            String ipAddress = args[0];
            int port = Integer.parseInt(args[1]);
            String type = args[2];
            ClientLightFrame clientLightFrame = new ClientLightFrame();
            new clientThreads(socket, ipAddress, port, type, null, clientLightFrame);
            
        }
        
    }
}

