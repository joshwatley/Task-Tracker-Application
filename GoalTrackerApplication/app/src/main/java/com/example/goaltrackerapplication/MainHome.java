package com.example.goaltrackerapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.appbar.AppBarLayout;


public class MainHome extends AppCompatActivity {

    private TabLayout tablayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;
    private static String personName;
    private static String personGivenName;
    private static String personFamilyName;
    private static String personEmail;
    private static String personId;
    private static String profilepic;
    private UserModel userModel;


    GoogleSignInClient mGoogleSignInClient;

    // user data getters
    public static String getName() {
        return personName;
    }

    public static String getPersonFamilyName() {
        return personFamilyName;
    }

    public static String getEmail() {
        return personEmail;
    }

    public static String getID() {
        return personId;
    }

    public static String getProfileURL() {
        return profilepic;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Boolean testem = getIntent().getExtras().getBoolean("testemail");
        //Toast.makeText(MainHome.this, String.valueOf(testem), Toast.LENGTH_LONG).show();

        if (testem){

        }else{

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        }


     /* GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);*/

        setContentView(R.layout.activity_login);

        setContentView(R.layout.activity_main_home);
        tablayout = findViewById(R.id.tablayout_id);
        //appBarLayout = findViewById(R.id.appbarid);
        viewPager = findViewById(R.id.viewpager_id);
        viewPagerAdapter adapter = new viewPagerAdapter(getSupportFragmentManager());


        // THIS IS WHAT IM DOING TO SET UP THE TABS FOR THE MENU **********************
        // adding fragments
        adapter.AddFragment(new FragmentGoals(), "Goals");
        //adapter.AddFragment(new FragmentCalender(), "Calender");
        adapter.AddFragment(new FragmentBreakdown(), "Breakdown");
        adapter.AddFragment(new FragmentMotivation(), "Motivations");
        adapter.AddFragment(new FragmentUserPanel(), "User");

        final int[] ICONS = new int[]{
                R.drawable.ic_goals,
               // R.drawable.ic_calender,
                R.drawable.ic_breakdown,
                R.drawable.ic_motivation,
                R.drawable.ic_userpanel,
        };


        // adapter setup
        viewPager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewPager);
        tablayout.getTabAt(0).setIcon(ICONS[0]);
        tablayout.getTabAt(1).setIcon(ICONS[1]);
        tablayout.getTabAt(2).setIcon(ICONS[2]);
        tablayout.getTabAt(3).setIcon(ICONS[3]);
        //tablayout.getTabAt(4).setIcon(ICONS[4]);
        // **********************************

        if (testem){
            //Toast.makeText(MainHome.this, "this should happen", Toast.LENGTH_LONG).show();

            personName = "eee.Name";
            personGivenName = "eee.GivenName";
            personFamilyName = "eee.FamilyName";
            personEmail = "eee.Email";
            personId = "eee.ID";
            profilepic = "ll";
            // this will update the account to the database

            try {
                if (personName == null) {
                    userModel = new UserModel(personId, "N/A", personEmail, profilepic);

                } else {
                    userModel = new UserModel(personId, personName, personEmail, profilepic);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //adding user to database upon login
            DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
            boolean success = dataBaseHelper.addUser(userModel);
            if (success) {
                Toast.makeText(MainHome.this, "New User Account Created!", Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(MainHome.this, "User Already Added to Database", Toast.LENGTH_LONG).show();
            }

        }else{

            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                personName = acct.getDisplayName();
                personGivenName = acct.getGivenName();
                personFamilyName = acct.getFamilyName();
                personEmail = acct.getEmail();
                personId = acct.getId();
                profilepic = acct.getPhotoUrl().toString();
                // this will update the account to the database

                try {
                    if (personName == null) {
                        userModel = new UserModel(personId, "N/A", personEmail, profilepic);

                    } else {
                        userModel = new UserModel(personId, personName, personEmail, profilepic);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //adding user to database upon login
                DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
                boolean success = dataBaseHelper.addUser(userModel);
                if (success) {
                    Toast.makeText(MainHome.this, "New User Account Created!", Toast.LENGTH_LONG).show();
                } else {
                    //Toast.makeText(MainHome.this, "User Already Added to Database", Toast.LENGTH_LONG).show();
                }
            }
        }

        // setting all the data from the user account to the program
        /*GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            personName = acct.getDisplayName();
            personGivenName = acct.getGivenName();
            personFamilyName = acct.getFamilyName();
            personEmail = acct.getEmail();
            personId = acct.getId();
            profilepic = acct.getPhotoUrl().toString();
            // this will update the account to the database

            try {
                if (personName == null) {
                    userModel = new UserModel(personId, "N/A", personEmail, profilepic);

                } else {
                    userModel = new UserModel(personId, personName, personEmail, profilepic);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //adding user to database upon login
            DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
            boolean success = dataBaseHelper.addUser(userModel);
            if (success) {
                Toast.makeText(MainHome.this, "User Added to Database", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainHome.this, "User Already Added to Database", Toast.LENGTH_LONG).show();
            }
        }*/

    }

    public void signOut() {
        if (mGoogleSignInClient == null){
            Toast.makeText(MainHome.this, "Signed Out EEE successfully", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainHome.this, LoginActivity.class);
            startActivity(intent);

        }else{
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(MainHome.this, "Signed Out successfully", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(MainHome.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    });
            sharedPref.saveISLogged_IN(MainHome.this, false);
        }
    }
///// ********************************************************************************************************************


















///// ********************************************************************************************************************

}
