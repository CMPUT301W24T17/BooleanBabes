package com.example.evenz;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserEditProfileActivity extends AppCompatActivity {

	private static final int PICK_IMAGE_REQUEST = 22;
	private static final String TAG = "UserEditProfileActivity";
	private Uri filePath;

	private ImageView imageView;
	private EditText nameInput, phoneInput, emailInput;

	private ImageUtility imageUtility;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_edit_profile);

		imageView = findViewById(R.id.vector_ek2);
		nameInput = findViewById(R.id.name_input);
		phoneInput = findViewById(R.id.phone_input);
		emailInput = findViewById(R.id.email_input);

		imageUtility = new ImageUtility();

		imageView.setOnClickListener(v -> selectImage());
		findViewById(R.id.back_button).setOnClickListener(v -> finish());
		findViewById(R.id.save_button).setOnClickListener(v -> saveUserInfo());
	}

	private void selectImage() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
			filePath = data.getData();
			try {
				Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
				imageView.setImageBitmap(bitmap);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void saveUserInfo() {
		String name = nameInput.getText().toString().trim();
		String phone = phoneInput.getText().toString().trim();
		String email = emailInput.getText().toString().trim();

		Bundle b = getIntent().getExtras();
		assert b != null;
		String userType = b.getString("role");
		String eventID = b.getString("eventID");

		@SuppressLint("HardwareIds") String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

		FirebaseFirestore db = FirebaseFirestore.getInstance();
		DocumentReference userDocRef = db.collection("users").document(deviceID);

		userDocRef.get().addOnCompleteListener(task -> {
			if (task.isSuccessful()) {
				DocumentSnapshot document = task.getResult();
				if (document.exists()) {
					updateUserProfile(name, phone, email, userType, userDocRef, eventID);
					Toast.makeText(UserEditProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
				} else {
					createNewUserProfile(name, phone, email, userType, userDocRef);
					Toast.makeText(UserEditProfileActivity.this, "Profile added successfully", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(UserEditProfileActivity.this, "Failed to check if user exists: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void updateUserProfile(String name, String phone, String email, String userType, DocumentReference userDocRef, String eventID) {
		Map<String, Object> userMap = new HashMap<>();
		userMap.put("name", name);
		userMap.put("phone", phone);
		userMap.put("email", email);

		userDocRef.update(userMap)
				.addOnSuccessListener(aVoid -> {
					Intent intent = new Intent(UserEditProfileActivity.this, HomeScreenActivity.class);
					Bundle b = new Bundle();
					b.putString("role", userType);
					b.putString("eventID", eventID);

					intent.putExtras(b);
					startActivity(intent);
				})
				.addOnFailureListener(e -> Toast.makeText(UserEditProfileActivity.this, "Failed to update profile: " + e.getMessage(), Toast.LENGTH_SHORT).show());
	}

	private void createNewUserProfile(String name, String phone, String email, String userType, DocumentReference userDocRef) {
		Map<String, Object> userMap = new HashMap<>();
		userMap.put("name", name);
		userMap.put("phone", phone);
		userMap.put("email", email);
		userMap.put("userType", userType);
		userMap.put("eventID", "");

		userDocRef.set(userMap)
				.addOnSuccessListener(aVoid -> {
					Intent intent = new Intent(UserEditProfileActivity.this, HomeScreenActivity.class);
					Bundle b = new Bundle();
					b.putString("role", userType);
					b.putString("eventID", "CLQuoRALxppaIHscuwnG");
					intent.putExtras(b);
					startActivity(intent);
				})
				.addOnFailureListener(e -> Toast.makeText(UserEditProfileActivity.this, "Failed to create profile: " + e.getMessage(), Toast.LENGTH_SHORT).show());
	}

}