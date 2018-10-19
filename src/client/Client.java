package client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String host = "localhost";
    private static final int portNumber = 9022;
    private String userName;
    private String serverHost;
    private int serverPort;

    public Client(String name, String host, int portNumber) {
        this.userName = name;
        this.serverHost = host;
        this.serverPort = portNumber;
    }

    public static void main(String[] args) {
        String clientName = null;
        Scanner scan = new Scanner(System.in);
        while(clientName == null || clientName.trim().equals("")) {
            System.out.println("Please enter your username");
            clientName = scan.nextLine();
            if(clientName.trim().equals("")) {
                System.out.println("Invalid name entered. Please try again");
            }
        }
        Client client = new Client(clientName,host,portNumber);
        client.beginClient(scan);
    }

    private void beginClient(Scanner scan) {
            try {
                Socket socket = new Socket(serverHost,serverPort);
                Thread.sleep(1000); //wait for network communication
                ServerThread serverThread = new ServerThread(socket,userName);
                Thread serverThreadAccess = new Thread(serverThread);
                serverThreadAccess.start();
                while(serverThreadAccess.isAlive()) {
                    if(scan.hasNextLine()) {
                        serverThread.addNextMessage(scan.nextLine());
                    }
                }
            }catch (IOException | InterruptedException e){
                System.out.println("Fatal error connection");
                e.printStackTrace();
            }
    }
}
