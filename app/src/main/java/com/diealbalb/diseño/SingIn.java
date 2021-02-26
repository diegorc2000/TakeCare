package com.diealbalb.diseño;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.diealbalb.MainActivity;
import com.diealbalb.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SingIn extends AppCompatActivity {

    private FirebaseAuth fba;
    private FirebaseUser user;
    EditText etNom;
    EditText etCorElec;
    EditText etNumTel;
    EditText etFPassword;
    EditText etComPassword;
    Button btnInicS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sing_in);

        etNom = findViewById(R.id.etNom);
        etCorElec = findViewById(R.id.etCorElec);
        etNumTel = findViewById(R.id.etNumTel);
        etFPassword = findViewById(R.id.etFPassword);
        etComPassword = findViewById(R.id.etComPassword);
        btnInicS = findViewById(R.id.btnInicS);

        fba = FirebaseAuth.getInstance();
        user = fba.getCurrentUser();

        if (user != null){
            etCorElec.setText(user.getEmail());
        }

        etCorElec.setText("");
    }

    public void newCuenta(View view) {
        String email = etCorElec.getText().toString().trim();
        String password = etFPassword.getText().toString().trim();
        String CPassword = etComPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty() || CPassword.isEmpty()){
            Toast.makeText(this, R.string.msj_no_datos, Toast.LENGTH_SHORT).show();
        }else if(!password.equals(CPassword)){
            Toast.makeText(this, R.string.toast_a2_datos_no_coinciden, Toast.LENGTH_LONG).show();
        }else{
            fba.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        user = fba.getCurrentUser();
                        //acceder a la pp
                        accederApp();
                        Toast.makeText(SingIn.this, R.string.msj_registrado,Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(SingIn.this, R.string.msj_no_registrado + "\n" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
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


}