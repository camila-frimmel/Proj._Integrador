package com.internet.frimmel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CadastroCliente extends AppCompatActivity {

    private FirebaseFirestore db;
    private EditText CadastroNome;
    private EditText CadastroCPF;
    private EditText CadastroEmail;
    private EditText CadastroTelefone;
    private EditText CadastroCEP;
    private EditText CadastroEndereço;
    private EditText CadastroPlano;
    private EditText CadastroSenha;
    private Button Salvar;
    private FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrocliente);
        Button Salvar = findViewById(R.id.ConfirmarCadastro);
        Button Cancela = findViewById(R.id.CancelarCadastro);

        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        CadastroNome = findViewById(R.id.CadastroNome);
        CadastroCPF = findViewById(R.id.CadastroCPF);
        CadastroEmail = findViewById(R.id.CadastroEmail);
        CadastroTelefone = findViewById(R.id.CadastroTelefone);
        CadastroCEP = findViewById(R.id.CadastroCEP);
        CadastroEndereço = findViewById(R.id.CadastroEndereço);
        CadastroPlano = findViewById(R.id.CadastroPlano);
        CadastroSenha = findViewById(R.id.CadastroSenha);

        Salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent cont = new Intent(getApplicationContext(), CadastroParte2.class);
                startActivity(cont);*/
                String nome = CadastroNome.getText().toString();
                String cpf = CadastroCPF.getText().toString();
                String email = CadastroEmail.getText().toString();
                String telefone = CadastroTelefone.getText().toString();
                String cep = CadastroCEP.getText().toString();
                String endereco = CadastroEndereço.getText().toString();
                String plano = CadastroPlano.getText().toString();
                String senha = CadastroSenha.getText().toString();

                validaDados(email, senha);
                salvarDadosNoFirestore (nome, cpf, email, telefone, cep, endereco, plano, senha);

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

    private void validaDados(String email, String senha) {

        if (!email.isEmpty()) {
            if (!senha.isEmpty()) {
                CriacontaFirebase(email, senha);
            } else {
                Toast.makeText(this, "Insira a senha", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Insira o email", Toast.LENGTH_SHORT).show();
        }
    }

    private void CriacontaFirebase(String email, String senha){
        mAuth.createUserWithEmailAndPassword(
                email, senha
        ).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                finish();
            }else {
                Toast.makeText(this,"ERRO!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void salvarDadosNoFirestore(String nome, String cpf, String email, String telefone, String cep, String endereco, String plano, String senha) {
        // Crie um novo documento na coleção "cliente" com os dados inseridos
        db.collection("cliente")
                .add(new DadosCliente(nome, cpf, email, telefone, cep, endereco, senha, plano))
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // Sucesso: O documento foi criado com êxito
                        String novoDocumentoId = documentReference.getId();
                        // Você pode fazer algo com o novoDocumentoId, se necessário
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
