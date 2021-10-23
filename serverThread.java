/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author hrptech
 */
public class serverThread implements Runnable {
    
    private ServerSocket serverSocket = null;
    private Socket socket = null;
    private int port;
    private DataOutputStream outPut = null;
    private DataInputStream inPut = null;
    private ArrayList<Socket> socketsList = new ArrayList<>();
    private ArrayList<DataOutputStream> outputList = new ArrayList<>();
    private ArrayList<DataInputStream> inputList = new ArrayList<>();
    private ArrayList<String> clientType = new ArrayList<>();
    private ArrayList<String> listOfCsvData = new ArrayList<>();
    private Thread thread = null;
    private Thread singleThreadMessage = null;
    
    public serverThread(int port, ServerSocket serverSocket, Socket socket) {
        this.port = port;
        this.serverSocket = serverSocket;
        this.socket = socket;
        new Thread(this).start();
    }
    
    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server Started.....");
            
            while (true) {
                socket = serverSocket.accept();
                System.out.println("Client Connected");
                inPut = new DataInputStream(socket.getInputStream());
                String msg = "";
                try {
                    if ((msg = inPut.readUTF()) != null) {
                        System.out.println("Message - > " + msg);
                        if (!isClientExist(msg) && (msg.equalsIgnoreCase("temperature") || msg.equalsIgnoreCase("light"))) {
                            outPut = new DataOutputStream(socket.getOutputStream());
                            outputList.add(outPut);
                            inputList.add(inPut);
                            socketsList.add(socket);
                            clientType.add(msg);
                            System.out.println("Client(" + msg + ") is Connected[" + socketsList.size() + "] ");
                            if (thread == null) {
                                InnerThread innerThread = new InnerThread();
                                thread = new Thread(innerThread);
                                thread.start();
                                
                                innerSendMessage inSendMessage = new innerSendMessage();
                                singleThreadMessage = new Thread(inSendMessage);
                                singleThreadMessage.start();
                            }
                        }
                    }
                } catch (Exception ex) {
                    System.out.println("");
                }

//readMsg();
            }
        } catch (Exception ex) {
            
        }
    }
    
    private class innerSendMessage implements Runnable {
        
        public void SingleMessageSendToClient() {
            try {
                Scanner scanner = new Scanner(System.in);
                boolean isStart = true;
                while (isStart) {
                    System.out.print("Enter Mesaage for clients .. ");
                    String messageLine = scanner.nextLine();
                    if (messageLine.equalsIgnoreCase("stop")) {
                        for (int index = 0; index < outputList.size(); index++) {
                            DataOutputStream dOutPut = outputList.get(index);
                            dOutPut.writeUTF(messageLine);
                            try {
                                Thread.sleep(1000);
                            } catch (Exception e) {
                                
                            }
                        }
                        writeCsvFile();
                        inputList.clear();
                        outputList.clear();
                        clientType.clear();
                        listOfCsvData.clear();
                        isStart = false;
                        serverSocket.close();
                        System.exit(0);
                        
                    }
                    
                }
            } catch (Exception e) {
                
            }
        }
        
        @Override
        public void run() {
            SingleMessageSendToClient();
        }
        
    }
    
    private class InnerThread implements Runnable {
        
        @Override
        public void run() {
            try {
                sendMsgToClient();
                Thread.sleep(3000);
            } catch (Exception ex) {
                
            }
        }
        
        public void sendMsgToClient() {
            Random random = new Random();
            DecimalFormat df = new DecimalFormat("0.00");
            while (true) {
                for (int threadCount = 0; threadCount < clientType.size(); threadCount++) {
                    
                    DataOutputStream dOutPut = outputList.get(threadCount);
                    String type = clientType.get(threadCount);
                    try {
                        int randomNum = 0;
                        if (type.equalsIgnoreCase("temperature")) {
                            randomNum = random.nextInt(200) + 1;
                            double tempVal = randomNum;
                            
                            double celsius = ((5 * (tempVal - 100.0)) / 9.0);
                            if (celsius > 0) {
                                listOfCsvData.add(type + "," + tempVal + "," + df.format(celsius) + "\n");
                                dOutPut.writeUTF(type + "|" + tempVal + "|" + df.format(celsius));
                            }
                        } else if (type.equalsIgnoreCase("light")) {
                            randomNum = random.nextInt(20) + 1;
                            //14 lumens is equals to 1 watt
                            int lumens = randomNum * 14;
                            dOutPut.writeUTF(type + "|" + randomNum + "|" + lumens);
                            listOfCsvData.add(type + "," + randomNum + "," + lumens + "\n");
                        }
                        
                    } catch (Exception ex) {
                        
                    }
                    try {
                        Thread.sleep(5000);
                    } catch (Exception e) {
                        
                    }
                }
            }
        }
        
    }
    
    public boolean isClientExist(String msg) {
        
        try {
            for (int index = 0; index < clientType.size(); index++) {
                String type = clientType.get(index);
                if (type.equalsIgnoreCase(msg)) {
                    return true;
                }
            }
        } catch (Exception e) {
            
        }
        return false;
    }
    
    public void writeCsvFile() {
        String fileName = "D:\\temp_light.csv";
        try {
            File file = new File(fileName);
            if (file.createNewFile()) {
                System.out.println("new File is Created");
            } else {
                System.out.println("Error");
            }
            if (file.exists()) {
                FileWriter fileWriter = new FileWriter(fileName);
                for (int index = 0; index < listOfCsvData.size(); index++) {
                    fileWriter.write("" + listOfCsvData.get(index));
                }
                fileWriter.close();
                System.out.println("Successfully");
                readCsvFile();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        //FileInputStream fileInputStream = new FileInputStream(new );
    }
    
    public void readCsvFile() {
        try {
            String fileName = "D:\\temp_light.csv";
            FileReader fileReader = new FileReader(fileName);
            int charCount = 0;
            while ((charCount = fileReader.read()) != -1) {
                System.out.print("" + (char) charCount);
            }
            fileReader.close();
        } catch (Exception e) {
            
        }
    }
    
}
