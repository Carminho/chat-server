package org.academiadecodigo.bootcamp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static final int PORT = 8000;


    public static void main(String[] args) {

        try {
            Server server = new Server();
            while (true){
                server.connect();
            }
        }catch (IOException ex){
        }
    }


    private ServerSocket serverSocket;


    public Server () throws IOException{
        serverSocket = new ServerSocket(PORT);
    }


    public void connect () throws IOException {
        Socket socket = serverSocket.accept();
        System.out.println("A new connection was established with " + socket.getInetAddress() + " at port " + socket.getPort());
    }




}
