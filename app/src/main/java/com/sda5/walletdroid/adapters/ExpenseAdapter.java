package com.sda5.walletdroid.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.sda5.walletdroid.R;
import com.sda5.walletdroid.models.Account;
import com.sda5.walletdroid.models.Expense;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ExpenseAdapter extends ArrayAdapter<Expense> {
    private Context mContext;
    private final List<Expense> expenses;
    private final List<String> selectedAccountIDList = new ArrayList<>();


    public ExpenseAdapter(Context context, ArrayList<Expense> expenses) {
        super(context, 0, expenses);
        mContext = context;
        this.expenses = expenses;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_expense_item, parent, false);

        Expense expense = expenses.get(position);

        final TextView tvExpenseTitle = listItem.findViewById(R.id.text_view_expense_title_item);
        final TextView tvExpenseDate = listItem.findViewById(R.id.text_view_expense_date_item);
        final TextView tvExpenseAmount = listItem.findViewById(R.id.text_view_expense_amount_item);

        tvExpenseTitle.setText(expense.getTitle());
        tvExpenseDate.setText(expense.getDate());
        tvExpenseAmount.setText(Double.toString(Math.round(expense.getAmount() * 10) / 10.0));
        tvExpenseDate.setTypeface(null, Typeface.ITALIC);

        return listItem;
    }
}
