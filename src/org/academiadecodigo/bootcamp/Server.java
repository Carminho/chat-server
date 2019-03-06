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


    private void listWorkers(PrintWriter out) {
        for (Worker w : workers) {
            out.println(w.nickname);
        }
    }


    //----------------------------------------------------------------------------------------------------------------------------


    public class Worker implements Runnable {

        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String nickname;
        BufferedWriter saveLog;

        public Worker(Socket socket) {
            this.socket = socket;
            try {
                saveLog = new BufferedWriter(new FileWriter("resource/log.txt"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        @Override
        public void run() {

            try {
                start();
                String input = "";

                while (!(input = in.readLine()).equals("*QUIT")) {

                    if (input.equals("*CHANGE")) {
                        nickname = in.readLine();
                        out.println("Your nickname has been changed to " + nickname);
                        continue;
                    }

                    if (input.equals("*USERS")) {
                        listWorkers(out);
                        continue;
                    }

                    sendMessage(input);
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

            out.println("WELCOME " + nickname + " :) \n\n-- Commands: --\n*QUIT to exit conversation\n*CHANGE to change nickname\n*USERS to list all users logged in\n");
            System.out.println(nickname + " logged in (IPaddress: " + socket.getInetAddress() + ")");
        }


        private void sendMessage(String input) throws IOException {
            String message = nickname + ": " + input;
            System.out.println(message);
            sendAll(message);
        }


        private void send(String message) {
            out.println(message);
            saveToFile(message);
        }



        private void closeConnection() throws IOException {
            sendAll(nickname + " left the conversation");
            System.out.println(nickname + " left the conversation!");

            socket.close();
            out.close();
            in.close();
            workers.remove(this);
        }


        private void saveToFile (String message){
            try {
                saveLog.write(message + "\n");
                saveLog.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}

