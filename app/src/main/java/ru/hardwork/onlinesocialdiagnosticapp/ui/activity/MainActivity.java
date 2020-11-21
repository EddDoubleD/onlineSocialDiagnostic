package ru.hardwork.onlinesocialdiagnosticapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import ru.hardwork.onlinesocialdiagnosticapp.R;
import ru.hardwork.onlinesocialdiagnosticapp.common.Common;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Intent homeActivity = new Intent(MainActivity.this, HomeActivity.class);
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    mAuth = FirebaseAuth.getInstance();
                    Common.firebaseUser = mAuth.getCurrentUser();
                    sleep(1400);
                } catch (Exception e) {
                    Log.e("MAIN", e.getMessage());
                } finally {
                    startActivity(homeActivity);
                    finish();
                }
            }
        }.start();
    }
}