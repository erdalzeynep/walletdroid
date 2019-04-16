package com.sda5.walletdroid.activities;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sda5.walletdroid.R;
import com.sda5.walletdroid.models.Account;
import com.sda5.walletdroid.models.Category;
import com.sda5.walletdroid.models.Expense;
import com.sda5.walletdroid.models.Group;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

public class AddExpenseActicity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner sprCategory;
    private Spinner sprGroup;
    private Button btnAdd;
    private Group selectedGroup;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private boolean[] checkedUsers;
    private ArrayList<Integer> expenseUsersCheck;

    // To save on database
    private EditText etTitle;
    private EditText etAmount;
    private LocalDate selectedDate;
    private Category selectedCategory;
    private ArrayList<Account> expenseUsers;

    // Firestore database stuff
    private FirebaseFirestore db;
    private CollectionReference expenseCollectionRef;
    private CollectionReference groupCollectionRef;
    private CollectionReference accountCollectionRef;
    private DocumentReference docRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
        expenseCollectionRef = db.collection("Expenses");
        groupCollectionRef = db.collection("Groups");
        accountCollectionRef = db.collection("Accounts");


        etTitle = findViewById(R.id.txt_addExpense_expenseTitle);
        etAmount = findViewById(R.id.txt_addExpense_expenseAmount);

        // Create sample category list for now
        // TODO update this when category is decided by team. it should retrieve data from fire store
        ArrayList<Category> catlist = new ArrayList<>();
        catlist.add(new Category("Food", 2000));
        catlist.add(new Category("Clothes", 3000));
        catlist.add(new Category("Transportation",5000));

        // Create spinner for user to choose the category of expense
        sprCategory = findViewById(R.id.spr_addExpense_category);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, catlist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sprCategory.setAdapter(adapter);
        sprCategory.setOnItemSelectedListener(this);


        // Get the date of expense from user
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                selectedDate = LocalDate.of(year, month, dayOfMonth);
                Toast.makeText(AddExpenseActicity.this, selectedDate.toString(), Toast.LENGTH_SHORT).show();
            }
        };


        // Create sample Group list
        //TODO update this when Zeynep is done by appUsers. it should retrieve data from firestore
//        Account a = new Account("Mehdi", "0793095896", "m3hdi.ap@gmail.com");
//        Account b = new Account("Obaid", "4567890987", "obaid@gmail.com");
//        Account c = new Account("Manimala", "34568876", "Manimala@gmail.com");


        // Create sample Group list
        //TODO update this when Zeynep is done by Group. it should retrieve data from firestore
//        ArrayList<Account> grouplist = new ArrayList<>();
//        grouplist.add(a);
//        grouplist.add(b);
//        grouplist.add(c);
//        Group sampleGroup1 = new Group(1, "Friends", grouplist );

        final ArrayList<Group> groups = new ArrayList<>();
//        groups.add(sampleGroup1);

        // Spinner for groups

        sprGroup = findViewById(R.id.spr_addExpense_group);
        groupCollectionRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            Group group = documentSnapshot.toObject(Group.class);
                            groups.add(group);
                        }

                    }

                });

        ArrayAdapter groupAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, groups);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sprGroup.setAdapter(groupAdapter);
        sprGroup.setOnItemSelectedListener(this);



    }

    // Method to be called when user choose a category for the expense
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner sprCategory = (Spinner) parent;
        Spinner sprGroup = (Spinner) parent;
        if(sprCategory.getId()==R.id.spr_addExpense_category){
            selectedCategory = (Category) parent.getItemAtPosition(position);
        }
        if(sprGroup.getId() == R.id.spr_addExpense_group){
            selectedGroup = (Group) parent.getItemAtPosition(position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    // specify people in group who actually used the expense
//    public void addExpenseUsers(View view) {
//        expenseUsers  = new ArrayList<>();
//        expenseUsersCheck = new ArrayList<>();
//
//        if(selectedGroup == null){
//            Toast.makeText(this, "First select the group", Toast.LENGTH_SHORT).show();
//        } else {
//            checkedUsers = new boolean[selectedGroup.getAccountIdList().size()];
//            AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
//            mBuilder.setTitle("Select users");
//            String[] stringArrayGroupList = new String[selectedGroup.getAccountIdList().size()];
//            int i = 0;
//            for (Account user: selectedGroup.getAccountIdList()){
//                stringArrayGroupList [i] = user.getName();
//                i++;
//            }
//
//            mBuilder.setMultiChoiceItems(stringArrayGroupList, checkedUsers, new DialogInterface.OnMultiChoiceClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int position, boolean isChecked) {
//                    if(isChecked){
//                        if(!expenseUsersCheck.contains(position)){
//                            expenseUsersCheck.add(position);
//                        } else{
//                            expenseUsersCheck.remove(position);
//                        }
//                    }
//                }
//            });
//
//            mBuilder.setCancelable(false);
//            mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    for(int i = 0; i < expenseUsersCheck.size(); i++){
//                        expenseUsers.add(selectedGroup.getList().get(expenseUsersCheck.get(i)));
//                    }
//                }
//            });
//
//            mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            });
//
//            mBuilder.setNeutralButton("Select All", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    expenseUsers = selectedGroup.getList();
//                }
//            });
//
//            AlertDialog dialog = mBuilder.create();
//            dialog.show();
//        }
//    }
//
//    public void save(View view) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
//        String selectedDateString = selectedDate.format(formatter);
//
//        Expense expense = new Expense(etTitle.getText().toString(),
//                                Double.parseDouble(etAmount.getText().toString()),
//                                selectedCategory,
//                                null, expenseUsers,
//                                selectedDateString,false);
//
//        docRef = expenseCollectionRef.document();
//        docRef
//                .set(expense)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(AddExpenseActicity.this, "Done", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(AddExpenseActicity.this, "Error", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
}
