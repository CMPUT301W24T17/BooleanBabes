package com.example.evenz;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * The FirebaseAttendeeManager class is responsible for managing the attendees data in Firebase.
 * <p>
 * This class provides methods to submit an attendee to Firebase, get all attendees for a specific event, get all attendees, and clear all attendees.
 *
 * @version 1.0
 * @see FirebaseFirestore
 * @see CollectionReference
 * @see Attendee
 */
public class FirebaseAttendeeManager {

    // Firestore instance
    private final FirebaseFirestore db;
    private final CollectionReference ref;

    /**
     * Default constructor that initializes the Firestore instance and the collection reference.
     */
    public FirebaseAttendeeManager() {
        this.db = FirebaseFirestore.getInstance();
        this.ref = db.collection("attendee");
    }

    /**
     * Constructor that takes a Firestore instance as a parameter.
     *
     * @param db the Firestore instance
     */
    public FirebaseAttendeeManager(FirebaseFirestore db) {
        this.db = db;
        this.ref = db.collection("attendee");
    }

    /**
     * Submits an attendee to Firebase.
     *
     * @param attendee the attendee to be submitted
     */
    public void submitAttendee(Attendee attendee) {
        // Convert Attendee object to a Map
        Map<String, Object> attendeeMap = new HashMap<>();
        attendeeMap.put("name", attendee.getName());
        attendeeMap.put("profilePicID", attendee.getProfilePicID());
        attendeeMap.put("phone", attendee.getPhone());
        attendeeMap.put("email", attendee.getEmail());
        attendeeMap.put("geolocation", attendee.getGeolocation() != null ? attendee.getGeolocation() : null);
        attendeeMap.put("notification", attendee.isNotifications());
        attendeeMap.put("eventList", attendee.getEventList());

        // Generate a new document ID
        String documentId = db.collection("attendee").document().getId();

        // Submit the attendee to Firebase
        db.collection("attendee").document(documentId).set(attendeeMap)
                .addOnSuccessListener(aVoid -> Log.d("submitAttendee", "DocumentSnapshot successfully written!"))
                .addOnFailureListener(e -> Log.w("submitAttendee", "Error writing document", e));
    }

    /**
     * Fetches all attendees for a specific event from Firebase.
     *
     * @param eventID the ID of the event
     * @return a task that represents the asynchronous operation of fetching the attendees
     */
    public Task<List<Attendee>> getEventAttendees(String eventID) {
        final List<Attendee> attendeesList = new ArrayList<>();

        return FirebaseFirestore.getInstance()
                .collection("attendee")
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            List<String> eventList = (List<String>) document.get("eventList");
                            if (eventList != null && eventList.contains(eventID)) {
                                Attendee attendee = new Attendee(
                                        document.getString("name"),
                                        document.getString("profilePicID"),
                                        document.getString("phone"),
                                        document.getString("email"),
                                        null, // geolocation is null as per your screenshot
                                        document.getBoolean("notification"),
                                        new ArrayList<>(eventList) // Create a new ArrayList from eventList to avoid UnsupportedOperationException
                                );
                                attendeesList.add(attendee);
                            }
                        }
                        return attendeesList;
                    } else {
                        throw task.getException();
                    }
                });
    }


    /**
     * Fetches all attendees from Firebase.
     *
     * @return a task that represents the asynchronous operation of fetching the attendees
     */
    public Task<List<Attendee>> getAllAttendeesAsynchronously() {
        final List<Attendee> attendeesList = new ArrayList<>();

        return db.collection("attendee").get().continueWith(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    Attendee attendee = new Attendee(
                            document.getString("name"),
                            document.getString("profilePicID"),
                            document.getString("phone"),
                            document.getString("email"),
                            null,
                            Boolean.TRUE.equals(document.getBoolean("notification")),
                            null
                    );
                    attendeesList.add(attendee);
                    // Log the details of the attendee for debugging purposes
                    Log.d("getAllAttendees", "Name: " + attendee.getName() + ", Email: " + attendee.getEmail());
                }
            }
            return attendeesList;
        });
    }

    /**
     * Clears all attendees from Firebase.
     */
    public void clearAttendees() {
        ref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    ref.document(document.getId()).delete()
                            .addOnSuccessListener(aVoid -> System.out.println("clearAttendees: " + document.getId() + " was successfully deleted."))
                            .addOnFailureListener(e -> System.out.println("clearAttendees: Failed to delete " + document.getId() + ". Error: " + e.getMessage()));
                }
            } else {
                System.out.println("clearAttendees: Error getting documents: " + task.getException());
            }
        });
    }
}