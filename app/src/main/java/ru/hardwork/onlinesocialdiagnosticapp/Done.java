package ru.hardwork.onlinesocialdiagnosticapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.hardwork.onlinesocialdiagnosticapp.application.OnlineSocialDiagnosticApp;
import ru.hardwork.onlinesocialdiagnosticapp.common.Common;
import ru.hardwork.onlinesocialdiagnosticapp.common.lite.DiagnosticContract;
import ru.hardwork.onlinesocialdiagnosticapp.factory.DescriptionViewModel;
import ru.hardwork.onlinesocialdiagnosticapp.factory.IFactory;
import ru.hardwork.onlinesocialdiagnosticapp.holders.DescriptionViewHolder;
import ru.hardwork.onlinesocialdiagnosticapp.model.diagnostic.Decryption;

import static java.lang.String.format;
import static ru.hardwork.onlinesocialdiagnosticapp.common.lite.DiagnosticContract.DiagnosticEntry.RESULT_TABLE;

public class Done extends AppCompatActivity {

    private static final String BASE_FORMAT = "yyyy.MM.dd HH:mm";
    private static final String RESULT = "RESULT";
    private static final String SPLITTER = ",";
    private static final String HTML = "<p><a href=\"%s\">Расшифровка теста</a></p>";

    private int diagnosticId;

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat(BASE_FORMAT);

    Decryption decryption;
    private Button btnTryAgain;
    private TextView resultText;
    private RecyclerView mRecyclerView;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        OnlineSocialDiagnosticApp application = OnlineSocialDiagnosticApp.getInstance();
        decryption = application.getDataManager().getDecryption().get(Common.descPosition - 1);

        resultText = findViewById(R.id.result);
        mRecyclerView = findViewById(R.id.descriptionRecycler);
        // magic
        LinearLayoutManager manager = new LinearLayoutManager(Done.this);
        mRecyclerView.setLayoutManager(manager);

        btnTryAgain = findViewById(R.id.btnTryAgain);
        btnTryAgain.setOnClickListener(view -> {
            Intent intent = new Intent(Done.this, Home.class);
            startActivity(intent);
            finish();
        });
        // Получение реузультатов тестирования
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ArrayList<Integer> result = extras.getIntegerArrayList(RESULT);
            diagnosticId = extras.getInt("DIAGNOSTIC_ID");
            // Дата прохождения тестирования
            Date date = new Date();
            // SQLite
            SQLiteDatabase db = application.getDbHelper().getWritableDatabase();
            ContentValues userResult = new ContentValues();
            userResult.put(DiagnosticContract.DiagnosticEntry.EMAIL, Common.currentUser.getLogIn());
            userResult.put(DiagnosticContract.DiagnosticEntry.DIAGNOSTIC_ID, diagnosticId);
            userResult.put(DiagnosticContract.DiagnosticEntry.RESULT, StringUtils.join(result));
            userResult.put(DiagnosticContract.DiagnosticEntry.DATE_PASSED, FORMAT.format(date));
            db.insert(RESULT_TABLE, null, userResult);
            // Ссылка на расшифровку
            resultText.setText(Html.fromHtml(format(HTML, decryption.getUrl())));
            resultText.setLinksClickable(true);
            resultText.setMovementMethod(LinkMovementMethod.getInstance());
            ViewModelFactory factory = new ViewModelFactory(result, decryption);
            List<DescriptionViewModel> desc = factory.build();
            DecryptionAdapter adapter = new DecryptionAdapter(desc);
            mRecyclerView.setAdapter(adapter);
        }
    }

    static class ViewModelFactory implements IFactory<DescriptionViewModel> {

        private ArrayList<Integer> processing;
        private Decryption decryption;

        public ViewModelFactory() {

        }

        public ViewModelFactory(ArrayList<Integer> processing, Decryption decryption) {
            this.processing = processing;
            this.decryption = decryption;
        }


        public void setProcessing(ArrayList<Integer> processing) {
            this.processing = processing;
        }

        public void setDecryption(Decryption decryption) {
            this.decryption = decryption;
        }

        @Override
        public List<DescriptionViewModel> build() {
            List<DescriptionViewModel> result = new ArrayList<>();
            // Получение шкал
            for (Decryption.Accent accent : decryption.getAccents()) {
                int semi = 0;
                int max = 0;
                String[] positive = StringUtils.split(accent.getPositive(), SPLITTER);
                if (positive != null) {
                    for (String s : positive) {
                        int i = Integer.parseInt(StringUtils.trim(s)) - 1;
                        if (processing.size() <= i) {
                            Log.e("Done:", "Не найден вариант для метрики");
                            continue;
                        }

                        if (processing.get(i) == 1) {
                            semi++;
                        }
                        max++;
                    }
                }
                // Вычисляем из отрицательных ответов
                String[] negative = StringUtils.split(accent.getNegative(), SPLITTER);
                if (negative != null) {
                    for (String s : negative) {
                        int i = Integer.parseInt(StringUtils.trim(s)) - 1;
                        if (processing.size() <= i) {
                            Log.e("Done:", "Не найден вариант для метрики");
                            continue;
                        }

                        if (processing.get(i) == 0) {
                            semi++;
                        }
                        max++;
                    }
                }
                // Получаем нормализованное значение
                semi *= accent.getMultiple();
                max *= accent.getMultiple();
                DescriptionViewModel desc = new DescriptionViewModel(accent.getName(), max, semi);
                result.add(desc);
            }

            return result;
        }
    }

    static class DecryptionAdapter extends RecyclerView.Adapter<DescriptionViewHolder> {

        private List<DescriptionViewModel> models;

        public DecryptionAdapter(List<DescriptionViewModel> models) {
            this.models = models;
        }

        @NonNull
        @Override
        public DescriptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_description, parent, false);
            return new DescriptionViewHolder(v);
        }

        @SuppressLint({"NewApi", "DefaultLocale"})
        @Override
        public void onBindViewHolder(@NonNull DescriptionViewHolder holder, int position) {
            DescriptionViewModel model = models.get(position);
            holder.descriptionName.setText(format("%s  %d/%d", model.getName(), model.getCurrent(), model.getMax()));
            holder.descriptionProgress.setMax(model.getMax());
            holder.descriptionProgress.setProgress(model.getCurrent(), true);
            holder.setItemClickListener((view, position1, isLongClick) -> {
                //
            });
        }

        @Override
        public int getItemCount() {
            return models.size();
        }
    }
}

