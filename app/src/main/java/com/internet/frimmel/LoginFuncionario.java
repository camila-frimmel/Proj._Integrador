package com.internet.frimmel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LoginFuncionario extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logfuncionario);
        Button entrarF = findViewById(R.id.entrarFuncionario);

        entrarF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Funciona = new  Intent(getApplicationContext(), MenuFuncionario.class);
                startActivity(Funciona);
            }
        });
    }
}


