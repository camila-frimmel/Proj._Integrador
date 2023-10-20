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
    private TextView textCPF;
    private TextView textEndereço;
    private TextView textPlano;

    public DadosCliente(String nome, String cpf, String email, String telefone, String cep, String endereco, String senha) {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dadoscliente);

        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();

        textNome = findViewById(R.id.textNome);
        textCPF = findViewById(R.id.textCPF);
        textEndereço = findViewById(R.id.textEndereço);
        textPlano = findViewById(R.id.textPlano);

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
                                            String plano = documentSnapshot.getString("Plano");
                                            textPlano.setText(plano);

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