package ru.hardwork.onlinesocialdiagnosticapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.hardwork.onlinesocialdiagnosticapp.R;
import ru.hardwork.onlinesocialdiagnosticapp.common.Common;
import ru.hardwork.onlinesocialdiagnosticapp.common.JSONResourceReader;
import ru.hardwork.onlinesocialdiagnosticapp.common.UIDataRouter;
import ru.hardwork.onlinesocialdiagnosticapp.model.diagnostic.DiagnosticTest;
import ru.hardwork.onlinesocialdiagnosticapp.model.diagnostic.Question;

public class StartActivity extends AppCompatActivity {

    @BindView(R.id.category_start_name)
    TextView categoryName;
    @BindView(R.id.diagnostic_start_name)
    TextView diagnosticName;
    @BindView(R.id.diagnostic_start_description)
    TextView diagnosticDescription;
    @BindView(R.id.btnPlay)
    Button btnPlay;

    private DiagnosticTest diagnostic;
    private String inviteUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);

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
        inviteUid = extras.getString("INVITE", "");
        categoryName.setText(catName);
        diagnosticName.setText(diagnostic.getName());
        diagnosticDescription.setText(diagnostic.getFullDescription());

    }

    @OnClick(R.id.btnPlay)
    public void play() {
        Intent diagnosticIntent = new Intent(StartActivity.this, DiagnosticActivity.class);
        Bundle dataSend = new Bundle();
        dataSend.putSerializable("DIAGNOSTIC", diagnostic);
        dataSend.putString("INVITE", inviteUid);
        diagnosticIntent.putExtras(dataSend);
        startActivity(diagnosticIntent);
        finish();
    }

    private void loadQuestions(int diagnosticId) {
        if (Common.questions.size() > 0) {
            Common.questions.clear();
        }

        JSONResourceReader resourceReader = new JSONResourceReader(getResources(), UIDataRouter.getResourceOrDefault(diagnosticId));
        Common.questions.addAll(Arrays.asList(resourceReader.constructUsingGson(Question[].class)));
    }
}