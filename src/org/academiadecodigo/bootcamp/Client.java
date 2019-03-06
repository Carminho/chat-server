package org.academiadecodigo.bootcamp;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

    private final String HOSTNAME = "localhost";

    private Socket socket;


    public static void main(String[] args) {                                                                    //thread 1

        Client client = new Client();

        try{
            client.connect();
        } catch (UnknownHostException ex1){
            System.out.println("Seems like that host doesn't exist. Please check it out.");
        } catch (IOException ex2){
            ex2.getCause();
        }
    }


    public void connect () throws UnknownHostException, IOException {

        socket = new Socket(HOSTNAME, Server.PORT);

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader terminalIn = new BufferedReader(new InputStreamReader(System.in));

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    while (socket.isBound()) {

                        System.out.println(in.readLine());
                    }
                } catch (IOException e){
                    System.out.println(e.getMessage());
                }

            }
        });


        while(true) {

            String message = terminalIn.readLine();
            out.println(message);

        }




    }




}