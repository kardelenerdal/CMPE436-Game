package com.example.mygame;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    //static String hostname = "18.204.213.255";
    static String hostname = "192.168.17.1";
    static int port = 8080;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);
    }

    public void popupMessage() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("You have to enter a valid game code!");
        alertDialogBuilder.setNegativeButton("ok", (dialogInterface, i) -> {
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void checkAndStartGame(View view) throws InterruptedException {
        TextInputEditText gameCodeEdit = (TextInputEditText) findViewById(R.id.inputGameCode);
        if (gameCodeEdit.getText().toString().isEmpty()) {
            popupMessage();
            return;
        }
        String messageToSend = "3#route:test;request:startGame;gameCode:" + Objects.requireNonNull(gameCodeEdit.getText()) + ";";
        System.out.println(messageToSend);
        NBR messageToSendNBR = NBR.parseString(messageToSend);
        ClientThread clientThread = new ClientThread(messageToSendNBR);
        Thread cThread = new Thread(clientThread);
        cThread.start();
        cThread.join();
        System.out.println("Response: " + clientThread.getResponse());
        startActivity(new Intent(MainActivity.this, LoadingActivity.class));
    }
}