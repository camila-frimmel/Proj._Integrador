package com.internet.frimmel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CadastroCliente extends AppCompatActivity {

    private FirebaseFirestore db;
    private EditText CadastroNome;
    private EditText CadastroCPF;
    private EditText CadastroEmail;
    private EditText CadastroTelefone;
    private Button Continua;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrocliente);
        Button Continua = findViewById(R.id.ConfirmarCadastro);
        Button Cancela = findViewById(R.id.CancelarCadastro);

        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();


        Continua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent cont = new Intent(getApplicationContext(), CadastroParte2.class);
                startActivity(cont);*/
                String nome = CadastroNome.getText().toString();
                String cpf = CadastroCPF.getText().toString();
                String email = CadastroEmail.getText().toString();
                String telefone = CadastroTelefone.getText().toString();

                salvarDadosNoFirestore (nome, cpf, email, telefone);

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

    private void salvarDadosNoFirestore(String nome, String cpf, String email, String telefone) {
        // Crie um novo documento na coleção "cliente" com os dados inseridos
        db.collection("cliente")
                .add(new DadosCliente(nome, cpf, email, telefone))
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // Sucesso: O documento foi criado com êxito
                        String novoDocumentoId = documentReference.getId();
                        // Você pode fazer algo com o novoDocumentoId, se necessário
                        Intent cont = new Intent(getApplicationContext(), CadastroParte2.class);
                        startActivity(cont);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Lidar com erros
                    }
                });
    }
}
