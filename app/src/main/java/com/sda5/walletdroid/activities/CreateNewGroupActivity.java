package com.sda5.walletdroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sda5.walletdroid.R;
import com.sda5.walletdroid.adapters.AccountAdapter;
import com.sda5.walletdroid.models.Account;
import com.sda5.walletdroid.models.Group;

import java.util.ArrayList;
import java.util.List;

public class CreateNewGroupActivity extends AppCompatActivity {
    private AccountAdapter accountAdapter;
    private ArrayList<Account> accounts = new ArrayList<>();

    FirebaseFirestore database;

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

    public void saveNewGroup(View view) {
        List<String> selectedAccountIDList = accountAdapter.getSelectedAccountIDList();
        String groupName = ((EditText) findViewById(R.id.et_group_name)).getText().toString();
        if (!groupName.equals("") && selectedAccountIDList.size() != 0) {
            Group group = new Group(groupName, accountAdapter.getSelectedAccountIDList());
            database.collection("Groups").document().set(group).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(CreateNewGroupActivity.this, ServiceActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(CreateNewGroupActivity.this, "Creating group failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else {
            Toast.makeText(this, "Please enter the group name and select the members", Toast.LENGTH_SHORT).show();
        }
    }
}

