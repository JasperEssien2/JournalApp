package com.example.android.journalapp.Contract;

import android.arch.lifecycle.ViewModelProvider;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.journalapp.Adapters.JournalAdapter;
import com.example.android.journalapp.Models.JournalViewModel;
import com.example.android.journalapp.POJOs.Journal;
import com.example.android.journalapp.databinding.FragmentMyJournalBinding;

import java.util.List;

public class JournalFragmentContract {

    public interface View{
        void addNewJournal();

        void observeData(JournalViewModel journalViewModel);

        ViewModelProvider getViewModelProviderFromView();

        //void onOptionMenuClicked(List<String> journalUids, String deletFrom);

        void setUpLinearManager();
    }

    public interface Action{
        void onAddButtonClicked(FragmentMyJournalBinding myJournalBinding,
                                FragmentManager fragmentManager,
                                int containerId);

        JournalViewModel setUpViewModel();

        void setAdapter(RecyclerView recyclerView, JournalAdapter journalAdapter);

        void setJournalList(List<Journal> journals);

        void setJournalList(Journal journal);

        void checkIfViewModelNotNullThenObserveData(JournalViewModel viewModel);
    }
}
