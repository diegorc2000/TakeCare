package com.diealbalb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.diealbalb.diseño.Login;
import com.diealbalb.fragmentos.AjustesFragmento;
import com.diealbalb.fragmentos.PrivacidadSeguridadFragmento;
import com.diealbalb.fragmentos.NosotrosFragmento;
import com.diealbalb.listeners.OnControlerFragmentListener;
import com.diealbalb.mensaje.MensajesActivity;
import com.diealbalb.publicaciones.Anuncio;
import com.diealbalb.publicaciones.AnuncioAdapter;
import com.diealbalb.publicaciones.AnunciosMain;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnControlerFragmentListener, AnuncioAdapter.OnItemClickListener {

    private FirebaseAuth fba;
    private FirebaseUser user;

    FrameLayout flMain;
    BottomNavigationView bottom;

    //fragmentos
    AjustesFragmento ajuestesFragment = new AjustesFragmento();

    //ANUNCIO
    private RecyclerView mRecyclerView;
    private AnuncioAdapter mAdapter;

    private ProgressBar mProgressCircle;

    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;

    private List<Anuncio> mAnuncio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fba = FirebaseAuth.getInstance();
        user = fba.getCurrentUser();

        flMain = findViewById(R.id.flMain);

        bottom = findViewById(R.id.bnMain);
        bottom.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //ANUNCIO
        mRecyclerView = findViewById(R.id.rv);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //mProgressCircle = findViewById(R.id.pbCircle);
        mAnuncio = new ArrayList<>();
        mAdapter = new AnuncioAdapter(MainActivity.this, mAnuncio);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(MainActivity.this);
        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mAnuncio.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Anuncio upload = postSnapshot.getValue(Anuncio.class);
                    upload.setKey(postSnapshot.getKey());
                    mAnuncio.add(upload);
                }
                mAdapter.notifyDataSetChanged();
                //mProgressCircle.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                //mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }

    //BOTOM NAVIGATION
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.itmInicio:
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                    return true;
                case R.id.itmPublicar:
                    startActivity(new Intent(MainActivity.this, AnunciosMain.class));
                    mRecyclerView.setVisibility(View.VISIBLE);
                    return true;
                case R.id.itmSms:
                    startActivity(new Intent(MainActivity.this, MensajesActivity.class));
                    return true;
                case R.id.itmAjustes:
                    loadFragment(ajuestesFragment);
                    mRecyclerView.setVisibility(View.INVISIBLE);
                    return true;
            }

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flMain, selectedFragment)
                    .addToBackStack(null)
                    .commit();
            return false;
        }
    };

    @Override
    public void selectFrgment(String texto) {

        Fragment selectedFragment = null;

        switch (texto) {
            case "nosotros":
                selectedFragment = new NosotrosFragmento();
                break;
            case "bibliografia":
                selectedFragment = new PrivacidadSeguridadFragmento();
                break;
            case "desconectarse":
                fba.signOut();
                Intent i = new Intent(this, Login.class);
                startActivity(i);
                finish();
                break;
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flMain, selectedFragment)
                .addToBackStack(null)
                .commit();
    }

    //Fragmentos
    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flMain, fragment);
        transaction.commit();

    }

    //PARA EL MENU onCreateOptionsMenu y onOptionsItemSelected Para el boton desconectar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.itmDesconectar) {
            fba.signOut();
            Intent i = new Intent(this, Login.class);
            startActivity(i);
            finish();//para cerrar el activity
        }
        return super.onOptionsItemSelected(item);
    }

    //ANUNCIO
    @Override
    public void onItemClick(int position) {
        //Toast.makeText(this, R.string.reserva + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onWhatEverClick(int position) {
        Toast.makeText(this, "Has reservado, ponte en contacto desde el chat grupal", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDeleteClick(int position) {
        Anuncio selectedItem = mAnuncio.get(position);
        final String selectedKey = selectedItem.getKey();

        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.child(selectedKey).removeValue();
                Toast.makeText(MainActivity.this, "Anuncio eliminar", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }

}