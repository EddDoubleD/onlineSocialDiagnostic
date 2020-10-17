package ru.hardwork.onlinesocialdiagnosticapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;

import ru.hardwork.onlinesocialdiagnosticapp.Model.DiagnosticTest;
import ru.hardwork.onlinesocialdiagnosticapp.Model.UserResult;
import ru.hardwork.onlinesocialdiagnosticapp.common.Common;
import ru.hardwork.onlinesocialdiagnosticapp.holders.UserResultViewHolder;
import ru.hardwork.onlinesocialdiagnosticapp.listener.ItemClickListener;
import ru.hardwork.onlinesocialdiagnosticapp.scenery.VerticalSpaceItemDecoration;

import static java.lang.String.format;

public class DiagnosticResult extends AppCompatActivity {

    private static final String USER_RESULT = "UserResult";
    private static final String DEFAULT_FORMAT = "yyyy.MM.dd HH:mm";
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat DATA_FORMAT = new SimpleDateFormat(DEFAULT_FORMAT);

    View mFragment;

    FirebaseRecyclerAdapter<UserResult, UserResultViewHolder> adapter;
    FirebaseDatabase database;
    DatabaseReference userResult;
    private boolean isUserScrolling;
    private boolean isListGoingUp = true;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostic_result);

        database = FirebaseDatabase.getInstance();
        userResult = database.getReference(USER_RESULT);

        mRecyclerView = findViewById(R.id.userResultRecycler);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration());

        loadUserResult();

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    isUserScrolling = true;
                    if (isListGoingUp) {
                        if (mLayoutManager.findLastCompletelyVisibleItemPosition() + 1 == 0) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                }
                            }, 50);
                        }
                    }
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    private void loadUserResult() {
        FirebaseRecyclerOptions<UserResult> options = new FirebaseRecyclerOptions
                .Builder<UserResult>()
                .setQuery(userResult, UserResult.class)
                .build();
        // Create adapter
        adapter = new FirebaseRecyclerAdapter<UserResult, UserResultViewHolder>(options) {

            @NonNull
            @Override
            public UserResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(DiagnosticResult.this);
                View view = inflater.inflate(R.layout.item_user_result, parent, false);
                return new UserResultViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull UserResultViewHolder holder, int position, @NonNull UserResult model) {
                long diagnosticId = model.getDiagnosticId();
                final int testPosition = (int) diagnosticId - 1;
                int size = Common.diagnosticTests.size();
                if (size < testPosition) {
                    // reload
                    return;
                }
                DiagnosticTest diagnostic = Common.diagnosticTests.get(testPosition);

                holder.diagnosticName.setText(diagnostic.getName());
                holder.diagnosticDate.setText(DATA_FORMAT.format(model.getDate()));

                holder.setItemClickListener(new ItemClickListener() {
                    @SuppressLint("ShowToast")
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        String msg = format(!isLongClick ? "%d clicked" : "%d long clicked", position);
                        Context context = DiagnosticResult.this;
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    }
                });

                holder.userCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        @SuppressLint("DefaultLocale") String msg = format("%d clicked", testPosition);
                        Context context = DiagnosticResult.this;
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        adapter.startListening();
        mRecyclerView.setAdapter(adapter);
    }
}