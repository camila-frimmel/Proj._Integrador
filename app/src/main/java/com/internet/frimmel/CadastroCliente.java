package com.internet.frimmel;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CadastroCliente extends AppCompatActivity {

    private FirebaseFirestore db;
    private EditText CadastroNome;
    private EditText CadastroCPF;
    private EditText CadastroEmail;
    private EditText CadastroTelefone;
    private EditText CadastroCEP;
    private EditText CadastroEndereço;
    private Spinner CadastroPlano;
    private EditText CadastroSenha;
    private Button Salvar;
    private FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrocliente);
        Salvar = findViewById(R.id.ConfirmarCadastro);
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
        CadastroPlano = (Spinner) findViewById(R.id.CadastroPlano);
        CadastroSenha = findViewById(R.id.CadastroSenha);

        /*CollectionReference opPlanos = db.collection("planos");
        List<String> lstaplanos = new ArrayList<>();

        opPlanos.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String Plano1 = document.getString("Plano 1");
                            lstaplanos.add(Plano1);
                            String Plano2 = document.getString("Plano 2");
                            lstaplanos.add(Plano2);
                            String Plano3 = document.getString("Plano 3");
                            lstaplanos.add(Plano3);
                        }

                        // Crie um ArrayAdapter com as opções recuperadas e configure o Spinner
                        @SuppressLint("ResourceType") ArrayAdapter<String> planoAdapter = new ArrayAdapter<>(getApplicationContext(),R.array.SpiPlano, android.R.layout.simple_spinner_item, lstaplanos);
                        planoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        CadastroPlano.setAdapter(planoAdapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firestore", "Erro ao recuperar opções de plano do Firestore", e);
                    }
                });*/

        Salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = CadastroNome.getText().toString();
                String cpf = CadastroCPF.getText().toString();
                String email = CadastroEmail.getText().toString();
                String telefone = CadastroTelefone.getText().toString();
                String cep = CadastroCEP.getText().toString();
                String endereco = CadastroEndereço.getText().toString();
                String plano = CadastroPlano.getSelectedItem().toString();
                String senha = CadastroSenha.getText().toString();

                if (validaCampos(nome, cpf, email, telefone, cep, endereco, plano, senha)) {
                    CriacontaFirebase(email, senha);
                    salvarDadosNoFirestore(nome, cpf, email, telefone, cep, endereco, plano, senha);
                }
            }
        });

        Cancela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupCad popupDialog = new PopupCad();
                popupDialog.show(getSupportFragmentManager(), "popup_dialog");
            }
        });
    }

    private boolean validaCampos(String nome, String cpf, String email, String telefone, String cep, String endereco, String plano, String senha) {
        if (nome.isEmpty() || cpf.isEmpty() || email.isEmpty() || telefone.isEmpty() || cep.isEmpty() || endereco.isEmpty() || plano.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!email.contains("@")) {
            Toast.makeText(this, "Email inválido", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void CriacontaFirebase(String email, String senha) {
        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Conta criada com sucesso", Toast.LENGTH_SHORT).show();
                        // Redirecione o usuário ou realize ação após o sucesso
                    } else {
                        Toast.makeText(this, "Erro ao criar conta", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void salvarDadosNoFirestore(String nome, String cpf, String email, String telefone, String cep, String endereco, String plano, String senha) {
        Map<String, Object> dados = new HashMap<>();
        dados.put("Nome", nome);
        dados.put("CPF", cpf);
        dados.put("Email", email);
        dados.put("Telefone", telefone);
        dados.put("CEP", cep);
        dados.put("Endereço", endereco);
        dados.put("Plano", plano);
        dados.put("Senha", senha);

        db.collection("cliente")
                .add(dados)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String novoDocumentoId = documentReference.getId();
                        Toast.makeText(CadastroCliente.this, "Salvo no banco", Toast.LENGTH_SHORT).show();
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firestore", "Erro ao salvar dados no Firestore", e);
                        Toast.makeText(CadastroCliente.this, "Erro ao salvar dados no Firestore", Toast.LENGTH_SHORT).show();
                    }
                });

        try {
            String arquivo = nome + ".pdf";
            String filePath = getFilesDir() + "/" + arquivo;
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            document.add(new Paragraph("Nome: " + nome));
            document.add(new Paragraph("CPF: " + cpf));
            document.add(new Paragraph("Endereço: " + endereco));
            document.add(new Paragraph("Plano: " + plano));
            document.add(new Paragraph("Telefone: " + telefone));

            document.close();

            // Carregue o PDF no Firebase Storage
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference pdfRef = storageRef.child("/" + arquivo); // Especifique o caminho no Storage

            File file = new File(filePath);

            pdfRef.putFile(Uri.fromFile(file))
                    .addOnSuccessListener(taskSnapshot -> {
                        Toast.makeText(this, "PDF enviado para o Firebase Storage com sucesso!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Erro ao enviar o PDF para o Firebase Storage: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao criar o PDF", Toast.LENGTH_SHORT).show();
        }
    }
}
