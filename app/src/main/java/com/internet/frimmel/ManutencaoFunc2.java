package com.internet.frimmel;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ManutencaoFunc2 extends AppCompatActivity {

    private FirebaseFirestore db;
    private TextView textHorario;
    private TextView textData;
    private TextView textMotivo;
    private TextView textNome;
    private TextView textEndereco;
    private TextView textFhone;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manufunc2);

        db = FirebaseFirestore.getInstance();

        // Referências para os elementos de exibição em seu layout
        textHorario = findViewById(R.id.textHorario);
        textData = findViewById(R.id.textData);
        textMotivo = findViewById(R.id.textMotivo);
        textNome = findViewById(R.id.textNome);
        textEndereco = findViewById(R.id.textEndereco);
        textFhone = findViewById(R.id.textFhone);

        readDataFromCollection("agenda");
        readDataFromAnotherCollection("cliente");
    }
        private void readDataFromCollection (String agenda) {
        db.collection(agenda)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // Assumindo que você deseja exibir apenas o primeiro documento encontrado
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                            String horario = document.getString("horario");
                            String data = document.getString("data");
                            String obs = document.getString("obs");

                            // Exibir os dados nos TextViews
                            textHorario.setText("Horário: " + horario);
                            textData.setText("Data: " + data);
                            textMotivo.setText("Observação: " + obs);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ManutencaoFunc2.this, "ERRO!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void readDataFromAnotherCollection (String cliente) {
        db.collection(cliente)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // Assumindo que você deseja exibir apenas o primeiro documento encontrado
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                            String nome = document.getString("Nome");
                            String endereco = document.getString("Endereço");
                            String telefone = document.getString("Telefone");

                            // Exibir os dados nos TextViews
                            textNome.setText("Nome: " + nome);
                            textEndereco.setText("Endereço: " + endereco);
                            textFhone.setText("Telefone: " + telefone);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ManutencaoFunc2.this, "ERRO!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
