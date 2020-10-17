package ru.hardwork.onlinesocialdiagnosticapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ru.hardwork.onlinesocialdiagnosticapp.Model.Question;
import ru.hardwork.onlinesocialdiagnosticapp.common.Common;

import static java.lang.String.format;

public class Diagnostic extends AppCompatActivity implements View.OnClickListener {

    final static long INTERVAL = 1000;
    final static long TIMEOUT = 7000;

    private static final String YES = "да";
    private static final String RESULT = "RESULT";
    private static final String QUESTION_NUM_MASK = "%d/%d";

    int index = 0, totalQuestion;

    ProgressBar progressBar;
    ImageView questionImage;
    Button btnYes, btnNo;
    TextView txtQuestionNum, questionText;

    private ArrayList<Integer> result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostic);
        // Views
        txtQuestionNum = findViewById(R.id.txtTotalQuestion);
        questionText = findViewById(R.id.question_text);
        questionImage = findViewById(R.id.question_image);

        progressBar = findViewById(R.id.progressBar);

        btnYes = findViewById(R.id.btnYes);
        btnYes.setOnClickListener(this);

        btnNo = findViewById(R.id.btnNo);
        btnNo.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("DefaultLocale")
    @Override
    public void onClick(View view) {
        if (index < totalQuestion) {
            Button clicked = (Button) view;
            result.add(clicked.getText().toString().equalsIgnoreCase(YES) ? 1 : 0);
            showQuestion(++index);
        } else {
            Intent done = new Intent(this, Done.class);
            Bundle dataSend = new Bundle();
            dataSend.putIntegerArrayList(RESULT, result);
            done.putExtras(dataSend);
            startActivity(done);
            finish();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("DefaultLocale")
    private void showQuestion(int index) {
        if (index < totalQuestion) {
            txtQuestionNum.setText(format(QUESTION_NUM_MASK, index, totalQuestion));
            progressBar.setProgress(index, true);

            Question question = Common.questions.get(index);
            if (question.isImageQuestion()) {
                Picasso.get()
                        .load(question.getText())
                        .centerCrop()
                        .into(questionImage);
                questionImage.setVisibility(View.VISIBLE);
                questionText.setVisibility(View.INVISIBLE);
            } else {
                questionImage.setVisibility(View.INVISIBLE);
                questionText.setVisibility(View.VISIBLE);
                questionText.setText(question.getText());
            }
        } else {
            Intent done = new Intent(this, Done.class);
            Bundle dataSend = new Bundle();
            dataSend.putIntegerArrayList(RESULT, result);
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