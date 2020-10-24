package ru.hardwork.onlinesocialdiagnosticapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.util.CollectionUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Arrays;

import ru.hardwork.onlinesocialdiagnosticapp.common.Common;
import ru.hardwork.onlinesocialdiagnosticapp.common.JSONResourceReader;
import ru.hardwork.onlinesocialdiagnosticapp.model.diagnostic.Category;
import ru.hardwork.onlinesocialdiagnosticapp.model.diagnostic.DiagnosticTest;
import ru.hardwork.onlinesocialdiagnosticapp.model.user.User;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences preferences;

    MaterialEditText edtNewUser, edtNewPassword, edtNewEmail;
    MaterialEditText edtUser, edtPassword;

    ImageView logo;

    Button btnSignUp, btnSignIn;

    FirebaseDatabase database;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String name = preferences.getString("USER_NAME", "guest");

        JSONResourceReader categoryReader = new JSONResourceReader(getResources(), R.raw.category);
        if (CollectionUtils.isEmpty(Common.categoryList)) {
            Common.categoryList.addAll(Arrays.asList(categoryReader.constructUsingGson(Category[].class)));
        }

        JSONResourceReader diagnosticReader = new JSONResourceReader(getResources(), R.raw.diagnostic_test);
        if (CollectionUtils.isEmpty(Common.categoryList)) {
            Common.diagnosticTests.addAll(Arrays.asList(diagnosticReader.constructUsingGson(DiagnosticTest[].class)));
        }

        final SharedPreferences.Editor preferencesEditor = preferences.edit();
        if (!"guest".equals(name)) {
            User user = new User();
            user.setLogIn(name);
            Intent homeActivity = new Intent(MainActivity.this, Home.class);
            Common.currentUser = user;
            startActivity(homeActivity);
            finish();
        }

        // Firebase
        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");
        // init drawable view
        logo = findViewById(R.id.logo);
        @SuppressLint("UseCompatLoadingForDrawables")
        Drawable drawable = getDrawable(R.drawable.ic_logo_main);
        logo.setImageDrawable(drawable);
        // ui init
        edtUser = findViewById(R.id.edtUser);
        edtPassword = findViewById(R.id.edtPassword);

        btnSignIn = findViewById(R.id.btn_sign_in);
        btnSignUp = findViewById(R.id.btn_sign_up);

        btnSignUp.setOnClickListener(view -> showSignUpDialog());

        btnSignIn.setOnClickListener(view -> signIn(edtUser.getText().toString(), edtPassword.getText().toString(), preferencesEditor));

    }

    private void signIn(final String logIn, final String password, final SharedPreferences.Editor editor) {
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(logIn).exists()) {
                    if (!logIn.isEmpty()) {
                        User user = snapshot.child(logIn).getValue(User.class);
                        if (user != null && user.getPassword().equals(password)) {
                            Intent homeActivity = new Intent(MainActivity.this, Home.class);
                            editor.putString("USER_NAME", user.getLogIn());
                            editor.commit();
                            Common.currentUser = user;
                            startActivity(homeActivity);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "Не верный пароль", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Пожалуйста введите логин", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Пользователь с таким логином не найден", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showSignUpDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Регистрация");
        alertDialog.setMessage("Пожалуйста введите инвормацию");

        LayoutInflater inflater = this.getLayoutInflater();
        View signUpLayout = inflater.inflate(R.layout.sign_up_layout, null);

        edtNewUser = signUpLayout.findViewById(R.id.edtNewUser);
        edtNewPassword = signUpLayout.findViewById(R.id.edtNewPassword);
        edtNewEmail = signUpLayout.findViewById(R.id.edtNewEmail);

        alertDialog.setView(signUpLayout);
        alertDialog.setIcon(R.drawable.ic_baseline_account_circle_24);

        alertDialog.setNegativeButton("ОТМЕНА", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.setPositiveButton("ЗАРЕГИСТРИРОВАТЬСЯ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final User user = new User(edtNewUser.getText().toString(),
                        edtNewPassword.getText().toString(),
                        edtNewEmail.getText().toString());

                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child(user.getLogIn()).exists()) {
                            Toast.makeText(MainActivity.this, "Пользователь с таким именем уже зарегистрирован", Toast.LENGTH_SHORT).show();
                        } else {
                            users.child(user.getLogIn()).setValue(user);
                            Toast.makeText(MainActivity.this, "Пользователь успешно зарегистрирован", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }
}