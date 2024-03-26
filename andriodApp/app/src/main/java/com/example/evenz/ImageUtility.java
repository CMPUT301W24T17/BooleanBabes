package com.example.evenz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public final class ImageUtility {

    private static final String TAG = "ImageUtility";
    private final FirebaseStorage storage;
    private final StorageReference storageReference;

    public ImageUtility() {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    /**
     * Uploads an image to Firebase Storage.
     *
     * @param filePath The Uri of the image file to be uploaded.
     * @return The generated ID for the uploaded image, or null if the filePath is null.
     */
    public String upload(Uri filePath) {
        if (filePath == null) {
            Log.e(TAG, "upload: filePath is null");
            return null;
        }

        String id = UUID.randomUUID().toString();
        StorageReference ref = storageReference.child("images/" + id);

        ref.putFile(filePath)
                .addOnSuccessListener(taskSnapshot -> Log.d(TAG, "upload: Image uploaded successfully"))
                .addOnFailureListener(e -> Log.e(TAG, "upload: Failed to upload image", e));

        return id;
    }

    /**
     * Displays an image from Firebase Storage in the given ImageView.
     *
     * @param imageID The ID of the image to be displayed.
     * @param imgView The ImageView where the image will be displayed.
     */
    public void displayImage(String imageID, ImageView imgView) {
        if (imageID == null || imageID.isEmpty() || imgView == null) {
            Log.e(TAG, "displayImage: imageID or imgView is null or imageID is empty");
            return;
        }

        StorageReference photoReference = storageReference.child("images/" + imageID);

        final long ONE_MEGABYTE = 1024 * 1024;
        photoReference.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener(bytes -> {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imgView.setImageBitmap(bmp);
                    Log.d(TAG, "displayImage: Image displayed successfully");
                })
                .addOnFailureListener(e -> {
                    if (e instanceof StorageException && ((StorageException) e).getErrorCode() == StorageException.ERROR_OBJECT_NOT_FOUND) {
                        Log.e(TAG, "displayImage: Image does not exist", e);
                        return;
                    } else {
                        Log.e(TAG, "displayImage: Failed to display image", e);
                    }
                });
    }
}