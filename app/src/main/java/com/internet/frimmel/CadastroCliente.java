package com.internet.frimmel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CadastroCliente extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrocliente);
        Button Continua = findViewById(R.id.ContinuarCadastro);
        Button Cancela = findViewById(R.id.CancelarCadastro);

        Continua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cont = new Intent(getApplicationContext(), CadastroParte2.class);
                startActivity(cont);
            }
        });

        Cancela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent canc = new Intent(getApplicationContext(), MenuFuncionario.class);
                startActivity(canc);
            }
        });


    }
}