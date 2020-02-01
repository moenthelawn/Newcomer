package com.example.newcomer;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class UserData extends Application {
    private String phoneNumber;
    private Integer radiusDistance;

    private ArrayList<String> interests;
    private ArrayList<Pair> statistics;
    private String userName;
    private String userID = "";
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mRef;

    public UserData() {
        this.radiusDistance = 3; //Default value for the radius of the current map that can be used to sync with other users
        this.interests = new ArrayList<String>();
        this.statistics = new ArrayList<Pair>();
        this.userName = "";
        //userID = generateUserID();
    }

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);

        //mListener = (OnUserUpdateListener) this;
        //mListener = (OnUserUpdateListener) this.getApplicationContext();\
        initializeDataBase();
        updateUserData();
    }

    private void initializeDataBase() {

        //FirebaseApp.initializeApp(getApplicationContext());

        this.mAuth = FirebaseAuth.getInstance();
        this.mDatabase = FirebaseDatabase.getInstance();

    }
    public void updateUserData(){

         //Otherwise, we would go and read in the top list of current statistics
            final DatabaseReference mStatistics = mDatabase.getReference("statistics");
            mStatistics.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //Then we send this data over to the call back for the main apsp
                    for (DataSnapshot child : dataSnapshot.getChildren()){

                        String key = child.getKey().toString();
                        int val = Integer.parseInt(child.getValue().toString());

                        Pair pair = new Pair(key,val);
                        statistics.add(pair);

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
    }

    public ArrayList<Pair> getStatistics(){
        return this.statistics;
    }
    public ArrayList<String> getInterests_AutoComplete(){
        //This function will get the autocomplete for the array string
        //In this we only want the top 6 most popular
        ArrayList<String> arrayList = new ArrayList<String >();
        for (int i = 0; i < 6;i++){
            Pair pair = statistics.get(i) ;
            arrayList.add((String) pair.getFirst());
        }
        return arrayList;
    }

    public void setPhoneNumber(String mobile) {
        this.phoneNumber = mobile; //Set the phone number to be the user's phone number
    }

    public void setRadiusDistance(int radius) {
        this.radiusDistance = radius;
    }

    public Integer getRadiusDistance() {
        return this.radiusDistance;
    }

    public int getMAX() { //Get the max array size that we can use which is based on the size of the array index that we currently set
        return getInterests_AutoComplete().size(); //Get the size of the interests
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
