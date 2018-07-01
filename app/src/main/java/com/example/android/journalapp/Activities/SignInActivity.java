package com.example.android.journalapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.android.journalapp.App;
import com.example.android.journalapp.Constants.FirebaseConstants;
import com.example.android.journalapp.Contract.RegistrationContract;
import com.example.android.journalapp.POJOs.UserDetails;
import com.example.android.journalapp.Presenters.RegistrationPresenter;
import com.example.android.journalapp.R;
import com.example.android.journalapp.databinding.ActivitySignInBinding;
import com.example.android.journalapp.utilities.DatabaseHelper;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.squareup.picasso.RequestCreator;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener,
        RegistrationContract.View{

    private static final int RC_SIGN_IN = 1;
    private static final String TAG = SignInActivity.class.getSimpleName();
    private ActivitySignInBinding mActivitySignInBinding;
    private RegistrationPresenter mRegistrationPresenter;
    private Animation mFabOpenAnim;
    private Animation mFabCloseAnim;
    private Animation mFabIconRotateForward;
    private Animation mFabIconRotateBackward;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivitySignInBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_sign_in);
        mRegistrationPresenter = new RegistrationPresenter(this);//, mFabOpenAnim, mFabCloseAnim,
//                mFabIconRotateForward, mFabIconRotateBackward);
        loadFabAnimation();
        mRegistrationPresenter.animateFab(mActivitySignInBinding);


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(FirebaseConstants.GOOGLE_WEB_CLIENT_ID)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_signin_more:
                mRegistrationPresenter.animateFab(mActivitySignInBinding);
                break;
            case R.id.fab_signin_with_email_password:
                DatabaseHelper.setmActivity(this);
                setErrorNull();
                mRegistrationPresenter.setupCustomLoadingBar(this, mActivitySignInBinding
                        .imageCustomProgressBarIcon);
                mRegistrationPresenter.setErrorCount(0);
                mRegistrationPresenter.authenticateUser(mActivitySignInBinding.coordinatorLayout);
                break;
            case R.id.fab_signin_with_google: signInWithGoogle();
                break;
            case R.id.fab_signin_with_facebook:
                break;
            case R.id.button_forgot_password:
                mRegistrationPresenter.setupCustomLoadingBar(this, mActivitySignInBinding
                        .imageCustomProgressBarIcon);
                mRegistrationPresenter.setErrorCount(0);
                int error = mRegistrationPresenter.authenticateUserEmail();
                if(error == 0){
                    mRegistrationPresenter.resetPassWord(this);
                }
                break;

            case R.id.text_signin_signup_link:
                startActivity(new Intent(this, SignUpActivity.class));
        }
    }

    @Override
    public void authUser() {
        //mRegistrationPresenter.setError(mActivitySignInBinding);
        mRegistrationPresenter.setEmail(mRegistrationPresenter.getTextFromEditText(
                mActivitySignInBinding.edittextSigninEmail));
        mRegistrationPresenter.setPassword(mRegistrationPresenter.getTextFromEditText(
                mActivitySignInBinding.edittextSigninPassword));
//        mRegistrationPresenter.authenticateUser();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        FirebaseAuth mAuth = App.getFirebaseAuth();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            App.setsFirebaseUser(user);
                            App.getsUsersDetailNode()
                                    .setValue(new UserDetails(user.getDisplayName(),
                                            user.getEmail(), user.getPhotoUrl().toString()));

                            startActivity(new Intent(SignInActivity.this, HomeActivity.class));
                            SignInActivity.this.finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                            App.setsFirebaseUser(null);
                        }

                        // ...
                    }
                });
    }

    @Override
    public void showProfilePics(RequestCreator requestCreator) {

    }

    @Override
    public void onShowMoreFabButtonClicked(boolean isFabOpen) {

    }

    @Override
    public void setUpProfilePic(int requestCode) {

    }

    @Override
    public void loadFabAnimation() {
        mFabOpenAnim = AnimationUtils.loadAnimation(this, R.anim.anime_fab_open);
        mFabCloseAnim = AnimationUtils.loadAnimation(this, R.anim.anime_fab_close);
        mFabIconRotateForward = AnimationUtils.loadAnimation(this, R.anim.anime_fab_rotate_forward);
        mFabIconRotateBackward = AnimationUtils.loadAnimation(this, R.anim.anime_fab_rotate_backward);
        mActivitySignInBinding.fabSigninMore.setOnClickListener(this);
        mActivitySignInBinding.fabSigninWithEmailPassword.setOnClickListener(this);
        mActivitySignInBinding.fabSigninWithFacebook.setOnClickListener(this);
        mActivitySignInBinding.fabSigninWithGoogle.setOnClickListener(this);
        mActivitySignInBinding.textSigninSignupLink.setOnClickListener(this);
        mRegistrationPresenter.loadFabAnimation(mFabOpenAnim, mFabCloseAnim, mFabIconRotateBackward,
                mFabIconRotateForward);
        mRegistrationPresenter.animateFab(mActivitySignInBinding);
    }

    @Override
    public void setPasswordError(String error) {
        mActivitySignInBinding.textinputLayoutSigninPassword.setError(error);
    }

    @Override
    public void setEmailError(String error) {
        mActivitySignInBinding.textinputLayoutSigninName.setError(error);
    }

    @Override
    public void setErrorNull() {
        mRegistrationPresenter.setError(mActivitySignInBinding);
    }

    @Override
    public void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void setConfirmPasswordError(String error) {

    }

    @Override
    public void setNumberError(String error) {

    }

    @Override
    public void setUsernameError(String error) {

    }
}
