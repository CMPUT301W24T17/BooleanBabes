package com.example.evenz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

/**
 * The AttendeesActivity class is an activity that displays a list of attendees for an event.
 * <p>
 * This class extends the AppCompatActivity class and overrides the onCreate method to set up the RecyclerView and load the attendees from Firebase.
 * <p>
 * The AttendeesActivity class has a RecyclerView for displaying the attendees, an AttendeeAdapter for binding the attendees to the RecyclerView, and a list of Attendee objects that it displays.
 *
 * @version 1.0
 * @see AppCompatActivity
 * @see RecyclerView
 * @see AttendeeAdapter
 * @see Attendee
 */
public class AttendeesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AttendeeAdapter adapter;
    private List<Attendee> attendeesList;

    /**
     * Called when the activity is starting. This is where most initialization should go.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_attendees_at_event);

        // Initialize the RecyclerView and set its layout manager
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the FirebaseAttendeeManager to fetch attendees from Firebase
        FirebaseAttendeeManager firebaseAttendeeManager = new FirebaseAttendeeManager();

        // Call getEventAttendees with the event ID
        Task<List<Attendee>> getAttendeesTask = firebaseAttendeeManager.getEventAttendees("Drake Concert");

// Add a listener to handle the result when the task completes
        getAttendeesTask.addOnSuccessListener(new OnSuccessListener<List<Attendee>>() {
            @Override
            public void onSuccess(List<Attendee> attendees) {
                // The attendees list contains all the attendees for the "Drake Concert" event
                // Here you can update your RecyclerView or UI components
                // For example:
                attendeesList = attendees;
                AttendeeAdapter adapter = new AttendeeAdapter(attendeesList);
                recyclerView.setAdapter(adapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle the error case
                Toast.makeText(AttendeesActivity.this, "Error fetching attendees: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Called when the header icon is clicked. This method starts the MainActivity.
     *
     * @param view The view that was clicked.
     */
    public void onHeaderIconClicked(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}