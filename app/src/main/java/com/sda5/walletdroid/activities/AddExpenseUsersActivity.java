package com.sda5.walletdroid.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sda5.walletdroid.R;
import com.sda5.walletdroid.adapters.AccountAdapterAddExpense;
import com.sda5.walletdroid.models.Account;
import com.sda5.walletdroid.models.Group;

import java.util.ArrayList;

public class AddExpenseUsersActivity extends AppCompatActivity {

    private AccountAdapterAddExpense accountAdapterAddExpense;
    private ArrayList<Account> accounts = new ArrayList<>();
    private ArrayList<String> expenseUsersId = new ArrayList<>();
    FirebaseAuth mAuth;
    FirebaseFirestore database;
    String currentUserId;
    String groupID;
    Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense_users);
        database = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        groupID = intent.getStringExtra("group_id");

        currentUserId = mAuth.getCurrentUser().getUid();

        ListView listView = findViewById(R.id.expense_users_list);
        listView.setScrollingCacheEnabled(false);

        accountAdapterAddExpense = new AccountAdapterAddExpense(getApplicationContext(), accounts, true);
        listView.setAdapter(accountAdapterAddExpense);


        database.collection("Groups")
                .document(groupID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot groupSnapshot = task.getResult();
                        group = groupSnapshot.toObject(Group.class);

                        database.collection("Accounts")
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
                                                if (!group.getAccountIdList().contains(account.getId())) {
                                                    accounts.add(account);
                                                }
                                            }
                                        }
                                        accountAdapterAddExpense.notifyDataSetChanged();
                                    }
                                });
                    }
                });
    }

    public void addExpenseUsers(View view) {
        expenseUsersId.addAll(accountAdapterAddExpense.getSelectedExpenseUsersIDList());
        database.collection("Groups")
                .document(groupID)
                .update("accountIdList", group.getAccountIdList())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            accountAdapterAddExpense.notifyDataSetChanged();
                            Toast.makeText(AddExpenseUsersActivity.this, "Users are added successfully",
                                    Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(AddExpenseUsersActivity.this, AddExpenseActicity.class);
                            intent.putExtra("group_id", groupID);
                            intent.putExtra("expenseUsersIds", expenseUsersId);
                            startActivity(intent);
                        }
                    }
                });
    }
}
