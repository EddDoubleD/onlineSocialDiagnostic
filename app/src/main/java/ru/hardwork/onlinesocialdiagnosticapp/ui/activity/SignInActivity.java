package ru.hardwork.onlinesocialdiagnosticapp.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

import ru.hardwork.onlinesocialdiagnosticapp.R;
import ru.hardwork.onlinesocialdiagnosticapp.common.Common;

import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * Регистрация/аутентификация пользователя
 */
public class SignInActivity extends AppCompatActivity {
    //
    MaterialEditText edtNewUser, edtNewPassword, edtNewEmail;
    MaterialEditText edtUser, edtPassword;
    //
    Button btnSignUp, btnSignIn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        setContentView(R.layout.activity_signup);
        // Аутентификация
        mAuth = FirebaseAuth.getInstance();
        // ui init
        edtUser = findViewById(R.id.edtUser);
        edtPassword = findViewById(R.id.edtPassword);
        // Войти
        btnSignIn = findViewById(R.id.btn_sign_in);
        btnSignIn.setOnClickListener(view -> signIn(edtUser.getText().toString(), edtPassword.getText().toString()));
        // Зарегистрироваться
        btnSignUp = findViewById(R.id.btn_sign_up);
        btnSignUp.setOnClickListener(view -> showSignUpDialog());
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        Common.firebaseUser = mAuth.getCurrentUser();
    }

    /**
     * Авторизация пользователя
     */
    private void signIn(final String email, final String password) {
        // Проверка корректности введенных данных
        boolean valid = validate(email, password);
        if (valid) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Common.firebaseUser = mAuth.getCurrentUser();
                            Intent homeActivity = new Intent(SignInActivity.this, HomeActivity.class);
                            startActivity(homeActivity);
                            finish();
                        } else {
                            Toast.makeText(SignInActivity.this, "Неверный логин или пароль", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }

    private boolean validate(String email, String password) {
        // Проверка вилидности email
        if (isEmpty(email)) {
            Toast.makeText(SignInActivity.this, "Не задан адрес электронной почты", Toast.LENGTH_SHORT).show();
            return false;
        }
        // проверка пароля
        if (isEmpty(password)) {
            Toast.makeText(SignInActivity.this, "Не задан пароль", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void showSignUpDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SignInActivity.this);
        alertDialog.setTitle("Регистрация");
        alertDialog.setMessage("Пожалуйста введите информацию");

        LayoutInflater inflater = this.getLayoutInflater();
        View signUpLayout = inflater.inflate(R.layout.sign_up_layout, null);

        edtNewUser = signUpLayout.findViewById(R.id.edtNewUser);
        edtNewPassword = signUpLayout.findViewById(R.id.edtNewPassword);
        edtNewEmail = signUpLayout.findViewById(R.id.edtNewEmail);

        alertDialog.setView(signUpLayout);
        alertDialog.setIcon(R.drawable.ic_baseline_account_circle_24);

        alertDialog.setNegativeButton("ОТМЕНА", (dialogInterface, i) -> dialogInterface.dismiss());

        alertDialog.setPositiveButton("ЗАРЕГИСТРИРОВАТЬСЯ", (dialogInterface, i) -> {
            mAuth.createUserWithEmailAndPassword(edtNewUser.getText().toString(), edtNewPassword.getText().toString())
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Common.firebaseUser = mAuth.getCurrentUser();
                            Intent homeActivity = new Intent(SignInActivity.this, HomeActivity.class);
                            startActivity(homeActivity);
                            finish();
                        } else {
                            Toast.makeText(SignInActivity.this, "Возникла ошикба при регистрации", Toast.LENGTH_SHORT).show();

                        }
                    });
            dialogInterface.dismiss();
        });

        alertDialog.show();
    }
}