package com.sda5.walletdroid.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
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
        final ImageView categoryImage = listItem.findViewById(R.id.expense_list_category_picture);


        if(expense.getCategory().equalsIgnoreCase("grocery")) categoryImage.setImageResource(R.drawable.grocery);
        if(expense.getCategory().equalsIgnoreCase("Clothes")) categoryImage.setImageResource(R.drawable.clothes);
        if(expense.getCategory().equalsIgnoreCase("Transportation")) categoryImage.setImageResource(R.drawable.transportation);
        if(expense.getCategory().equalsIgnoreCase("Eat out")) categoryImage.setImageResource(R.drawable.eat);
        if(expense.getCategory().equalsIgnoreCase("Recurring")) categoryImage.setImageResource(R.drawable.recurring);
        if(expense.getCategory().equalsIgnoreCase("Utility")) categoryImage.setImageResource(R.drawable.utility);
        if(expense.getCategory().equalsIgnoreCase("Membership")) categoryImage.setImageResource(R.drawable.membership);
        if(expense.getCategory().equalsIgnoreCase("Other")) categoryImage.setImageResource(R.drawable.other);

        tvExpenseTitle.setText(expense.getTitle());
        tvExpenseDate.setText(expense.getDate());
        Long individualAmount = (Math.round(expense.getAmount())/ (expense.getExpenseAccountIds().size()));
        tvExpenseAmount.setText(Long.toString(individualAmount) + " SEK");
        tvExpenseDate.setTypeface(null, Typeface.ITALIC);

        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), com.sda5.walletdroid.activities.ExpenseDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("expense", expense);
                intent.putExtras(bundle);
                getContext().startActivity(intent);
            }
        });

        return listItem;
    }
}
