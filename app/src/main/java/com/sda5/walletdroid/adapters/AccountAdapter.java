package com.sda5.walletdroid.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sda5.walletdroid.R;
import com.sda5.walletdroid.models.Account;

import java.util.ArrayList;
import java.util.List;

public class AccountAdapter extends ArrayAdapter<Account> {
    private Context mContext;
    private final List<Account> accounts;

    public AccountAdapter(Context context, ArrayList<Account> accounts) {
        super(context, 0, accounts);
        mContext = context;
        this.accounts = accounts;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.account, parent, false);

        Account account = accounts.get(position);

        TextView txtAccount = listItem.findViewById(R.id.account);
        txtAccount.setText(account.getOwnerName());
        txtAccount.setTag(account.getId());

        return listItem;
    }
}
