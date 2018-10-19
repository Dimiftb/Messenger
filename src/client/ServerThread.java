package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

public class ServerThread implements Runnable {
    private Socket socket;
    private String userName;
    private boolean hasMessages = false;
    private final LinkedList<String> messagesToBeSent;

    public ServerThread(Socket socket, String name) {
        this.socket = socket;
        this.userName = name;
        messagesToBeSent = new LinkedList<String>();
    }

    public void addNextMessage(String message) {
        synchronized (messagesToBeSent) {
            hasMessages = true;
            messagesToBeSent.push(message);
        }
    }

    @Override
    public void run() {
        System.out.println("Welcome to this rocking Messenger");
        System.out.println("Local port:" + socket.getLocalPort());
        System.out.println("Server: " + socket.getRemoteSocketAddress());
        try {
            PrintWriter serverOut = new PrintWriter(socket.getOutputStream(), false);
            InputStream serverInputStream = socket.getInputStream();
            Scanner serverIn = new Scanner(serverInputStream);
            while(!socket.isClosed()) {
                if(serverInputStream.read() != -1) {
                    if(serverIn.hasNextLine()) {
                        System.out.println(serverIn.nextLine());
                    }
                }
                if(hasMessages) {
                    String nextMessageToSend;
                    synchronized (messagesToBeSent) {
                        nextMessageToSend = messagesToBeSent.pop();
                        hasMessages = !messagesToBeSent.isEmpty();
                        serverOut.print(userName + ":" + nextMessageToSend);
                        serverOut.flush();
                    }
                }
            }

        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
