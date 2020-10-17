package ru.hardwork.onlinesocialdiagnosticapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import ru.hardwork.onlinesocialdiagnosticapp.common.Common;

/**
 *
 */
public class AccountFragment extends Fragment {

    View mFragment;

    CardView cardResultsId;
    TextView userLogIn;

    public static AccountFragment newInstance() {
        AccountFragment accountFragment = new AccountFragment();
        return accountFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragment = inflater.inflate(R.layout.fragment_account, container, false);

        userLogIn = mFragment.findViewById(R.id.userLogIn);
        if (Common.currentUser != null && !Common.currentUser.getLogIn().equalsIgnoreCase("guest")) {
            userLogIn.setText(Common.currentUser.getLogIn());
        }

        cardResultsId = mFragment.findViewById(R.id.cardResultsId);
        cardResultsId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent diagnosticResult = new Intent(getActivity(), DiagnosticResult.class);
                startActivity(diagnosticResult);
            }
        });

        return mFragment;
    }
}