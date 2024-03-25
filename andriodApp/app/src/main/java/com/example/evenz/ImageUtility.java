package com.example.evenz;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public final class ImageUtility {

    FirebaseStorage storage;
    StorageReference storageReference;

    public ImageUtility(){
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    /**
     * Given that filePath has already been defined within the activity, uploads an image previously selected by the user
     * This includes adding the image to the firebase storage with proper notifications for upload progress.
     * @param filePath Uri filepath of
     * @return returns generated id for image uploaded
     */
    public String upload(Uri filePath) {
        if (filePath != null) {

            String id = UUID.randomUUID().toString();
            // Defining the child of storageReference
            storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference();
            StorageReference ref = storageReference.child("images/" + id);

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath);

            return id;
        }
        return null;
    }

    /**
     * Displays an image from the firebase storage given
     * @param imageID id of the image being displayed
     * @param imgView The image view to place the image on
     */
    public void displayImage(String imageID, ImageView imgView)
    {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference photoReference = storageReference.child("images/" + imageID);

        final long ONE_MEGABYTE = 1024 * 1024;
        photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imgView.setImageBitmap(bmp);
            }
        });
    }

}