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

import ru.hardwork.onlinesocialdiagnosticapp.common.Common;
import ru.hardwork.onlinesocialdiagnosticapp.model.user.User;

/**
 *
 */
public class AccountFragment extends Fragment {

    View mFragment;

    CardView cardResultsId, settings;
    TextView userLogIn;

    public static AccountFragment newInstance() {
        AccountFragment accountFragment = new AccountFragment();
        return accountFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ApplySharedPref")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragment = inflater.inflate(R.layout.fragment_account, container, false);

        userLogIn = mFragment.findViewById(R.id.userLogIn);
        if (Common.currentUser != null && !Common.currentUser.getLogIn().equalsIgnoreCase("guest")) {
            userLogIn.setText(Common.currentUser.getLogIn());
        }

        cardResultsId = mFragment.findViewById(R.id.cardResultsId);
        cardResultsId.setOnClickListener(view -> {
            Intent diagnosticResult = new Intent(getActivity(), DiagnosticResult.class);
            startActivity(diagnosticResult);
        });

        settings = mFragment.findViewById(R.id.settings);
        settings.setOnClickListener(view -> {
            Intent diagnosticResult = new Intent(getActivity(), DiagnosticResult.class);
            startActivity(diagnosticResult);

            SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor editor = preference.edit();
            User user = new User();
            user.setLogIn("guest");
            user.setRole(User.Role.GUEST);
            editor.putString("USER_NAME", "guest");
            editor.commit();
            editor.clear();

            Common.currentUser = user;
        });
        return mFragment;
    }
}