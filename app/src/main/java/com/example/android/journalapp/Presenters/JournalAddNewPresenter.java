package com.example.android.journalapp.Presenters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.android.journalapp.Activities.HomeActivity;
import com.example.android.journalapp.App;
import com.example.android.journalapp.Constants.Constants;
import com.example.android.journalapp.Contract.JournallAddNewContract;
import com.example.android.journalapp.Fragments.ShareChoiceDialogFragment;
import com.example.android.journalapp.POJOs.Journal;
import com.example.android.journalapp.utilities.DatabaseHelper;
import com.example.android.journalapp.utilities.ImagePickerUtility;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_OK;

public class JournalAddNewPresenter implements JournallAddNewContract.Action {

    private static final String TAG = JournalAddNewPresenter.class.getSimpleName();
    private JournallAddNewContract.View mView;
    private Uri mUriPath;
    private EditText mJournalText, mJournalTitle;

    private String mText = "", mTitle = "", mIMageUrl;
    private final String ERROR_JOURNAL_TEXT_EMPTY = "Journal mText cannot be empty";
    private final String ERROR_JOURNAL_TITLE_EMPTY = "Journal mTitle cannot be empty";

    public JournalAddNewPresenter(JournallAddNewContract.View view){
        mView = view;
    }

    //TODO... AddingNewJournal on first trial wont work fix that
    @Override
    public void onSaveButtonClicked(String journalPushId, FragmentManager fragmentManager) {
        //journal.getPushId()
        boolean isJournalTitleEmpty = true, isJournalTextEmpty = true;

        if(mJournalText != null){
            mText = getTextFromEditText(mJournalText);
            if(TextUtils.isEmpty(mText)){
                isJournalTextEmpty = true;
                mView.showErrorIfTextEmpty(ERROR_JOURNAL_TEXT_EMPTY);
            }else{
                isJournalTextEmpty = false;
            }
        }
        if(mJournalTitle != null){
            mTitle = getTextFromEditText(mJournalTitle);
            if(TextUtils.isEmpty(mTitle)){
                isJournalTitleEmpty = true;
                mView.showErrorIfTitleEmpty(ERROR_JOURNAL_TITLE_EMPTY);
            }else {
                isJournalTitleEmpty = false;
            }
        }
        if(!isJournalTextEmpty && !isJournalTitleEmpty) {
            if (fragmentManager != null) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment prevFrag = fragmentManager.findFragmentByTag(
                        Constants.ARGUMENTS_SHARE_JOURNAL_FRAGMENT);
                if (prevFrag != null) {
                    fragmentTransaction.remove(prevFrag);
                }
                fragmentTransaction.addToBackStack(Constants.ARGUMENTS_SHARE_JOURNAL_FRAGMENT);
                final ShareChoiceDialogFragment dialogFragment =
                        ShareChoiceDialogFragment.newInstance(journalPushId);
                dialogFragment.getJournalAddNewPresenter(mView.getJournalAddNewPresenter());
                dialogFragment.show(fragmentTransaction,
                        Constants.ARGUMENTS_SHARE_JOURNAL_FRAGMENT);
            }
        }

    }

    @Override
    public void onSaveToHighPiorityOptionClicked(String journalPushId) {
//        Log.e("JournalAddNewPresenter", "******* onSaveFriendsOptionClicked() ****** ");
////        DatabaseReference dbRef = App.getsUsersJournalsNode().push();
////        String pushId = dbRef.getKey();
//        String pushId;
//        if(journalPushId == null || TextUtils.isEmpty(journalPushId)) {
//            DatabaseReference dbRef = App.getsUsersJournalsNode().push();
//            pushId = dbRef.getKey();
//        }else{
//            pushId = journalPushId;
//        }
        mView.closeFragment();
    }


    @Override
    public void onSavePersonalOptionClicked(String journalPushId) {
        Log.e("JournalAddNewPresenter", "******* onSavePersonalOptionClicked() ****** ");
        String pushId;
        if(journalPushId == null || TextUtils.isEmpty(journalPushId)) {
            DatabaseReference dbRef = App.getsUsersJournalsNode().push();
            pushId = dbRef.getKey();
            if(mUriPath != null) DatabaseHelper.storeJournalPic(mUriPath, pushId);
            DatabaseHelper.putJournalInJournalNode(pushId, new Journal(mText, mTitle,
                    ServerValue.TIMESTAMP, pushId, false));
        }else{
            DatabaseHelper.updateJournalInJournalNode(journalPushId, mText, mTitle,
                    ServerValue.TIMESTAMP);
        }
        mView.closeFragment();
    }

    @Override
    public void onShareToOtherSourcesClicked() {
        //Dude open an intent and share the file
        mView.closeFragment();
    }


    @Override
    public void fetchImage(ImagePickerUtility imagePicker) {
        imagePicker.chooseFileIntent();
    }

    @Override
    public void setUriPath(Uri uri) {
        mUriPath = uri;
    }

    @Override
    public void storeImageInDatabase() {

    }

    @Override
    public Uri getUriPath() {
        return mUriPath;
    }

    @Override
    public void loadPicassoImageToView(View v, int errorImage) {
        if(mUriPath != null) {

            Picasso.get()
                    .load(mUriPath)
//                    .centerCrop()
                    .error(errorImage)
                    .into(((ImageView) v));

        }
    }

    @Override
    public void loadPicassoImageToView(String imageUrl, View v, int errorImage) {
        if(imageUrl != null) {

            Picasso.get()
                    .load(imageUrl)
//                    .centerCrop()
                    .error(errorImage)
                    .into(((ImageView) v));
            Log.e(TAG, "'''''''''''''''''loadPicassoImageView() called ''''''''''''''''''''''" +
                    "''''''''");

        }
    }

    @Override
    public String getTextFromEditText(EditText editText){
        return editText.getText().toString().trim();
    }

    @Override
    public String checkIfImageRequestWasSuccessful(int requestCode, int resultCode,
                                                 int imagePickerVal, Intent data,
                                                 String journalPushId, View detailViewImage) {
        if(requestCode == imagePickerVal && resultCode == RESULT_OK && data != null &&
                data.getData() != null){
            mUriPath = data.getData();
            if(journalPushId != null) {
                DatabaseHelper.storeJournalPic(mUriPath, journalPushId);
                mView.showImage();
            }else{
                mView.showImage(mUriPath);
            }

        }else{
            mView.showImageResourceErrorToast("Action was not successful, Try again");
        }
        return mUriPath.toString();
    }

    @Override
    public void setEditText(EditText editText) {
        mJournalText = editText;
    }

    @Override
    public void setEditTextTitle(EditText editText) {
        mJournalTitle = editText;
    }

    @Override
    public void viewToolBarTitle(Activity activity, String title) {
        if((((HomeActivity)activity).getSupportActionBar() != null)) {
            (((HomeActivity)activity))
                    .getSupportActionBar()
                    .setTitle(title);
        }
    }
}
