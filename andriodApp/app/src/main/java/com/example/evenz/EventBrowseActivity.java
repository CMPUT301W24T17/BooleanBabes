package com.example.evenz;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Hashtable;

//call ArrayList<Event> eventDataList

public class EventBrowseActivity extends AppCompatActivity {
    private RecyclerView eventsRecyclerView;
    private EventAdapter eventAdapter;
    private ArrayList<Event> eventDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendees_browse_events);

        eventsRecyclerView = findViewById(R.id.eventsRecyclerView);

        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize eventDataList
        eventDataList = new ArrayList<>();
        eventAdapter = new EventAdapter(this, eventDataList);
        eventsRecyclerView.setAdapter(eventAdapter);

        fetchEvents();

        findViewById(R.id.back_attendee_browse_events).setOnClickListener(v -> finish());
    }

    private void fetchEvents() {
        // Code to fetch events from Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("events").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshots, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString());
                    return;
                }

                if (querySnapshots != null) {
                    eventDataList.clear();
                    for (QueryDocumentSnapshot doc : querySnapshots) {
                        String eventID = doc.getId();
                        // Parse the event data and add it to your list, then notify the adapter
                        Event tempEvent = EventUtility.parseEvent(doc);

                        eventDataList.add(tempEvent);
                    }
                    eventAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    // Helper method to parse the Firestore document into an Event object
    private Event parseEvent(QueryDocumentSnapshot doc) {
        // Example of reconstructing a Geolocation object
        // Assuming you store geolocation as a map with latitude and longitude
        float xcoord = doc.contains("xcoord") ? ((Number) doc.get("xcoord")).floatValue() : 0; //TODO: add geolocation stuff, figure it out
        float ycoord = doc.contains("ycoord") ? ((Number) doc.get("ycoord")).floatValue() : 0;
        String geolocationID = doc.getString("geolocationID"); // Assuming there's an ID field; adjust if necessary

        Geolocation geolocation = new Geolocation(geolocationID, xcoord, ycoord);

        // Using placeholders for Bitmaps as you'll load them asynchronously in the adapter/view
        Bitmap placeholderBitmap = null; // TODO: add actual QR for attendee checkIN and Browse

        return new Event(doc.getString("organizationName"), doc.getString("eventName"), doc.getString("eventPosterID"),
                doc.getString("description"), geolocation, placeholderBitmap,
                placeholderBitmap, 0,
                new Hashtable<>(), doc.getDate("eventDate"), new ArrayList<String>(), doc.getString("location"));
    }

}


