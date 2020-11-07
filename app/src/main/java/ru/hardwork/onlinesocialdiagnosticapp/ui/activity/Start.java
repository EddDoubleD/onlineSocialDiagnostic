package ru.hardwork.onlinesocialdiagnosticapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.hardwork.onlinesocialdiagnosticapp.R;
import ru.hardwork.onlinesocialdiagnosticapp.common.Common;
import ru.hardwork.onlinesocialdiagnosticapp.common.JSONResourceReader;
import ru.hardwork.onlinesocialdiagnosticapp.common.UIDataRouter;
import ru.hardwork.onlinesocialdiagnosticapp.model.diagnostic.DiagnosticTest;
import ru.hardwork.onlinesocialdiagnosticapp.model.diagnostic.Question;

public class Start extends AppCompatActivity {

    private DiagnosticTest diagnostic;
    public List<Question> questions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        diagnostic = (DiagnosticTest) extras.getSerializable("DIAGNOSTIC");
        // Загружаем диагностику
        if (diagnostic == null) {
            diagnostic = new DiagnosticTest();
            diagnostic.setId(0);
        }

        loadQuestions(diagnostic.getId());

        String catName = extras.getString("CAT_NAME", "Категория");
        TextView categoryName = findViewById(R.id.category_start_name);
        if (categoryName != null) {
            categoryName.setText(catName);
        }

        TextView diagnosticName = findViewById(R.id.diagnostic_start_name);
        if (diagnosticName != null) {
            diagnosticName.setText(diagnostic.getName());
        }

        TextView diagnosticDescription = findViewById(R.id.diagnostic_start_description);
        if (diagnosticDescription != null) {
            diagnosticDescription.setText(diagnostic.getFullDescription());
        }

        Button btnPlay = findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(view -> {
            //Intent intent = new Intent(Start.this, DiagnosticRV.class);
            Intent diagnosticIntent = new Intent(Start.this, Diagnostic.class);
            Bundle dataSend = new Bundle();
            dataSend.putSerializable("DIAGNOSTIC", diagnostic);
            diagnosticIntent.putExtras(dataSend);
            startActivity(diagnosticIntent);
            finish();
        });

    }

    private void loadQuestions(int diagnosticId) {
        if (Common.questions.size() > 0) {
            Common.questions.clear();
        }

        JSONResourceReader resourceReader = new JSONResourceReader(getResources(), UIDataRouter.getResourceOrDefault(diagnosticId));
        Common.questions.addAll(Arrays.asList(resourceReader.constructUsingGson(Question[].class)));
    }
}