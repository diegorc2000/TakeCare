package com.diealbalb.publicaciones;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.diealbalb.MainActivity;
import com.diealbalb.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class PublicacionesMain extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Button pBtnImagen;
    private Button pBtnPublicarAnuncio;
    private TextView pTvAnuncios;
    private EditText pEtNomAnuncio;
    private ImageView pIvAnuncio;
    private ProgressBar pPbAnuncio;

    private Uri pImagenUri;

    private StorageReference pStorageRef;
    private DatabaseReference pDatabaseRef;

    private StorageTask pUploadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicaciones_main);

        pBtnImagen = findViewById(R.id.btnImagen);
        pBtnPublicarAnuncio = findViewById(R.id.btnPublicarAnuncio);
        pTvAnuncios = findViewById(R.id.tvAnuncios);
        pEtNomAnuncio = findViewById(R.id.etNomAnuncio);
        pIvAnuncio = findViewById(R.id.ivAnuncio);
        pPbAnuncio = findViewById(R.id.pbAnuncio);

        pStorageRef = FirebaseStorage.getInstance().getReference("publicaciones");
        pDatabaseRef = FirebaseDatabase.getInstance().getReference("publicaciones");

        pBtnImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirOpcion();
            }
        });

        pBtnPublicarAnuncio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pUploadTask != null && pUploadTask.isInProgress()) {
                    Toast.makeText(PublicacionesMain.this, R.string.publicando_anuncio, Toast.LENGTH_SHORT).show();
                } else {
                    publicarAnuncio();
                }
            }
        });

        pTvAnuncios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirImageActivity();
            }
        });
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void publicarAnuncio() {
        if (pImagenUri != null) {
            StorageReference fileReference = pStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(pImagenUri));

            pUploadTask = fileReference.putFile(pImagenUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    pPbAnuncio.setProgress(0);
                                }
                            }, 500);
                            Toast.makeText(PublicacionesMain.this, "Upload successful", Toast.LENGTH_LONG).show();
                            Anuncio upload = new Anuncio(pEtNomAnuncio.getText().toString().trim(),
                                    taskSnapshot.getUploadSessionUri().toString());
                            String uploadId = pDatabaseRef.push().getKey();
                            pDatabaseRef.child(uploadId).setValue(upload);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PublicacionesMain.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            pPbAnuncio.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, R.string.no_data, Toast.LENGTH_SHORT).show();
        }

    }

    private void abrirOpcion() {
        Intent i = new Intent();
        i.setType("imagenes/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, PICK_IMAGE_REQUEST);
    }

    //ctrl + o
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            pImagenUri = data.getData();

            Glide.with(PublicacionesMain.this).load(pImagenUri.toString()).into(pIvAnuncio);
            //mImageView.setImageURI(mImagenUri);
        }
    }

    private void abrirImageActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }






}