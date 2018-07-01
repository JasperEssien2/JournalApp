package com.example.android.journalapp.Presenters;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;

import com.example.android.journalapp.Activities.HomeActivity;
import com.example.android.journalapp.Adapters.JournalAdapter;
import com.example.android.journalapp.Constants.Constants;
import com.example.android.journalapp.Constants.FirebaseConstants;
import com.example.android.journalapp.Contract.JournalListContract;
import com.example.android.journalapp.Fragments.AddDetailFragment;
import com.example.android.journalapp.POJOs.Journal;
import com.example.android.journalapp.R;
import com.example.android.journalapp.databinding.ItemJournalBinding;
import com.example.android.journalapp.utilities.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class JournalListPresenter implements JournalListContract.Actions{
    private static final String TAG = JournalListPresenter.class.getSimpleName();
    private JournalListContract.View mJournalView;
    private int mViewType;
    private ItemJournalBinding mItemJournalBinding;
    private boolean mWithImage;
    private List<Journal> mJournals= new ArrayList<Journal>();;
    private SparseBooleanArray mSelectedItem = new SparseBooleanArray();
    private EditOptions mEditOptions;
    private HomeActivity homeActivity;

    @Override
    public void bindView(List<Journal> journals, JournalListContract.View view){
        mJournalView = view;
        mJournals = journals;
    }

    @Override
    public void unBindView(){
        mJournalView = null;
    }

    private RecyclerView mRecyclerView;
    @Override
    public void onBindJournalAtPosition(FragmentManager fragmentManager,
                                        ItemJournalBinding itemJournalBinding, int position,
                                        JournalAdapter.JournalViewHolder viewHolder, int viewType,
                                        RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mViewType = viewType;
        viewHolder.itemView.setSelected(mSelectedItem.get(position, false));
        if(mJournals.get(position).getImageUrl() != null ||
                mJournals.get(position).getImageUrl() == null) mWithImage = true;
        else mWithImage = false;

        //mJournalView.setItemViewType(itemJournalBinding, mWithImage, viewType);

        mItemJournalBinding = itemJournalBinding;
        mJournalView.showJournalImage(mJournals.get(position));
        mJournalView.showJournalDate(mJournals.get(position));
        mJournalView.showJournalTitle(mJournals.get(position));
        mJournalView.showWeatherJournalIsLiked(mJournals.get(position));
        mJournalView.onStarButtonClicked((
                (AppCompatImageButton)viewHolder.itemView.findViewById(R.id.image_button_star)),
                mJournals.get(position));
        //viewHolder.onStarButtonClicked(mJournals.get(position));
        mJournalView.onDeleteButtonClicked(mItemJournalBinding.imageDeleteButton,
                mJournals, position, mRecyclerView);
    }

    @Override
    public void onItemClicked(FragmentManager fragmentManager, Journal journal, int itemPos) {

        Log.e(TAG, "onItemClicked method called -----------------------");
        if (fragmentManager != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //mView.addNewJournal();
            AddDetailFragment addDetailFragment = AddDetailFragment.newInstance(
                    journal.getTitle(), journal.getText(), journal.getPushId(), journal
            );
            //addDetailFragment.setJournalInstance(journal);
            //mSavedJournalInfo = this;
            //mSavedJournalInfo.clickedDetails(journal.getTitle(), journal.getText(), journal.getPushId());
            fragmentTransaction.add(R.id.drawer_layout, addDetailFragment,
                    Constants.ARGUMENTS_ADD_DETAIL_JOURNAL_FRAGMENT);
            fragmentTransaction.addToBackStack(Constants.ARGUMENTS_ADD_DETAIL_JOURNAL_FRAGMENT);

            fragmentTransaction.commit();
        }
    }

    //DELETE: this line below is no longer useful
    @Override
    public void onLikesCountTextClicked(Journal journal) {
        mItemJournalBinding.textLikesCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    @Override
    public void onStarButtonClicked(View view, Journal journal) {
        Boolean isStarred = !journal.isStarred();
        view.setActivated(isStarred);
        Log.e(TAG, "*********** onStarButtonClicked() " + isStarred + " ************* ");

        DatabaseHelper.starClicked(view, journal.getPushId(), isStarred);
    }

    @Override
    public void onAddNewButtonClicked() {

    }

    @Override
    public void onShareButtonClicked() {

    }

    @Override
    public void onCopyButtonClicked() {

    }

    @Override
    public ArrayList<String> deleteJournalsUids() {
        ArrayList<String> deleteJournalKeys = new ArrayList<>();
        for(int i = 0; i < mJournals.size(); i++){
            if(mSelectedItem.get(i)){
                deleteJournalKeys.add(mJournals.get(i).getPushId());
            }
        }
        return deleteJournalKeys;
    }

    @Override
    public int getJournalsItemCount() {
        return mJournals.size();
    }

    @Override
    public int getViewType() {
        return mViewType;
    }

    @Override
    public void setItemViewTypeMyJournal(ItemJournalBinding itemJournalBinding, boolean withImage,
                                         int viewType) {
//        if(!withImage) itemJournalBinding.imageJournal.setVisibility(View.GONE);
//
//        if(withImage) {
//            itemJournalBinding.textJournalUsername.setVisibility(View.VISIBLE);
//            itemJournalBinding.imageJournal.setVisibility(View.VISIBLE);
//            itemJournalBinding.imageButtonLike.setVisibility(View.VISIBLE);
//            itemJournalBinding.imageLikeBackground.setVisibility(View.VISIBLE);
//            itemJournalBinding.textLikesCount.setText(View.VISIBLE);
//        }
        //mJournalView.setItemViewType(mWithImage, mViewType);
    }

    @Override
    public void setItemViewTypeSharedJournal(ItemJournalBinding itemJournalBinding,
                                             boolean withImage, int viewType) {
//        if(!withImage) itemJournalBinding.imageJournal.setVisibility(View.GONE);
//
//        if(withImage) {
//            itemJournalBinding.textJournalUsername.setVisibility(View.VISIBLE);
//            itemJournalBinding.imageJournal.setVisibility(View.VISIBLE);
//            itemJournalBinding.imageButtonLike.setVisibility(View.GONE);
//            itemJournalBinding.imageLikeBackground.setVisibility(View.GONE);
//            itemJournalBinding.textLikesCount.setText(View.GONE);
//        }
//        //mJournalView.setItemViewType(mWithImage, mViewType);
    }

    //TODO: YOU MIGHT DELETE THIS BELOW
    @Override
    public void setJournalList(List<Journal> journals) {
        mJournals = journals;
    }

    @Override
    public void setSelected(HomeActivity homeActivity, View v, int position) {
        if(mSelectedItem.size() == 0){
            homeActivity.menu.clear();
            homeActivity
                    .getMenuInflater()
                    .inflate(R.menu.home, homeActivity.menu);
        }

        if(mSelectedItem.get(position, false)){
            mSelectedItem.delete(position);
            v.setSelected(false);
            v.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.white));
        }else{
            mSelectedItem.put(position, true);
            v.setSelected(true);
            v.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.colorLightGray));
        }
        if(mSelectedItem.size() != 0){
            homeActivity.menu.clear();
            homeActivity
                    .getMenuInflater()
                    .inflate(R.menu.item_selecteed_edit_actions, homeActivity.menu);
            mJournalView.onOptionMenuClicked(deleteJournalsUids(),
                    FirebaseConstants.FIREBASE_JOURNAL_DELETE);
        }
        if(mSelectedItem.size() == 0){
            homeActivity.menu.clear();
            homeActivity
                    .getMenuInflater()
                    .inflate(R.menu.home, homeActivity.menu);
        }
    }

    public void setEditOptions(EditOptions editOptions){
        this.mEditOptions = editOptions;
    }

    public void delete(){
        ArrayList<String> journalUids = new ArrayList<>();
        for(int i = 0 ; i < mJournals.size(); i++){
            if(mSelectedItem.get(i)){
                journalUids.add(i, mJournals.get(i).getPushId());
            }
        }
        mEditOptions.deleteButtonClicked(journalUids, FirebaseConstants.FIREBASE_JOURNAL_DELETE);
    }

    public void share(){

    }

    public interface EditOptions{
        void deleteButtonClicked(ArrayList<String> journalIds, String deleteFrom);

        void shareButtonClicked(ArrayList<String> journalIds, String deleteFrom);
    }

}
