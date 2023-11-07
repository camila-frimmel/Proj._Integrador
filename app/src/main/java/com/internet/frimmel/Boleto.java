package com.internet.frimmel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class Boleto extends AppCompatActivity {

    private Button viaBoleto;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boleto);

        viaBoleto = findViewById(R.id.ViaBoleto);

        viaBoleto.setOnClickListener(view -> {
            exibirPDFDoFirebase();
        });
    }

    private void exibirPDFDoFirebase() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference pdfRef = storageRef.child("gs://appfrimmel.appspot.com/gs:/appfrimmel.appspot.com/cliente.pdf"); // Especifique o caminho do arquivo no Storage

        // Especifique o caminho local onde você deseja salvar o PDF
        File localFile = new File(getExternalFilesDir(null), "gs://appfrimmel.appspot.com/gs:/appfrimmel.appspot.com/cliente.pdf");

        pdfRef.getFile(localFile)
                .addOnSuccessListener(taskSnapshot -> {
                    // O arquivo PDF foi baixado com sucesso
                    Uri pdfUri = Uri.fromFile(localFile);

                    // Agora você pode abrir o PDF, por exemplo:
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(pdfUri, "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    try {
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(this, "Erro ao abrir o PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erro ao baixar o PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}