/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author hrptech
 */
public class Server {

    
    public static void main(String args[]) {
        ServerSocket serverSocket = null;
        Socket socket = null;
        if (args.length > 0) {
            int port = Integer.parseInt(args[0]);
            serverThread serThread = new serverThread(port, serverSocket, socket);
           // serThread.writeCsvFile();
        }
    }
}
