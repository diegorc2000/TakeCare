package com.diealbalb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }

    public void inico(View view) {
        Intent i = new Intent(this, MainActivity.class);

        startActivity(i);
    }

    public void recordar(View view) {

    }

    public void registro(View view) {
    }
}