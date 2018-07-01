package com.example.android.journalapp.Contract;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.widget.EditText;

import com.example.android.journalapp.POJOs.Journal;
import com.example.android.journalapp.Presenters.JournalAddNewPresenter;
import com.example.android.journalapp.utilities.ImagePickerUtility;

public class JournallAddNewContract {

    public interface View{
        void showImage();

        void showImage(Uri uriPath);

        void showImageResourceErrorToast(String error);

        void showErrorIfTitleEmpty(String error);

        void showErrorIfTextEmpty(String error);

        void onSaveButtonClickedShowSaveOption();

        void closeFragment();

        void setJournalInstance(Journal journal);

        void setTitleText(String title);

        void passJournalText(String text);

        void setJournalPushId(String pushId);

        JournalAddNewPresenter getJournalAddNewPresenter();

        void setJournalAddNewPresenter(JournalAddNewPresenter journalAddNewPresenter);
    }

    public interface Action{
        void onSaveButtonClicked(String journalPushId, FragmentManager fragmentManager);

        void onSaveToHighPiorityOptionClicked(String journalUid);

        void onSavePersonalOptionClicked(String journalUid);

        void viewToolBarTitle(Activity activity, String title);

        void onShareToOtherSourcesClicked();

        void fetchImage(ImagePickerUtility imagePicker);

        void setUriPath(Uri uri);

        void storeImageInDatabase();

        Uri getUriPath();

        String checkIfImageRequestWasSuccessful(int requestCode, int resultCode,
                                              int imagePickerVal, Intent data,
                                              String journalPushId, android.view.View detailViewImage);

        void loadPicassoImageToView(android.view.View v, int errorImage);

        void loadPicassoImageToView(String imageUrl, android.view.View v, int errorImage);

        void setEditText(EditText editText);

        void setEditTextTitle(EditText editText);

        String getTextFromEditText(EditText editText);
    }
}
