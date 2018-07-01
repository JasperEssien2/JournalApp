package com.example.android.journalapp.Contract;

import android.app.Activity;
import android.support.design.widget.CoordinatorLayout;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.android.journalapp.Anime.CustomProgressBar;
import com.example.android.journalapp.databinding.ActivitySignInBinding;
import com.example.android.journalapp.databinding.ActivitySignUpBinding;
import com.squareup.picasso.RequestCreator;

public class RegistrationContract {

    private final String EMAIL_NOT_VALID = "Enter a valid email";
    private final String PASSWORD_LESS = "Password must be greater than six characters";

    public interface View{

        void loadFabAnimation();

        void showProfilePics(RequestCreator requestCreator);

        void onShowMoreFabButtonClicked(boolean isFabOpen);

        void setUpProfilePic(int requestCode);

        void authUser();

        void setPasswordError(String error);

        void setConfirmPasswordError(String error);

        void setEmailError(String error);

        void setNumberError(String error);

        void setUsernameError(String error);

        void setErrorNull();

        void signInWithGoogle();

        Activity getActivity();

    }

    public interface Action{
        void setError(ActivitySignInBinding activitySignInBinding);

        void setError(ActivitySignUpBinding activitySignUpBinding);

        void setLoadingViews(CustomProgressBar customProgressBar,
                             CoordinatorLayout coordinatorLayout);
        void onCreateUserWithGmailPassWordClicked();

        void onCreateUserWithFacebookClicked();

        void onCreateUserWithGoogleClicked();

        void onSigninUserWithGmailPassWordClicked();

        void setupCustomLoadingBar(Activity activity, ImageView imageView);

        void onSigninUserWithFacebookClicked();

        void onSigninUserWithGoogleClicked();

        void animateFab(ActivitySignInBinding activitySignInBinding);

        void animateFab(ActivitySignUpBinding activitySignUpBinding);

        int errorCount();

        void setErrorCount(int errorCount);

        void checkUsername();

        void checkNumber();

        void checkPassword();

        void checkConfirmPassword();

        void checkEmail();

        void setUsername(String username);

        void setEmail(String email);

        void setNumber(String number);

        void setConfirmPassword(String confirmPassword);

        void setPassword(String password);

        void setUserImageUrl(String userPicsUrl);

        void loadFabAnimation(Animation openFab, Animation closeFab, Animation rotateForward,
                              Animation rotateBackward);

        String getTextFromEditText(EditText editText);

        int authenticateUserEmail();

        void authenticateUser(CoordinatorLayout coordinatorLayout);

        void authenticateUserSignup(CoordinatorLayout coordinatorLayout);

        void resetPassWord(Activity activity);
    }
}
