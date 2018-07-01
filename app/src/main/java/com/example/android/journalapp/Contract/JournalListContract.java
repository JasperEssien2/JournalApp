package com.example.android.journalapp.Contract;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageButton;

import com.example.android.journalapp.Activities.HomeActivity;
import com.example.android.journalapp.Adapters.JournalAdapter;
import com.example.android.journalapp.POJOs.Journal;
import com.example.android.journalapp.databinding.ItemJournalBinding;

import java.util.ArrayList;
import java.util.List;

public class JournalListContract {

    public interface View{

        void showJournals(List<Journal> journals);

        void showJournalImage(Journal journal);

        void showJournalTitle(Journal journal);

        void showJournalDate(Journal journal);

        void showWeatherJournalIsLiked(Journal journal);

        //This is for onLongCLick any of the journal item
        void showCopyShareLayout();

        void onOptionMenuClicked(List<String> journalUids, String deletFrom);

        void setItemViewType(ItemJournalBinding itemJournalBinding, boolean withImage, int viewType);

        void onItemSelected();

        void showDetailOnItemClicked(FragmentManager fragmentManager, Journal journal);

        void onStarButtonClicked(AppCompatImageButton imageButton, Journal journal);

        void onDeleteButtonClicked(ImageButton imageButton, List<Journal> journals, int pos, RecyclerView recyclerView);
    }

    public interface Actions{

        void bindView(List<Journal> journals, JournalListContract.View view);

        void unBindView();

        void onBindJournalAtPosition(FragmentManager fragmentManager,
                                     ItemJournalBinding itemJournalBinding, int position,
                                     JournalAdapter.JournalViewHolder viewHolder, int viewType, RecyclerView recyclerView);

        int getJournalsItemCount();

        void onLikesCountTextClicked(Journal journal);

        void onItemClicked(FragmentManager fragmentManager, Journal journal, int itemPos);

        void onStarButtonClicked(android.view.View view, Journal journal);

        void onAddNewButtonClicked();

        ArrayList<String> deleteJournalsUids();

        void onShareButtonClicked();

        void onCopyButtonClicked();

        int getViewType();

        void setItemViewTypeMyJournal(ItemJournalBinding itemJournalBinding,
                                      boolean withImage, int viewType);

        void setItemViewTypeSharedJournal(ItemJournalBinding itemJournalBinding,
                                          boolean withImage, int viewType);

        void setJournalList(List<Journal> journals);

        void setSelected(HomeActivity activity, android.view.View v, int position);
    }

    public interface Repository{

        void getSharedJournals();

        void getFeedJournals();

        void getPersonalJournals();
    }
}
