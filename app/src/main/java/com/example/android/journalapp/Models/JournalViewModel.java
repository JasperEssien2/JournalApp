package com.example.android.journalapp.Models;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.example.android.journalapp.App;
import com.example.android.journalapp.Constants.Constants;
import com.example.android.journalapp.Constants.FirebaseConstants;
import com.example.android.journalapp.LiveData.FirebaseQueryLiveData;
import com.example.android.journalapp.POJOs.Journal;
import com.example.android.journalapp.utilities.DatabaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class JournalViewModel extends ViewModel {
    private static final String TAG = JournalViewModel.class.getSimpleName();
//    FirebaseQueryLiveData firebaseQueryLiveData = new FirebaseQueryLiveData(App.getsUsersJournalsNode(),
//            FirebaseConstants.FIREBASE_USE_VALUE_LISTENER);

    FirebaseQueryLiveData firebaseQueryLiveData = new FirebaseQueryLiveData(App.getsUsersJournalsNode(),
            FirebaseConstants.FIREBASE_USE_CHILD_LISTENER);
    private String mDataTypeRef;

    private List<String> mJournalUpdaterUpdaterIds = new ArrayList<>();
    private List<String> mJournalIds = new ArrayList<>();
    private List<DatabaseReference> mUpdaterDatabaseReferenceList =
            new ArrayList<>();

//    public LiveData<List<Journal>> getJournalListLiveData(){
//
//        LiveData<List<Journal>> journalListLiveData = Transformations.map(firebaseQueryLiveData,
//                new Deserializer());
//        return journalListLiveData;
//    }

    public LiveData<Journal> getJournalLiveData(){
        LiveData<Journal> journalLiveData = Transformations.map(firebaseQueryLiveData,
                new DeserializerChild());
        return journalLiveData;
    }

//    public DataSnapshot getSelected(int position){
//
//    }
    public void setWhichDataToGet(String dataTypeRef){
        //Is it personal or my shared or feed
        mDataTypeRef = dataTypeRef;
        DatabaseReference databaseReference = null;
        switch (dataTypeRef){
            case Constants.ARGUMENTS_PERSONAL_DATA_TYPE:
                databaseReference = App.getsJournalTypePersonalNode();
                break;
            case Constants.ARGUMENTS_SHARED_BY_ME_DATA_TYPE:
                databaseReference = App.getsJournalTypeSharedByMeNode();
                break;
            case Constants.ARGUMENTS_FEEDS_DATA_TYPE:
                databaseReference = App.getsJournalTypeFeedsNode();
                break;
        }
        //Log.e(TAG, "_______ databaseReference type set value = " + dataTypeRef.toString() + " ___________");

//        if(databaseReference != null){
//            //Log.e(TAG, "_______ databaseReference NOT null ___________");
//            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if(dataTypeRef.equals(Constants.ARGUMENTS_FEEDS_DATA_TYPE)){
//                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//
//                            String uid = snapshot.getKey();
//                            String updaterUid = snapshot.getValue(String.class);
//                            mJournalIds.add(uid);
//                            mJournalUpdaterUpdaterIds.add(updaterUid);
//                            //Log.e(TAG, "%%%%%%% JournalIds -- (" + mJournalIds.toString() + " )%%%%%%%");
//                        }
//                    }else {
//                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                            String uid = snapshot.getKey();
//                            //Log.e(TAG, "%%%%%%% JournalIds -- (" + mJournalIds.toString() + " )%%%%%%%");
//                            mJournalIds.add(uid);
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//        }//else Log.e(TAG, "_______ databaseReference IS null ___________");
    }
    Journal previousJournal = null;
    private class DeserializerChild implements Function<DataSnapshot, Journal>{
        @Override
        public Journal apply(DataSnapshot input) {
//            Log.e(TAG, "**************************************************************");
//            Log.e(TAG, "______________in DerializerChild()  " + input.toString() + "_____________");
//            Log.e(TAG, "**************************************************************");
            Journal journal = input.getValue(Journal.class);
//            if(journal == previousJournal){
//                journal = pr
//            }
            if (!(input.getKey().equals(FirebaseConstants.JOURNALS_TYPE_NODE))) {
                if (journal != null) {
                    if (journal.getTimeStamp() != null) {
                        String date = DatabaseHelper.getDateFromTimeStamp((Long) journal.
                                getTimeStamp());
                        journal.setUploadDate(date);
                    }
                    previousJournal = journal;
                }
            }
            return journal;
        }
    }

    private class Deserializer implements Function<DataSnapshot, List<Journal>> {
        @Override
        public List<Journal> apply(DataSnapshot input) {
            //Log.e(TAG, "_______ Journal(In Derializer)___________");
            int index = 0;
            ArrayList<Journal> mJournals = new ArrayList<Journal>();
            for(DataSnapshot dataSnapshot : input.getChildren()) {
                //Log.e(TAG, "_______ Journal(In Derializer) DataSnapShot ---- ___________");
                if(dataSnapshot != null) {
                    if (dataSnapshot.getKey().equals(mJournalIds.get(index))) {
                        //Log.e(TAG, "_______ Journal(In Derializer) Has child ---- ___________");
                        Journal journal = dataSnapshot.getValue(Journal.class);
                        if (journal != null) {
                            if (journal.getTimeStamp() != null) {
                                String date = DatabaseHelper.getDateFromTimeStamp((Long) journal.
                                        getTimeStamp());
                                journal.setUploadDate(date);
                            }
                            //Log.e(TAG, "_______ Journal(" + mJournals.toString() + ")___________");
                            mJournals.add(journal);
                        }
                        index++;
                        if (index >= mJournalIds.size()) {
                            index = 0;
                            break;
                        }
                    }
                }
            }
            index = 0;
            return mJournals;
        }
    }

    private int searchUpdaterIdIndex = 0;
    private void searchUpdaterUserId(){

        App.getsRootDatabase().getReference()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (snapshot.hasChild(mJournalUpdaterUpdaterIds.get(searchUpdaterIdIndex))) {
                                mUpdaterDatabaseReferenceList.add(dataSnapshot.getRef());
                                searchUpdaterIdIndex++;
                                if(searchUpdaterIdIndex >= mJournalUpdaterUpdaterIds.size()) {
                                    searchUpdaterIdIndex = 0;
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

}
