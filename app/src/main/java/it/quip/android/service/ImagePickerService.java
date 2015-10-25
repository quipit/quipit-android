package it.quip.android.service;


import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;


public class ImagePickerService {

    private static final String TAG = ImagePickerService.class.getName();
    private static final String PHOTO_FILE_NAME = "photo.jpg";

    private final String filePrefix;

    public ImagePickerService(String filePrefix) {
        this.filePrefix = filePrefix;
    }

    public Intent pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        return intent;
    }

    public Intent takeCameraImage() {
        return new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    }

    public Uri onImagePicked(Intent data) {
        if (data != null) {
            return data.getData();
        }

        return null;
    }

    public Uri onImageTaken() {
        Uri takenPhotoUri = getPhotoFileUri(PHOTO_FILE_NAME);
        if (null == takenPhotoUri) {
            log("no uri returned for the taken photo");
        }

        return takenPhotoUri;
    }


    private Uri getPhotoFileUri(String filename) {
        if (!isExternalStorageAvailable()) {
            return null;
        }

        File mediaStoreDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                filePrefix);

        if (!mediaStoreDir.exists() && !mediaStoreDir.mkdirs()) {
            log("did not create directory after taking photo");
        }

        return Uri.fromFile(new File(mediaStoreDir, filename));
    }

    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private void log(String message) {
        Log.d(TAG, message);
    }
}
