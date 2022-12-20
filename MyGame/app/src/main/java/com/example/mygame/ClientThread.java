package com.example.mygame;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientThread implements Runnable {
    static String hostname = "18.204.213.255";
    //static String hostname = "192.168.17.1";
    static int port = 8080;
    static String test = "3#route:test;name:Kardelen;surname:Erdal;";
    private NBR message;
    private volatile String response = "";

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
            System.out.println("cevap gelicek");
            while ((line = in.readLine()) != null) {
                System.out.println(line + "KARDELENNNNNNNN");
                response += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getResponse() {
        return response;
    }
}
