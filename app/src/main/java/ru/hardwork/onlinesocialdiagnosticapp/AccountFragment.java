package ru.hardwork.onlinesocialdiagnosticapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.mutable.MutableBoolean;

import ru.hardwork.onlinesocialdiagnosticapp.common.Common;
import ru.hardwork.onlinesocialdiagnosticapp.model.user.User;

/**
 *
 */
public class AccountFragment extends Fragment {

    View mFragment;
    CardView quitAccountView;

    CardView settings;
    TextView userLogIn, inOut;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        authStateListener = firebaseAuth -> {
            FirebaseUser firebaseUser = mAuth.getCurrentUser();
            if (firebaseUser != null && ObjectUtils.notEqual(firebaseAuth, Common.firebaseUser)) {
                Common.firebaseUser = firebaseUser;
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAuth.removeAuthStateListener(authStateListener);
    }

    /**
     * Обнуление
     */
    private void zeroing(MutableBoolean exit) {
        userLogIn.setText("guest");
        inOut.setText("Войти");
        exit.setFalse();
    }

    @SuppressLint({"ApplySharedPref", "SetTextI18n"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragment = inflater.inflate(R.layout.fragment_account, container, false);

        userLogIn = mFragment.findViewById(R.id.userLogIn);
        quitAccountView = mFragment.findViewById(R.id.quitAccountView);
        inOut = quitAccountView.findViewById(R.id.inOut);
        final MutableBoolean exit = new MutableBoolean();
        if (Common.firebaseUser != null) {
            userLogIn.setText(Common.firebaseUser.getEmail());
            inOut.setText("Выйти");
            exit.setTrue();
        } else {
            zeroing(exit);
        }
        //
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(getContext());
        final SharedPreferences.Editor editor = preference.edit();
        quitAccountView.setOnClickListener(view -> {
            if (exit.booleanValue()) {
                zeroing(exit);
                editor.putString("USER_NAME", "guest");
                editor.commit();
                editor.clear();
            } else {
                Intent signInActivity = new Intent(getContext(), SignInActivity.class);
                startActivity(signInActivity);
            }
        });



        settings = mFragment.findViewById(R.id.settings);
        settings.setOnClickListener(view -> {
            Intent diagnosticResult = new Intent(getActivity(), Home.class);
            startActivity(diagnosticResult);
            Common.firebaseUser = null;
            User user = new User();
            user.setLogIn("guest");
            user.setRole(User.Role.GUEST);
            Common.currentUser = user;
        });


        return mFragment;
    }
}