package com.example.mygame;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

public class ClientThread implements Runnable {
    static String hostname = "18.204.213.255";
    //static String hostname = "192.168.17.1";
    static int port = 8080;
    private NBR message;
    private String response = "";

    public ClientThread(NBR message) {
        this.message = message;
    }

    @Override
    public void run() {
        try {
            Socket s = new Socket(hostname, port);
            DataOutputStream out = new DataOutputStream(s.getOutputStream());

            out.writeBytes(message.toString());
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            String line = "";
            while ((line = in.readLine()) != null) {
                System.out.println(line + "KARDELENNNNNNNN");
                response = response.concat(line);
            }
        } catch (SocketException e) {
            response = "1#error:socketError;";
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getResponse() {
        return response;
    }
}
