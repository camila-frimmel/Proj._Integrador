package com.internet.frimmel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.internet.frimmel.databinding.ActivityLogclienteBinding;

public class LoginCliente extends AppCompatActivity {

    private ActivityLogclienteBinding binding;

    private FirebaseAuth mAuth;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logcliente);
        Button entrarC = findViewById(R.id.entrarCliente);
        Button aju = findViewById(R.id.PrecisaAjuda);
        Button Esquece = findViewById(R.id.EsqueceuSenha);

    entrarC.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), MenuCliente.class);
            startActivity(intent);

        }
    });

    aju.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent ajuda = new Intent(getApplicationContext(), AjudaCliente.class);
            startActivity(ajuda);
        }
    });

    Esquece.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent esq = new Intent(getApplicationContext(), EsqueceuSenha.class);
            startActivity(esq);
        }
    });
    }
}
