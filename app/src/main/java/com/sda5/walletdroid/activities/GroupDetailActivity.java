package com.sda5.walletdroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sda5.walletdroid.R;
import com.sda5.walletdroid.adapters.AccountAdapterGroupDetail;
import com.sda5.walletdroid.models.Account;
import com.sda5.walletdroid.models.Group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class GroupDetailActivity extends AppCompatActivity {
    private String groupID;
    private FirebaseFirestore database;
    private String currentUserId;
    private String accountId;
    private AccountAdapterGroupDetail accountAdapter;
    private Group group;
    private ArrayList<Account> accounts = new ArrayList<>();
    private Button btnAddMember;
    private Button btnDeleteMember;
    private Button btnDeleteGroup;
    private Button btnLeaveGroup;
    private Button btnSettle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);

        database = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        currentUserId = auth.getCurrentUser().getUid();

        Intent intent = getIntent();
        groupID = intent.getStringExtra("group_id");


        final ListView listView = findViewById(R.id.member_list);
        listView.setScrollingCacheEnabled(false);


        btnAddMember = findViewById(R.id.btn_add_member);
        btnDeleteMember = findViewById(R.id.btn_delete_member);
        btnDeleteGroup = findViewById(R.id.btn_delete_group);
        btnLeaveGroup = findViewById(R.id.btn_leave_group);
        btnSettle = findViewById(R.id.btn_settle);

        database.collection("Groups")
                .whereEqualTo("id", groupID)
                .addSnapshotListener((value, e) -> {
                    if (e != null) {
                        return;
                    }
                    if (null != value) {
                        Optional<Group> groupOptional = value.toObjects(Group.class).stream().findAny();
                        if (groupOptional.isPresent()) {
                            group = groupOptional.get();
                            accountAdapter = new AccountAdapterGroupDetail(getApplicationContext(), group, accounts, isGroupAdmin());
                            listView.setAdapter(accountAdapter);

                            if (!group.getAdminUserId().equals(currentUserId)) {
                                btnSettle.setVisibility(View.GONE);
                                btnAddMember.setVisibility(View.GONE);
                                btnDeleteMember.setVisibility(View.GONE);
                                btnDeleteGroup.setVisibility(View.GONE);
                            } else {
                                btnLeaveGroup.setVisibility(View.GONE);
                            }
                            CollectionReference accountRef = database.collection("Accounts");
                            for (String accountId : group.getAccountIdList()) {
                                accountRef
                                        .whereEqualTo("id", accountId)
                                        .get()
                                        .addOnSuccessListener(queryDocumentSnapshots -> {
                                    accounts.add(queryDocumentSnapshots.toObjects(Account.class).get(0));
                                    accountAdapter.notifyDataSetChanged();
                                });

                            }
                        }
                    }
                });

        /***Goes Account collection and finds current user's account id*/

        database.collection("Accounts").whereEqualTo("userID", currentUserId).get().addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot accountSnapshot = task.getResult();
                        if (null != accountSnapshot) {
                            Optional<Account> account = accountSnapshot.toObjects(Account.class).stream().findFirst();
                            if (account.isPresent()) {
                                accountId = account.get().getId();
                            }
                        }
                    }
                });
    }

    private boolean isGroupAdmin() {
        return group.getAdminUserId().equals(currentUserId);
    }

    public void leaveGroup(View view) {
        group.getAccountIdList().remove(accountId);
        database.collection("Groups").document(groupID)
                .update("accountIdList", group.getAccountIdList())
                .addOnCompleteListener(task -> {
                    Toast.makeText(GroupDetailActivity.this, "You left the group",
                            Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(GroupDetailActivity.this, ServiceActivity.class));
                });
    }


    public void addMember(View view) {
        Intent intent = new Intent(this, AddNewGroupMemberActivity.class);
        intent.putExtra("group_id", groupID);
        startActivity(intent);
    }

    public void deleteMembers(View view) {
        int sizeOfSelectedAccountIdList = accountAdapter.getSelectedAccountIDList().size();
        for (int i = 0; i < sizeOfSelectedAccountIdList; i++) {
            group.getAccountIdList().remove(accountAdapter.getSelectedAccountIDList().get(i));
        }

        database.collection("Groups").document(groupID)
                .update("accountIdList", group.getAccountIdList())
                .addOnCompleteListener(task -> {
                    finish();
                    startActivity(getIntent());
                });
    }

    public void deleteGroup(View view) {
        database.collection("Groups")
                .document(groupID)
                .delete()
                .addOnCompleteListener(task -> {
                    Toast.makeText(GroupDetailActivity.this,
                            "Group is deleted successfully",
                            Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(GroupDetailActivity.this, ServiceActivity.class));
                });
    }

    public void settleTheGroupExpenses(View view) {

        HashMap<String, Double> groupBalance = group.getBalance();
        groupBalance.forEach((key, value) -> groupBalance.put(key, 0.0));
        database.collection("Groups").document(groupID).update("balance", groupBalance);
        finish();
        startActivity(getIntent());
    }
}
