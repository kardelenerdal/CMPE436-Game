package com.example.mygame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class GameActivity extends AppCompatActivity {

    int gun = 0;
    TextView nofGuns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_game);
        nofGuns = (TextView) findViewById(R.id.nofGuns);
    }

    public void display(int n) {
        nofGuns.setText("Number of guns: " + n);
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
        String response = sendMessageToServer("shoot");
        System.out.println("Response: " + response);
        if (response.equals("win")) {
            startActivity(new Intent(GameActivity.this, WinFragment.class));
        } else if (response.equals("lost")) {
            startActivity(new Intent(GameActivity.this, LastFragment.class));
        }
    }

    public void bazooka(View view) throws InterruptedException {
        if (gun < 3) {
            popupMessage("use bazooka");
            return;
        }
        gun -= 3;
        display(gun);
        String response = sendMessageToServer("bazooka");
        System.out.println("Response: " + response);
        if (response.equals("win")) {
            startActivity(new Intent(GameActivity.this, WinFragment.class));
        } else if (response.equals("lost")) {
            startActivity(new Intent(GameActivity.this, LastFragment.class));
        }
    }

    public void rifle(View view) throws InterruptedException {
        if (gun < 2) {
            popupMessage("use rifle");
            return;
        }
        gun -= 2;
        display(gun);
        String response = sendMessageToServer("rifle");
        System.out.println("Response: " + response);
        if (response.equals("win")) {
            startActivity(new Intent(GameActivity.this, WinFragment.class));
        } else if (response.equals("lost")) {
            startActivity(new Intent(GameActivity.this, LastFragment.class));
        }
    }

    public void block(View view) throws InterruptedException {
        display(gun);
        String response = sendMessageToServer("block");
        System.out.println("Response: " + response);
        if (response.equals("win")) {
            startActivity(new Intent(GameActivity.this, WinFragment.class));
        } else if (response.equals("lost")) {
            startActivity(new Intent(GameActivity.this, LastFragment.class));
        }
    }

    public void reload(View view) throws InterruptedException {
        gun++;
        display(gun);
        String response = sendMessageToServer("reload");
        System.out.println("Response: " + response);
        if (response.equals("win")) {
            startActivity(new Intent(GameActivity.this, WinFragment.class));
        } else if (response.equals("lost")) {
            startActivity(new Intent(GameActivity.this, LastFragment.class));
        }
    }

    public String sendMessageToServer(String action) throws InterruptedException {
        String messageToSend = "3#route:test;request:action;action:" + action + ";";
        NBR messageToSendNBR = NBR.parseString(messageToSend);
        ClientThread clientThread = new ClientThread(messageToSendNBR);
        Thread cThread = new Thread(clientThread);
        cThread.start();
        cThread.join();
        return clientThread.getResponse();
    }

}
