package org.academiadecodigo.bootcamp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    private final int PORT = Server.PORT;
    private final String HOSTNAME = "localhost";

    private Socket socket;
    private String nickname;
    private BufferedReader terminalIn;


    public static void main(String[] args) {

        Client client = new Client();

        try{
            client.connect();
            client.setNickname();
            while (true) {
                client.startConversation();
            }
        } catch (UnknownHostException ex1){
            System.out.println("Seems like that host doesn't exist. Please check it out.");
        } catch (IOException ex2){
            ex2.getCause();
        }
    }


    public void connect () throws UnknownHostException, IOException {
        socket = new Socket(HOSTNAME, Server.PORT);
        Thread thread = new Thread(new MessageOut(socket));
        thread.start();
        System.out.println("a new thread was initiated");
    }


    public BufferedReader setNickname () throws IOException {
        terminalIn = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter your nickname: ");
        nickname = terminalIn.readLine();
        return terminalIn;
    }


    private void startConversation () throws IOException {
        //send nickname to server
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        out.print(nickname + "is logged.");

        //message introduced by client and sent to server
        System.out.print(nickname + ": ");
        String message = terminalIn.readLine();
        out.print(nickname + ": " + message);
        out.flush();

    }


    private class MessageOut implements Runnable {

        private Socket socket;

        public MessageOut (Socket socket){
            this.socket = socket;
        }

        @Override
        public void run(){
            //messages received from server and printed to terminal
            try {
                while(true) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    System.out.println("Server: " + in.readLine());
                }
            }catch (IOException ex){
            }
        }
    }

}