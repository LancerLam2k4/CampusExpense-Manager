package com.example.task1.ExpenseManager;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.task1.BudgetFrament;
import com.example.task1.HomeFragment;
import com.example.task1.R;
import com.example.task1.SettingFragment;
import com.example.task1.StatisticalFrament;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ExpenseManagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_manager);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        // Set default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    if (item.getItemId() == R.id.nav_home) {
                        selectedFragment = new HomeFragment();
                    } else if (item.getItemId() == R.id.nav_budget) {
                        selectedFragment = new BudgetFrament();
                    } else if (item.getItemId() == R.id.nav_statistical) {
                        selectedFragment = new StatisticalFrament();
                    } else if (item.getItemId() == R.id.nav_settings) {
                        selectedFragment = new SettingFragment();
                    }


                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
            };
}
