package io.sudutech.walletdroid;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import io.sudutech.walletdroid.fragments.ExpenseFragment;
import io.sudutech.walletdroid.fragments.GroupFragment;
import io.sudutech.walletdroid.fragments.InvestFragment;
import io.sudutech.walletdroid.fragments.QueyFragment;
import io.sudutech.walletdroid.fragments.SettleFragment;

public class ServiceActivity extends AppCompatActivity {


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
}
