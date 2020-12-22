package ru.hardwork.onlinesocialdiagnosticapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.hardwork.onlinesocialdiagnosticapp.R;
import ru.hardwork.onlinesocialdiagnosticapp.common.Common;

public class SettingsActivity extends AppCompatActivity {


    @BindView(R.id.flush)
    Button flush;
    @BindView(R.id.drop)
    Button drop;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        this.mAuth = FirebaseAuth.getInstance();
    }

    @OnClick()
    public void drop() {

    }

    /**
     * Кнопка чистит кэш приложения
     */
    @OnClick(R.id.flush)
    public void flush() {
        mAuth.signOut();
        Common.firebaseUser = null;
        Intent home = new Intent(this, HomeActivity.class);
        startActivity(home);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
