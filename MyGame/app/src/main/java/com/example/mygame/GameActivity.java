package com.example.mygame;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    int gun = 0;
    TextView nofGuns;
    TextView opponentName;
    TextView round;
    TextView opponentAction;
    public String game_id;
    public String player_id;
    public String myNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_game);
        nofGuns = (TextView) findViewById(R.id.nofGuns);
        opponentName = (TextView) findViewById(R.id.nameOfOpponent);
        round = (TextView) findViewById(R.id.round);
        opponentAction = (TextView) findViewById(R.id.opponentAction);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            game_id = extras.getString("game_id");
            player_id = extras.getString("player_id");
            myNumber = extras.getString("myNumber");
        }
    }

    public void display(int n) {
        nofGuns.setText("Bullets: " + n);
    }

    public void popupMessage(String action) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("You don't have enough guns.");
        alertDialogBuilder.setTitle("You can't " + action + "!");
        alertDialogBuilder.setNegativeButton("ok", (dialogInterface, i) -> {
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void shoot(View view) throws InterruptedException {
        if (gun == 0) {
            popupMessage("shoot");
            return;
        }
        gun--;
        display(gun);
        doAction("PISTOL");
    }

    public void bazooka(View view) throws InterruptedException {
        if (gun < 3) {
            popupMessage("use bazooka");
            return;
        }
        gun -= 3;
        display(gun);
        doAction("BAZOOKA");
    }

    public void rifle(View view) throws InterruptedException {
        if (gun < 2) {
            popupMessage("use rifle");
            return;
        }
        gun -= 2;
        display(gun);
        doAction("RIFLE");
    }

    public void block(View view) throws InterruptedException {
        display(gun);
        doAction("PROTECT");
    }

    public void reload(View view) throws InterruptedException {
        gun++;
        display(gun);
        // reload request
        doAction("RELOAD");
    }

    public NBR getGameState() throws InterruptedException {
        NBR gameStateRequest = new NBR();
        gameStateRequest.put("route", "game.state");
        gameStateRequest.put("game_id", game_id);
        String res = sendMessageToServer(gameStateRequest);
        System.out.println(res);
        return NBR.parseString(res);
    }

    public String sendMessageToServer(NBR nbr) throws InterruptedException {
        ClientThread clientThread = new ClientThread(nbr);
        Thread cThread = new Thread(clientThread);
        cThread.start();
        cThread.join();
        return clientThread.getResponse();
    }

    public void doAction(String action) throws InterruptedException {
        NBR message = new NBR();
        message.put("route", "game.action");
        message.put("player_id", player_id);
        message.put("game_id", game_id);
        message.put("action", action);
        message.put("bullet_amount", String.valueOf(gun));
        String response = sendMessageToServer(message);
        NBR responseNBR = NBR.parseString(response);
        System.out.println("Response: " + response);
        // check game state if success
        if (responseNBR.get("success") != null && responseNBR.get("success").equals("true")) {
            NBR gameState;
            while (true) {
                gameState = getGameState();
                // write opponent name
                String oppName = "Name of your opponent: " + gameState.get("name" + (myNumber.equals("1") ? "2" : "1"));
                opponentName.setText(oppName);
                if (gameState.get("error") != null || gameState.get("state").equals("WAIT")) {
                    Thread.sleep(1000);
                } else {
                    System.out.println("Game State bu: " + gameState);
                    break;
                }
            }
            if (gameState.get("state").equals("CONT")) {
                round.setText("Round: " + gameState.get("round"));
                opponentAction.setText("Opponent: " + gameState.get("action" + (myNumber.equals("1") ? "2" : "1")));
            } else if (gameState.get("state").equals("WON1")) {
                if (myNumber.equals("1")) {
                    startActivity(new Intent(GameActivity.this, WinActivity.class));
                } else {
                    startActivity(new Intent(GameActivity.this, LostActivity.class));
                }
            } else {
                if (myNumber.equals("1")) {
                    startActivity(new Intent(GameActivity.this, LostActivity.class));
                } else {
                    startActivity(new Intent(GameActivity.this, WinActivity.class));
                }
            }
        }

    }

}
