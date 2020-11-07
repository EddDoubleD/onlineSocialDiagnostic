package ru.hardwork.onlinesocialdiagnosticapp.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.base.Function;
import com.google.common.base.Splitter;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import ru.hardwork.onlinesocialdiagnosticapp.R;
import ru.hardwork.onlinesocialdiagnosticapp.application.OnlineSocialDiagnosticApp;
import ru.hardwork.onlinesocialdiagnosticapp.common.Common;
import ru.hardwork.onlinesocialdiagnosticapp.holders.UserResultViewHolder;
import ru.hardwork.onlinesocialdiagnosticapp.model.diagnostic.DiagnosticTest;
import ru.hardwork.onlinesocialdiagnosticapp.model.user.UserResult;
import ru.hardwork.onlinesocialdiagnosticapp.scenery.VerticalSpaceItemDecoration;
import ru.hardwork.onlinesocialdiagnosticapp.ui.activity.Done;

import static ru.hardwork.onlinesocialdiagnosticapp.common.lite.DiagnosticContract.DiagnosticEntry.DATE_PASSED;
import static ru.hardwork.onlinesocialdiagnosticapp.common.lite.DiagnosticContract.DiagnosticEntry.DIAGNOSTIC_ID;
import static ru.hardwork.onlinesocialdiagnosticapp.common.lite.DiagnosticContract.DiagnosticEntry.EMAIL;
import static ru.hardwork.onlinesocialdiagnosticapp.common.lite.DiagnosticContract.DiagnosticEntry.RESULT;
import static ru.hardwork.onlinesocialdiagnosticapp.common.lite.DiagnosticContract.DiagnosticEntry.RESULT_TABLE;


public class ResultFragment extends Fragment {

    private static final String BASE_FORMAT = "yyyy.MM.dd HH:mm";
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat DATA_FORMAT = new SimpleDateFormat(BASE_FORMAT);
    View resultFragment;
    private RecyclerView mRecyclerView;
    private boolean isListGoingUp = true;

    private HashMap<Integer, DiagnosticTest> localCache = new HashMap<>();
    private List<UserResult> userResults = new ArrayList<>();

    public static ResultFragment newInstance() {
        ResultFragment resultFragment = new ResultFragment();
        return resultFragment;
    }


    public ResultFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        resultFragment = inflater.inflate(R.layout.fragment_result, container, false);

        userResults.clear();

        OnlineSocialDiagnosticApp app = OnlineSocialDiagnosticApp.getInstance();
        SQLiteDatabase db = app.getDbHelper().getReadableDatabase();
        String[] projection = {
                EMAIL,
                DIAGNOSTIC_ID,
                RESULT,
                DATE_PASSED
        };
        String selection = EMAIL + " = ?";
        String[] selectionArgs = {Common.currentUser.getLogIn()};

        Cursor cursor = db.query(RESULT_TABLE, projection, selection, selectionArgs, null, null, DIAGNOSTIC_ID);
        List<UserResult> results = f.apply(cursor);
        Collections.sort(results, (left, right) -> {
            long d = left.getDate().getTime() - right.getDate().getTime();
            return d == 0 ? 0 : d > 0 ? -1 : 1;
        });
        userResults.addAll(results);
        mRecyclerView = resultFragment.findViewById(R.id.userResultRecycler);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(
                getContext(),
                LinearLayoutManager.VERTICAL,
                false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration());
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    if (isListGoingUp) {
                        if (mLayoutManager.findLastCompletelyVisibleItemPosition() + 1 == 0) {
                            Handler handler = new Handler();
                            handler.postDelayed(() -> {
                            }, 50);
                        }
                    }
                }
            }
        });

        UserResultViewAdapter adapter = new UserResultViewAdapter();
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.scheduleLayoutAnimation();

        return resultFragment;
    }

    public class UserResultViewAdapter extends RecyclerView.Adapter<UserResultViewHolder> {

        @NonNull
        @Override
        public UserResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.item_user_result, parent, false);
            return new UserResultViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull UserResultViewHolder holder, int position) {
            UserResult model = userResults.get(position);
            final DiagnosticTest diagnostic = loadDiagnosticById(model.getDiagnosticId());
            if (diagnostic == null) {
                return;
            }

            final int color = diagnostic.getId() % 5;
            @SuppressLint("UseCompatLoadingForDrawables")
            Drawable shape = getActivity().getDrawable(Common.shapes[color]);
            holder.resultLine.setBackground(shape);
            holder.diagnosticName.setText(diagnostic.getName());
            holder.diagnosticDate.setText(DATA_FORMAT.format(model.getDate()));

            holder.userCardView.setOnClickListener(view -> {
                Intent done = new Intent(getContext(), Done.class);
                Bundle dataSend = new Bundle();
                dataSend.putIntegerArrayList("RESULT", convertStr2IntFunc.apply(model.getResult()));
                dataSend.putSerializable("DIAGNOSTIC", diagnostic);
                done.putExtras(dataSend);
                startActivity(done);
            });
        }

        @Override
        public int getItemCount() {
            return userResults.size();
        }
    }

    private DiagnosticTest loadDiagnosticById(int id) {
        if (localCache.containsKey(id)) {
            return localCache.get(id);
        }

        for (DiagnosticTest diagnostic : Common.diagnosticTests) {
            if (diagnostic.getId() == id) {
                localCache.put(id, diagnostic);
                return diagnostic;
            }
        }

        return null;
    }

    private static final ConvertResultFunction f = new ConvertResultFunction();

    private static class ConvertResultFunction implements Function<Cursor, List<UserResult>> {

        @Nullable
        @Override
        public List<UserResult> apply(@Nullable Cursor cursor) {
            List<UserResult> results = new ArrayList<>();
            while (cursor != null && cursor.moveToNext()) {
                if (cursor.getInt(1) == 0) {
                    continue; // crutch
                }
                UserResult result = new UserResult();
                result.setUser(cursor.getString(0));
                result.setDiagnosticId(cursor.getInt(1));
                result.setResult(cursor.getString(2).replace("[", "").replace("]", ""));
                try {
                    result.setDate(DATA_FORMAT.parse(cursor.getString(3)));
                } catch (ParseException e) {
                    e.printStackTrace();
                } finally {
                    results.add(result);
                }
            }

            return results;
        }
    }


    private static ConvertString2IntArr convertStr2IntFunc = new ConvertString2IntArr();

    private static class ConvertString2IntArr implements Function<String, ArrayList<Integer>> {

        @Nullable
        @Override
        public ArrayList<Integer> apply(@Nullable String input) {
            ArrayList<Integer> result = new ArrayList<>();
            if (input == null) {
                return result;
            }

            for (String s : Splitter.on(",").trimResults().splitToList(input)) {
                result.add(Integer.parseInt(s));
            }
            return result;
        }
    }

}