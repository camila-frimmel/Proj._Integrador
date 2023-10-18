package com.internet.frimmel;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    }
}


  /*  // Suponha que você tenha uma lista de IDs de contas disponíveis
    String[] accountIds = {"ID_CONTA_1", "ID_CONTA_2", "ID_CONTA_3"};

    // Use um diálogo de seleção de conta
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
builder.setTitle("Selecione a conta");

        builder.setItems(accountIds, (dialog, which) -> {
        String selectedAccountId = accountIds[which];
        // Use o ID selecionado para acessar os dados da conta
        db.collection("cliente").document(selectedAccountId).get().addOnSuccessListener(
        documentSnapshot -> {
        if (documentSnapshot.exists()) {
        String nomeRazaoSocial = documentSnapshot.getString("nome/razao social");
        textNome.setText(nomeRazaoSocial);
        } else {
        Toast.makeText(this, "ERRO!", Toast.LENGTH_SHORT).show();
        }
        }
        );
        });

        builder.create().show();*/