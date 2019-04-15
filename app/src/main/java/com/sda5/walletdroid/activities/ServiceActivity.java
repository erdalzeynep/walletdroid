package com.sda5.walletdroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.sda5.walletdroid.R;
import com.sda5.walletdroid.fragments.ExpenseFragment;
import com.sda5.walletdroid.fragments.GroupFragment;
import com.sda5.walletdroid.fragments.InvestFragment;
import com.sda5.walletdroid.fragments.QueyFragment;
import com.sda5.walletdroid.fragments.SettleFragment;

public class ServiceActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_group:
                    fragment = new GroupFragment();
                    break;
                case R.id.navigation_expense:
                    fragment = new ExpenseFragment();
                    break;
                case R.id.navigation_query:
                    fragment = new QueyFragment();
                    break;
                case R.id.navigation_settle:
                    fragment = new SettleFragment();
                    break;
                case R.id.navigation_stock:
                    fragment = new InvestFragment();
            }
            return loadFragment(fragment);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadFragment(new GroupFragment());
    }

    private boolean loadFragment(Fragment fragment){
        if(fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
            return true;
        }
            return false;
    }

    public void signOut(View view) {
        mAuth.signOut();
        finish();
    }

    public void createNewGroup(View view) {
        Intent intent = new Intent(this, CreateNewGroupActivity.class);
        startActivity(intent);
    }
}
