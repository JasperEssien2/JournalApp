package com.example.android.journalapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.android.journalapp.Constants.FirebaseConstants;
import com.example.android.journalapp.Contract.RegistrationContract;
import com.example.android.journalapp.Presenters.RegistrationPresenter;
import com.example.android.journalapp.R;
import com.example.android.journalapp.databinding.ActivitySignUpBinding;
import com.example.android.journalapp.utilities.DatabaseHelper;
import com.example.android.journalapp.utilities.ImagePickerUtility;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener,
        RegistrationContract.View {

    public static final int IMAGE_PICKER_CONSTANT = 7789;
    private static final int RC_SIGN_IN = 1;

    private ActivitySignUpBinding mActivitySignUpBinding;
    private RegistrationPresenter mRegistrationPresenter;

    private boolean mIsFabOpen = true;
    private Animation mFabOpenAnim;
    private Animation mFabCloseAnim;
    private Animation mFabIconRotateForward;
    private Animation mFabIconRotateBackward;
    private Uri mImageUriPath;
    private String mProfilePicsUrl;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivitySignUpBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_sign_up);
        mRegistrationPresenter = new RegistrationPresenter(this);

        loadFabAnimation();
        mRegistrationPresenter.animateFab(mActivitySignUpBinding);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(FirebaseConstants.GOOGLE_WEB_CLIENT_ID)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_signup_more:
                //animateFab();
                mRegistrationPresenter.animateFab(mActivitySignUpBinding);
                break;
            case R.id.fab_signup_with_email_pasword:
                DatabaseHelper.setmActivity(this);
                setErrorNull();

                mRegistrationPresenter.setErrorCount(0);
                mRegistrationPresenter.setupCustomLoadingBar(this, mActivitySignUpBinding
                        .imageCustomProgressBarIcon);
                mRegistrationPresenter.authenticateUserSignup(mActivitySignUpBinding.coordinatorLayout);
                //mRegistrationPresenter.onCreateUserWithGmailPassWordClicked();
                break;
            case R.id.fab_signup_with_facebook:
                mRegistrationPresenter.onCreateUserWithFacebookClicked();
                break;
            case R.id.fab_signup_with_google:
                mRegistrationPresenter.onCreateUserWithGoogleClicked();
                break;
            case R.id.image_button_signup_picker:
                ImagePickerUtility imagePickerUtility = new ImagePickerUtility(this,
                        IMAGE_PICKER_CONSTANT);
                imagePickerUtility.chooseFileIntent();
        }
    }


    @Override
    public void authUser() {
        mRegistrationPresenter.setEmail(mRegistrationPresenter.getTextFromEditText(
                mActivitySignUpBinding.edittextSignupEmail));
        mRegistrationPresenter.setUsername(mRegistrationPresenter.getTextFromEditText(
                mActivitySignUpBinding.edittextSignupUsername));
        mRegistrationPresenter.setNumber(mRegistrationPresenter.getTextFromEditText(
                mActivitySignUpBinding.edittextSignupPhoneNum));

        mRegistrationPresenter.setPassword(mRegistrationPresenter.getTextFromEditText(
                mActivitySignUpBinding.edittextSignupPassword));

        mRegistrationPresenter.setConfirmPassword(mRegistrationPresenter.getTextFromEditText(
                mActivitySignUpBinding.edittextSignupConfirmPassword));
        mRegistrationPresenter.setUserImageUrl(mProfilePicsUrl);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICKER_CONSTANT && resultCode == RESULT_OK && data != null &&
                data.getData() != null) {
            mImageUriPath = data.getData();
            Picasso.get()
                    .load(mImageUriPath)
//                    .centerCrop()
                    .error(R.color.colorLightGray)
                    .into(mActivitySignUpBinding.circleImageSignupProfilePics);
            mProfilePicsUrl = DatabaseHelper.storeProfilePic(mImageUriPath);
        } else {
            Toast.makeText(this, "Action was not successful, Try again",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void loadFabAnimation() {
        mFabOpenAnim = AnimationUtils.loadAnimation(this, R.anim.anime_fab_open);
        mFabCloseAnim = AnimationUtils.loadAnimation(this, R.anim.anime_fab_close);
        mFabIconRotateForward = AnimationUtils.loadAnimation(this, R.anim.anime_fab_rotate_forward);
        mFabIconRotateBackward = AnimationUtils.loadAnimation(this, R.anim.anime_fab_rotate_backward);
        mActivitySignUpBinding.fabSignupMore.setOnClickListener(this);
        mActivitySignUpBinding.fabSignupWithEmailPasword.setOnClickListener(this);
        mActivitySignUpBinding.fabSignupWithFacebook.setOnClickListener(this);
        mActivitySignUpBinding.fabSignupWithGoogle.setOnClickListener(this);
        mActivitySignUpBinding.imageButtonSignupPicker.setOnClickListener(this);
        mRegistrationPresenter.loadFabAnimation(mFabOpenAnim, mFabCloseAnim, mFabIconRotateBackward,
                mFabIconRotateForward);
        mRegistrationPresenter.animateFab(mActivitySignUpBinding);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void showProfilePics(RequestCreator requestCreator) {
        requestCreator
                .into(mActivitySignUpBinding.circleImageSignupProfilePics);
    }

    @Override
    public void onShowMoreFabButtonClicked(boolean isFabOpen) {

    }

    @Override
    public void setUpProfilePic(int requestCode) {

    }

    @Override
    public void setPasswordError(String error) {
        mActivitySignUpBinding.textinputLayoutSignupPassword.setError(error);
    }

    @Override
    public void setConfirmPasswordError(String error) {
        mActivitySignUpBinding.textinputLayoutSignupConfirmPassword.setError(error);
    }

    @Override
    public void setEmailError(String error) {
        mActivitySignUpBinding.textinputLayoutSignupEmail.setError(error);
    }

    @Override
    public void setNumberError(String error) {
        mActivitySignUpBinding.textinputLayoutSignupPhoneNum.setError(error);
    }

    @Override
    public void setUsernameError(String error) {
        mActivitySignUpBinding.textinputLayoutSignupUsername.setError(error);
    }

    @Override
    public void setErrorNull() {
        mRegistrationPresenter.setError(mActivitySignUpBinding);
    }

    @Override
    public void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
}
