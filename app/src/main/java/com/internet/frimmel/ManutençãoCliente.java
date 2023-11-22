package com.internet.frimmel;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class ManutençãoCliente extends AppCompatActivity {

    private FirebaseFirestore db;
    private Button Confirmar;
    private EditText Horario;
    private EditText Data;
    private EditText Obs;
    private TextView ViewEndereço;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manutencaocliente);
        Confirmar = findViewById(R.id.ConfirmarAgenda);
        Button Cancelar = findViewById(R.id.CancelarAgenda);

        //FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();

        Horario = findViewById(R.id.editHorario);
        Data = findViewById(R.id.editDia);
        Obs = findViewById(R.id.editTextTextMultiLine);
        ViewEndereço = findViewById(R.id.ViewEndereço);

        readDataFromCollection("cliente");


        Confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String horario = Horario.getText().toString();
                String data = Data.getText().toString();
                String obs = Obs.getText().toString();

                if (validaCampos(horario, data, obs)) {
                    salvarDadosNoFirestore(horario, data, obs);
                }
            }
        });

        Cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupAgenda popupDialog = new PopupAgenda();
                popupDialog.show(getSupportFragmentManager(), "popup_dialog");
            }
        });

    }

    private boolean validaCampos(String horario, String data, String obs) {
        if (horario.isEmpty() || data.isEmpty() || obs.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void salvarDadosNoFirestore(String horario, String data, String obs) {
        Map<String, Object> dados = new HashMap<>();
        dados.put("Horário", horario);
        dados.put("Data", data);
        dados.put("Obs", obs);

        db.collection("agenda")
                .add(dados)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String novoDocumentoId = documentReference.getId();
                        Toast.makeText(ManutençãoCliente.this, "Salvo no banco", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firestore", "Erro ao salvar dados no Firestore", e);
                        Toast.makeText(ManutençãoCliente.this, "Erro ao salvar dados no Firestore", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void readDataFromCollection(String cliente) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String userEmail = user.getEmail();

            // Faz a consulta no Firestore com a cláusula de filtro para o email
            db.collection("cliente")
                    .whereEqualTo("Email", userEmail)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                // Assume que há apenas um documento correspondente ao email autenticado
                                DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                                String endereco = document.getString("Endereço");

                                // Exibe o endereço na sua View (substitua ViewEndereço pelo ID correto)
                                ViewEndereço.setText("" + endereco);
                            } else {
                                // Não há documentos correspondentes ao email autenticado
                                Toast.makeText(ManutençãoCliente.this, "Nenhum cliente encontrado para o email autenticado", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ManutençãoCliente.this, "ERRO!", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // O usuário não está autenticado
            Toast.makeText(ManutençãoCliente.this, "Usuário não autenticado", Toast.LENGTH_SHORT).show();
        }
    }

}
