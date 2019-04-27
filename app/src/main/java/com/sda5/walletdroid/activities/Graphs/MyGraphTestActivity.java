package com.sda5.walletdroid.activities.Graphs;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.sda5.walletdroid.R;

import java.util.HashMap;

public class MyGraphTestActivity extends AppCompatActivity {

    final HashMap<String, Integer> expenseMaps = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_graph_test);

        expenseMaps.put("Food",32);
        expenseMaps.put("Transport",55);
        expenseMaps.put("Clothes",77);
        expenseMaps.put("Books",120);
        expenseMaps.put("Car",200);
        expenseMaps.put("Trips",10);
        expenseMaps.put("Children",77);
        expenseMaps.put("Others",150);

        Intent intent = new Intent(this,MyLineGraph.class);
        intent.putExtra("map",expenseMaps);
        startActivity(intent);
        finish();
    }
}
