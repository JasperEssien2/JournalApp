package com.example.android.journalapp.utilities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.android.journalapp.Activities.HomeActivity;
import com.example.android.journalapp.Anime.CustomProgressBar;
import com.example.android.journalapp.App;
import com.example.android.journalapp.Constants.FirebaseConstants;
import com.example.android.journalapp.POJOs.Journal;
import com.example.android.journalapp.POJOs.UserDetails;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper {

    private static Activity mActivity;

    public static Activity getmActivity() {
        return mActivity;
    }

    public static void setmActivity(Activity activity) {
        mActivity = activity;
    }

    public static void createUserWithEmailAndPassword(
            final Activity activity, final UserDetails userDetails, String email, String password,
            CustomProgressBar customProgressBar, CoordinatorLayout coordinatorLayout){

        App.getFirebaseAuth()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        final FirebaseUser firebaseUser = App.getFirebaseAuth().getCurrentUser();
                        if(task.isSuccessful()){
                            customProgressBar.stopLoading();
                            Toast.makeText(activity, "Completed and successful"
                                    , Toast.LENGTH_SHORT).show();
                            OnCompleteListener setUserDetailsOnCompleteListener =
                                    new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if(task.isSuccessful()){
                                                activity.startActivity(new Intent(
                                                        activity, HomeActivity.class)
                                                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|
                                                            Intent.FLAG_ACTIVITY_CLEAR_TOP|
                                                            Intent.FLAG_ACTIVITY_NEW_TASK));
                                            }else{
                                                customProgressBar.stopLoading();
                                                if(firebaseUser != null) firebaseUser.delete();
                                            }
                                        }
                                    };
                            OnFailureListener setUserDetailsOnFailureListener =
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            customProgressBar.stopLoading();
                                        }
                                    };

                            if(firebaseUser != null) {
                                App.setUserDetails(firebaseUser.getUid(), userDetails,
                                        setUserDetailsOnCompleteListener);
                            }
                        }else{
                            try {
                                throw task.getException();
                            } catch(FirebaseAuthInvalidCredentialsException e) {
                                Snackbar.make(coordinatorLayout, "Invalid Credentials",
                                        Snackbar.LENGTH_SHORT).show();

                            } catch(FirebaseAuthUserCollisionException e) {
                                Snackbar.make(coordinatorLayout, "This account is already " +
                                                "registered",
                                        Snackbar.LENGTH_SHORT).show();
                            } catch(Exception e) {
                                Snackbar.make(coordinatorLayout, "Check your internet connection," +
                                                "Try again",
                                        Snackbar.LENGTH_SHORT).show();
                            }
//                            Toast.makeText(activity, "Not successful"
//                                    , Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public static void signInWithEMailAndPassword(String email, String password,
                                                  CustomProgressBar customProgressBar,
                                                  CoordinatorLayout coordinatorLayout){
        App.getFirebaseAuth()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            customProgressBar.stopLoading();
                            Toast.makeText(mActivity, "Sign in successfully", Toast.LENGTH_SHORT)
                                    .show();
                            App.setsFirebaseUser(task.getResult().getUser());
                            mActivity.startActivity(new Intent(
                                    mActivity, HomeActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|
                                            Intent.FLAG_ACTIVITY_CLEAR_TOP|
                                            Intent.FLAG_ACTIVITY_NEW_TASK));
                        }else{
                            customProgressBar.stopLoading();
                            try {
                                throw task.getException();
                            } catch(FirebaseAuthInvalidCredentialsException e) {
                                Snackbar.make(coordinatorLayout, "Invalid Credentials",
                                        Snackbar.LENGTH_SHORT).show();

                            } catch(FirebaseAuthUserCollisionException e) {
                                Snackbar.make(coordinatorLayout, "This account is already " +
                                                "registered",
                                        Snackbar.LENGTH_SHORT).show();
                            } catch(Exception e) {
                                Snackbar.make(coordinatorLayout, "Check your internet connection," +
                                                "Try again",
                                        Snackbar.LENGTH_SHORT).show();
                            }
//                            Toast.makeText(mActivity, "Sign in not successful", Toast.LENGTH_SHORT)
//                                    .show();
                        }
                    }
                });
    }

    public static void logOut(){
        App.getFirebaseAuth().signOut();
        //App.setsFirebaseUser(null);
    }

    public static void updateJournalInJournalNode(String journalID,
                                                  String title, String text, Object timeStamp){

        App.getsUsersJournalsNode()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(journalID).exists()){
                            App.getsUsersJournalsNode()
                                    .child(journalID)
                                    .child("title")
                                    .setValue(title);

                            App.getsUsersJournalsNode()
                                    .child(journalID)
                                    .child("text")
                                    .setValue(text);

                            App.getsUsersJournalsNode()
                                    .child(journalID)
                                    .child("timeStamp")
                                    .setValue(timeStamp);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    public static void putJournalInJournalNode(String pushId, Journal journal){
        Map<String, Object> map = new HashMap<>();
        map.put(pushId, journal);
        App.getsUsersJournalsNode()
                .updateChildren(map);
    }

    public static void putJournalInMySharedDataRef(String pushId){
        Map<String, Object> map = new HashMap<>();
        map.put(pushId, true);
        App.getsJournalTypeSharedByMeNode()
                .updateChildren(map);
    }

    public static void putJournalInFeedSharedDataRef(String pushId){
        Map<String, Object> map = new HashMap<>();
        map.put(pushId, App.getsFirebaseUser().getUid());
        App.getsJournalTypeFeedsNode()
                .updateChildren(map);
    }

    public static void putJournalInMyPersonalDataRef(String pushId){
        Map<String, Object> map = new HashMap<>();
        map.put(pushId, true);
        App.getsJournalTypePersonalNode()
                .updateChildren(map);
    }

//    public static void putMembers(String uid, UserDetails userDetails){
//        Map<String, Object> map = new HashMap<>();
//        map.put(uid, userDetails);
//        App.getsFriendsNode()
//                .updateChildren(map)
//                .addOnCompleteListener(getmActivity(), new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        Toast.makeText(mActivity, "Friend added", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }

//    public static void putJournalInMembersNode(List<String> journalUids, Journal journal){
//        //TODO Do all this task in background
//
//        //First iterate through the currentUser friendList
//        for(String friendUid : getFriendsUids()){
//            //Get the datareference of the journal_type_feed nested in journals_type_node which is
//            //nested in the journals node
//            DatabaseReference dbRef =
//                    App.getsRootDatabase()
//                            .getReference()
//                            .child(friendUid)
//                            .child(FirebaseConstants.JOURNALS_NODE)
//                            .child(FirebaseConstants.JOURNALS_TYPE_NODE)
//                            .child(FirebaseConstants.JOURNAL_TYPE_FEEDS);
//
//            //Create a Map object
//            Map<String, Object> journalMap = new HashMap<>();
//            //Put the selected journalsId's in the map object
//            for(String journalUid : journalUids)
//                journalMap.put(journalUid, App.getsFirebaseUser().getUid());
//            //Then finally update the reference
//            dbRef.updateChildren(journalMap);
//            //Now in the journal_type_feeds_node reference, will contain a journal_id matching the
//            //userId of the
//
//            //To get it from the database.. get the journalId then then the user id
//            //Search friend_list then get the reference to the friend using the uid, then get the
//            //journalId reference using the journalId then perform what ever action you want to perform
//            //To delete just delete the node from the journals_type_feed
////            Map<String, Object> map = new HashMap<>();
////            map.put(friendUid, true);
////            dbRef.child(FirebaseConstants.JOURNALS_TYPE_NODE)
////                    .child(FirebaseConstants.JOURNAL_TYPE_FEEDS)
////                    .updateChildren(map);
//        }
//    }

//    private static List<String> getFriendsUids(){
//        List<String> friendsUid = new ArrayList<>();
//
//        App.getsRootDatabase().getReference()
//                .child(App.getsFirebaseUser().getUid())
//                .child(FirebaseConstants.FRIENDS_NODE)
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
//                            String uid = snapshot.getValue(String.class);
//                            friendsUid.add(uid);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//        return friendsUid;
//    }

    public static void deleteAllJournal(){
        App.getsUsersJournalsNode()
                .setValue(null);
    }

    public static void deleteJournal(String journalUid){
        App.getsUsersJournalsNode()
                .child(journalUid)
                .setValue(null);
    }
    //The method below is for multiple delete option
    public static void deleteJournal(List<String> journalUids, String deleteFrom){

        for(String uid : journalUids){
            if(deleteFrom.equals(FirebaseConstants.FIREBASE_JOURNAL_DELETE)) {
                App.getsUsersJournalsNode()
                        .child(uid).setValue(null);
//                App.getsJournalTypePersonalNode()
//                        .child(uid).setValue(null);
//                App.getsJournalTypeFeedsNode()
//                        .child(uid).setValue(null);
            }
//            if(deleteFrom.equals(FirebaseConstants.FIREBASE_JOURNAL_DELETE_ALL)){
//                App.getsJournalTypeFeedsNode()
//                        .child(uid).setValue(null);
//                //delete from users
//            }
//            if(deleteFrom.equals("delete from shared by me")){
//                App.getsJournalTypeSharedByMeNode()
//                        .child(uid).setValue(null);
//            }
//            if(deleteFrom.equals("delete from personal")){
//                App.getsJournalTypePersonalNode()
//                        .child(uid).setValue(null);
//            }
        }
    }

    public static void starClicked(View view, String journalUid, boolean isStarred){
        //Get the current user friend node ref
        //get the updaterUid and get the ref
        //get the journals node
        //get the particular journal ref by getting the child with the journalUid
        Log.e("DatabaseHelper", "**************************** StarCLicked(), " +
                "journalUid -- " + journalUid + " **********");
        DatabaseReference journalDbRef =
        App.getsFriendsNode()
                .child(FirebaseConstants.JOURNALS_NODE)
                .child(journalUid);
        runOnLikeButtonClickTransaction(view, journalDbRef, isStarred);

    }

    private static void runOnLikeButtonClickTransaction(View view, DatabaseReference journalDbRef,
                                                        final boolean isStarred){
        journalDbRef
                .child("isStarred")
                .runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                        Log.e("DatabaseHelper", "---------------- doTransaction() -------");
                        Boolean isStarredB = mutableData.getValue(Boolean.class);
                        Log.e("DatabaseHelper", "--------- mutableData.getKey()" +
                                mutableData.getKey() + " -------");
                        if(isStarredB == null) return Transaction.success(mutableData);
                        else Log.e("DatabaseHelper", "-------- isStarred NOT null -------");

                        isStarredB = !isStarred;
                        journalDbRef
                                .child("isStarred")
                                .setValue(isStarredB);
                        mutableData.setValue(isStarredB);
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

                            Journal journal = dataSnapshot.getValue(Journal.class);
                        if(journal != null) view.setActivated(journal.isStarred());
                    }
                });
    }

    public static String getDateFromTimeStamp(Long timeStamp) {
        Date date = new Date(timeStamp);
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yy");
        return dt.format(date);
    }

    private static String url = "";
    public static String storeProfilePic(Uri filePath){
        App.getsProfileImageStorageRef()
                .putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        url = taskSnapshot.toString();
                        Log.e("DatabaseHelper", "Url ------------------------>>>>>>>  " +
                                url + " <<<<<<<<<<<<<<<<<<<<<<<<<<___________ ");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                url = null;
                Log.e("DatabaseHelper", "Url Uploading failed -------------->>>>>>>  " +
                        url + " <<<<<<<<<<<<<<<<<<<<<<<<<<___________ ");
            }
        });
        return url;
    }

    private static String journalUrl;
    public static void storeJournalPic(Uri filePath, String journalId){
        UploadTask uploadTask =
        App.getsJournalImageStorageRef()
                .child(journalId)
                .putFile(filePath);
        Task<Uri> urlTask = uploadTask
                .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return App.getsJournalImageStorageRef().child(journalId).getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            journalUrl = task.getResult().toString();
                            Log.e("DatabaseHelper", "Journal url ---------------->>>>>>>  " +
                                    journalUrl + " <<<<<<<<<<<<<<<<<<<<<<<<<<___________ ");
                            App.getsUsersJournalsNode()
                                    .child(journalId)
                                    .child("imageUrl")
                                    .setValue(journalUrl);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                journalUrl = null;
                Log.e("DatabaseHelper", "Url Uploading failed -------------->>>>>>>  " +
                        journalUrl + " <<<<<<<<<<<<<<<<<<<<<<<<<<___________ ");
            }
        });

        //return journalUrl;
    }
}
