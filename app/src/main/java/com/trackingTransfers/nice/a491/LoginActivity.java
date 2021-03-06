package com.trackingTransfers.nice.a491;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
//    private FirebaseAuth mAuth;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("ID");
    private EditText mId;
    private EditText mPassword;
    private ArrayList<String> listId = new ArrayList();
    private ArrayList<String> listPassword = new ArrayList();
    private String changeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

//        mAuth = FirebaseAuth.getInstance();

        mId = findViewById(R.id.editText);
        mPassword = findViewById(R.id.editText3);

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                listId.add(dataSnapshot.getKey());
                listPassword.add(dataSnapshot.getValue(String.class));

            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                listId.remove(dataSnapshot.getKey());
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

    private void nextPage(Class page, String user){
        Intent next = new Intent(this,page);
        next.putExtra("user", user);
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
            Toast.makeText(LoginActivity.this, "please enter ID",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(LoginActivity.this, "please enter Password",Toast.LENGTH_SHORT).show();
            return;
        }
        int i = 0;
        boolean checkId = false;
        boolean checkPass = false;
        while(i < listId.size()){
            if(listId.get(i).equals(id)){

                checkId = true;
                String user = listId.get(i);
                String newUser = convertID(user);
                if(listPassword.get(i).equals(password)) {
                    checkPass = true;
//                    checkHospital(user);
                    nextPage(UserTypeActivity.class, newUser);
                }
            }
            i++;
        }
        if(checkId == false || checkPass == false){
            Toast.makeText(LoginActivity.this, "ID or Password missing",Toast.LENGTH_SHORT).show();
            return;
        }

    }

    public String convertID(String user){
        String newUser = " ";
        switch (user){
            case "01":
                newUser = "00";
                break;
            case "maharat":
                newUser = "01";
                break;
            case "nakornpink":
                newUser = "02";
                break;
            case "sarapee":
                newUser = "03";
                break;
            case "sansai":
                newUser = "04";
                break;
            case "sankampang":
                newUser = "05";
                break;
            case "hangdong":
                newUser = "06";
                break;
            case "doisaket":
                newUser = "07";
                break;
            case "sanpatong":
                newUser = "08";
                break;
            case "maeon":
                newUser = "09";
                break;
            case "maewang":
                newUser = "10";
                break;
            case "maetang":
                newUser = "11";
                break;
            case "samoeng":
                newUser = "12";
                break;
            case "jomthong":
                newUser = "13";
                break;
            case "chiangdow":
                newUser = "14";
                break;
            case "hod":
                newUser = "15";
                break;
            case "phrao":
                newUser = "16";
                break;
            case "doitao":
                newUser = "17";
                break;
            case "chaiprakarn":
                newUser = "18";
                break;
            case "wianghang":
                newUser = "19";
                break;
            case "fang":
                newUser = "20";
                break;
            case "dvnhos":
                newUser = "21";
                break;
            case "maeai":
                newUser = "22";
                break;
            case "omkoi":
                newUser = "23";
                break;
            case "watchan":
                newUser = "24";
                break;
            case "rajavej":
                newUser = "25";
                break;
            case "klaimor":
                newUser = "26";
                break;
            case "mccormick":
                newUser = "27";
                break;
            case "centralmemorial":
                newUser = "28";
                break;
            case "thchos":
                newUser = "29";
                break;
            case "banthi":
                newUser = "30";
                break;
            case "banhong":
                newUser = "31";
                break;
            case "maetha":
                newUser = "32";
                break;
            case "lamphun":
                newUser = "33";
                break;
            case "lee":
                newUser = "34";
                break;
            case "wnlhos":
                newUser = "35";
                break;
            case "pasang":
                newUser = "36";
                break;
            case "srisangwan":
                newUser = "37";
                break;
            case "khunyuam":
                newUser = "38";
                break;
            case "pai":
                newUser = "39";
                break;
            case "maesariang":
                newUser = "40";
                break;
            case "maelanoi":
                newUser = "41";
                break;
            case "sobmoei":
                newUser = "42";
                break;
            case "pangmapha":
                newUser = "43";
                break;
        }
        return newUser;
    }


}
