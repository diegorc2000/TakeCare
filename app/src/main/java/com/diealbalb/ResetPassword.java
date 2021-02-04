package com.diealbalb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {

    EditText etEmail;
    Button btnReset;
    Button btnCancelar;

    private String email = "";

    private FirebaseAuth mAuth;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);

        etEmail = findViewById(R.id.etMail);
        btnReset = findViewById(R.id.btnReset);
        btnCancelar = findViewById(R.id.btnCancelar);

        mAuth = FirebaseAuth.getInstance();
        mDialog =  new ProgressDialog(this);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = etEmail.getText().toString();

                if(!email.isEmpty()){
                    resetPasword();
                }else{
                    Toast.makeText(ResetPassword.this, R.string.i_mail , Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void resetPasword(){

        mAuth.setLanguageCode("es");
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    mDialog.setMessage("Espera...");
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.show();
                    Toast.makeText(ResetPassword.this, R.string.correo_enviado, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ResetPassword.this, R.string.no_correo_restablecido , Toast.LENGTH_SHORT).show();
                }

                mDialog.dismiss();
            }
        });
    }




    public void cancelar(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }
}