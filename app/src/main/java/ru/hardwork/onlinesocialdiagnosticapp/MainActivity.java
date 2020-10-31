package ru.hardwork.onlinesocialdiagnosticapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.util.CollectionUtils;

import java.util.Arrays;

import pl.droidsonroids.gif.GifImageView;
import ru.hardwork.onlinesocialdiagnosticapp.common.Common;
import ru.hardwork.onlinesocialdiagnosticapp.common.JSONResourceReader;
import ru.hardwork.onlinesocialdiagnosticapp.common.UIDataRouter;
import ru.hardwork.onlinesocialdiagnosticapp.model.diagnostic.Category;
import ru.hardwork.onlinesocialdiagnosticapp.model.diagnostic.DiagnosticTest;
import ru.hardwork.onlinesocialdiagnosticapp.model.user.User;

public class MainActivity extends AppCompatActivity {

    ImageView logo;
    GifImageView gifImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logo = findViewById(R.id.logoMain);
        Drawable ic_logo_main = getDrawable(R.drawable.ic_logo_main);
        logo.setImageDrawable(ic_logo_main);
        gifImageView = findViewById(R.id.gifLogo);
        Drawable drawable = getDrawable(R.color.background);
        gifImageView.setBackground(drawable);

        final Intent homeActivity = new Intent(MainActivity.this, Home.class);
        Thread timer = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(1400);
                } catch (Exception e) {
                    Log.e("MAIN", e.getMessage());
                } finally {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    String name = preferences.getString(UIDataRouter.USER_NAME, UIDataRouter.DEFAULT_USER);
                    JSONResourceReader categoryReader = new JSONResourceReader(getResources(), R.raw.category);
                    if (CollectionUtils.isEmpty(Common.categoryList)) {
                        Common.categoryList.addAll(Arrays.asList(categoryReader.constructUsingGson(Category[].class)));
                    }
                    JSONResourceReader diagnosticReader = new JSONResourceReader(getResources(), R.raw.diagnostic_test);
                    if (CollectionUtils.isEmpty(Common.diagnosticTests)) {
                        Common.diagnosticTests.addAll(Arrays.asList(diagnosticReader.constructUsingGson(DiagnosticTest[].class)));
                    }

                    User user = new User();
                    user.setLogIn(name);

                    Common.currentUser = user;
                    startActivity(homeActivity);
                    finish();
                }
            }
        };

        timer.start();
    }

}