package com.example.evenz;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

public class InitialPageActivity extends AppCompatActivity {

    private DocumentReference doc;
    private FirebaseFirestore db;
    private CollectionReference usersRef;

    private String deviceID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container_initial_page);

        FrameLayout attendeeLayout = findViewById(R.id.container_group1);
        FrameLayout organizerLayout = findViewById(R.id.container_group2);
        FrameLayout guestLayout = findViewById(R.id.container_group3);

        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("users");

        String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        usersRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshots,
                                @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString());
                    return;
                }
                if (querySnapshots != null) {
                    for (QueryDocumentSnapshot doc: querySnapshots) {
                        String userID = doc.getId();
                        if (userID.equals(deviceID)) {
                            if (doc.getString("userType").equals("attendee")) {
                                if (doc.getString("eventList") != null) {
                                    Intent intent = new Intent(InitialPageActivity.this, HomeScreenActivity.class);
                                    Bundle b = new Bundle();
                                    b.putString("role", "attendee");
                                    b.putString("eventID", "CLQuoRALxppaIHscuwnG");
                                    intent.putExtras(b);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(InitialPageActivity.this, HomeScreenActivity.class);
                                    Bundle b = new Bundle();
                                    b.putString("role", "attendee");
                                    b.putString("eventID", "CLQuoRALxppaIHscuwnG");
                                    intent.putExtras(b);
                                    startActivity(intent);
                                }
                            } else if (doc.getString("userType").equals("organizer")) {
                                Intent intent = new Intent(InitialPageActivity.this, HomeScreenActivity.class); //TODO: replace with ORG homepage
                                Bundle b = new Bundle();
                                b.putString("role", "organizer");
                                b.putString("eventID", doc.getString("eventList"));
                                intent.putExtras(b);
                                startActivity(intent);
                            }
                        }
                    }
                }
            }
        });


        attendeeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InitialPageActivity.this, UserEditProfileActivity.class);
                Bundle b = new Bundle();
                b.putString("role", "attendee");
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        organizerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InitialPageActivity.this, EventCreationActivity.class);
                Bundle b = new Bundle();
                b.putString("role", "organizer");
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        guestLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: make GUEST LOGIN, VERIFY with TA
            }
        });
    }
}