package com.example.android.journalapp.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.journalapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SharedJournalsFragment extends Fragment {


    public SharedJournalsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shared_journals, container, false);
    }

}
