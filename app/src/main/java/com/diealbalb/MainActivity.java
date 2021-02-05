package com.diealbalb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth fba;
    private FirebaseUser user;
    TextView tvInicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fba = FirebaseAuth.getInstance();
        user = fba.getCurrentUser();

        tvInicio = findViewById(R.id.tvInicio);
        tvInicio.setText(String.format(getString(R.string.tv_inicio), user.getEmail()));
    }

    //PARA EL MENU onCreateOptionsMenu y onOptionsItemSelected

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.itmDesconectar){
            fba.signOut();
            Intent i = new Intent(this, Login.class);
            startActivity(i);
            finish();//para cerrar el activity
        }
        return super.onOptionsItemSelected(item);
    }
}