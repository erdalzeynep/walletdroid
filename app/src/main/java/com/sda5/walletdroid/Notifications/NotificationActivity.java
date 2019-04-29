package com.sda5.walletdroid.Notifications;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.annotations.Nullable;
import com.sda5.walletdroid.R;

public class NotificationActivity extends AppCompatActivity {
    TextView NotifyData;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        NotifyData = (TextView)findViewById(R.id.text);

        String message = getIntent().getStringExtra("Message");
        String from  = getIntent().getStringExtra("From");

        NotifyData.setText("From: " + from + " Message: "+message);
    }
}
