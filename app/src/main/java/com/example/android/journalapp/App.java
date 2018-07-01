package com.example.android.journalapp;

import android.app.Application;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.example.android.journalapp.Activities.HomeActivity;
import com.example.android.journalapp.Activities.SignInActivity;
import com.example.android.journalapp.Constants.FirebaseConstants;
import com.example.android.journalapp.POJOs.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class App extends Application {

    private static FirebaseAuth sFirebaseAuth;
    private static FirebaseDatabase sRootDatabase;
    private static DatabaseReference sUsersNodeDataRef;
    private static DatabaseReference sUsersDetailNode;
    private static DatabaseReference sUsersJournalsNode;
    private static DatabaseReference sFriendsNode;
    private static DatabaseReference sJournalTypeNode;
    private static DatabaseReference sJournalTypeSharedByMeNode;
    private static DatabaseReference sJournalTypePersonalNode;
    private static DatabaseReference sJournalTypeFeedsNode;
    private static FirebaseUser sFirebaseUser;
    private static UserDetails mUserDetails;

    private static FirebaseStorage sFirebaseStorage;
    private static StorageReference sStorageReference;
    private static StorageReference sProfileImageStorageRef;
    private static StorageReference sJournalImageStorageRef;

    @Override
    public void onCreate() {
        super.onCreate();
        if(FirebaseApp.getInstance() != null){
            //TODO: Uncomment the line of code below later
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            sRootDatabase = FirebaseDatabase.getInstance();
            sFirebaseAuth = FirebaseAuth.getInstance();
            sFirebaseUser = sFirebaseAuth.getCurrentUser();
            sFirebaseStorage = FirebaseStorage.getInstance();
            sStorageReference = sFirebaseStorage.getReference();
            sProfileImageStorageRef = sStorageReference.
                    child(FirebaseConstants.FIREBASE_PROFILE_IMAGE_NODE);
            sJournalImageStorageRef = sStorageReference
                    .child(FirebaseConstants.FIREBASE_JOURNAL_IMAGE_NODE);

            if(sFirebaseUser == null){
                startActivity(new Intent(this, SignInActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|
                        Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }else{
                sUsersNodeDataRef = sRootDatabase.getReference().child(sFirebaseUser.getUid());
                setUsersDetail(sFirebaseUser.getUid());
                startActivity(new Intent(this, HomeActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|
                                Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
            sFirebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    //sFirebaseAuth = firebaseAuth;
                    if(firebaseAuth.getCurrentUser() != null) {
                        firebaseAuth.updateCurrentUser(firebaseAuth.getCurrentUser());
                        setUsersDetail(firebaseAuth.getCurrentUser().getUid());
                    }
                }
            });
        }else{
            startActivity(new Intent(this, SignInActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|
                            Intent.FLAG_ACTIVITY_CLEAR_TASK));
        }
    }

    /**
     * This is for getting setting the UserDetails object to be returned
     * @param userDetails
     */
    private static void setUserDetails(UserDetails userDetails){
        mUserDetails = userDetails;
    }

    /**
     * This gets the currentUser uid and sets the user details to make the references available to the app
     * @param uid
     */
    public static void setUsersDetail(String uid){
        sUsersNodeDataRef = sRootDatabase.getReference().child(uid);
        sUsersDetailNode = sUsersNodeDataRef.child(FirebaseConstants.USER_DETAIL_NODE);
        sUsersDetailNode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserDetails userDetails = dataSnapshot.getValue(UserDetails.class);
                if(userDetails != null) {
                    setUserDetails(userDetails);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                sFirebaseAuth.signOut();
                sFirebaseUser = null;
            }
        });
        sUsersJournalsNode = sUsersNodeDataRef.child(FirebaseConstants.JOURNALS_NODE);
        sFriendsNode = sUsersNodeDataRef.child(FirebaseConstants.FRIENDS_NODE);
        sJournalTypeNode = sUsersJournalsNode.child(FirebaseConstants.JOURNALS_TYPE_NODE);
        sJournalTypePersonalNode = sJournalTypeNode.child(FirebaseConstants.JOURNAL_TYPE_PERSONAL);
        sJournalTypeSharedByMeNode = sJournalTypeNode.child(FirebaseConstants.JOURNAL_TYPE_SHARED_BY_ME);
        sJournalTypeFeedsNode = sJournalTypeNode.child(FirebaseConstants.JOURNAL_TYPE_FEEDS);
    }

    /**
     * This method below sets the userDetails in when the user first registers with the app
     * @param uid
     * @param userDetails
     * @param onCompleteListener
     */
    public static void setUserDetails(String uid, UserDetails userDetails,
                                      OnCompleteListener onCompleteListener){
        sUsersNodeDataRef = sRootDatabase.getReference().child(uid);
        sUsersDetailNode = sUsersNodeDataRef.child(FirebaseConstants.USER_DETAIL_NODE);
        sUsersDetailNode.setValue(userDetails)
                .addOnCompleteListener(onCompleteListener);
        sUsersJournalsNode = sUsersNodeDataRef.child(FirebaseConstants.JOURNALS_NODE);
        sFriendsNode = sUsersNodeDataRef.child(FirebaseConstants.FRIENDS_NODE);
        sJournalTypeNode = sUsersJournalsNode.child(FirebaseConstants.JOURNALS_TYPE_NODE);
        sJournalTypePersonalNode = sJournalTypeNode.child(FirebaseConstants.JOURNAL_TYPE_PERSONAL);
        sJournalTypeSharedByMeNode = sJournalTypeNode.child(FirebaseConstants.JOURNAL_TYPE_SHARED_BY_ME);
        sJournalTypeFeedsNode = sJournalTypeNode.child(FirebaseConstants.JOURNAL_TYPE_FEEDS);


    }

    public static FirebaseAuth getFirebaseAuth(){
        return sFirebaseAuth;
    }

    public static void setsFirebaseUser(FirebaseUser firebaseUser){
        sFirebaseUser = firebaseUser;
    }

    public static DatabaseReference getsUsersDetailNode() {
        return sUsersDetailNode;
    }

    public static DatabaseReference getsUsersJournalsNode() {
        return sUsersJournalsNode;
    }

    public static DatabaseReference getsFriendsNode() {
        return sFriendsNode;
    }

    public static DatabaseReference getsJournalTypeNode() {
        return sJournalTypeNode;
    }

    public static DatabaseReference getsJournalTypeSharedByMeNode() {
        return sJournalTypeSharedByMeNode;
    }

    public static FirebaseUser getsFirebaseUser() {
        return sFirebaseUser;
    }

    public static DatabaseReference getsJournalTypePersonalNode() {
        return sJournalTypePersonalNode;
    }

    public static DatabaseReference getsJournalTypeFeedsNode() {
        return sJournalTypeFeedsNode;
    }

    public static FirebaseDatabase getsRootDatabase() {
        return sRootDatabase;
    }

    public static StorageReference getsProfileImageStorageRef() {
        return sProfileImageStorageRef;
    }

    public static StorageReference getsJournalImageStorageRef() {
        return sJournalImageStorageRef;
    }
}
