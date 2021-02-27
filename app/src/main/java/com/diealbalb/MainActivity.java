package com.diealbalb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
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
import com.diealbalb.fragmentos.InicioFragment;
import com.diealbalb.mensaje.MensajesActivity;
import com.diealbalb.publicaciones.Anuncio;
import com.diealbalb.publicaciones.AnuncioAdapter;
import com.diealbalb.publicaciones.PublicacionesMain;
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

public class MainActivity extends AppCompatActivity implements AnuncioAdapter.OnItemClickListener{

    private FirebaseAuth fba;
    private FirebaseUser user;

    FrameLayout flMain;
    BottomNavigationView bottom;

    //Anuncios
    private RecyclerView mRecyclerView;
    private AnuncioAdapter mAdapter;

    private ProgressBar mProgressCircle;

    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;

    private List<Anuncio> mPublicaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fba = FirebaseAuth.getInstance();
        user = fba.getCurrentUser();

        flMain = findViewById(R.id.flMain);

        bottom = findViewById(R.id.bottom_navigation);
        bottom.setOnNavigationItemSelectedListener(navListener);


        //Anuncios
        mRecyclerView = findViewById(R.id.rv);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressCircle = findViewById(R.id.pbCircle);

        mPublicaciones = new ArrayList<>();

        mAdapter = new AnuncioAdapter(MainActivity.this, mPublicaciones);

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListner(MainActivity.this);

        mStorage = FirebaseStorage.getInstance();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Publicaciones");

        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                mPublicaciones.clear();

                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                    Anuncio publicacion = postSnapshot.getValue(Anuncio.class);

                    publicacion.setmKey(postSnapshot.getKey());

                    mPublicaciones.add(publicacion);
                }

                mAdapter.notifyDataSetChanged();

                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

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
                    startActivity(new Intent(MainActivity.this, PublicacionesMain.class));
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

    //Anuncio
    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "Click normal" + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onWhatEverClick(int position) {
        Toast.makeText(this, "Click loco" + position, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDeleteClick(int position) {
        Anuncio selectedItem = mPublicaciones.get(position);
        final String selectedKey = selectedItem.getmKey();

        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.child(selectedKey).removeValue();
                Toast.makeText(MainActivity.this, "Item seleccionado", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }

}