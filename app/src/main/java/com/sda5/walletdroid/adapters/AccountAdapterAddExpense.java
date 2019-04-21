package com.sda5.walletdroid.adapters;

        import android.content.Context;
//        import android.support.annotation.NonNull;
//        import android.support.annotation.Nullable;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.CheckBox;
        import android.widget.CompoundButton;
        import android.widget.TextView;

        import com.sda5.walletdroid.R;
        import com.sda5.walletdroid.models.Account;

        import java.util.ArrayList;
        import java.util.List;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;

public class AccountAdapterAddExpense extends ArrayAdapter<Account> {
    private Context mContext;
    private final List<Account> accounts;
    private final boolean showCheckboxes;
    private final List<String> selectedExpenseUsersIDList = new ArrayList<>();
    private final List<String> selectedExpenseUsersNameList = new ArrayList<>();


    public AccountAdapterAddExpense(Context context, ArrayList<Account> accounts, boolean showCheckboxes) {
        super(context, 0, accounts);
        mContext = context;
        this.accounts = accounts;
        this.showCheckboxes = showCheckboxes;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_account_item, parent, false);

        final Account account = accounts.get(position);

        TextView textViewAccount = listItem.findViewById(R.id.textview_account_item);
        textViewAccount.setText(account.getOwnerName());
        textViewAccount.setTag(account.getId());

        final CheckBox checkBoxAccount = listItem.findViewById(R.id.checkbox_account_item);
        checkBoxAccount.setTag(account.getId());

        if (showCheckboxes) {
            checkBoxAccount.setVisibility(View.VISIBLE);
        } else {
            checkBoxAccount.setVisibility(View.GONE);
        }

        checkBoxAccount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String selectedAccountID = checkBoxAccount.getTag().toString();
                if (isChecked) {
                    selectedExpenseUsersIDList.add(selectedAccountID);
                    selectedExpenseUsersNameList.add(account.getOwnerName());
                } else {
                    selectedExpenseUsersIDList.remove(selectedAccountID);
                    selectedExpenseUsersNameList.add(account.getOwnerName());
                }
            }
        });

        return listItem;
    }

    public List<String> getSelectedExpenseUsersIDList() {
        return selectedExpenseUsersIDList;
    }

    public List<String> getSelectedExpenseUsersNameList(){
        return selectedExpenseUsersNameList;
    }
}
