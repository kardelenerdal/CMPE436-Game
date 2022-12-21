package com.example.mygame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    public String player_id;
    public String game_id;
    public String key;
    public String myNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);
    }

    public void startGame(View view) throws InterruptedException {
        if (Objects.equals(player_id, "")) {
            popupMessage("You should add a player first!!!!!");
        }
        myNumber = "1";
        String messageToSend = "2#route:game.start;player_id:" + player_id + ";";
        System.out.println(messageToSend);
        NBR messageToSendNBR = NBR.parseString(messageToSend);
        ClientThread clientThread = new ClientThread(messageToSendNBR);
        Thread cThread = new Thread(clientThread);
        cThread.start();
        cThread.join();
        NBR response = NBR.parseString(clientThread.getResponse());
        game_id = response.get("game_id");
        key = response.get("key");

        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setMessage("Your key is " + key + ". Your game id is " + game_id);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Got It", (dialog1, which) -> {
            Intent intent = new Intent(MainActivity.this, LoadingActivity.class);
            intent.putExtra("myNumber", myNumber);
            intent.putExtra("game_id", game_id);
            intent.putExtra("player_id", player_id);
            startActivity(intent);
        });
        dialog.show();
    }

    public void enterGame(View view) throws InterruptedException {
        if (Objects.equals(player_id, "")) {
            popupMessage("You should add a player first!!!!!");
        }
        myNumber = "2";
        TextInputEditText inputGameCode = findViewById(R.id.inputGameCode);
        if (inputGameCode.getText().toString().isEmpty()) {
            popupMessage("Enter game code!!!!!");
            return;
        }
        String messageToSend = "3#route:game.enter;key:" + inputGameCode.getText() + ";player_id:" + player_id + ";";
        System.out.println(messageToSend);
        NBR messageToSendNBR = NBR.parseString(messageToSend);
        ClientThread clientThread = new ClientThread(messageToSendNBR);
        Thread cThread = new Thread(clientThread);
        cThread.start();
        cThread.join();
        NBR response = NBR.parseString(clientThread.getResponse());
        game_id = response.get("game_id");
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        intent.putExtra("myNumber", myNumber);
        intent.putExtra("game_id", game_id);
        intent.putExtra("player_id", player_id);
        startActivity(intent);
    }

    public void addPlayer(View view) throws InterruptedException {
        TextInputEditText inputYourName = findViewById(R.id.inputYourName);
        if (inputYourName.getText().toString().isEmpty()) {
            popupMessage("Enter your name!!!!!");
            return;
        }
        String messageToSend = "2#route:player.add;name:" + Objects.requireNonNull(inputYourName.getText()) + ";";
        System.out.println(messageToSend);
        NBR messageToSendNBR = NBR.parseString(messageToSend);
        ClientThread clientThread = new ClientThread(messageToSendNBR);
        Thread cThread = new Thread(clientThread);
        cThread.start();
        cThread.join();
        NBR response = NBR.parseString(clientThread.getResponse());
        player_id = response.get("id");
        popupMessage(("Your id: " + player_id));
    }

    public void popupMessage(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setNegativeButton("ok", (dialogInterface, i) -> {
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}