package org.academiadecodigo.bootcamp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
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
        workers = new ArrayList<>();
    }


    public void connect() throws IOException {
        Socket socket = serverSocket.accept();
        Worker worker = new Worker(socket);
        workers.add(worker);
        ExecutorService thread = Executors.newCachedThreadPool();
        thread.submit(worker);
    }


    public void sendAll(String message) {
        for (Worker w : workers) {
            w.send(message);
        }
    }


    private void listWorkers (){
        for (Worker w: workers){
            System.out.println(w.nickname);
        }
    }


    //----------------------------------------------------------------------------------------------------------------------------

    public class Worker implements Runnable {

        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String nickname;

        public Worker(Socket socket) {
            this.socket = socket;
        }


        @Override
        public void run() {

            try {

                start();
                String input = "";

                while (!(input = in.readLine()).equals("*quit")) {

                    if (input.equals("*change")) {
                        nickname = in.readLine();
                        out.println("Your nickname has been changed to " + nickname);
                        continue;
                    }

                    String message = nickname + ": " + input;
                    System.out.println(message);
                    sendAll(message);
                }

                closeConnection();

            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }

        }


        private void start() throws IOException {
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            out.println("Enter your nickname: ");

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            nickname = in.readLine();

            out.println("WELCOME " + nickname + " :) \n\n-- Commands: --\n*quit to exit conversation \n*change to change nickname\n");
            System.out.println(nickname + " logged in (IPaddress: " + socket.getInetAddress() + ")");
        }


        private void send(String message) {
            out.println(message);
        }


        private void closeConnection() throws IOException {
            sendAll(nickname + " left the conversation");
            System.out.println(nickname + " left the conversation!");

            socket.close();
            out.close();
            in.close();
        }

    }
}

