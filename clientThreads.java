/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 *
 * @author hrptech
 */
public class clientThreads implements Runnable {

    Socket socket = null;
    String ipAddress;
    int port;
    String clientType;
    DataOutputStream outPut = null;
    DataInputStream inPut = null;
    ClientTempFrame clientFrameTemp;
    ClientLightFrame clientFrameLight;

    public clientThreads(Socket socket, String ipAddress, int port, String clienttype, ClientTempFrame clientFrameTemp, ClientLightFrame clientFrameLight) {
        this.socket = socket;
        this.ipAddress = ipAddress;
        this.port = port;
        this.clientType = clienttype;
        this.clientFrameTemp = clientFrameTemp;
        this.clientFrameLight = clientFrameLight;
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            socket = new Socket(ipAddress, port);
            outPut = new DataOutputStream(socket.getOutputStream());
            outPut.writeUTF(clientType);
            outPut.flush();
            inPut = new DataInputStream(socket.getInputStream());
            String message = "";
            while ((message = inPut.readUTF()) != null) {
                if (message.equalsIgnoreCase("stop")) {
                    outPut.close();
                    socket.close();
                    inPut.close();
                    System.out.println("Server Stop Clients...");
                    System.exit(0);
                } else {

                    String type = message.split("\\|")[0];
                    int temp = (int) (Double.parseDouble(message.split("\\|")[2]));
                    if (type.equalsIgnoreCase("temperature")) {
                        if (clientFrameTemp != null) {
                            clientFrameTemp.update(temp);
                            System.out.println("Temp - > " + temp);
                        }
                    } else if (type.equalsIgnoreCase("light")) {
                        if (clientFrameLight != null) {
                            if (temp > 255) {
                                temp = 255;
                            }
                            if (temp > 1 && temp <= 255) {
                                clientFrameLight.update(temp);
                                System.out.println("Lumens - > " + temp);
                            }
                        }
                    }
                }
//System.out.println("Message From Server - > " + message);
            }
        } catch (Exception e) {

        }
    }
}
