package com.diealbalb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.diealbalb.diseño.Login;
import com.diealbalb.fragmentos.InicioFragment;
import com.diealbalb.fragmentos.PublicacionFragment;
import com.diealbalb.listeners.OnControlerFragmentListener;
import com.diealbalb.mensaje.MensajesActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth fba;
    private FirebaseUser user;

    FrameLayout flMain;
    BottomNavigationView bottom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fba = FirebaseAuth.getInstance();
        user = fba.getCurrentUser();

        flMain = findViewById(R.id.flMain);

        bottom = findViewById(R.id.bottom_navigation);
        bottom.setOnNavigationItemSelectedListener(navListener);


    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.itmHome:
                    selectedFragment = new InicioFragment();
                    break;
                case R.id.itmPublicacion:
                    selectedFragment = new PublicacionFragment();
                    break;
                case R.id.itmSMS:
                    startActivity(new Intent(MainActivity.this, MensajesActivity.class));
                    break;
            }

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flMain, selectedFragment)
                    .addToBackStack(null)
                    .commit();
            return true;
        }
    };

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