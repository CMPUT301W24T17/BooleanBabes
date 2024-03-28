
package com.example.evenz;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class ScanQRActivity extends AppCompatActivity {
    //    TextView txv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentIntegrator intent =  new IntentIntegrator(ScanQRActivity.this);
        intent.setOrientationLocked(true);
        intent.setPrompt("Scan a QR code");
        intent.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        intent.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if(intentResult != null) {
            String contents = intentResult.getContents();

            if (contents != null) {
                Intent intent = new Intent(ScanQRActivity.this, HomeScreenActivity.class); //TODO: replace with ORG homepage
                Bundle b = new Bundle();
                b.putString("role", "attendee");
                b.putString("eventID", intentResult.getContents());
                intent.putExtras(b);
                startActivity(intent);
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}