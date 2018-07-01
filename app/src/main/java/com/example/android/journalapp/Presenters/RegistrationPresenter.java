package com.example.android.journalapp.Presenters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.journalapp.Anime.CustomProgressBar;
import com.example.android.journalapp.App;
import com.example.android.journalapp.Contract.RegistrationContract;
import com.example.android.journalapp.POJOs.UserDetails;
import com.example.android.journalapp.databinding.ActivitySignInBinding;
import com.example.android.journalapp.databinding.ActivitySignUpBinding;
import com.example.android.journalapp.utilities.DatabaseHelper;
import com.example.android.journalapp.utilities.Validation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class RegistrationPresenter implements RegistrationContract.Action{

    private ActivitySignInBinding mActivitySignInBinding;
    //private ActivitySignUpBinding mActivitySignUpBinding;
    private final String EMAIL_NOT_VALID = "Enter a valid email";
    private final String PASSWORD_LESS = "Password must be greater than six characters";
    private final String EMPTY_FIELD_TEXT = "Field must not be empty";
    private final String CONFIRM_PASSWORD = "Password does not match";

    private RegistrationContract.View mView;
    private String mEmail, mUsername, mPassword, mConfirmPassword, mNumber, mProfilePics;
    private int mError = 0;
    private boolean mIsFabOpen = true;
    private Animation mFabOpenAnim;
    private Animation mFabCloseAnim;
    private Animation mFabIconRotateForward;
    private Animation mFabIconRotateBackward;
    private CustomProgressBar mCustomProgressBar;
    private CoordinatorLayout mCoordinatorLayout;
    //private GoogleSignInOptions mGoogleSignInClient

    public RegistrationPresenter(RegistrationContract.View view){
        //mActivitySignInBinding = activitySignInBinding;
        mView = view;
    }

    @Override
    public void setLoadingViews(CustomProgressBar customProgressBar,
                                CoordinatorLayout coordinatorLayout) {
        //mCustomProgressBar = customProgressBar;
        mCoordinatorLayout = coordinatorLayout;

    }

    @Override
    public void onSigninUserWithGmailPassWordClicked() {
        if(mError == 0) DatabaseHelper.signInWithEMailAndPassword(mEmail, mPassword,
                mCustomProgressBar, mCoordinatorLayout);
        else{
            mCustomProgressBar.stopLoading();
            mView.authUser();
        }
    }

    @Override
    public void onSigninUserWithFacebookClicked() {

    }

    @Override
    public void onSigninUserWithGoogleClicked() {
    }

    @Override
    public void onCreateUserWithGmailPassWordClicked() {
        if(mError == 0){
            UserDetails userDetails = new UserDetails(mEmail, mEmail, mProfilePics);
            DatabaseHelper.createUserWithEmailAndPassword(
                    mView.getActivity(), userDetails, mEmail, mPassword, mCustomProgressBar,
                    mCoordinatorLayout
            );
        }else{
            mCustomProgressBar.stopLoading();
            mView.authUser();
        }
    }

    @Override
    public void onCreateUserWithFacebookClicked() {

    }

    @Override
    public void onCreateUserWithGoogleClicked() {
        // Configure Google Sign In
    }

    @Override
    public void loadFabAnimation(Animation openFab, Animation closeFab, Animation rotateForward, Animation rotateBackward) {
        mFabOpenAnim = openFab;
        mFabCloseAnim = closeFab;
        mFabIconRotateForward = rotateForward;
        mFabIconRotateBackward = rotateBackward;
    }

    @Override
    public void animateFab(ActivitySignInBinding activitySignInBinding) {
        if(mIsFabOpen){
            activitySignInBinding.fabSigninMore.startAnimation(mFabIconRotateBackward);

            activitySignInBinding.fabSigninWithEmailPassword.startAnimation(mFabCloseAnim);
            activitySignInBinding.fabSigninWithFacebook.startAnimation(mFabCloseAnim);
            activitySignInBinding.fabSigninWithGoogle.startAnimation(mFabCloseAnim);

            activitySignInBinding.fabSigninWithEmailPassword.setClickable(false);
            activitySignInBinding.fabSigninWithFacebook.setClickable(false);
            activitySignInBinding.fabSigninWithGoogle.setClickable(false);

            activitySignInBinding.fabSigninWithEmailPassword.setVisibility(View.INVISIBLE);
            activitySignInBinding.fabSigninWithFacebook.setVisibility(View.INVISIBLE);
            activitySignInBinding.fabSigninWithGoogle.setVisibility(View.INVISIBLE);

            mIsFabOpen = false;
        }else {
            activitySignInBinding.fabSigninMore.startAnimation(mFabIconRotateForward);

            activitySignInBinding.fabSigninWithEmailPassword.startAnimation(mFabOpenAnim);
            activitySignInBinding.fabSigninWithFacebook.startAnimation(mFabOpenAnim);
            activitySignInBinding.fabSigninWithGoogle.startAnimation(mFabOpenAnim);

            activitySignInBinding.fabSigninWithEmailPassword.setClickable(true);
            activitySignInBinding.fabSigninWithFacebook.setClickable(true);
            activitySignInBinding.fabSigninWithGoogle.setClickable(true);

            activitySignInBinding.fabSigninWithEmailPassword.setVisibility(View.VISIBLE);
            activitySignInBinding.fabSigninWithFacebook.setVisibility(View.VISIBLE);
            activitySignInBinding.fabSigninWithGoogle.setVisibility(View.VISIBLE);

            mIsFabOpen = true;
        }
    }

    @Override
    public void animateFab(ActivitySignUpBinding activitySignUpBinding) {
        if(mIsFabOpen){
            activitySignUpBinding.fabSignupMore.startAnimation(mFabIconRotateBackward);

            activitySignUpBinding.fabSignupWithEmailPasword.startAnimation(mFabCloseAnim);
            activitySignUpBinding.fabSignupWithFacebook.startAnimation(mFabCloseAnim);
            activitySignUpBinding.fabSignupWithGoogle.startAnimation(mFabCloseAnim);

            activitySignUpBinding.fabSignupWithEmailPasword.setClickable(false);
            activitySignUpBinding.fabSignupWithFacebook.setClickable(false);
            activitySignUpBinding.fabSignupWithGoogle.setClickable(false);

            activitySignUpBinding.fabSignupWithEmailPasword.setVisibility(View.INVISIBLE);
            activitySignUpBinding.fabSignupWithFacebook.setVisibility(View.INVISIBLE);
            activitySignUpBinding.fabSignupWithGoogle.setVisibility(View.INVISIBLE);

            mIsFabOpen = false;
        }else {
            activitySignUpBinding.fabSignupMore.startAnimation(mFabIconRotateForward);

            activitySignUpBinding.fabSignupWithEmailPasword.startAnimation(mFabOpenAnim);
            activitySignUpBinding.fabSignupWithFacebook.startAnimation(mFabOpenAnim);
            activitySignUpBinding.fabSignupWithGoogle.startAnimation(mFabOpenAnim);

            activitySignUpBinding.fabSignupWithEmailPasword.setClickable(true);
            activitySignUpBinding.fabSignupWithFacebook.setClickable(true);
            activitySignUpBinding.fabSignupWithGoogle.setClickable(true);

            activitySignUpBinding.fabSignupWithEmailPasword.setVisibility(View.VISIBLE);
            activitySignUpBinding.fabSignupWithFacebook.setVisibility(View.VISIBLE);
            activitySignUpBinding.fabSignupWithGoogle.setVisibility(View.VISIBLE);

            mIsFabOpen = true;
        }
    }

    @Override
    public int errorCount() {
        return mError;
    }

    @Override
    public void setErrorCount(int errorCount) {
        mError = errorCount;
    }

    @Override
    public void checkUsername() {
        if(!Validation.validateFields(mUsername)){
            mView.setUsernameError(EMPTY_FIELD_TEXT);
            mError++;
        }
    }

    @Override
    public void checkPassword() {
        if(mPassword.length() < 6){
            mView.setPasswordError(PASSWORD_LESS);
            mError++;
        }
    }

    @Override
    public void checkConfirmPassword() {
        if(!mPassword.equals(mConfirmPassword)){
            mView.setConfirmPasswordError(CONFIRM_PASSWORD);
            mError++;
        }
    }

    @Override
    public void checkEmail() {
        if(!Validation.validateEmail(mEmail)){
            mView.setEmailError(EMAIL_NOT_VALID);
            mError++;
        }
    }

    @Override
    public void checkNumber() {
        if(!Validation.validateEmail(mNumber)){
            mView.setNumberError(EMPTY_FIELD_TEXT);
            mError++;
        }
    }

    @Override
    public void setUsername(String username) {
        mUsername = username;
        //checkUsername();
    }

    @Override
    public void setEmail(String email) {
        mEmail = email;
        //checkEmail();
    }

    @Override
    public void setConfirmPassword(String confirmPassword) {
        mConfirmPassword = confirmPassword;
    }

    @Override
    public void setPassword(String password) {
        mPassword = password;
    }

    @Override
    public void setUserImageUrl(String userPicsUrl){
        mProfilePics = userPicsUrl;
    }
    @Override
    public void setError(ActivitySignInBinding activitySignInBinding) {
        activitySignInBinding.textinputLayoutSigninName.setError(null);
        activitySignInBinding.textinputLayoutSigninPassword.setError(null);
    }

    @Override
    public void setError(ActivitySignUpBinding activitySignUpBinding) {
        activitySignUpBinding.textinputLayoutSignupUsername.setError(null);
        activitySignUpBinding.textinputLayoutSignupPassword.setError(null);
        activitySignUpBinding.textinputLayoutSignupPhoneNum.setError(null);
        activitySignUpBinding.textinputLayoutSignupEmail.setError(null);
        activitySignUpBinding.textinputLayoutSignupConfirmPassword.setError(null);
    }

    @Override
    public void setNumber(String number) {
        mNumber = number;
    }

    @Override
    public String getTextFromEditText(EditText editText){
        return editText.getText().toString().trim();
    }

    @Override
    public int authenticateUserEmail(){
        mError = 0;
        mView.authUser();
        checkEmail();
        if(mError == 0){
            return mError;
        }
        return -1;
    }
    @Override
    public void authenticateUser(CoordinatorLayout coordinatorLayout) {
        mCustomProgressBar.startLoading();
        mError = 0;
        mView.authUser();
        checkEmail();
        checkPassword();
        setLoadingViews(mCustomProgressBar, coordinatorLayout);
        if(mError == 0) {
            onSigninUserWithGmailPassWordClicked();
            mError = 0;
        }
        else{
            mCustomProgressBar.stopLoading();
            //TODO: STop custom progress bar
        }
    }

    @Override
    public void setupCustomLoadingBar(Activity activity, ImageView imageView) {
        mCustomProgressBar = new CustomProgressBar(imageView, activity);
    }

    @Override
    public void authenticateUserSignup(CoordinatorLayout coordinatorLayout) {
        mCustomProgressBar.startLoading();
        mError = 0;
        mView.authUser();
        checkUsername();
        checkEmail();
        checkPassword();
        checkConfirmPassword();
        //checkNumber();
        setLoadingViews(mCustomProgressBar, coordinatorLayout);
        if(mError == 0) {
            onCreateUserWithGmailPassWordClicked();
            mError = 0;
        }else {
            mCustomProgressBar.stopLoading();
        }
    }

    @Override
    public void resetPassWord(Activity activity) {
        App.getsUsersDetailNode()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserDetails userDetails = dataSnapshot.getValue(UserDetails.class);
                        if(userDetails != null) {
                            FirebaseAuth.getInstance().sendPasswordResetEmail(userDetails
                                    .getmEmailAddress())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(activity,
                                                        "Password sent to email", Toast.LENGTH_SHORT).show();
                                            }else{
                                                Toast.makeText(activity,
                                                        "Password change not successful", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
