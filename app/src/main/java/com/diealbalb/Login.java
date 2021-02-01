package com.diealbalb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    TextView tvText;
    EditText etNombre;
    EditText etPassword;;
    Button btnIniciar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        tvText = findViewById(R.id.tvText);
        etNombre = findViewById(R.id.etNombre);
        etPassword = findViewById(R.id.etPassword);
        btnIniciar = findViewById(R.id.btnInicS);
    }

    public void inicio(View view) {
        String nombre = etNombre.getText().toString();
        String contraseña = etPassword.getText().toString();

        if (nombre.isEmpty() || contraseña.isEmpty()){
            Toast.makeText(this, R.string.msg_no_data, Toast.LENGTH_LONG).show();
        }else {
            Intent i = new Intent(this, MainActivity.class);

            String dato = etNombre.getText().toString();
            i.putExtra("NOMBRE", dato);

            startActivity(i);
        }
    }

    public void recordar(View view) {
        Intent i = new Intent(this, MainActivity.class);

        startActivity(i);
    }

    public void registro(View view) {
        Intent i = new Intent(this, SingIn.class);

        startActivity(i);
    }
}