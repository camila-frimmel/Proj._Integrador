package com.internet.frimmel;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class RemoveCadastro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_removecad);
       Button Remove = findViewById(R.id.ConfirmaRemove);

       Remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopRemove popupDialog = new PopRemove();
                popupDialog.show(getSupportFragmentManager(), "popup_dialog");
            }
        });
    }
}