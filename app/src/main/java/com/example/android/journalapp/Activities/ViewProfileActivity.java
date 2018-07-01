package com.example.android.journalapp.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.journalapp.Contract.ViewProfileContract;
import com.example.android.journalapp.R;

public class ViewProfileActivity extends AppCompatActivity implements ViewProfileContract.View{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
    }

    @Override
    public void viewUsername() {

    }

    @Override
    public void viewEmailAddress() {

    }

    @Override
    public void viewProfilePics() {

    }
}
