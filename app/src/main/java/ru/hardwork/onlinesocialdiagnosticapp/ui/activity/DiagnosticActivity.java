package ru.hardwork.onlinesocialdiagnosticapp.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import ru.hardwork.onlinesocialdiagnosticapp.R;
import ru.hardwork.onlinesocialdiagnosticapp.common.Common;
import ru.hardwork.onlinesocialdiagnosticapp.model.diagnostic.DiagnosticTest;
import ru.hardwork.onlinesocialdiagnosticapp.model.diagnostic.Question;

import static java.lang.String.format;

public class DiagnosticActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String RESULT = "RESULT";
    private static final String QUESTION_NUM_MASK = "%d/%d";

    int index = 0, totalQuestion;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.txtTotalQuestion)
    TextView txtQuestionNum;
    @BindView(R.id.question_text)
    TextView questionText;
    @BindViews({R.id.firstButton, R.id.secondButton, R.id.thirdButton, R.id.fourthButton, R.id.fifthButton, R.id.sixButton})
    List<Button> buttons;

    private List<String> options = new ArrayList<>();

    private DiagnosticTest diagnostic;
    private String inviteUid;
    private ArrayList<Integer> result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostic);
        ButterKnife.bind(this);

        for (Button button : buttons) {
            button.setOnClickListener(this);
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            diagnostic = (DiagnosticTest) extras.getSerializable("DIAGNOSTIC");
            inviteUid = extras.getString("INVITE");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("DefaultLocale")
    @Override
    public void onClick(View view) {
        if (index < totalQuestion) {
            Button clicked = (Button) view;
            result.add(options.indexOf(clicked.getText().toString()));
            showQuestion(++index);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("DefaultLocale")
    private void showQuestion(int index) {
        if (index < totalQuestion) {
            txtQuestionNum.setText(format(QUESTION_NUM_MASK, index, totalQuestion));
            progressBar.setProgress(index, true);

            Question question = Common.questions.get(index);
            questionText.setVisibility(View.VISIBLE);
            questionText.setText(question.getText());

            options = question.getType();

            for (int i = 0; i < buttons.size(); i++) {
                if (options.size() > i) {
                    buttons.get(i).setText(options.get(i));
                } else {
                    buttons.get(i).setVisibility(View.INVISIBLE);
                }
            }

        } else {
            Intent done = new Intent(this, DoneActivity.class);
            Bundle dataSend = new Bundle();
            dataSend.putIntegerArrayList(RESULT, result);
            dataSend.putSerializable("DIAGNOSTIC", diagnostic);
            dataSend.putBoolean("FROM_DIAGNOSTIC", true);
            dataSend.putString("INVITE", inviteUid);
            done.putExtras(dataSend);
            startActivity(done);
            finish();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onResume() {
        super.onResume();
        totalQuestion = Common.questions.size();
        progressBar.setMax(totalQuestion);
        result = new ArrayList<>();
        showQuestion(index);
    }
}