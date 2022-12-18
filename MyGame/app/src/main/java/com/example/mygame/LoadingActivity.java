package com.example.mygame;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class LoadingActivity extends AppCompatActivity {

    private boolean twoPlayers = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_loading);
       /* while (!twoPlayers) {
            String response = checkIfTwoPlayers();
            System.out.println("Respond: " + response);
            if (response.equals("1")) {
                twoPlayers = true;
            }
            try {
                Thread.sleep(200000);
            } catch (InterruptedException e) {
                System.out.println("cannot sleep");
            }
        }*/
        (new Handler()).postDelayed(this::changeToGameActivity, 10000);

    }

    private String checkIfTwoPlayers() {
        String messageToSend = "3#route:test;request:twoPlayers;gameCode:xxxx;";
        NBR messageToSendNBR = NBR.parseString(messageToSend);
        ClientThread clientThread = new ClientThread(messageToSendNBR);
        Thread cThread = new Thread(clientThread);
        cThread.start();
        try {
            cThread.join();
        } catch (InterruptedException e) {
            System.out.println("cannot join");
        }
        return clientThread.getResponse();
    }

    public void changeToGameActivity() {
        startActivity(new Intent(LoadingActivity.this, GameActivity.class));
    }
}
