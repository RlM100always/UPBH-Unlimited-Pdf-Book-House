package com.techtravelcoder.educationalbooks.activity;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.techtravelcoder.educationalbooks.R;
import com.techtravelcoder.educationalbooks.pdf.PDFShowActivity;

import java.util.HashMap;

public class GoogleSignInHelper {
    private static final int RC_SIGN_IN = 9001;

    private Activity mActivity;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    public GoogleSignInHelper(Activity activity) {
        mActivity = activity;
        mAuth = FirebaseAuth.getInstance();

        // Initialize GoogleSignInOptions
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Initialize GoogleSignInClient
        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
    }

    public void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        mActivity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken(), account);
            } catch (ApiException e) {
                Toast.makeText(mActivity, "Google sign in failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken, GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();

                            // You can perform any other actions here after successful authentication
                            // For example, adding user information to Firebase
                            addUserInfoToFirebase(user, account);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(mActivity, "Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void addUserInfoToFirebase(FirebaseUser user, GoogleSignInAccount account) {
        HashMap<String, Object> userInfo = new HashMap<>();
        userInfo.put("userId", user.getUid());
        userInfo.put("userMail", account.getEmail());
        userInfo.put("userName", user.getDisplayName());

        if (user.getPhotoUrl() != null) {
            userInfo.put("userImage", user.getPhotoUrl().toString());
        }

        // Get a reference to the database
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("UserInfo").child(user.getUid());

        // Set user information in the database
        userRef.setValue(userInfo)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(mActivity, "Login Successful", Toast.LENGTH_SHORT).show();



                        } else {
                            Toast.makeText(mActivity, "Failed ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}
