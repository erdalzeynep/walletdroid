package com.sda5.walletdroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sda5.walletdroid.R;
import com.sda5.walletdroid.adapters.AccountAdapter;
import com.sda5.walletdroid.helper.ListViewHelper;
import com.sda5.walletdroid.models.Account;
import com.sda5.walletdroid.models.Group;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import androidx.appcompat.app.AppCompatActivity;

public class CreateNewGroupActivity extends AppCompatActivity {
    private AccountAdapter accountAdapter;
    private ArrayList<Account> accounts = new ArrayList<>();
    private FirebaseAuth mAuth;
    private FirebaseFirestore database;
    private String accountId;
    private CheckBox checkBoxExternalAccount;
    private EditText editTextExternalAccountEmail;
    private Button buttonAddExternalAccount;
    private ListView listViewExternalAccounts;
    private ArrayList<String> externalAccountItems = new ArrayList<>();
    private ArrayAdapter<String> externalUserAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_group);

        database = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        ListView listView = findViewById(R.id.account_list);
        listView.setScrollingCacheEnabled(false);

        externalUserAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                externalAccountItems);

        accountAdapter = new AccountAdapter(getApplicationContext(), accounts, true);
        listView.setAdapter(accountAdapter);

        ListViewHelper.setListViewSizeDynamically(accountAdapter, listView);

        checkBoxExternalAccount = findViewById(R.id.checkBox_external_account);
        editTextExternalAccountEmail = findViewById(R.id.editText_external_account_email);
        buttonAddExternalAccount = findViewById(R.id.button_add_external_account);
        listViewExternalAccounts = findViewById(R.id.listView_external_accounts);

        editTextExternalAccountEmail.setVisibility(View.INVISIBLE);
        buttonAddExternalAccount.setVisibility(View.INVISIBLE);
        listViewExternalAccounts.setVisibility(View.INVISIBLE);

        checkBoxExternalAccount.setOnClickListener(v -> {
            if (checkBoxExternalAccount.isChecked()) {
                editTextExternalAccountEmail.setVisibility(View.VISIBLE);
                buttonAddExternalAccount.setVisibility(View.VISIBLE);
                listViewExternalAccounts.setVisibility(View.VISIBLE);
                listViewExternalAccounts.setAdapter(externalUserAdapter);
            }

            else{
                editTextExternalAccountEmail.setVisibility(View.INVISIBLE);
                buttonAddExternalAccount.setVisibility(View.INVISIBLE);
                listViewExternalAccounts.setVisibility(View.INVISIBLE);
            }
        });


        database.collection("Accounts")
                .whereEqualTo("internalAccount", true)
                .addSnapshotListener((value, e) -> {
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
                });
    }

    public void saveNewGroup(View view) {
        String userID = mAuth.getCurrentUser().getUid();

        Task<Void> currentUsersAccountRetrieverTask = database.collection("Accounts")
                .whereEqualTo("userID", userID)
                .get()
                .onSuccessTask(this::addCurrentUsersAccountToList);

        List<Task<Void>> externalAccountRetrieverTasks = new ArrayList<>();
        for (final String email : externalAccountItems) {
            Task<Void> externalAccountRetrieverTask = getExternalAccountRetrieverTask(email);
            externalAccountRetrieverTasks.add(externalAccountRetrieverTask);
        }

        List<Task<Void>> allTasks = Stream.concat(externalAccountRetrieverTasks.stream(), Stream.of(currentUsersAccountRetrieverTask))
                .collect(Collectors.toList());

        Tasks.whenAll(allTasks)
                .continueWithTask(this::persistGroup)
                .addOnSuccessListener(this::goToGroupPage);
    }

    private Task<Void> addCurrentUsersAccountToList(QuerySnapshot accountSnapshots) {
        if (null != accountSnapshots) {
            Optional<Account> account = accountSnapshots.toObjects(Account.class).stream().findFirst();
            if (account.isPresent()) {
                accountId = account.get().getId();
                accountAdapter.addSelectedAccountId(accountId);
                return Tasks.forResult(null);
            }
        }
        return Tasks.forException(new Exception("User's account doesn't exist"));
    }

    private Task<Void> getExternalAccountRetrieverTask(String email) {
        return database.collection("Accounts")
                .whereEqualTo("email", email).get()
                .onSuccessTask(task -> getOrCreateExternalAccount(email, task));
    }

    private Task<Void> getOrCreateExternalAccount(String email, QuerySnapshot accountSnapshot) {
            Optional<Account> accountOptional = accountSnapshot.toObjects(Account.class).stream().findFirst();
            if (accountOptional.isPresent()) {
                accountAdapter.addSelectedAccountId(accountOptional.get().getId());
                return Tasks.forResult(null);
            } else {
                final Account externalAccount = new Account(false, email);
                return database.collection("Accounts")
                        .document(externalAccount.getId())
                        .set(externalAccount)
                        .addOnCompleteListener(task11 -> {
                            if (task11.isSuccessful()) {
                                accountAdapter.addSelectedAccountId(externalAccount.getId());
                            }
                        });
            }
    }

    private void goToGroupPage(Void aVoid) {
        Intent intent = new Intent(CreateNewGroupActivity.this, ServiceActivity.class);
        startActivity(intent);
    }

    private Task<Void> persistGroup(Task<Void> voidTask) {
        String groupName = ((EditText) findViewById(R.id.et_group_name)).getText().toString();
        if (!groupName.equals("")) {
            Group group = new Group(groupName, accountAdapter.getSelectedAccountIDList(), accountId);
            return database.collection("Groups").document(group.getId()).set(group);
        } else {
            Toast.makeText(CreateNewGroupActivity.this, "Please enter the group name and select the members", Toast.LENGTH_SHORT).show();
            return voidTask;
        }
    }

    public void addExternalAccount(View view) {
        String externalAccountEmail = editTextExternalAccountEmail.getText().toString();
        externalAccountItems.add(externalAccountEmail);
        externalUserAdapter.notifyDataSetChanged();
        editTextExternalAccountEmail.setText("");
        ListViewHelper.setListViewSizeDynamically(externalUserAdapter, listViewExternalAccounts);


    }
}
