package com.example.evenz;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class UserEditProfileActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_edit_profile);

		findViewById(R.id.back_button).setOnClickListener(v -> finish());
	}
}