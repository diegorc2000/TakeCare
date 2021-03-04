package com.diealbalb.diseño;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.diealbalb.MainActivity;
import com.diealbalb.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private FirebaseAuth fba;
    private FirebaseUser user;

    TextView tvText;
    EditText etMail;
    EditText etPassword;
    Button btnIniciar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        tvText = findViewById(R.id.tvText);
        etMail = findViewById(R.id.etMail);
        etPassword = findViewById(R.id.etPassword);
        btnIniciar = findViewById(R.id.btnInicS);

        fba = FirebaseAuth.getInstance();
        user = fba.getCurrentUser();

        if(user != null){
            etMail.setText(user.getEmail());

        }
    }

    public void inicio(View view) {
        String email = etMail.getText().toString();
        String contraseña = etPassword.getText().toString();

        if (email.isEmpty() || contraseña.isEmpty()){
            Toast.makeText(this, R.string.msg_no_data, Toast.LENGTH_LONG).show();
        }else {
            fba.signInWithEmailAndPassword(email, contraseña).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        user = fba.getCurrentUser();
                        accederApp();
                        finish();
                    }else {
                        Toast.makeText(Login.this, getString(R.string.msj_no_accede) + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    private void accederApp() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    public void recordar(View view) {
        Intent i = new Intent(this, ResetPassword.class);

        startActivity(i);
    }

    public void registro(View view) {
        Intent i = new Intent(this, SingIn.class);

        startActivity(i);
    }
}