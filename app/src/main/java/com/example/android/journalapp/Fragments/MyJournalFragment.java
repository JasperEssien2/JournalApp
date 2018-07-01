package com.example.android.journalapp.Fragments;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.journalapp.Adapters.JournalAdapter;
import com.example.android.journalapp.Constants.Constants;
import com.example.android.journalapp.Contract.JournalFragmentContract;
import com.example.android.journalapp.Models.JournalViewModel;
import com.example.android.journalapp.POJOs.Journal;
import com.example.android.journalapp.Presenters.JournalFragmentPresenter;
import com.example.android.journalapp.Presenters.JournalListPresenter;
import com.example.android.journalapp.R;
import com.example.android.journalapp.databinding.FragmentMyJournalBinding;
import com.google.firebase.FirebaseApp;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyJournalFragment extends Fragment implements JournalFragmentContract.View,
        View.OnClickListener {

    private static final String TAG = MyJournalFragment.class.getSimpleName();

    private FragmentMyJournalBinding mFragmentMyJournalBinding = null;
    private JournalFragmentPresenter mJourneyFragmentPresenter;
    private JournalViewModel mJournalViewModel;

    public MyJournalFragment() {
        // Required empty public constructor
        mJourneyFragmentPresenter = new JournalFragmentPresenter(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mJournalViewModel = mJourneyFragmentPresenter.setUpViewModel();
        FirebaseApp.initializeApp(getActivity());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mJourneyFragmentPresenter.checkIfViewModelNotNullThenObserveData(
                //mJourneyFragmentPresenter.getJournalViewModel());
                mJournalViewModel);
        if(getActivity() != null) {
            mJourneyFragmentPresenter.setAdapter(mFragmentMyJournalBinding.recyclerMyJournal,
                    new JournalAdapter(getActivity().getSupportFragmentManager(), getActivity(),
                            new JournalListPresenter()));
        }

    }

    @Override
    public ViewModelProvider getViewModelProviderFromView() {
        return ViewModelProviders.of(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mJourneyFragmentPresenter = new JournalFragmentPresenter(this);
        if (getActivity() != null) {
            mFragmentMyJournalBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_journal,
                    container, false);
            mFragmentMyJournalBinding.fabAddNewJournal.setOnClickListener(this);
            setUpLinearManager();
            return mFragmentMyJournalBinding.getRoot();//inflater.inflate(R.layout.fragment_my_journal, container, false);
        }
        //view = itemJournalBinding.getRoot();
        else return new View(getContext());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_add_new_journal:
                addNewJournal();
        }
    }

    @Override
    public void addNewJournal() {
        if (getActivity() != null)
            mJourneyFragmentPresenter.onAddButtonClicked(mFragmentMyJournalBinding,
                    getActivity().getSupportFragmentManager(), R.id.drawer_layout);
    }

    /**
     * This method observes the data and notify when there's a change in the data
     *
     * @param journalViewModel
     */
    @Override
    public void observeData(JournalViewModel journalViewModel) {
        journalViewModel.setWhichDataToGet(Constants.ARGUMENTS_PERSONAL_DATA_TYPE);
        //LiveData<List<Journal>> liveDataJournal = journalViewModel.getJournalListLiveData();
        LiveData<Journal> liveDataJournal = journalViewModel.getJournalLiveData();
        //Pass in the type of data you want to get in the JournalViewModel
//        liveDataJournal.observe(this, (List<Journal> journals) -> {
//            Log.e(TAG, "*********** observeData() called journals: " + journals.toString() + "**********");
//            mJourneyFragmentPresenter.setJournalList(journals);
//        });

        liveDataJournal.observe(this, (Journal journal) -> {
            if(journal != null) {
                mJourneyFragmentPresenter.setJournalList(journal);
                //TODO: get the previous list check to see if it equals the former item if it does delete the previous
                //then add the new;
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(this).attach(this).commit();
            }
        });
    }



    @Override
    public void onResume() {
        super.onResume();
        mJourneyFragmentPresenter.setJournalList(new ArrayList<Journal>());
    }

    @Override
    public void onPause() {
        super.onPause();
        mJourneyFragmentPresenter.setJournalList(new ArrayList<Journal>());
    }

    @Override
    public void setUpLinearManager() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mFragmentMyJournalBinding.recyclerMyJournal.setLayoutManager(layoutManager);
    }

    @Override
    public void onStart() {
        super.onStart();
        mJourneyFragmentPresenter.setJournalList(new ArrayList<Journal>());
    }
}
