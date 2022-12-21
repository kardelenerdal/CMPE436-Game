package com.example.mygame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class LostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_last);
    }

    public void goBackHome(View view) {
        startActivity(new Intent(LostActivity.this, MainActivity.class));
    }
}
