package com.example.mygame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class WinActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_win);
    }

    public void goBackHome(View view) {
        startActivity(new Intent(WinActivity.this, MainActivity.class));
    }
}
