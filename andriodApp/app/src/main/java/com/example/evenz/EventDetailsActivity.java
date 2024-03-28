package com.example.evenz;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class EventDetailsActivity  extends AppCompatActivity {
    private String eventID;
    private ImageView eventPoster, homeButton;
    private TextView eventLocation, eventDetail, eventWelcomeNote;

    private NotificationsAdapter notificationsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();
        assert b != null;
        String role = b.getString("role");
        eventID = b.getString("eventID");

        if (Objects.equals(role, "attendee")) {
            setContentView(R.layout.attendee_event_info_sign_up);

            eventPoster = findViewById(R.id.poster_attendee_eventInfo);
            eventLocation = findViewById(R.id.loc_attendee_eventInfo);
            eventDetail = findViewById(R.id.info_attendee_eventInfo);
            eventWelcomeNote = findViewById(R.id.attendee_event_detail_welcome);
            homeButton = findViewById(R.id.home_event_details_attendee);
            homeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(new Intent(EventDetailsActivity.this, HomeScreenActivity.class));
                    Bundle b = new Bundle();
                    b.putString("role", "attendee");
                    b.putString("eventID", eventID);
                    intent.putExtras(b);
                    startActivity(intent);
                }
            });

        } else {
            setContentView(R.layout.org_event_info);

            eventPoster = findViewById(R.id.poster_org_eventInfo);
            eventLocation = findViewById(R.id.loc_org_eventInfo);
            eventDetail = findViewById(R.id.info_org_eventInfo);
            eventWelcomeNote = findViewById(R.id.org_event_detail_welcome);
            homeButton = findViewById(R.id.home_event_details_org);
            homeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(new Intent(EventDetailsActivity.this, HomeScreenActivity.class));
                    Bundle b = new Bundle();
                    b.putString("role", "organizer");
                    b.putString("eventID", eventID);
                    intent.putExtras(b);
                    startActivity(intent);
                }
            });

            ImageView shareQR = findViewById(R.id.shareQR_event_details);
            shareQR.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    QRGenerator test = new QRGenerator();
                    Bitmap bitmap = test.generate(eventID, 400, 400);
                    Uri bitmapUri = saveBitmapToCache(bitmap);

                    Intent intent = new Intent(EventDetailsActivity.this, ShareQRActivity.class);

                    intent.putExtra("eventID", eventID);
                    intent.putExtra("BitmapImage", bitmapUri.toString());
                    startActivity(intent);
                }
            });
        }
        if (!eventID.isEmpty()) {
            fetchEventDetailsAndNotifications(eventID);
        }
    }

    private Uri saveBitmapToCache(Bitmap bitmap) {
        try {
            File cachePath = new File(getCacheDir(), "images");
            cachePath.mkdirs(); // don't forget to make the directory
            FileOutputStream stream = new FileOutputStream(cachePath + "/image.png"); // overwrites this image every time
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

            File imagePath = new File(getCacheDir(), "images");
            File newFile = new File(imagePath, "image.png");
            return Uri.fromFile(newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void fetchEventDetailsAndNotifications(String eventId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("events").document(eventId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot != null && documentSnapshot.exists()) {
                Event event = documentSnapshot.toObject(Event.class);
                // Directly update the TextView with the event's location

                if (event != null) {
                    String eventName = event.getEventName();
                    eventWelcomeNote.setText("\uD83D\uDC4B Welcome to " + event.getEventName() + "! \uD83D\uDE80");
                    eventDetail.setText(event.getDescription());
                    eventDetail.setMovementMethod(new ScrollingMovementMethod());
                    eventLocation.setText(event.getLocation());
                    displayImage(event.getEventPosterID(), eventPoster);
                }
            } else {
                // TODO: Handle the case where the event doesn't exist in the database
            }
        }).addOnFailureListener(e -> {
            // TODO: handle errors
        });
    }

    private void displayImage(String imageID, ImageView imgView)
    {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference photoReference= storageReference.child("images/" + imageID);

        final long ONE_MEGABYTE = 1024 * 1024;
        photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imgView.setImageBitmap(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(EventDetailsActivity.this, "No Such file or Path found!!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
