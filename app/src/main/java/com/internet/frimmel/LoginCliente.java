package com.internet.frimmel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LoginCliente extends AppCompatActivity {

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
