package com.example.android.journalapp.Presenters;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.android.journalapp.Adapters.JournalAdapter;
import com.example.android.journalapp.Constants.Constants;
import com.example.android.journalapp.Contract.JournalFragmentContract;
import com.example.android.journalapp.Fragments.AddDetailFragment;
import com.example.android.journalapp.Models.JournalViewModel;
import com.example.android.journalapp.POJOs.Journal;
import com.example.android.journalapp.databinding.FragmentMyJournalBinding;

import java.util.ArrayList;
import java.util.List;

public class JournalFragmentPresenter implements JournalFragmentContract.Action {
    private static final String TAG = JournalFragmentPresenter.class.getSimpleName();


    private JournalFragmentContract.View mView;
    private RecyclerView mRecyclerView;

    private JournalViewModel mJournalViewModel;
    private List<Journal> mJournals = new ArrayList<Journal>();

    public JournalFragmentPresenter(JournalFragmentContract.View view) {
        mView = view;
    }

    @Override
    public void onAddButtonClicked(FragmentMyJournalBinding myJournalBinding,
                                   FragmentManager fragmentManager,
                                   int containerId) {
        if (fragmentManager != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //mView.addNewJournal();
            fragmentTransaction.add(containerId, new AddDetailFragment(),
                    Constants.ARGUMENTS_ADD_DETAIL_JOURNAL_FRAGMENT);
            fragmentTransaction.addToBackStack(Constants.ARGUMENTS_ADD_DETAIL_JOURNAL_FRAGMENT);
            fragmentTransaction.commit();
        }
    }

    @Override
    public JournalViewModel setUpViewModel() {
        //Log.e(TAG, "********** setUpViewModel() called ********** onCreate()***** ");
        JournalViewModel mJournalViewModel = mView.getViewModelProviderFromView()
                .get(JournalViewModel.class);
        //Log.e(TAG, "********** mJournalViewModel() --- " + mJournalViewModel.toString() + "*************** ");
        return mJournalViewModel;
    }
    private JournalAdapter mJournalAdapter;
    @Override
    public void setAdapter(RecyclerView recyclerView, JournalAdapter journalAdapter) {
        //journalAdapter.clearList(mJournals);
        mJournalAdapter = journalAdapter;
        mRecyclerView = recyclerView;
        recyclerView.setAdapter(journalAdapter);
    }
    @Override
    public void setJournalList(List<Journal> journals) {
        Log.e(TAG, "________________setJournalList() " + journals.toString() + "_________");
        //mView.setUpAdapter();
        mJournalAdapter.clearList(journals);
        //mJournalAdapter.notifyDataSetChanged();
//        mJournals = journals;
    }

    @Override
    public void setJournalList(Journal journal) {
        Log.e(TAG, "________________setJournalList() " + journal.toString() + "_________");
        //mView.setUpAdapter();
        if(journal.getTitle() != null) {
            mJournalAdapter.addToList(journal, mRecyclerView);
            //4mJournalAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void checkIfViewModelNotNullThenObserveData(JournalViewModel viewModel) {
        if (viewModel != null) {
            //Log.e(TAG, "*************** viewModel NOT null *************");
            mView.observeData(viewModel);
        }else{
            //Log.e(TAG, "*************** viewModel IS null *************");
        }
    }
}
