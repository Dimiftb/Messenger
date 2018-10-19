package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
    private static final int portNumber = 9022;
    private int serverPort;
    private List<ClientThread> clients;

    public ChatServer(int portNumber) {
        this.serverPort = portNumber;
    }

    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer(portNumber);
        chatServer.createServer();
    }

    public List<ClientThread> getClients() {
        return clients;
    }

    public void createServer() {
        ServerSocket serverSocket;
        try{
            serverSocket = new ServerSocket(serverPort);
            acceptClients(serverSocket);
        }catch (IOException e) {
            System.err.println("Couldn't listen to server" + serverPort);
            System.exit(1);
        }
    }

    public void acceptClients(ServerSocket serverSocket) {
        System.out.println("server starts at port:" + serverSocket.getLocalSocketAddress());
        clients = new ArrayList<ClientThread>();
        while(true) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Server accepts:" + socket.getRemoteSocketAddress());
                ClientThread client = new ClientThread(this, socket);
                Thread thread = new Thread(client);
                thread.start();
                clients.add(client);
            }catch (IOException e) {
                System.out.println("accept failed on" + serverPort);

            }

        }
    }

}
