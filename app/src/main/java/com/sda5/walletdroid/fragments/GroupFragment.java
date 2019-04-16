package com.sda5.walletdroid.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sda5.walletdroid.R;
import com.sda5.walletdroid.adapters.GroupAdapter;
import com.sda5.walletdroid.models.Account;
import com.sda5.walletdroid.models.Group;

import java.util.ArrayList;
import java.util.Optional;

public class GroupFragment extends Fragment {
    private GroupAdapter groupAdapter;
    private ArrayList<Group> groups = new ArrayList<>();
    FirebaseFirestore database;
    private FirebaseAuth mAuth;
    private String accountId;
    String currentUSerID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_group, null);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        currentUSerID = mAuth.getCurrentUser().getUid();


        database.collection("Accounts").whereEqualTo("userID", currentUSerID).get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot accountSnapshot = task.getResult();
                            if (null != accountSnapshot) {
                                Optional<Account> account = accountSnapshot.toObjects(Account.class).stream().findFirst();
                                if (account.isPresent()) {
                                    accountId = account.get().getId();
                                    database.collection("Groups").whereArrayContains("accountIdList", accountId)
                                            .get()
                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                                        Group group = documentSnapshot.toObject(Group.class);
                                                        groups.add(group);
                                                    }
                                                    groupAdapter.notifyDataSetChanged();
                                                }
                                            });

                                } else {

                                }
                            }
                        }
                    }
                }

        );

        ListView listView = v.findViewById(R.id.group_list);
        listView.setScrollingCacheEnabled(false);

        groupAdapter = new GroupAdapter(v.getContext(), groups);
        listView.setAdapter(groupAdapter);

        return v;
    }
}
