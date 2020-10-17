package ru.hardwork.onlinesocialdiagnosticapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ru.hardwork.onlinesocialdiagnosticapp.Model.Question;
import ru.hardwork.onlinesocialdiagnosticapp.common.Common;

public class Start extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference questions;

    TextView categoryName, diagnosticName, diagnosticDescription;
    Button btnPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);

        database = FirebaseDatabase.getInstance();
        questions = database.getReference("Questions");

        Bundle extras = getIntent().getExtras();
        String catName = extras.getString("CAT_NAME", "Категория");
        categoryName = findViewById(R.id.category_start_name);
        if (categoryName != null) {
            categoryName.setText(catName);
        }

        String name = extras.getString("DIAGNOSTIC_NAME", "Название теста");
        diagnosticName = findViewById(R.id.diagnostic_start_name);
        if (diagnosticName != null) {
            diagnosticName.setText(name);
        }

        String description = extras.getString("DIAGNOSTIC_DESC", "Описание");
        diagnosticDescription = findViewById(R.id.diagnostic_start_description);
        if (diagnosticDescription != null) {
            diagnosticDescription.setText(description);
        }

        loadQuestions(Common.diagnosticId); // Идентификатор диагностики

        btnPlay = findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(view -> {
            Intent intent = new Intent(Start.this, Diagnostic.class);
            //Intent intent = new Intent(Start.this, DiagnosticRV.class);
            startActivity(intent);
            finish();
        });

    }

    private void loadQuestions(long diagnosticId) {
        if (Common.questions.size() > 0) {
            Common.questions.clear();
        }

        questions.orderByChild("diagnosticId").equalTo(diagnosticId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            Question question = postSnapshot.getValue(Question.class);
                            Common.questions.add(question);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}