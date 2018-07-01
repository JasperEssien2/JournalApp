package com.example.android.journalapp.utilities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

public class ImagePickerUtility {
    private Activity activity;
    private int imagePickerConstant;
    private Fragment mFragment;
    public ImagePickerUtility(Activity activity, int imagePickerConstant){
        this.activity = activity;
        this.imagePickerConstant = imagePickerConstant;
    }

    public ImagePickerUtility(Activity activity, Fragment fragment, int imagePickerConstant){
        this.activity = activity;
        this.imagePickerConstant = imagePickerConstant;
        mFragment = fragment;
    }

    public ImagePickerUtility(int imagePickerConstant){
        this.imagePickerConstant = imagePickerConstant;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void chooseFileIntent(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(Intent.createChooser(intent, "Select Profile Pics"),
                imagePickerConstant);
    }

    public void chooseFileIntentFragments(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mFragment.startActivityForResult(Intent.createChooser(intent, "Select Profile Pics"),
                imagePickerConstant);
    }

    public String getFileExtension(Uri uri) {
        if(uri == null){
            Toast.makeText(activity, "Insert an image", Toast.LENGTH_SHORT).show();
            return null;
        }
        ContentResolver cR = activity.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}
