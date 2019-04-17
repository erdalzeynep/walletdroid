package com.sda5.walletdroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.sda5.walletdroid.R;
import com.sda5.walletdroid.adapters.AccountAdapter;
import com.sda5.walletdroid.models.Account;
import com.sda5.walletdroid.models.Group;

import java.util.ArrayList;
import java.util.Optional;

public class GroupDetailActivity extends AppCompatActivity {
    String groupID;
    FirebaseFirestore dataBase;
    FirebaseAuth auth;
    private String currentUserId;
    private String accountId;
    private AccountAdapter accountAdapter;
    private Group group;
    private ArrayList<Account> accounts = new ArrayList<>();
    private Button btnAddMember;
    private Button btnDeleteMember;
    private Button btnDeleteGroup;
    private Button btnLeaveGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);

        dataBase = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUserId = auth.getCurrentUser().getUid();

        Intent intent = getIntent();
        groupID = intent.getStringExtra("group_id");


        final ListView listView = findViewById(R.id.member_list);
        listView.setScrollingCacheEnabled(false);

        accountAdapter = new AccountAdapter(getApplicationContext(), accounts);
        listView.setAdapter(accountAdapter);

        btnAddMember = findViewById(R.id.btn_add_member);
        btnDeleteMember = findViewById(R.id.btn_delete_member);
        btnDeleteGroup = findViewById(R.id.btn_delete_group);
        btnLeaveGroup = findViewById(R.id.btn_leave_group);

        dataBase.collection("Groups")
                .whereEqualTo("id", groupID)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        if (null != value) {
                            group = value.toObjects(Group.class).get(0);
                            if (!group.getAdminUserId().equals(currentUserId)) {
                                btnAddMember.setVisibility(View.GONE);
                                btnDeleteMember.setVisibility(View.GONE);
                                btnDeleteGroup.setVisibility(View.GONE);
                            } else {
                                btnLeaveGroup.setVisibility(View.GONE);
                            }
                            CollectionReference accountRef = dataBase.collection("Accounts");
                            for (String accountId : group.getAccountIdList()) {
                                accountRef.whereEqualTo("id", accountId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        accounts.add(queryDocumentSnapshots.toObjects(Account.class).get(0));
                                        accountAdapter.notifyDataSetChanged();
                                    }
                                });

                            }

                        }
                    }
                });

        /***Goes Account collection and finds current user's account id*/

        dataBase.collection("Accounts").whereEqualTo("userID", currentUserId).get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot accountSnapshot = task.getResult();
                            if (null != accountSnapshot) {
                                Optional<Account> account = accountSnapshot.toObjects(Account.class).stream().findFirst();
                                if (account.isPresent()) {
                                    accountId = account.get().getId();
                                }
                            }
                        }
                    }
                });
    }

    public void leaveGroup(View view) {
        group.getAccountIdList().remove(accountId);
        dataBase.collection("Groups").document(groupID)
                .update("accountIdList", group.getAccountIdList())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(GroupDetailActivity.this, "You left the group",
                                Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(GroupDetailActivity.this, ServiceActivity.class));
                    }

                });
    }


    public void addMember(View view) {
        Intent intent = new Intent(this, AddNewGroupMemberActivity.class);
        intent.putExtra("group_id" , groupID);
        startActivity(intent);
    }

    public void deleteMembers(View view) {
        int sizeOfSelectedAccountIdList = accountAdapter.getSelectedAccountIDList().size();
        for (int i=0 ; i< sizeOfSelectedAccountIdList ; i++){
            group.getAccountIdList().remove(accountAdapter.getSelectedAccountIDList().get(i));
        }

        dataBase.collection("Groups").document(groupID)
                .update("accountIdList" , group.getAccountIdList())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        accountAdapter.notifyDataSetChanged();
                        finish();
                        startActivity(getIntent());
                    }
                });
    }

}

