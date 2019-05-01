package com.sda5.walletdroid.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sda5.walletdroid.GMailSender;
import com.sda5.walletdroid.R;
import com.sda5.walletdroid.adapters.AccountAdapterGroupDetail;
import com.sda5.walletdroid.helper.ListViewHelper;
import com.sda5.walletdroid.models.Account;
import com.sda5.walletdroid.models.Group;
import com.sda5.walletdroid.models.Notification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static android.text.Html.FROM_HTML_MODE_COMPACT;
import static android.text.Html.FROM_HTML_SEPARATOR_LINE_BREAK_DIV;

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
    private Account currentAccount;


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
                                currentAccount = account.get();
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
        HashMap<String, Double> groupBalance = group.getBalance();
        Double balanceForCurrentUser = groupBalance.get(accountId);
        if (balanceForCurrentUser != 0) {
            Toast.makeText(GroupDetailActivity.this, "You are not allowed to leave group since you have a balance. ",
                    Toast.LENGTH_SHORT).show();

        } else {
            group.getAccountIdList().remove(accountId);
            database.collection("Groups").document(groupID)
                    .update("accountIdList", group.getAccountIdList())
                    .addOnCompleteListener(task -> {
                        Toast.makeText(GroupDetailActivity.this, "You left the group",
                                Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(GroupDetailActivity.this, ServiceActivity.class));
                    });
        }
    }


    public void addMember(View view) {
        Intent intent = new Intent(this, AddNewGroupMemberActivity.class);
        intent.putExtra("group_id", groupID);
        startActivity(intent);
    }

    public void deleteMembers(View view) {
        Map<String, Double> groupBalance = group.getBalance();
        for (String accountID : accountAdapter.getSelectedAccountIDList()) {
            groupBalance.remove(accountID);
        }

        List<String> accountIDsToBeDeleted = accountAdapter.getSelectedAccountIDList();
        for (String accountId : accountIDsToBeDeleted) {
            accounts.remove(accountId);
            accountAdapter.notifyDataSetChanged();
            group.getAccountIdList().remove(accountId);
            groupBalance.remove(accountId);
        }

        Map<String, Object> updateFields = new HashMap<>();
        updateFields.put("accountIdList", group.getAccountIdList());
        updateFields.put("balance", groupBalance);

        database.collection("Groups").document(groupID)
                .update(updateFields)
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

    public void sendEmail(View v, List<String> emails, String message) {
        String[] emailList = emails.toArray(new String[emails.size()]);
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Settlement Info");
        intent.putExtra(Intent.EXTRA_EMAIL, emailList);
        startActivity(intent);

    }

    public void settleTheGroupExpenses(View view) {
        HashMap<String, Double> previousGroupBalance = new HashMap<>(group.getBalance());
        HashMap<String, Double> groupBalance = group.getBalance();
        groupBalance.forEach((key, value) -> groupBalance.put(key, 0.0));
        database.collection("Groups")
                .document(groupID)
                .update("balance", groupBalance)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String from;
                        String tokenId;
                        String message;
                        String amount;
                        String groupName = group.getName().toUpperCase();
                        Notification notification;
                        List<Account> externalAccounts = new ArrayList<>();
                        Map<Account, String> balanceStatus = new HashMap<>();
                        for (Account account : accounts) {
                            amount = previousGroupBalance.get(account.getId()).toString();
                            balanceStatus.put(account, amount);
                            if (account.isInternalAccount()) {
                                from = currentAccount.getOwnerName().toUpperCase();
                                message = "Hi! You owe " + amount + "Kr for settlement of expenses of group : " + groupName;
                                tokenId = account.getTokenID();
                                notification = new Notification(from, groupName, message, tokenId);
                                database.collection("Accounts")
                                        .document(account.getId()).collection("Notifications")
                                        .document(notification.getNotificationId())
                                        .set(notification)
                                        .addOnCompleteListener(task1 -> {

                                        });
                            } else if (previousGroupBalance.get(account.getId()) != 0) {
                                externalAccounts.add(account);
                            }
                        }

                        if (externalAccounts.size() > 0) {
                            boolean successfulSendMail = true;
                            for (Account account : externalAccounts) {
                                String ownerName = account.getOwnerName();
                                String amountForPerson = balanceStatus.get(account);
                                String subject = "WalletDroid settlement detail for group: "+groupName;
                                String messageContentIndividual ="Hi "+ownerName+"! Your balance is "+amountForPerson+" Kr in group: "+groupName;
                                // you can call sendEmail() method inside this for. this for goes through external users in group
                                // which they have balance different than zero.
                                String emailTo = account.getEmail();
                                String emailFrom = "sudutechio@gmail.com";
                                String emailPass = "M3hdi#23";
                                GMailSender sender = new GMailSender(emailFrom, emailPass);
                                try {
                                    sender.sendMail(subject, messageContentIndividual, emailFrom, emailTo);
                                } catch (Exception e){
                                    Log.e("Email problem: ", e.getMessage());
                                    successfulSendMail = false;
                                }
                            }
                            if(successfulSendMail)
                                Toast.makeText(GroupDetailActivity.this, "Emails SENT", Toast.LENGTH_SHORT).show();
                        } else {
                            finish();
                            startActivity(getIntent());
                        }
                    }
                });
    }

    public static Spanned fromHtml(String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(source);
        }
    }
}
