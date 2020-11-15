package ru.hardwork.onlinesocialdiagnosticapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ru.hardwork.onlinesocialdiagnosticapp.R;
import ru.hardwork.onlinesocialdiagnosticapp.common.Common;
import ru.hardwork.onlinesocialdiagnosticapp.common.UIDataRouter;
import ru.hardwork.onlinesocialdiagnosticapp.model.user.User;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        // Кнопка чистит кэш приложения
        Button flush = findViewById(R.id.flush);
        flush.setOnClickListener(view -> {
            Intent home = new Intent(this, HomeActivity.class);
            startActivity(home);
            Common.firebaseUser = null;
            User user = new User();
            user.setLogIn(UIDataRouter.DEFAULT_USER);
            user.setRole(User.Role.GUEST);
            Common.currentUser = user;
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
