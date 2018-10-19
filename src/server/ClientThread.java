package server;

import client.Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientThread implements Runnable {

    private ChatServer server;
    private Socket socket;
    private PrintWriter clientOut;

    public ClientThread(ChatServer server,Socket socket) {
        this.server = server;
        this.socket = socket;
    }

    public PrintWriter getWriter() {
        return clientOut;
    }

    @Override
    public void run() {
        try {
            //setup
            clientOut = new PrintWriter(socket.getOutputStream(), false);
            Scanner in = new Scanner(socket.getInputStream());
            //commence communication
            while (!socket.isClosed()) {
                if (in.hasNextLine()) {
                    String input = in.nextLine();
                    for (ClientThread certainClient : server.getClients()) {
                        PrintWriter certainClientOut = certainClient.getWriter();
                        if(certainClientOut != null) {
                            certainClientOut.write(input + "\r\n");
                            certainClientOut.flush();
                        }

                    }
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
