package com.sda5.walletdroid.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sda5.walletdroid.R;
import com.sda5.walletdroid.helper.StartEndDate;
import com.sda5.walletdroid.models.Account;
import com.sda5.walletdroid.models.Expense;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SeeExpenseGraphForParticularCategory extends AppCompatActivity {


    private FirebaseFirestore database;
    private String currentUserId;
    private String accountId;
    private String categoryName;
    private ArrayList<Expense> expenses;
    private Spinner sprCategory;
    private Spinner sprTimePeriod;
    private String selectedCategory;
    private String selectedTimePeriod;
    private Integer selectedTimePeriodInteger;
    ArrayList<String> catlist = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_expense_graph_for_particular_category);

        database = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        currentUserId = auth.getCurrentUser().getUid();

        catlist.add("Grocery");
        catlist.add("Clothes");
        catlist.add("Transportation");
        catlist.add("Recurring");
        catlist.add("Eat out");
        catlist.add("Utility");
        catlist.add("Membership");
        catlist.add("Other");

        // Create spinner for user to choose the category of query
        sprCategory = findViewById(R.id.query_category);
        ArrayAdapter adapterCategory = new ArrayAdapter(this, android.R.layout.simple_spinner_item, catlist);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sprCategory.setAdapter(adapterCategory);
        sprCategory.setSelection(0);
        sprCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Map<String, Integer> timePeriod = new HashMap<>();

        timePeriod.put("Last One Year", 12);
        timePeriod.put("Last Six Months", 6);
        timePeriod.put("Last Three Months", 3);
        timePeriod.put("Last Month", 1);
        timePeriod.put("Select time", -1);

        // Create spinner for user to choose the time period of query
        sprTimePeriod = findViewById(R.id.query_month);
        ArrayAdapter adapterTimePeriod = new ArrayAdapter(this, android.R.layout.simple_spinner_item, timePeriod.keySet().toArray());
        adapterTimePeriod.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sprTimePeriod.setAdapter(adapterTimePeriod);
        sprTimePeriod.setSelection(0);
        sprTimePeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTimePeriod = (String) parent.getItemAtPosition(position);
                selectedTimePeriodInteger = timePeriod.get(selectedTimePeriod);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void runQuery(View view) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        StartEndDate startEndDate = getStartEndDate(selectedTimePeriodInteger);
        long startDate = startEndDate.getStartDate();
        long endDate = startEndDate.getEndDate();

        expenses = new ArrayList<>();
        database.collection("Accounts").whereEqualTo("userID", currentUserId).get().addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot accountSnapshot = task.getResult();
                        if (null != accountSnapshot) {
                            Optional<Account> account = accountSnapshot.toObjects(Account.class).stream().findFirst();
                            if (account.isPresent()) {
                                accountId = account.get().getId();
                                database.collection("Expenses")
                                        .whereEqualTo("payerAccountId", accountId)
                                        .whereEqualTo("category", selectedCategory)
                                        .whereGreaterThanOrEqualTo("dateMillisec", startDate)
                                        .whereLessThanOrEqualTo("dateMillisec", endDate)
                                        .orderBy("dateMillisec")
                                        .get().addOnSuccessListener(queryDocumentSnapshots -> {
                                    Map<String, Double> totalExpenseMapByMonth = new HashMap<>();
                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        Expense expense = documentSnapshot.toObject(Expense.class);
                                        expenses.add(expense);
                                        int expenseMonth = LocalDate.parse(expense.getDate(), formatter).getMonth().getValue();
                                        int expenseYear = LocalDate.parse(expense.getDate(), formatter).getYear();
                                        String key = expenseYear + "-" + expenseMonth;

                                        Double totalAmountForMonth = totalExpenseMapByMonth.getOrDefault(key, 0.0);
                                        totalAmountForMonth += expense.getAmount();
                                        totalExpenseMapByMonth.put(key, totalAmountForMonth);
                                    }

                                    System.out.println("______________________" + totalExpenseMapByMonth.entrySet().toString());
                                });
                            }
                        }
                    }
                }
        );
    }

    public StartEndDate getStartEndDate(Integer howManyMonths) {
        Integer startMonth;
        Integer startYear;
        Integer todaysMonth;
        Integer todaysYear;
        long startDate;

        LocalDate todaysDateLocaleDate = LocalDate.now();
        long todaysDateLong = todaysDateLocaleDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        todaysMonth = todaysDateLocaleDate.getMonth().getValue();
        todaysYear = todaysDateLocaleDate.getYear();
        startYear = todaysYear;

        if (howManyMonths == todaysMonth) {
            startMonth = 12;

        } else if (howManyMonths < todaysMonth) {
            startMonth = todaysMonth - howManyMonths;
        } else {
            while (howManyMonths > todaysMonth) {
                todaysMonth += 12;
                startYear -= 1;
            }
            startMonth = todaysMonth - howManyMonths + 1;
        }

        startDate = LocalDate.of(startYear, startMonth, 1)
                .atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        StartEndDate startEndDate = new StartEndDate(startDate, todaysDateLong);

        return startEndDate;
    }

    public StartEndDate getStartEndDateForOneMonth(int month, int year) {

        long startDateLong;
        long endDateLong;

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        startDateLong = startDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        endDateLong = endDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        return new StartEndDate(startDateLong, endDateLong);

    }
}
