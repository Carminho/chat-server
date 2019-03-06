package org.academiadecodigo.bootcamp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static final int PORT = 8000;


    public static void main(String[] args) {

        try {
            Server server = new Server();
            while (true){
                server.interact(server.connect());
            }
        }catch (IOException ex){
        }
    }


    private ServerSocket serverSocket;


    public Server () throws IOException{
        serverSocket = new ServerSocket(PORT);
    }


    public Socket connect () throws IOException {
        Socket socket = serverSocket.accept();
        System.out.println("A new connection was established with " + socket.getInetAddress() + " at port " + socket.getPort());
        return socket;
    }


    public void interact (Socket socket) throws IOException{
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        while (true) {
            System.out.println(in.readLine());
            String clientNickname = in.readLine();
            System.out.println(clientNickname);
            String message = in.readLine();
            System.out.println(message);
        }
    }

}
