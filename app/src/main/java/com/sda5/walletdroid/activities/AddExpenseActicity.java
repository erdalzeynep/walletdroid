package com.sda5.walletdroid.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
//import android.support.annotation.NonNull;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sda5.walletdroid.R;
import com.sda5.walletdroid.models.Account;
import com.sda5.walletdroid.models.Category;
import com.sda5.walletdroid.models.Expense;
import com.sda5.walletdroid.models.Group;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class AddExpenseActicity extends AppCompatActivity {

    private ArrayList<Group> groups = new ArrayList<>();

    private Spinner sprCategory;
    private Spinner sprBuyer;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Group selectedGroup;


    // To save on database
    private EditText etTitle;
    private EditText etAmount;
    private LocalDate selectedDate;
    private Category selectedCategory;
    private ArrayList<Account> accountsForBuyer = new ArrayList<>();
    private ArrayList<String> expenseUsersId = new ArrayList<>();
    private ArrayList<String> expenseUsersName = new ArrayList<>();
    private String buyerId;

    // Firestore database stuff
    private FirebaseFirestore database;

    private String accountId;
    String currentUserId;
    private FirebaseAuth mAuth;
    private String groupId;

    // onRestoreInstanceState
    static String tempTitle;
    static String tempAmount;
    static int sprCategoryDefaultItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        FirebaseApp.initializeApp(this);
        database = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        groupId = getIntent().getStringExtra("group_id");
        expenseUsersId = getIntent().getStringArrayListExtra("expenseUsersIds");
        expenseUsersName = getIntent().getStringArrayListExtra("expenseUsersAccounts");

        etTitle = findViewById(R.id.txt_addExpense_expenseTitle);
        etAmount = findViewById(R.id.txt_addExpense_expenseAmount);


        // Create sample category list for now
        // TODO update this when category is decided by team. it should retrieve data from fire store
        ArrayList<Category> catlist = new ArrayList<>();
        catlist.add(new Category("Food", 2000));
        catlist.add(new Category("Clothes", 3000));
        catlist.add(new Category("Transportation", 5000));



        // Create spinner for user to choose the category of expense
        sprCategory = findViewById(R.id.spr_addExpense_category);
        ArrayAdapter adapterCategory = new ArrayAdapter(this, android.R.layout.simple_spinner_item, catlist);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sprCategory.setAdapter(adapterCategory);
        sprCategory.setSelection(sprCategoryDefaultItem);
        sprCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = (Category) parent.getItemAtPosition(position);
                sprCategoryDefaultItem = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (expenseUsersId != null) {
            sprBuyer = findViewById(R.id.spr_addExpense_buyer);
            ArrayAdapter adapterBuyer = new ArrayAdapter(this, android.R.layout.simple_spinner_item, expenseUsersName);
            adapterBuyer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adapterBuyer.notifyDataSetChanged();
            sprBuyer.setAdapter(adapterBuyer);
            sprBuyer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String buyerSelected = (String) parent.getItemAtPosition(position);
                    for (int i = 0; i < expenseUsersName.size(); i++) {
                        if (buyerSelected == expenseUsersName.get(i)) {
                            buyerId = expenseUsersId.get(i);
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }

        // Get the date of expense from user
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                selectedDate = LocalDate.of(year, month, dayOfMonth);
                Toast.makeText(AddExpenseActicity.this, selectedDate.toString(), Toast.LENGTH_SHORT).show();
            }
        };
    }


    public void pickDate(View view) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                year, month, day);
        dialog.show();
    }


    public void addExpenseUsers(View view) {
        if(!etTitle.getText().toString().trim().isEmpty() || !etAmount.getText().toString().trim().isEmpty()){
            tempTitle = etTitle.getText().toString();
            tempAmount = etAmount.getText().toString();
        }
        Intent intent = new Intent(this, ChooseGroupForExpenseActivity.class);
        startActivity(intent);
    }

    public void saveExpense(View v){
        if(selectedDate == null){
            selectedDate = LocalDate.now();
        }
        if(etTitle.getText().toString().trim().isEmpty() ||
                etAmount.getText().toString().trim().isEmpty() ||
                selectedCategory == null ||
                groupId == null ||
                selectedDate == null ||
                expenseUsersId.size() == 0 || expenseUsersId == null ||
                buyerId == null){
            Toast.makeText(this, "Please enter all fields first", Toast.LENGTH_SHORT).show();
        }else{
            String title = etTitle.getText().toString().trim();
            double amount = Double.parseDouble(etAmount.getText().toString());
            String date = selectedDate.toString();
            Expense expense = new Expense(title, amount, selectedCategory, buyerId, groupId, date,expenseUsersId, false);

            database.collection("Expenses").document(expense.getId()).set(expense).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(AddExpenseActicity.this, "Added successfully", Toast.LENGTH_SHORT).show();
                    etAmount.setText("");
                    etTitle.setText("");
                    sprCategory.setSelection(0);
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddExpenseActicity.this, "Sth is wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(!etTitle.getText().toString().trim().isEmpty()||!etAmount.getText().toString().trim().isEmpty()){
            outState.putString("Title", etTitle.getText().toString().trim());
            outState.putString("Amount", etAmount.getText().toString().trim());
            outState.putInt("CategoryPosition", sprCategoryDefaultItem);

            tempTitle = etTitle.getText().toString().trim();
            tempAmount = etAmount.getText().toString().trim();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        tempTitle = savedInstanceState.getString("Title");
        tempAmount = savedInstanceState.getString("Amount");
        sprCategoryDefaultItem = savedInstanceState.getInt("CategoryPosition");
    }

    @Override
    protected void onResume() {
        super.onResume();
        etTitle.setText(tempTitle);
        etAmount.setText(tempAmount);
    }
}


