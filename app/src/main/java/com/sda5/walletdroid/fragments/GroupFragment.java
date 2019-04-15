package com.sda5.walletdroid.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.sda5.walletdroid.activities.CreateNewGroupActivity;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_group, null);
        mAuth = FirebaseAuth.getInstance();

        database = FirebaseFirestore.getInstance();

        String currentUSerID = mAuth.getCurrentUser().getUid();

        ListView listView =  v.findViewById(R.id.group_list);
        listView.setScrollingCacheEnabled(false);

        groupAdapter = new GroupAdapter(v.getContext(), groups);
        listView.setAdapter(groupAdapter);


        database.collection("Groups")
                .whereEqualTo("userID", currentUSerID)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }

                        for (QueryDocumentSnapshot doc : value) {
                            groups.add(doc.toObject(Group.class));
                        }
                        groupAdapter.notifyDataSetChanged();
                    }
                });
        return v;
    }
}
