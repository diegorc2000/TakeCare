package com.diealbalb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.diealbalb.diseño.Login;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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

        /*tvInicio = findViewById(R.id.tvInicio);
        tvInicio.setText(String.format(getString(R.string.tv_inicio), user.getEmail()));

        tvMain = findViewById(R.id.tvMain);*/

        BottomNavigationView bottomNavBar = findViewById(R.id.bottom_navigation);

        bottomNavBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.itmHome:
                        item.setChecked(true);
                        Toast.makeText(MainActivity.this, "Likes clicked.", Toast.LENGTH_SHORT).show();
                        item.setEnabled(true);
                        break;
                    case R.id.itmPublicacion:
                        item.setChecked(true);
                        Toast.makeText(MainActivity.this, "Add clicked.", Toast.LENGTH_SHORT).show();
                        item.setEnabled(true);
                        break;
                    case R.id.itmSMS:
                        item.setChecked(true);
                        Toast.makeText(MainActivity.this, "Add clicked.", Toast.LENGTH_SHORT).show();
                        item.setEnabled(true);
                        break;
                }
                return false;
            }
        });

    }

    //PARA EL MENU onCreateOptionsMenu y onOptionsItemSelected Para el boton desconectar
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