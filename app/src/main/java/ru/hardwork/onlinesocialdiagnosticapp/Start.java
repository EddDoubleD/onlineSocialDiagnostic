package ru.hardwork.onlinesocialdiagnosticapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

import ru.hardwork.onlinesocialdiagnosticapp.common.Common;
import ru.hardwork.onlinesocialdiagnosticapp.common.JSONResourceReader;
import ru.hardwork.onlinesocialdiagnosticapp.common.UIDataRouter;
import ru.hardwork.onlinesocialdiagnosticapp.model.diagnostic.Question;

public class Start extends AppCompatActivity {

    TextView categoryName, diagnosticName, diagnosticDescription;
    Button btnPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);

        Bundle extras = getIntent().getExtras();
        int diagnosticId = extras.getInt("DIAGNOSTIC_ID", 0);
        // Загружаем диагностику
        loadQuestions(diagnosticId == 0 ? 0 : diagnosticId - 1);

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
            diagnosticDescription.setText(Common.diagnosticTests.get(diagnosticId - 1).getFullDescription());
        }


        btnPlay = findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(view -> {
            Intent intent = new Intent(Start.this, Diagnostic.class);
            //Intent intent = new Intent(Start.this, DiagnosticRV.class);
            startActivity(intent);
            finish();
        });

    }

    private void loadQuestions(int diagnosticId) {
        if (Common.questions.size() > 0) {
            Common.questions.clear();
        }
        JSONResourceReader resourceReader = new JSONResourceReader(getResources(), UIDataRouter.questions.get(diagnosticId));
        Common.questions.addAll(Arrays.asList(resourceReader.constructUsingGson(Question[].class)));
    }
}