package org.academiadecodigo.bootcamp;

import com.sun.corba.se.spi.orbutil.threadpool.Work;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public static final int PORT = 8000;


    public static void main(String[] args) {

        try {
            Server server = new Server();
            while (true) {
                server.connect();
            }
        } catch (IOException ex) {
        }
    }


    private ServerSocket serverSocket;
    private ArrayList<Worker> workers;


    public Server() throws IOException {
        serverSocket = new ServerSocket(PORT);
    }


    public void connect() throws IOException {
        Socket socket = serverSocket.accept();
        System.out.println("A new connection was established with " + socket.getInetAddress() + " at port " + socket.getPort());
        Worker worker = new Worker(socket);
        ExecutorService thread = Executors.newCachedThreadPool();
        thread.submit(worker);
    }


    //----------------------------------------------------------------------------------------------------------------------------

    public class Worker implements Runnable {

        private Socket socket;

        public Worker(Socket socket) {
            this.socket = socket;
        }


        @Override
        public void run() {

            try {
                PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                out.println("Enter your nickname: ");


                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String nickname = in.readLine();
                out.println("WELCOME " + nickname + " :) ");

                System.out.println(nickname + " logged in");
                while (socket.isBound()) {
                    out.println(nickname + ": ");
                    System.out.println(nickname + ": " + in.readLine());
                }
            }catch (IOException ex){

            }

        }
    }
}

