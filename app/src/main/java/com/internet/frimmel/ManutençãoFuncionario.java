package com.internet.frimmel;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ManutençãoFuncionario extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> funcionariosList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manufuncionario);

        // Inicializar componentes
        listView = findViewById(R.id.listView);
        funcionariosList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, funcionariosList);
        listView.setAdapter(adapter);

        // Configurar o acesso ao Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("agenda")
                .orderBy("Horário", Query.Direction.ASCENDING) // Substitua "horario" pelo nome do seu campo de horário
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Limpar a lista antes de adicionar os novos dados
                        funcionariosList.clear();

                        // Iterar sobre os documentos do Firestore e adicionar à lista
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String horarioFuncionario = document.getString("Horário");
                            funcionariosList.add(horarioFuncionario);
                        }

                        // Notificar o adaptador sobre as mudanças
                        adapter.notifyDataSetChanged();
                    } else {
                        // Lidar com erros de leitura do Firestore
                    }
                });
    }
}
