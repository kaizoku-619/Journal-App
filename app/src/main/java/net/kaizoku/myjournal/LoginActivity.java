package net.kaizoku.myjournal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.android.gms.common.api.ResultCallback;

public class LoginActivity extends AppCompatActivity {

    // Request sing in code. Could be anything as you required.
    public static final int RequestSignInCode = 7;

    // Firebase Auth Object.
    public FirebaseAuth firebaseAuth;

    // Google API Client object.
    public GoogleApiClient googleApiClient;

    // Sing out button.
    Button signOutButton;

    // Google Sign In button .
    SignInButton signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initFirebase();

        // Adding Click listener to User Sign in Google button.
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserSignInMethod();
            }
        });

        // Adding Click Listener to User Sign Out button.
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserSignOutFunction();
            }
        });
    }

    private void initView() {
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signOutButton = (Button) findViewById(R.id.sign_out_button);
    }

    private void initFirebase() {
        // Getting Firebase Auth Instance into firebaseAuth object.
        firebaseAuth = FirebaseAuth.getInstance();

        // Creating and Configuring Google Sign In object.
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Creating and Configuring Google Api Client.
        googleApiClient = new GoogleApiClient.Builder(LoginActivity.this)
                .enableAutoManage(LoginActivity.this ,
                        new GoogleApiClient.OnConnectionFailedListener() {
                            @Override
                            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                                Toast.makeText(
                                        getApplicationContext(), "Connection Failed", Toast.LENGTH_LONG)
                                        .show();
                            }
                        } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();
    }

    public void UserSignInMethod() {
        // Passing Google Api Client into Intent.
        Intent AuthIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(AuthIntent, RequestSignInCode);
    }

    public void UserSignOutFunction() {
        // Sing Out the User.
        firebaseAuth.signOut();
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback() {
                    @Override
                    public void onResult(@NonNull Result result) {
                        // Write down your any code here which you want to execute After Sign Out.
                        // Printing Logout toast message on screen.
                        Toast.makeText(LoginActivity.this, "Logout Successfully", Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RequestSignInCode){
            GoogleSignInResult googleSignInResult =
                    Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (googleSignInResult.isSuccess()) {
                GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();
                FirebaseUserAuth(googleSignInAccount);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }
    }

    public void FirebaseUserAuth(GoogleSignInAccount googleSignInAccount) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
//        Toast.makeText(LoginActivity.this,""+ authCredential.getProvider(),Toast.LENGTH_LONG).show();

        firebaseAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task AuthResultTask) {
                        if (AuthResultTask.isSuccessful()){
                            // Getting Current Login user details.
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        }
                    }
                });
    }
}
