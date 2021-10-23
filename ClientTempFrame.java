/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author hrptech
 */
public class ClientTempFrame extends JFrame{
    private int angle = 0;
    private int speed = 1;
    public ClientTempFrame(){
        setLayout(null);
        setBounds(0, 0, 500, 400);
        setResizable(false);
        JLabel lbl = new JLabel("Temp[Fan]");
        lbl.setBounds(5, 5, 485, 30);
        lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl.setOpaque(true);
        lbl.setBackground(Color.WHITE);
        add(lbl);
        add(new InnerFanPanel());
        setVisible(true);
    }
     public class InnerFanPanel extends JPanel {

        public InnerFanPanel() {
            setBounds(0, 35, 400, 300);
            Timer timer = new Timer(10, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    angle += speed;
                    repaint();
                }
            });
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.

            int xc = getWidth() / 2;
            int yc = getHeight() / 2;
            int rad = (int) (Math.min(getWidth(), getHeight()) * 0.4);
            int x = xc - rad;
            int y = yc - rad;
            g.fillArc(x, y, 2 * rad, 2 * rad, 0 + angle, 30);
            g.fillArc(x, y, 2 * rad, 2 * rad, 90 + angle, 30);
            g.fillArc(x, y, 2 * rad, 2 * rad, 180 + angle, 30);
            g.fillArc(x, y, 2 * rad, 2 * rad, 270 + angle, 30);
            //  System.out.println("angle - > " + angle);
        }

    }

    public void update(int speed) {
        this.speed = speed;
    }
    public static void main(String args[]){
        Socket socket = null;
        if (args.length > 2) {
            String ipAddress = args[0];
            int port = Integer.parseInt(args[1]);
            String type = args[2];
            ClientTempFrame clientLightFrame = new ClientTempFrame();
            new clientThreads(socket, ipAddress, port, type, clientLightFrame, null);
            
        }
        
    }
}

