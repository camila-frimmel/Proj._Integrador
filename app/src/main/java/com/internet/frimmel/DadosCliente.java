package com.internet.frimmel;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

public class DadosCliente extends AppCompatActivity {

    private FirebaseFirestore db;
    private TextView textNome;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dadoscliente);

        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();

        textNome = findViewById(R.id.textNome);


        db.collection("cliente").document("jSwcXZxcNen15nJqadH4").get().addOnSuccessListener(
                documentSnapshot -> {
                    if (documentSnapshot.exists()){
                        String nomeRazaoSocial = documentSnapshot.getString("nome.razao social");
                        textNome.setText(nomeRazaoSocial);

                    } else {
                        Toast.makeText(this,"ERRO!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
