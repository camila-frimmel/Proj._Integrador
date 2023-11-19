package com.internet.frimmel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
    private FirebaseFirestore db;

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
        db = FirebaseFirestore.getInstance();

        db.collection("agenda")
                .orderBy("Data", Query.Direction.ASCENDING) // Substitua "Data" pelo nome do seu campo de data
                .orderBy("Horário", Query.Direction.ASCENDING) // Substitua "Horário" pelo nome do seu campo de horário
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Firestore", "Dados recuperados com sucesso");
                        funcionariosList.clear();

                        // Iterar sobre os documentos do Firestore e adicionar à lista
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String horarioFuncionario = document.getString("Horário");
                            String dataFuncionario = document.getString("Data");
                            String Data = dataFuncionario;
                            String horario = horarioFuncionario;
                            funcionariosList.add(Data);
                            funcionariosList.add(horario);

                        }

                        // Notificar o adaptador sobre as mudanças
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("Firestore", "Erro ao recuperar dados", task.getException());
                        Toast.makeText(ManutençãoFuncionario.this, "ERRO!", Toast.LENGTH_SHORT).show();
                    }
                });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            // Obter o item clicado da lista
            String itemSelecionado = funcionariosList.get(position);

            // Criar uma Intent para iniciar a nova atividade (substitua "NovaAtividade.class" pelo nome da sua nova atividade)
            Intent intent = new Intent(ManutençãoFuncionario.this, ManutencaoFunc2.class);


            // Iniciar a nova atividade
            startActivity(intent);
        });


    }
}
