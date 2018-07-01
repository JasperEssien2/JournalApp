package com.example.android.journalapp.Fragments;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;

import com.example.android.journalapp.Constants.Constants;
import com.example.android.journalapp.Presenters.JournalAddNewPresenter;
import com.example.android.journalapp.R;
import com.example.android.journalapp.databinding.FragmentShareChoiceBinding;

public class ShareChoiceDialogFragment extends DialogFragment implements View.OnClickListener{

    private CheckBox personalCheck, friendsCheck, otherSourceCheck;
    private ImageButton closeButton;
    private FragmentShareChoiceBinding mFragmentShareChoiceBinding;
    //private DataPassListener dataPassListener;
    private boolean mShareToFriendsChecked, mSharePersonalChecked, mSHareOtherSourcesChecked;
    private JournalAddNewPresenter mJournalAddNewPresenter;

//    public interface  DataPassListener{
//        void passData(String choice);
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //dataPassListener = (DataPassListener) getActivity();
    }
    public static ShareChoiceDialogFragment newInstance(String journalPushId) {

        Bundle args = new Bundle();
        args.putCharSequence(Constants.BUNDLE_CHOICE_JOURNAL_PUSH_ID, journalPushId);

        ShareChoiceDialogFragment fragment = new ShareChoiceDialogFragment();
        Log.e("ShareChoiceDialog", "newInstance() created, journalPushId -- " +
        journalPushId + "----------------------------");
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentShareChoiceBinding = DataBindingUtil.
                inflate(inflater, R.layout.fragment_share_choice, container, false);

        mFragmentShareChoiceBinding.checkBoxShareToFriends.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(compoundButton.isChecked()){

                            mShareToFriendsChecked = true;
                        }else{
                            mShareToFriendsChecked = false;
                        }
                    }
                }
        );
        mFragmentShareChoiceBinding.checkBoxShareToPersonal.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(compoundButton.isChecked()){
                            mSharePersonalChecked = true;
                        }else mSharePersonalChecked = false;
                    }
                }
        );
        return mFragmentShareChoiceBinding.getRoot();
    }

    @Override
    public void onClick(View view) {

    }

    public void getJournalAddNewPresenter(JournalAddNewPresenter journalAddNewPresenter){
        mJournalAddNewPresenter = journalAddNewPresenter;
    }

    public void onDismissGetCheckbox(){
        //TODO: change onSHareToFriendsOptionCLicked to onSaveStarredOptionClicked
        String journalPushId = null;
        if(getArguments() != null){
            if(getArguments().containsKey(Constants.BUNDLE_CHOICE_JOURNAL_PUSH_ID)){
                journalPushId = (String) getArguments()
                        .get(Constants.BUNDLE_CHOICE_JOURNAL_PUSH_ID);
                Log.e("ShareChoiceDialog", "onDismissGetCheckbox() called, journalPushId is" +
                        journalPushId + " *********************************** ");
            }
        }
        if(mShareToFriendsChecked) mJournalAddNewPresenter.onSaveToHighPiorityOptionClicked(
                journalPushId);
        if(mSharePersonalChecked) mJournalAddNewPresenter.onSavePersonalOptionClicked(
                journalPushId);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        onDismissGetCheckbox();
        super.onDismiss(dialog);
    }
}
