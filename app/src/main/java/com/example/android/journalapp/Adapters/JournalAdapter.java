package com.example.android.journalapp.Adapters;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.android.journalapp.Activities.HomeActivity;
import com.example.android.journalapp.Constants.Constants;
import com.example.android.journalapp.Contract.JournalListContract;
import com.example.android.journalapp.POJOs.Journal;
import com.example.android.journalapp.Presenters.JournalListPresenter;
import com.example.android.journalapp.R;
import com.example.android.journalapp.databinding.ItemJournalBinding;
import com.example.android.journalapp.utilities.DatabaseHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.JournalViewHolder> {
    private List<Journal> mJournals = new ArrayList<>();
    private int mViewType;
    private ItemJournalBinding mItemJournalBinding;
    private static final String TAG = JournalAdapter.class.getSimpleName();
    //Using MVP
    private final JournalListPresenter mJournalListPresenter;
    private FragmentManager mFragmentManager;
    private Activity mActivity;
    private RecyclerView mRecyclerView;

//    public JournalAdapter(int viewType){
//        mViewType = viewType;
//    }

    public JournalAdapter(FragmentManager fragmentManager, Activity activity,
                          JournalListPresenter journalListPresenter){
        mJournalListPresenter = journalListPresenter;
        mFragmentManager = fragmentManager;
        mActivity = activity;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @Override
    public int getItemViewType(int position) {
        return mJournalListPresenter.getViewType();
    }

    @NonNull
    @Override
    public JournalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        mItemJournalBinding = DataBindingUtil.inflate(
                layoutInflater, R.layout.item_journal, parent, false
        );

        /**
         * The switch statement below is used to check the viewType required
         */
//        switch (viewType){
//            case Constants.ARGUMENTS_MY_JOURNAL_VIEW_TYPE:
//                setItemViewTypeMyJournal();
//                break;
//            case Constants.ARGUMENTS_FEEDS_JOURNAL_VIEW_TYPE:
//            case Constants.ARGUMENTS_SHARED_JOURNAL_VIEW_TYPE:
//                setItemViewTypeSharedJournal();
//                break;
//        }
        return new JournalViewHolder(mItemJournalBinding.getRoot());
    }
    private SparseBooleanArray mSelectedItem;
    @Override
    public void onBindViewHolder(@NonNull JournalViewHolder holder, int position) {
//        Journal journal = mJournals.get(position);
//        if(journal == null)
//            return;
//        //if(journal.setUsername(); != null && mItemJournalBinding.)
//        mItemJournalBinding.textJournalDate.setText(journal.getUploadDate());
//        mItemJournalBinding.textJournalTitle.setText(journal.getText());
//        if(journal.getImageUrl() != null){
//            //TODO: Use picasso to load images the Integer.parseInt is just for test purpose
//            mItemJournalBinding.imageJournal.setImageResource(Integer.parseInt(journal.getImageUrl()));
//        }
//        if(mViewType == Constants.ARGUMENTS_FEEDS_JOURNAL_VIEW_TYPE ||
//                mViewType == Constants.ARGUMENTS_SHARED_JOURNAL_VIEW_TYPE){
//            mItemJournalBinding.textLikesCount.setText(String.valueOf(journal.getLikesCount()));
//            mItemJournalBinding.textJournalUsername.setText(journal.getUsername());
//        }
        mJournalListPresenter.onBindJournalAtPosition(mFragmentManager, mItemJournalBinding,
                position, holder, mViewType, mRecyclerView);
        //holder.itemView.setSelected(mSelectedItem.get());
    }

    @Override
    public int getItemCount() {
//        if(mJournals == null)
//            return 0;
        return mJournals.size();
        //return mJournalListPresenter.getJournalsItemCount();
    }

    public class JournalViewHolder extends RecyclerView.ViewHolder
            implements JournalListContract.View, View.OnLongClickListener, View.OnClickListener,
            HomeActivity.EditOptions{



        public JournalViewHolder(View itemView) {
            super(itemView);
            ((HomeActivity)mActivity).setEditOptions(this);
            mItemJournalBinding.getRoot().setOnClickListener(this);
            mItemJournalBinding.getRoot().setOnLongClickListener(this);
//            final int[][] states =new int[][]{
//                    new int[] {android.R.attr.state_activated}, //checked
//                    new int[] {-android.R.attr.state_activated} //unchecked
//            };
//            final int[] colors = new int[]{
//                    ContextCompat.getColor(mItemJournalBinding
//                            .imageButtonStar.getContext(), R.color.colorPink),
//                    ContextCompat.getColor(mItemJournalBinding
//                            .imageButtonStar.getContext(), R.color.colorLightGray),
//            };
//
//            ((AppCompatImageButton)itemView.findViewById(R.id.image_button_star))
//                    .setSupportImageTintList(new ColorStateList(states, colors));

            mJournalListPresenter.bindView(mJournals, this); //This sets the current view
        }

        @Override
        public void showJournals(List<Journal> journals) {
        }

        @Override
        public void showJournalImage(Journal journal) {
            if(journal.getImageUrl() != null){
                //mItemJournalBinding.imageJournal.setIm
                Picasso.get()
                        .load(journal.getImageUrl())
                        //.centerCrop()
                        .error(R.color.colorLightGray)
                        .into(mItemJournalBinding.imageJournal);
            }
        }

        @Override
        public void onClick(View view) {
            if(view.getId() == mItemJournalBinding.getRoot().getId()){
                mJournalListPresenter.onItemClicked(mFragmentManager,
                        mJournals.get(getAdapterPosition()), getAdapterPosition());
            }else if(view.getId() == mItemJournalBinding.imageButtonStar.getId()){
//                mJournalListPresenter.onStarButtonClicked(mItemJournalBinding.imageButtonStar,
//                        mJournals.get(getAdapterPosition()));
//                Log.e(TAG, "++++++++++++++++++ onClickId == StarImageButton ++++++++++++++++");
            }
        }

        @Override
        public void showJournalTitle(Journal journal) {
            //TODO: GET TITLE INSTEAD
            mItemJournalBinding.textJournalTitle.setText(journal.getTitle());
        }

        @Override
        public void showJournalDate(Journal journal) {
            mItemJournalBinding.textJournalDate.setText(journal.getUploadDate());
        }

        @Override
        public void showWeatherJournalIsLiked(Journal journal) {
//            if(journal.is)
            final int[][] states =new int[][]{
                    new int[] {android.R.attr.state_activated}, //checked
                    new int[] {-android.R.attr.state_activated} //unchecked
            };
            final int[] colors = new int[]{
                    ContextCompat.getColor(mItemJournalBinding.imageButtonStar.getContext(),
                            R.color.colorPink),
                    ContextCompat.getColor(mItemJournalBinding.imageButtonStar.getContext(),
                            R.color.colorLightGray),
            };

            mItemJournalBinding
                    .imageButtonStar
                    .setSupportImageTintList(new ColorStateList(states, colors));

            if(journal.isStarred()){
                mItemJournalBinding.imageButtonStar.setSelected(true);
            }else{
                mItemJournalBinding.imageButtonStar.setSelected(false);
            }
        }

        @Override
        public void showCopyShareLayout() {

        }

        @Override
        public void onItemSelected() {

        }

        @Override
        public void showDetailOnItemClicked(FragmentManager fragmentManager, Journal journal) {
            mJournalListPresenter.onItemClicked(fragmentManager, journal, 0);
        }

        @Override
        public void setItemViewType(ItemJournalBinding itemJournalBinding, boolean withImage, int viewType) {
            switch (viewType){
                case Constants.ARGUMENTS_MY_JOURNAL_VIEW_TYPE:
                    mJournalListPresenter.setItemViewTypeMyJournal(itemJournalBinding, withImage,
                            viewType);
                    break;
                case Constants.ARGUMENTS_FEEDS_JOURNAL_VIEW_TYPE:
                case Constants.ARGUMENTS_SHARED_JOURNAL_VIEW_TYPE:
                    mJournalListPresenter.setItemViewTypeSharedJournal(itemJournalBinding,
                            withImage, viewType);
                    break;
            }

        }

        @Override
        public boolean onLongClick(View view) {
            Menu menu = ((HomeActivity)mActivity).menu;
            if(view.getId() == mItemJournalBinding.getRoot().getId()) {
                ((HomeActivity)mActivity).menu.clear();
                ((HomeActivity)mActivity).getMenuInflater().inflate(
                        R.menu.item_selecteed_edit_actions, menu);
                mJournalListPresenter.setSelected(((HomeActivity)mActivity), view,
                        getAdapterPosition());
            }else {
                ((HomeActivity)mActivity).menu.clear();
                ((HomeActivity)mActivity).getMenuInflater().inflate(
                        R.menu.home, menu);
            }
            return true;
        }

        @Override
        public void onOptionMenuClicked(List<String> journalUids, String deletFrom) {
            if(mDeleteButtonClicked){
                DatabaseHelper.deleteJournal(journalUids, deletFrom);
                Log.e(TAG, "********* onOptionMenuCLicked() called mDeleteButtonCLicked is true");
            }
            Log.e(TAG, "********* onOptionMenuCLicked() called mDeleteButtonCLicked is false");
        }

        //Mutiple delete clicked the method below is suppose to handle it
        private boolean mDeleteButtonClicked = false;
        @Override
        public void onMultipleDeleteButton(boolean isClicked) {
            mDeleteButtonClicked = isClicked;
        }

        @Override
        public void onDeleteButtonClicked(ImageButton imageButton, List<Journal> journals, int pos,
                                          RecyclerView recyclerView) {
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseHelper.deleteJournal(journals.get(pos).getPushId());
                    recyclerView.removeViewAt(pos);
                    notifyItemRemoved(pos);
//                    notifyDataSetChanged();
//                    notifyItemRangeChanged(pos + 1, journals.size());
                }
            });
        }

        private boolean mShareButton = false;
        @Override
        public void shareButtonClicked(boolean shareButton) {
            mShareButton = shareButton;
        }

        @Override
        public void onStarButtonClicked(AppCompatImageButton imageButton, Journal journal) {
           imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mJournalListPresenter.onStarButtonClicked(imageButton, journal);
                }
            });
        }
    }


    public void clearList(List<Journal> journals){
        mJournals.clear();
        uidsIndex.clear();
        mLinkedHashMap.clear();
        //notifyDataSetChanged();
//        Log.e(TAG, "________________setList() called " + mJournals.toString() + "_____________");
    }

    //This is an organized type of hashmap
    private Journal previousJournal = null;
    private List<String> uidsIndex = new ArrayList<>();
    private LinkedHashMap<String, Journal> mLinkedHashMap = new LinkedHashMap<String, Journal>();

    public void addToList(Journal journal, RecyclerView recyclerView){
        if(journal != null) {
            mJournals.add(mJournals.size(), journal);
            //notifyDataSetChanged();
            mLinkedHashMap.put(journal.getPushId(), journal);
            uidsIndex.add(journal.getPushId());

            if(mLinkedHashMap.containsKey(journal.getPushId())){
                int index = uidsIndex.indexOf(journal.getPushId());
                mJournals.remove(index);
                //recyclerView.removeViewAt(index);
                //notifyItemRemoved(index);
                //notifyItemRangeChanged(index, mJournals.size());
                mJournals.add(mJournals.size(), journal);
                notifyDataSetChanged();
                mLinkedHashMap.remove(journal.getPushId());
                uidsIndex.remove(index);
                mLinkedHashMap.put(journal.getPushId(), journal);
                uidsIndex.add(journal.getPushId());
            }else {
//                mJournals.add(mJournals.size(), journal);
//                notifyDataSetChanged();
            }
            previousJournal = journal;
        }
        //mJournals = tempList;
    }

    public List<Journal> getList(){
        return mJournals;
    }
}
