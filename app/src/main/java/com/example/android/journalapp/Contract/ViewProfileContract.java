package com.example.android.journalapp.Contract;

import com.example.android.journalapp.POJOs.Journal;

public class ViewProfileContract {

    public interface View{
        void viewUsername();

        void viewEmailAddress();

        void viewProfilePics();
    }

    public interface Action{
        void setUsername(Journal journal);

        void setEmailAddress(Journal journal);

        void setProfilePics();
    }
}
