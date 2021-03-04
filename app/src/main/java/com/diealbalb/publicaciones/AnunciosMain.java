package com.diealbalb.publicaciones;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.diealbalb.MainActivity;
import com.diealbalb.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AnunciosMain extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Button mButtonChooseImage;
    private Button mButtonUpload;
    private TextView mTextViewShowUploads;
    private EditText mETNombre;
    private EditText mETDescripcion;
    private ImageView mImageView;
    private ProgressBar mProgressBar;

    private Uri mImageUri;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    ValueEventListener vel;

    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncios);

        mButtonChooseImage = findViewById(R.id.btnArchivo);
        mButtonUpload = findViewById(R.id.btnPublicar);
        mTextViewShowUploads = findViewById(R.id.tvAnuncion);
        mETNombre = findViewById(R.id.etNombre);
        mETDescripcion = findViewById(R.id.etDescripcion);
        mImageView = findViewById(R.id.ivArchivo);
        mProgressBar = findViewById(R.id.pbLineal);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads/");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads/");

        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(AnunciosMain.this, "Subiendo Anuncio", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }
            }
        });

        mTextViewShowUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagesActivity();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.with(this).load(mImageUri).into(mImageView);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        final String decripcion = mETDescripcion.getText().toString().trim();
        final String nombre = mETNombre.getText().toString().trim();

        if (nombre.isEmpty() || decripcion.isEmpty()){
            Toast.makeText(this, R.string.no_data, Toast.LENGTH_SHORT).show();
        }else{
            final StorageReference fotoRef = mStorageRef.child(mImageUri.getEncodedPath());

            UploadTask ut = fotoRef.putFile(mImageUri);

            Task<Uri> urlTask = ut.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task)
                        throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return fotoRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        Anuncio a = new Anuncio(nombre, decripcion, downloadUri.toString());
                        mDatabaseRef.child(nombre + " " + decripcion).setValue(a);

                        addDatabaseListener(nombre + " " + decripcion);
                    }
                }
            });
        }

    }

    private void addDatabaseListener(String clave) {
        if (vel == null){
            vel = new ValueEventListener(){

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Anuncio a = snapshot.getValue(Anuncio.class);
                    if (a != null) cargarPerfil(a);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            mDatabaseRef.child(clave).addValueEventListener(vel);
        }
    }

    private void cargarPerfil(Anuncio a) {
        mETNombre.setText(a.getmNombre());
        mETDescripcion.setText(a.getmDescripcion());
        Glide.with(mImageView.getContext()).load(a.getImageUrl()).into(mImageView);

        mETNombre.setText("");
        mETDescripcion.setText("");
        mImageView.setImageResource(0);
    }

    private void openImagesActivity() {
        Intent intent = new Intent(this, MainActivity.class); //MainActivity
        startActivity(intent);
    }

    public void volver(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}