package com.internet.frimmel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CadastroParte2 extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro2);
        Button Volte = findViewById(R.id.VoltarCadastro2);

        Volte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent volta = new Intent(getApplicationContext(), CadastroCliente.class);
                startActivity(volta);
            }
        });
    }
}