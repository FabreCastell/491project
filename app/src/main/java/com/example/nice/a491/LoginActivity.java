package com.example.nice.a491;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText mId;
    private EditText mPassword;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("ID");
    ArrayList<String> listId = new ArrayList();
    ArrayList<String> listPassword = new ArrayList();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mAuth = FirebaseAuth.getInstance();
        mId = findViewById(R.id.editText);
        mPassword = findViewById(R.id.editText3);

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                listId.add(dataSnapshot.getKey());
                listPassword.add(dataSnapshot.getValue(String.class));
                Log.d("zzzzzzzzz","id " + listId);
                Log.d("zzzzzzzzz","pass " + listPassword);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                listId.remove(dataSnapshot.getValue(String.class));
                listPassword.remove(dataSnapshot.getValue(String.class));
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    // This "process" method MUST be bound in the layout XML file, "android:onClick="process""
    public void process(View v) {
        if (v.getId() == R.id.login){
//            createAccount(mId.getText().toString(), mPassword.getText().toString());
//            signIn(mId.getText().toString(), mPassword.getText().toString());
            check(mId.getText().toString(), mPassword.getText().toString());
        }
        hideKeyboardInput(v);
    }

    private void nextPage(Class page){
        Intent next = new Intent(this,page);
        startActivity(next);
    }

    // To hide Android soft keyboard
    private void hideKeyboardInput(View v){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
//    }

//    public void createAccount (String id, String password){
//        if(TextUtils.isEmpty(id)){
//            Toast.makeText(LoginActivity.this, "please enter email",Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if(TextUtils.isEmpty(password)){
//            Toast.makeText(LoginActivity.this, "please enter password",Toast.LENGTH_SHORT).show();
//            return;
//        }
//        mAuth.createUserWithEmailAndPassword(id, password)
//        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d("user", "createUserWithEmail:success");
////                    FirebaseUser user = mAuth.getCurrentUser();
////                    updateUI(user);
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Log.w("user", "createUserWithEmail:failure", task.getException());
//                    Toast.makeText(LoginActivity.this, "Authentication failed.",
//                            Toast.LENGTH_SHORT).show();
//                    updateUI(null);
//                }
//
//                // ...
//            }
//        });
//    }

//    public void signIn(String id, String password){
//        if(TextUtils.isEmpty(id)){
//            Toast.makeText(LoginActivity.this, "please enter email",Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if(TextUtils.isEmpty(password)){
//            Toast.makeText(LoginActivity.this, "please enter password",Toast.LENGTH_SHORT).show();
//            return;
//        }
//        mAuth.signInWithEmailAndPassword(id, password)
//        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d("user", "signInWithEmail:success");
//                    FirebaseUser user = mAuth.getCurrentUser();
//                    updateUI(user);
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Log.w("user", "signInWithEmail:failure", task.getException());
//                    Toast.makeText(LoginActivity.this, "Authentication failed.",
//                            Toast.LENGTH_SHORT).show();
//                    updateUI(null);
//                }
//
//                // ...
//            }
//        });
//    }



//    private void updateUI(FirebaseUser user){
//        if (user != null) {
//            nextPage(MainActivity.class);
//        } else {
//
//        }
//    }

    private void check(String id, String password){
        if(TextUtils.isEmpty(id)){
            Toast.makeText(LoginActivity.this, "please enter id",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(LoginActivity.this, "please enter password",Toast.LENGTH_SHORT).show();
            return;
        }
        int i = 0;
        boolean checkId = false;
        boolean checkPass = false;
        while(i < listId.size()){
            Log.d("cid","pass " + id);
            Log.d("cid","pass " + listId);
            if(listId.get(i).equals(id)){

                checkId = true;
                Log.d("cpass","pass " + password);
                Log.d("cpass","pass " + listPassword);
                if(listPassword.get(i).equals(password)) {
                    checkPass = true;
                    nextPage(MainActivity.class);
                }
            }
            i++;
        }
        if(checkId == false || checkPass == false){
            Toast.makeText(LoginActivity.this, "ID or Password missing",Toast.LENGTH_SHORT).show();
            return;
        }

    }



}
