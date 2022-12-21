package com.example.mygame;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class LoadingActivity extends AppCompatActivity {

    private boolean twoPlayers = false;
    public String state;
    public String game_id;
    public String player_id;
    public String myNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_loading);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                game_id = extras.getString("game_id");
                player_id = extras.getString("player_id");
                myNumber = extras.getString("myNumber");
            }
            while (!twoPlayers) {
                String game_state = checkIfTwoPlayers(game_id);
                System.out.println(game_state);
                if (game_state != null && !game_state.equals("WAIT")) {
                    twoPlayers = true;
                    changeToGameActivity();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private String checkIfTwoPlayers(String game_id) {
        String messageToSend = "2#route:game.state;game_id:" + game_id + ";";
        NBR messageToSendNBR = NBR.parseString(messageToSend);
        ClientThread clientThread = new ClientThread(messageToSendNBR);
        Thread cThread = new Thread(clientThread);
        cThread.start();
        try {
            cThread.join();
        } catch (InterruptedException e) {
            System.out.println("cannot join");
        }
        String resp = clientThread.getResponse();
        NBR response;
        try {
            response = NBR.parseString(resp);
        } catch (Exception e) {
            return "WAIT";
        }
        try {
            response.get("error");
        } catch (Exception ignored) {

        }
        state = response.get("state");
        return state;
    }

    public void changeToGameActivity() {
        Intent intent = new Intent(LoadingActivity.this, GameActivity.class);
        intent.putExtra("myNumber", myNumber);
        intent.putExtra("game_id", game_id);
        intent.putExtra("player_id", player_id);
        startActivity(intent);
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
