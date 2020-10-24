package ru.hardwork.onlinesocialdiagnosticapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ru.hardwork.onlinesocialdiagnosticapp.common.Common;
import ru.hardwork.onlinesocialdiagnosticapp.holders.QuestionViewHolder;
import ru.hardwork.onlinesocialdiagnosticapp.model.diagnostic.Question;
import ru.hardwork.onlinesocialdiagnosticapp.scenery.VerticalSpaceItemDecoration;

public class DiagnosticRV extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference questions;
    // Адаптер для загрузки категорий в CategoryViewHolder
    FirebaseRecyclerAdapter<Question, QuestionViewHolder> adapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostic_rv);

        database = FirebaseDatabase.getInstance();
        questions = database.getReference("Questions");

        mRecyclerView = findViewById(R.id.questionRecycler);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(
                this.getBaseContext(),
                LinearLayoutManager.VERTICAL,
                false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration());

        loadQuestions(Common.diagnosticId);
    }

    private void loadQuestions(long diagnosticId) {
        // Options for recycler view by Firebase
        FirebaseRecyclerOptions<Question> options = new FirebaseRecyclerOptions
                .Builder<Question>()
                .setQuery(questions.orderByChild("diagnosticId").equalTo(diagnosticId), Question.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<Question, QuestionViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull QuestionViewHolder holder, int position, @NonNull Question question) {
                holder.questionText.setText(question.getText());
            }

            @NonNull
            @Override
            public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(DiagnosticRV.this);
                View view = inflater.inflate(R.layout.item_question, parent, false);
                return new QuestionViewHolder(view);
            }
        };

        adapter.notifyDataSetChanged();
        adapter.startListening();
        mRecyclerView.setAdapter(adapter);
    }
}