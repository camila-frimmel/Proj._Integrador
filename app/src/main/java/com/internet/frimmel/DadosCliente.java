package com.internet.frimmel;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;

public class DadosCliente extends AppCompatActivity {

    private FirebaseFirestore db;
    private TextView textNome;
    private TextView textCPF;
    private TextView textEndereço;
    private TextView textNumero;
    private TextView textPlano;
    private TextView textMensal;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dadoscliente);

        //FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();

        textNome = findViewById(R.id.textNome);
        textCPF = findViewById(R.id.textCPF);
        textEndereço = findViewById(R.id.textEndereço);
        textPlano = findViewById(R.id.textPlano);
        textNumero = findViewById(R.id.textNumero);
        textMensal = findViewById(R.id.textMensal);


        // Obtém o email do cliente autenticado a partir do Firebase Authentication
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String email = currentUser.getEmail();

            // Agora você pode usar o email para acessar os dados do cliente
            // Certifique-se de que a coleção "cliente" tenha um campo "email" para corresponder
            // ao email do cliente no Firestore.

            db.collection("cliente")
                    .whereEqualTo("email", email)
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        if (!querySnapshot.isEmpty()) {
                            // Suponha que haja apenas um documento correspondente ao email
                            String clienteDocumentId = querySnapshot.getDocuments().get(0).getId();

                            // Use o ID do documento para acessar os dados da conta do cliente
                            db.collection("cliente").document(clienteDocumentId).get().addOnSuccessListener(
                                    documentSnapshot -> {
                                        if (documentSnapshot.exists()) {
                                            String nomeRazaoSocial = documentSnapshot.getString("Nome");
                                            textNome.setText(nomeRazaoSocial);
                                            String cpf = documentSnapshot.getString("CPF");
                                            textCPF.setText(cpf);
                                            String endereco = documentSnapshot.getString("Endereço");
                                            textEndereço.setText(endereco);
                                            String telefone = documentSnapshot.getString("Telefone");
                                            textNumero.setText(telefone);
                                            String plano = documentSnapshot.getString("Plano");
                                            textPlano.setText(plano);
                                            String mensal = documentSnapshot.getString("Mensalidade");
                                            textMensal.setText(mensal);


                                        } else {
                                            Toast.makeText(this, "ERRO!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                            );
                        } else {
                            // O email não corresponde a nenhuma conta
                            Toast.makeText(this, "Conta não encontrada", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // Não há usuário autenticado, faça o tratamento apropriado
            Toast.makeText(this, "Nenhum usuário autenticado", Toast.LENGTH_SHORT).show();
        }


        try {
            String filePath = getFilesDir() + "/cliente.pdf";
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            document.add(new Paragraph("Nome: " + textNome.getText()));
            document.add(new Paragraph("CPF: " + textCPF.getText()));
            document.add(new Paragraph("Endereço: " + textEndereço.getText()));
            document.add(new Paragraph("Plano: " + textPlano.getText()));
            document.add(new Paragraph("Telefone: " + textNumero.getText()));

            document.close();

            // Carregue o PDF no Firebase Storage
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference pdfRef = storageRef.child("gs://appfrimmel.appspot.com/cliente.pdf"); // Especifique o caminho no Storage

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