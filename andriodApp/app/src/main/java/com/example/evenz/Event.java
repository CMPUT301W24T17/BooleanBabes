package com.example.evenz;

import static android.content.ContentValues.TAG;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Map;

public class Event
{
    private String organizationName;
    private String eventName;
    private String eventPosterID;
    private String description;
    private Geolocation geolocation;
    private Bitmap qrCodeBrowse;
    private Bitmap qrCodeCheckIn;
    private int eventAttendLimit;
    private Date eventDate;
    private Map<String, Long> userList;

    /**
     * This is the public constructor to create an event
     *
     * @param organizationName The name of the Organizer of the event
     * @param eventName          The name of the event
     * @param eventPosterID      The id for the event poster image
     * @param description        The description of the event
     * @param geolocation        The location of the event
     * @param qrCodeBrowse       The qrcode to browse the event
     * @param qrCodeCheckIn      The qrcode to check in to the event
     * @param eventAttendLimit   The limit of attendees in the event
     * @param userList           The list of users signed up to attend the event which is the number of times checked in (0 is rsvp) and the id of the user
     */
    public Event(String organizationName, String eventName, String eventPosterID, String description, Geolocation geolocation, Bitmap qrCodeBrowse, Bitmap qrCodeCheckIn, int eventAttendLimit, Map<String, Long> userList, Date eventDate)
    {
        this.eventName = eventName;
        this.eventPosterID = eventPosterID;
        this.description = description;
        this.geolocation = geolocation;
        this.qrCodeBrowse = qrCodeBrowse;
        this.qrCodeCheckIn = qrCodeCheckIn;
        this.userList = userList;
        this.eventAttendLimit = eventAttendLimit;
        this.organizationName = organizationName;
        this.eventDate = eventDate;
    }

    public String getOrganizationName() {
        return this.organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public Date getEventDate() {
        return this.eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventName() {
        return this.eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventPosterID() {
        return eventPosterID;
    }

    public void setEventPosterID(String eventPosterID) {
        this.eventPosterID = eventPosterID;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Geolocation getGeolocation() {
        return this.geolocation;
    }

    public void setGeolocation(Geolocation geolocation) {
        this.geolocation = geolocation;
    }

    public Bitmap getQrCodeBrowse() {
        return this.qrCodeBrowse;
    }

    public void setQrCodeBrowse(Bitmap qrCodeBrowse) {
        this.qrCodeBrowse = qrCodeBrowse;
    }

    public Bitmap getQrCodeCheckIn() {
        return this.qrCodeCheckIn;
    }

    public void setQrCodeCheckIn(Bitmap qrCodeCheckIn) {
        this.qrCodeCheckIn = qrCodeCheckIn;
    }

    public long getEventAttendLimit() {
        return this.eventAttendLimit;
    }

    public int setEventAttendLimit(int eventAttendLimit) {
        return eventAttendLimit;
    }



    public Map<String, Long> getAttendeeIDList() {
        return userList;
    }

    /**
     * This function returns the attendee list for the event
     * @return Returns the list in the format of an ArrayList of Pairs being <Attendee, check-in count>
     */
    public ArrayList<Pair<Attendee, Long>> getAttendeeList() {
        ArrayList<Pair<Attendee, Long>> attendees = new ArrayList<Pair<Attendee, Long>>();
        Enumeration<String> enu = Collections.enumeration(userList.keySet());
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String tempID;
        while (enu.hasMoreElements())
        {
            tempID = enu.nextElement();
            DocumentReference docRef = db.collection("users").document(tempID);
            String finalTempID = tempID;
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                            attendees.add(new Pair<Attendee, Long>((Attendee)(document.get(finalTempID)), userList.get(finalTempID)));
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });

        }

        return attendees;
    }

    public void setUserList(Map<String, Long> userList) {
        this.userList = userList;
    }
}