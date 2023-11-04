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
    private TextView DataTextView;
    private TextView textMotivo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manufunc2);

        db = FirebaseFirestore.getInstance();

        // Referências para os elementos de exibição em seu layout
        textHorario = findViewById(R.id.textHorario);
        //DataTextView = findViewById(R.id.dataTextView);
        textMotivo = findViewById(R.id.textMotivo);

        // Realizar a leitura dos dados do Firestore
        db.collection("agenda")
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
                            //dataTextView.setText("Data: " + data);
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
}
