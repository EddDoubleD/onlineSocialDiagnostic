package ru.hardwork.onlinesocialdiagnosticapp.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
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

import ru.hardwork.onlinesocialdiagnosticapp.R;
import ru.hardwork.onlinesocialdiagnosticapp.application.OnlineSocialDiagnosticApp;
import ru.hardwork.onlinesocialdiagnosticapp.common.Common;
import ru.hardwork.onlinesocialdiagnosticapp.common.lite.DiagnosticContract;
import ru.hardwork.onlinesocialdiagnosticapp.factory.DecryptionViewModelFactory;
import ru.hardwork.onlinesocialdiagnosticapp.factory.DescriptionViewModel;
import ru.hardwork.onlinesocialdiagnosticapp.holders.DescriptionViewHolder;
import ru.hardwork.onlinesocialdiagnosticapp.model.diagnostic.Decryption;
import ru.hardwork.onlinesocialdiagnosticapp.model.diagnostic.DiagnosticTest;

import static java.lang.String.format;
import static ru.hardwork.onlinesocialdiagnosticapp.common.lite.DiagnosticContract.DiagnosticEntry.RESULT_TABLE;

public class DoneActivity extends AppCompatActivity {

    private static final String BASE_FORMAT = "yyyy.MM.dd HH:mm";
    private static final String RESULT = "RESULT";
    private static final String HTML = "<p><a href=\"%s\">Расшифровка теста</a></p>";

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat(BASE_FORMAT);

    private Decryption decryption;
    private Button btnTryAgain;
    private TextView resultText;
    private RecyclerView mRecyclerView;

    private boolean fromDiagnostic;

    @SuppressLint({"DefaultLocale", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Устанавливаем контент
        setContentView(R.layout.activity_done);
        // Получаем ссылку на экземляр контекста приложения
        OnlineSocialDiagnosticApp application = OnlineSocialDiagnosticApp.getInstance();
        // Получение реузультатов тестирования
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }

        resultText = findViewById(R.id.result);
        mRecyclerView = findViewById(R.id.descriptionRecycler);
        // magic
        LinearLayoutManager manager = new LinearLayoutManager(DoneActivity.this);
        mRecyclerView.setLayoutManager(manager);

        btnTryAgain = findViewById(R.id.btnTryAgain);
        btnTryAgain.setOnClickListener(view -> {
            Intent intent = new Intent(DoneActivity.this, HomeActivity.class);
            Bundle dataSend = new Bundle();
            dataSend.putInt("MENU_POSITION", 1);
            startActivity(intent);
            finish();
        });

        DiagnosticTest diagnostic = (DiagnosticTest) extras.getSerializable("DIAGNOSTIC");
        ArrayList<Integer> result = extras.getIntegerArrayList(RESULT);
        decryption = application.getDataManager().getDecryption().get(diagnostic.getMetricId());

        fromDiagnostic = extras.getBoolean("FROM_DIAGNOSTIC", false);
        if (fromDiagnostic) {
            Date date = new Date();
            // SQLite
            SQLiteDatabase db = application.getDbHelper().getWritableDatabase();
            ContentValues userResult = new ContentValues();
            userResult.put(DiagnosticContract.DiagnosticEntry.EMAIL, Common.currentUser.getLogIn());
            userResult.put(DiagnosticContract.DiagnosticEntry.DIAGNOSTIC_ID, diagnostic.getId());
            userResult.put(DiagnosticContract.DiagnosticEntry.RESULT, StringUtils.join(result));
            userResult.put(DiagnosticContract.DiagnosticEntry.DATE_PASSED, FORMAT.format(date));
            db.insert(RESULT_TABLE, null, userResult);
        }
        // Ссылка на расшифровку
        resultText.setText(Html.fromHtml(format(HTML, decryption.getUrl())));
        resultText.setTextColor(R.color.plaintText);
        resultText.setLinksClickable(true);
        resultText.setMovementMethod(LinkMovementMethod.getInstance());
        //
        DecryptionViewModelFactory factory = new DecryptionViewModelFactory(result, decryption);
        List<DescriptionViewModel> desc = factory.build();
        DecryptionAdapter adapter = new DecryptionAdapter(desc);
        mRecyclerView.setAdapter(adapter);
    }

    class DecryptionAdapter extends RecyclerView.Adapter<DescriptionViewHolder> {

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
        public void onBindViewHolder(@NonNull DescriptionViewHolder holder, final int position) {

            DescriptionViewModel model = models.get(position);
            holder.descriptionName.setText(format("%s  %d/%d", model.getName(), model.getCurrent(), model.getMax()));
            holder.descriptionProgress.setMax(model.getMax());
            holder.descriptionProgress.setProgress(model.getCurrent(), true);

            holder.descriptionName.setOnClickListener(e -> {
                DescriptionViewModel m = models.get(position);
                if (m.getDescription() != null) {
                    showDescriptionDialog(m);
                }
            });
        }

        @Override
        public int getItemCount() {
            return models.size();
        }
    }

    private void showDescriptionDialog(DescriptionViewModel model) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DoneActivity.this);
        alertDialog.setTitle(model.getName());
        alertDialog.setMessage(model.getDescription());

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.description_details_layout, null);
        alertDialog.setView(view);
        alertDialog.setIcon(R.drawable.ic_baseline_account_circle_24);

        alertDialog.setPositiveButton("ОК", (dialogInterface, i) -> {
            dialogInterface.dismiss();
        });
        alertDialog.show();

    }
}

