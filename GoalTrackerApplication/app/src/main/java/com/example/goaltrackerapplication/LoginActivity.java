package com.example.goaltrackerapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;


public class LoginActivity extends AppCompatActivity {

    private int RC_SIGN_IN = 0;
    private EditText Email;
    private EditText Password;
    private Button Login;
    private TextView Incorrect;
    private SignInButton gsignin;
    GoogleSignInClient mGoogleSignInClient;
    sharedPref sharedPref1;
    sharedPref sharedPref2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        databaseCreation();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login to Goals App");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Email = findViewById(R.id.etEmail);
        Password = findViewById(R.id.etPassword);
        Login = findViewById(R.id.btnLogin);
        Incorrect = findViewById(R.id.tbIncorrectLogin);
        gsignin = findViewById(R.id.gsignin);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(Email.getText().toString(), Password.getText().toString());
            }
        });

        gsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.gsignin:
                        signin();
                        break;
                }
            }
        });

        // check for login
        sharedPref2 = sharedPref.getInstance();

        if (sharedPref.getISLogged_IN(LoginActivity.this)) {
            Intent NextScreen = new Intent(getApplicationContext(), MainHome.class);
            NextScreen.putExtra("testemail", false);
            startActivity(NextScreen);
            //Toast.makeText(LoginActivity.this, "Account Already Logged In", Toast.LENGTH_LONG).show();
            finish();
        }
        else{
            //Toast.makeText(LoginActivity.this, "No Account Logged In", Toast.LENGTH_LONG).show();
        }
        }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void validate(String uEmail, String uPassword) { // validating the user email and password
        // this will be updated to check a local database
        // TO BE DONE
        if ((uEmail.equals("eee")) && (uPassword.equals("eee"))) {
            Intent intent = new Intent(LoginActivity.this, MainHome.class);
            intent.putExtra("testemail", true);
            startActivity(intent);
        } else {
            Incorrect.setText("YOU HAVE ENTERED INCORRECT DETAILS");
        }
    }

    private void signin(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        sharedPref1 = sharedPref.getInstance();
        sharedPref1.saveISLogged_IN(this, true);//add this on user sucessful login
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            Intent intent = new Intent(LoginActivity.this, MainHome.class);
            intent.putExtra("testemail", false);

            startActivity(intent);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Error", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void databaseCreation(){
        // CREATING THE DATABASE FOR THE SYSTEM
        DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
    }


}




