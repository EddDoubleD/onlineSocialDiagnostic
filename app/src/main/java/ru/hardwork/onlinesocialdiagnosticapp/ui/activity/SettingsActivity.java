package ru.hardwork.onlinesocialdiagnosticapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import ru.hardwork.onlinesocialdiagnosticapp.R;
import ru.hardwork.onlinesocialdiagnosticapp.common.Common;

public class SettingsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        mAuth = FirebaseAuth.getInstance();
        // Кнопка чистит кэш приложения
        Button flush = findViewById(R.id.flush);
        flush.setOnClickListener(view -> {
            mAuth.signOut();
            Common.firebaseUser = null;
            Intent home = new Intent(this, HomeActivity.class);
            startActivity(home);
            finish();
        });

        Button drop = findViewById(R.id.drop);
        drop.setOnClickListener(v -> {

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
