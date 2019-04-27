package com.sda5.walletdroid.activities.Graphs;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.sda5.walletdroid.R;

import java.util.ArrayList;

public class MyPieChartActivityTest extends AppCompatActivity  {

     ArrayList<String> expenseCat = new ArrayList<>();
     ArrayList<Integer> expenseValues = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pie_chart_test);

        expenseCat.add(0,"Recreation");
        expenseCat.add(1,"Food");
        expenseCat.add(2,"Transport");
        expenseCat.add(3,"Books");
        expenseCat.add(4,"Car");
        expenseCat.add(5,"Savings");
        expenseCat.add(6,"Sports");
        expenseCat.add(7,"Eat out");

        expenseValues.add(0,189);
        expenseValues.add(1,34);
        expenseValues.add(2,66);
        expenseValues.add(3,10);
        expenseValues.add(4,99);
        expenseValues.add(5,27);
        expenseValues.add(6,150);
        expenseValues.add(7,60);

        Intent intent = new Intent(this,MyPieChartActivity.class);
        intent.putExtra("listCat",expenseCat);
        intent.putExtra("listValue",expenseValues);
        startActivity(intent);
        finish();
    }
}
