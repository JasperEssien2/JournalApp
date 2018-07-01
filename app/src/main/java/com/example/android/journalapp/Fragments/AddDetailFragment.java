package com.example.android.journalapp.Fragments;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.journalapp.Activities.HomeActivity;
import com.example.android.journalapp.Constants.Constants;
import com.example.android.journalapp.Contract.JournallAddNewContract;
import com.example.android.journalapp.POJOs.Journal;
import com.example.android.journalapp.Presenters.JournalAddNewPresenter;
import com.example.android.journalapp.R;
import com.example.android.journalapp.databinding.FragmentAddDetailBinding;
import com.example.android.journalapp.utilities.ImagePickerUtility;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddDetailFragment extends Fragment implements JournallAddNewContract.View,
        View.OnClickListener{

    private static final String TAG = AddDetailFragment.class.getSimpleName();
    private FragmentAddDetailBinding mFragmentAddDetailBinding;
    private JournalAddNewPresenter mJournalAddNewPresenter;
    private final int IMAGE_PICKER_VAL = 222;
    private String mChoice;
    private Journal mJournal;
    private String journalPushId;
    private String mUriPath;

    public AddDetailFragment() {
        // Required empty public constructor
    }

    public static AddDetailFragment newInstance(String title, String text, String journalPushId,
                                                Journal journal) {

        Bundle args = new Bundle();

        args.putSerializable(Constants.BUNDLE_JOURNAL_SERIALIZATION_KEY, journal);
        AddDetailFragment fragment = new AddDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentAddDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_detail,
                container, false);
        ((HomeActivity)getActivity())
                .setSupportActionBar((Toolbar)mFragmentAddDetailBinding.toolbar);
        Bundle bundle = getArguments();
        String title = null, text = null, pushId = null;
        Journal journal = null;
        if(bundle != null){
            journal = (Journal) bundle.getSerializable(Constants.BUNDLE_JOURNAL_SERIALIZATION_KEY);
            Log.e(TAG, "^^^^^^^^^^^^^^^^^^^^^^^^^^^ Bundle NOT Null ^^^^^^^^^^^^^^^^^^^^^^");
        }else Log.e(TAG, "^^^^^^^^^^^^^^^^^^^^^^^^^^^ Bundle IS Null ^^^^^^^^^^^^^^^^^^^^^^");
        if(journal != null){
            title = journal.getTitle();
            text = journal.getText();
            pushId = journal.getPushId();
            if(title != null && journal.getText() != null && journal.getPushId() != null){
                setTitleText(title);
                passJournalText(text);
                setJournalPushId(pushId);
                setJournalInstance(journal);
            }
        }
        mJournalAddNewPresenter = new JournalAddNewPresenter(this);
        mFragmentAddDetailBinding.fabSaveNewJournal.setOnClickListener(this);
        mFragmentAddDetailBinding.imageJournal.setOnClickListener(this);
        mJournalAddNewPresenter.viewToolBarTitle(getActivity(), title);
//        getActivity()
//                .getActionBar()
//                .
//        getActivity().onBackPressed();
        return mFragmentAddDetailBinding.getRoot();
    }


    @Override
    public void setJournalAddNewPresenter(JournalAddNewPresenter journalAddNewPresenter) {
        mJournalAddNewPresenter = journalAddNewPresenter;
    }

    @Override
    public void onClick(View view) {
        //call showImage
        switch (view.getId()){
            case R.id.fab_save_new_journal:
                Toast.makeText(getContext(), "Save new Clicked", Toast.LENGTH_SHORT).show();
                mJournalAddNewPresenter.setEditText(mFragmentAddDetailBinding.editTextJournalText);
                mJournalAddNewPresenter.setEditTextTitle(mFragmentAddDetailBinding
                        .editTextJournalTitle);
                if(mUriPath != null) mJournalAddNewPresenter.setUriPath(Uri.parse(mUriPath));

                onSaveButtonClickedShowSaveOption();
                break;
            case R.id.image_journal:
                Toast.makeText(getContext(), "Show Image", Toast.LENGTH_SHORT).show();
                //showImage();
                ImagePickerUtility imagePickerUtility = new ImagePickerUtility(getActivity(),
                        this, IMAGE_PICKER_VAL);
                imagePickerUtility.chooseFileIntentFragments();

                //mJournalAddNewPresenter.fetchImage(imagePickerUtility);

                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       mUriPath =  mJournalAddNewPresenter.checkIfImageRequestWasSuccessful(requestCode, resultCode,
                IMAGE_PICKER_VAL, data, journalPushId, new ImageView(getContext()));

        //mJournalAddNewPresenter.getUriPath();
        Log.e(TAG, "onActivityResult.... getUriPath-- " + mJournalAddNewPresenter.getUriPath());
        Log.e(TAG, "onActivityResult.... journalPushId-- " + journalPushId);
//        DatabaseHelper.storeJournalPic(mJournalAddNewPresenter.getUriPath(), journalPushId);

    }

    @Override
    public void showImage() {
        mJournalAddNewPresenter
                .loadPicassoImageToView(mJournal.getImageUrl(), mFragmentAddDetailBinding.imageJournal,
                        R.drawable.ic_add_picture);
//        mJournalAddNewPresenter.loadPicassoImageToView(mFragmentAddDetailBinding.imageJournal,
//                R.drawable.ic_add_picture);
    }

    @Override
    public void showImage(Uri uriPath) {
        Picasso
                .get()
                .load(uriPath)
                .error(R.drawable.ic_add_a_photo)
                .into(mFragmentAddDetailBinding.imageJournal);
//        mJournalAddNewPresenter.loadPicassoImageToView(mFragmentAddDetailBinding.imageJournal,
//                R.drawable.ic_add_picture);
    }

    @Override
    public void showErrorIfTitleEmpty(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorIfTextEmpty(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveButtonClickedShowSaveOption() {
        //TODO: Inflate choice dialog
        //String journalId = null;
        //if(journalPushId != null){
            mJournalAddNewPresenter.onSaveButtonClicked(journalPushId, getChildFragmentManager());

    }

    //TODO: DELETE THIS METHOD BELOW TOO
    @Override
    public void setJournalInstance(Journal journal){
        Log.e(TAG, "setJournalInstance() ----- " + journal.getPushId() + "<< PushId");
        mJournal = journal;
    }
    @Override
    public void setTitleText(String title) {
        mFragmentAddDetailBinding.editTextJournalTitle.setText(title);
    }

    @Override
    public void passJournalText(String text) {
        mFragmentAddDetailBinding.editTextJournalText.setText(text);
    }

    @Override
    public void setJournalPushId(String pushId) {
        //mFragmentAddDetailBinding.editTextJournalText.setText(title);
        journalPushId = pushId;
    }

    @Override
    public void showImageResourceErrorToast(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public JournalAddNewPresenter getJournalAddNewPresenter() {
        return mJournalAddNewPresenter;
    }

    @Override
    public void closeFragment() {
        if (getActivity() != null) {
            AddDetailFragment fragment = (AddDetailFragment)
                    getActivity()
                            .getSupportFragmentManager()
                            .findFragmentByTag(Constants.ARGUMENTS_SHARE_JOURNAL_FRAGMENT);
            if (fragment != null) {
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .remove(fragment)
                        .commit();
            }
        }
    }
}
