package com.sda5.walletdroid.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sda5.walletdroid.R;
import com.sda5.walletdroid.adapters.AccountAdapter;
import com.sda5.walletdroid.models.Account;

import java.util.ArrayList;
import java.util.List;

import static com.sda5.walletdroid.R.id.et_group_name;

public class CreateNewGroupActivity extends AppCompatActivity {
    private AccountAdapter accountAdapter;
    private ArrayList<Account> accounts = new ArrayList<>();
    FirebaseFirestore database;
    private EditText groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_group);

        database = FirebaseFirestore.getInstance();

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        database.setFirestoreSettings(settings);

        ListView listView = findViewById(R.id.account_list);
        listView.setScrollingCacheEnabled(false);

        groupName = findViewById(et_group_name);

        accountAdapter = new AccountAdapter(getApplicationContext(), accounts);
        listView.setAdapter(accountAdapter);

        database.collection("Account")
                .whereEqualTo("isInternal", true)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }

                        for (QueryDocumentSnapshot doc : value) {
                            accounts.add(doc.toObject(Account.class));
                        }
                        accountAdapter.notifyDataSetChanged();
                    }
                });

    }

}
