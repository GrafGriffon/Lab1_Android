package com.example.myapplication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.UserHandle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {

    private SignInButton signInButton;
    private GoogleSignInClient googleSignInClient;
    private String TAG = "mainTag";
    private FirebaseAuth mAuth;
    private int RESULT_CODE_SINGIN = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signInButton = findViewById(R.id.SignIn_Button);
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("517213944964-f7o2dgeutd7ksvei4sj4if061bet1e18.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent singInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(singInIntent, RESULT_CODE_SINGIN);
            }
        });
        getSupportActionBar().hide();
    }

    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null) {
            Toast.makeText(MainActivity.this, "Вы авторизованы", Toast.LENGTH_LONG).show();
            UpdateUI(currentUser);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_CODE_SINGIN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }


    private void handleSignInResult(Task<GoogleSignInAccount> task) {

        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Toast.makeText(MainActivity.this, "Signed In successfully", Toast.LENGTH_LONG).show();
            FirebaseGoogleAuth(account);

        } catch (ApiException e) {
            e.printStackTrace();
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(MainActivity.this, "SignIn Failed!!!", Toast.LENGTH_LONG).show();
            FirebaseGoogleAuth(null);
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "successful", Toast.LENGTH_LONG).show();
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    UpdateUI(firebaseUser);
                } else {
                    Toast.makeText(MainActivity.this, "Failed!", Toast.LENGTH_LONG).show();
                    UpdateUI(null);
                }
            }
        });
    }

    private void UpdateUI(FirebaseUser fUser) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (account != null) {
            String personName = account.getDisplayName();
            String personEmail = account.getEmail();

            Toast.makeText(MainActivity.this, personName + "  " + personEmail, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, CatActivity.class);
            startActivity(intent);
        }
    }
}

//FirebaseAuth.getInstance().signOut();