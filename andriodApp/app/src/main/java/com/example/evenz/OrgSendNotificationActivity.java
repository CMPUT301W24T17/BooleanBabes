package com.example.evenz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.UnstableApi;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrgSendNotificationActivity extends AppCompatActivity {
    TextView post;
    String eventID;
    EditText editTextNotificationType, editTextNotificationInfo;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();
        assert b != null;
        eventID = b.getString("eventID");

        setContentView(R.layout.org_send_event_notification);
        post = findViewById(R.id.post_notification_button);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initUI();
                postNotificaion();
                Intent intent = new Intent(new Intent(OrgSendNotificationActivity.this, HomeScreenActivity.class));
                Bundle b = new Bundle();
                b.putString("role", "organizer");
                b.putString("eventID", eventID);
                intent.putExtras(b);
                startActivity(intent);
                startActivity(new Intent(OrgSendNotificationActivity.this, HomeScreenActivity.class));
            }
        });

        findViewById(R.id.back_send_notification).setOnClickListener(v -> finish());
    }

    private void initUI() {
        editTextNotificationType = findViewById(R.id.editTextNotificationType);
        editTextNotificationInfo = findViewById(R.id.editTextNotificationInfo);
    }

    private void postNotificaion () {
        String notificationType = editTextNotificationType.getText().toString().trim();
        String notificationInfo = editTextNotificationInfo.getText().toString().trim();

        fetchEventDetailsAndNotifications(eventID, notificationType, notificationInfo);
    }

    private void fetchEventDetailsAndNotifications(String eventID, String notificationType,
                                                   String notificationInfo) {
        EventUtility.notificationOps(notificationType, notificationInfo, eventID, 1);
    }
}