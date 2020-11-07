package ru.hardwork.onlinesocialdiagnosticapp.ui.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ru.hardwork.onlinesocialdiagnosticapp.R;
import ru.hardwork.onlinesocialdiagnosticapp.ui.fragment.AccountFragment;
import ru.hardwork.onlinesocialdiagnosticapp.ui.fragment.CategoryFragment;
import ru.hardwork.onlinesocialdiagnosticapp.ui.fragment.GroupFragment;
import ru.hardwork.onlinesocialdiagnosticapp.ui.fragment.ResultFragment;

public class Home extends AppCompatActivity {

    /**
     * Bottom navigation menu listener
     */
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.action_category:
                        selectedFragment = new CategoryFragment();
                        break;
                    case R.id.action_result:
                        selectedFragment = new ResultFragment();
                        break;
                    case R.id.action_group:
                        selectedFragment = new GroupFragment();
                        break;
                    case R.id.action_account:
                        selectedFragment = new AccountFragment();
                        break;
                }

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout, selectedFragment)
                        .addToBackStack(null)
                        .commit();
                return true;
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        // keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,
                    CategoryFragment.newInstance()).commit();
        }
    }
}