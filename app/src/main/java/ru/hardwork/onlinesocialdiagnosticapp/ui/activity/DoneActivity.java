package ru.hardwork.onlinesocialdiagnosticapp.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.hardwork.onlinesocialdiagnosticapp.R;
import ru.hardwork.onlinesocialdiagnosticapp.application.OnlineSocialDiagnosticApp;
import ru.hardwork.onlinesocialdiagnosticapp.common.Common;
import ru.hardwork.onlinesocialdiagnosticapp.common.lite.DiagnosticContract;
import ru.hardwork.onlinesocialdiagnosticapp.factory.DecryptionViewModelFactory;
import ru.hardwork.onlinesocialdiagnosticapp.factory.DescriptionViewModel;
import ru.hardwork.onlinesocialdiagnosticapp.model.diagnostic.Decryption;
import ru.hardwork.onlinesocialdiagnosticapp.model.diagnostic.DiagnosticTest;
import ru.hardwork.onlinesocialdiagnosticapp.model.firebase.Invite;
import ru.hardwork.onlinesocialdiagnosticapp.scenery.SpeedyLinearLayoutManager;
import ru.hardwork.onlinesocialdiagnosticapp.scenery.VerticalSpaceItemDecoration;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static ru.hardwork.onlinesocialdiagnosticapp.common.UIDataRouter.DEFAULT_USER;
import static ru.hardwork.onlinesocialdiagnosticapp.common.lite.DiagnosticContract.DiagnosticEntry.RESULT_TABLE;

public class DoneActivity extends AppCompatActivity {

    private static final String BASE_FORMAT = "yyyy.MM.dd HH:mm";
    private static final String RESULT = "RESULT";

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat(BASE_FORMAT);

    private Decryption decryption;
    private Button btnTryAgain;
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

        String uid = extras.getString("INVITE");

        mRecyclerView = findViewById(R.id.descriptionRecycler);
        final SpeedyLinearLayoutManager mLayoutManager = new SpeedyLinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration());
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
            String userName = Common.firebaseUser == null ? DEFAULT_USER : Common.firebaseUser.getEmail();
            userResult.put(DiagnosticContract.DiagnosticEntry.EMAIL, userName);
            userResult.put(DiagnosticContract.DiagnosticEntry.DIAGNOSTIC_ID, diagnostic.getId());
            userResult.put(DiagnosticContract.DiagnosticEntry.RESULT, StringUtils.join(result));
            userResult.put(DiagnosticContract.DiagnosticEntry.DATE_PASSED, FORMAT.format(date));
            db.insert(RESULT_TABLE, null, userResult);
        }

        if (isNotEmpty(uid)) {
            DatabaseReference invites = FirebaseDatabase.getInstance().getReference("invite");
            Query query = invites.orderByKey().equalTo(uid);
            MutableBoolean key = new MutableBoolean();
            key.setFalse();
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (key.getValue()) {
                        return; // The update has already happened
                    } else {
                        key.setTrue();
                    }

                    Iterator<DataSnapshot> iterator = snapshot.getChildren().iterator();
                    if (!iterator.hasNext()) {
                        Toast.makeText(DoneActivity.this, format("Не найдено приглашение с идентификатором %s", uid), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    DataSnapshot firstChild = iterator.next();
                    Invite joinInvite = firstChild.getValue(Invite.class);
                    Invite.Result inviteResult = new Invite.Result(StringUtils.join(result), new Date());

                    joinInvite.addResult(inviteResult);
                    invites.child(uid).setValue(joinInvite, (databaseError, databaseReference) -> {
                        if (databaseError != null) {
                            Toast.makeText(DoneActivity.this, format("Ошибка сохранения %s, попробуйте повторить попытку", databaseError.getMessage()), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    if (error.getCode() != 0) {
                        Toast.makeText(DoneActivity.this, format("Ошибка сохранения %s, попробуйте повторить попытку", error.getMessage()), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        //
        DecryptionViewModelFactory factory = new DecryptionViewModelFactory(result, decryption);
        List<DescriptionViewModel> desc = factory.build();
        DecryptionAdapter adapter = new DecryptionAdapter(desc);
        mRecyclerView.setAdapter(adapter);
    }

    @SuppressLint("ResourceAsColor")
    private void showDescriptionDialog(DescriptionViewModel model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DoneActivity.this, R.style.DialogTheme);
        builder.setTitle(model.getName());
        builder.setMessage(model.getDescription());
        //alertDialog.setIcon(R.drawable.ic_baseline_account_circle_24); //
        builder.setPositiveButton("ОК", (dialogInterface, i) -> {
            dialogInterface.dismiss();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        int height = 100;
        height += Math.min(7, Math.max(3, model.getDescription().length() / 34)) * 100;
        alertDialog.getWindow().setLayout(650, height);

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

        @SuppressLint({"NewApi", "DefaultLocale", "UseCompatLoadingForDrawables"})
        @Override
        public void onBindViewHolder(@NonNull DescriptionViewHolder holder, final int position) {

            DescriptionViewModel model = models.get(position);
            holder.descriptionName.setText(model.getName());
            holder.descriptionCount.setText(format("%d/%d", model.getCurrent(), model.getMax()));
            holder.descriptionProgress.setMax(model.getMax());
            holder.descriptionProgress.setProgress(model.getCurrent(), true);
            int portion = model.getMax() / 3;

            Drawable drawable;
            if (model.getCurrent() < portion) {
                drawable = getDrawable(R.drawable.mustard_shape);
            } else if (model.getCurrent() < portion * 2) {
                drawable = getDrawable(R.drawable.round_two);
            } else {
                drawable = getDrawable(R.drawable.purple_shape);
            }
            holder.descriptionLayout.setBackground(drawable);

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

    static class DescriptionViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.description_layout)
        public LinearLayout descriptionLayout;
        @BindView(R.id.descriptionName)
        public TextView descriptionName;
        @BindView(R.id.descriptionCount)
        public TextView descriptionCount;
        @BindView(R.id.descriptionProgress)
        public ProgressBar descriptionProgress;

        public DescriptionViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

