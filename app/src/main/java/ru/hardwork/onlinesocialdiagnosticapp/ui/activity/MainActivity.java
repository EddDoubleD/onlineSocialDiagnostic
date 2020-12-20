package ru.hardwork.onlinesocialdiagnosticapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import ru.hardwork.onlinesocialdiagnosticapp.R;
import ru.hardwork.onlinesocialdiagnosticapp.common.Common;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String INVITE = "invite";
    private static final String AUTH = "auth";

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
                    Common.firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    // Optimizing group loading
                    String uid = Common.firebaseUser != null ? Common.firebaseUser.getUid() : EMPTY;
                    FirebaseDatabase.getInstance().getReference(INVITE).orderByChild(AUTH).equalTo(uid);
                    sleep(1000);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                } finally {
                    startActivity(homeActivity);
                    finish();
                }
            }
        }.start();
    }
}