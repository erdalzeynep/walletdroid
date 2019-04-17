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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sda5.walletdroid.R;
import com.sda5.walletdroid.adapters.AccountAdapter;
import com.sda5.walletdroid.models.Account;
import com.sda5.walletdroid.models.Group;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CreateNewGroupActivity extends AppCompatActivity {
    private AccountAdapter accountAdapter;
    private ArrayList<Account> accounts = new ArrayList<>();
    FirebaseAuth mAuth;
    FirebaseFirestore database;
    private String currentUserId;
    private String accountId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_group);

        database = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        currentUserId = mAuth.getCurrentUser().getUid();

        ListView listView = findViewById(R.id.account_list);
        listView.setScrollingCacheEnabled(false);

        accountAdapter = new AccountAdapter(getApplicationContext(), accounts);
        listView.setAdapter(accountAdapter);

        database.collection("Accounts")
                .whereEqualTo("internalAccount", true)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        for (QueryDocumentSnapshot doc : value) {
                            Account account = doc.toObject(Account.class);
                            if (!account.getUserID().equals(mAuth.getUid())) {
                                accounts.add(account);
                            }
                        }
                        accountAdapter.notifyDataSetChanged();
                    }
                });
    }

    public void saveNewGroup(View view) {
        String userID = mAuth.getCurrentUser().getUid();
        final List<String> selectedAccountIDList = accountAdapter.getSelectedAccountIDList();
        database.collection("Accounts").whereEqualTo("userID", userID).get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot accountSnapshot = task.getResult();
                            if (null != accountSnapshot) {
                                Optional<Account> account = accountSnapshot.toObjects(Account.class).stream().findFirst();
                                if (account.isPresent()) {
                                    if (selectedAccountIDList.size() != 0) {
                                        persistGroup(selectedAccountIDList);
                                    } else {
                                        Toast.makeText(CreateNewGroupActivity.this, "Please enter the group name and select the members", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    }
                }
        );
    }

    private void persistGroup(final List<String> memberIDs) {
        database.collection("Accounts").whereEqualTo("userID", currentUserId).get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot accountSnapshot = task.getResult();
                            if (null != accountSnapshot) {
                                Optional<Account> account = accountSnapshot.toObjects(Account.class).stream().findFirst();
                                if (account.isPresent()) {
                                    accountId = account.get().getId();
                                    memberIDs.add(account.get().getId());
                                    String groupName = ((EditText) findViewById(R.id.et_group_name)).getText().toString();
                                    if (!groupName.equals("")) {
                                        Group group = new Group(groupName, accountAdapter.getSelectedAccountIDList(), accountId);
                                        database.collection("Groups").document(group.getId()).set(group).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                                        Toast.makeText(CreateNewGroupActivity.this, "Please enter the group name and select the members", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }
                        }
                    }
                });


    }
}
