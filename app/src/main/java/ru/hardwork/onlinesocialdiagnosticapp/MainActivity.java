package ru.hardwork.onlinesocialdiagnosticapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import net.bohush.geometricprogressview.GeometricProgressView;

import ru.hardwork.onlinesocialdiagnosticapp.common.Common;
import ru.hardwork.onlinesocialdiagnosticapp.common.UIDataRouter;
import ru.hardwork.onlinesocialdiagnosticapp.model.user.User;

public class MainActivity extends AppCompatActivity {

    ImageView logo;
    GeometricProgressView progressView;
    // GifImageView gifImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressView = findViewById(R.id.progressView);
        progressView.setColor(R.color.palePurple);

        logo = findViewById(R.id.logoMain);
        Drawable ic_logo_main = getDrawable(R.drawable.ic_logo_main);
        logo.setImageDrawable(ic_logo_main);
        //gifImageView = findViewById(R.id.gifLogo);

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