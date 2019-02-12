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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

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
//                Log.d("zzzzzzzzz","id " + listId);
//                Log.d("zzzzzzzzz","pass " + listPassword);
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
            Log.d("cid","pass " + id);
            Log.d("cid","pass " + listId);
            if(listId.get(i).equals(id)){

                checkId = true;
                String user = listId.get(i);
                Log.d("cpass","pass " + password);
                Log.d("cpass","pass " + listPassword);
                if(listPassword.get(i).equals(password)) {
                    checkPass = true;
//                    checkHospital(user);
                    nextPage(MainActivity.class, user);
                }
            }
            i++;
        }
        if(checkId == false || checkPass == false){
            Toast.makeText(LoginActivity.this, "ID or Password missing",Toast.LENGTH_SHORT).show();
            return;
        }

    }

//    public void checkHospital(String user){
//        switch (user){
//            case "01":
//                changeUser = "โรงพยาบาลมหาราชนครเชียงใหม่";
//                break;
//            case "02":
//                changeUser = "โรงพยาบาลนครพิงค์";
//                break;
//            case "03":
//                changeUser = "โรงพยาบาลสารภี";
//                break;
//            case "04":
//                changeUser = "โรงพยาบาลสันทราย";
//                break;
//            case "05":
//                changeUser = "โรงพยาบาลสันกำแพง";
//                break;
//            case "06":
//                changeUser = "โรงพยาบาลหางดง";
//                break;
//            case "07":
//                changeUser = "โรงพยาบาลดอยสะเก็ด";
//                break;
//            case "08":
//                changeUser = "โรงพยาบาลสันป่าตอง";
//                break;
//            case "09":
//                changeUser = "โรงพยาบาลแม่ออน";
//                break;
//            case "10":
//                changeUser = "โรงพยาบาลแม่วาง";
//                break;
//            case "11":
//                changeUser = "โรงพยาบาลแม่แตง";
//                break;
//            case "12":
//                changeUser = "โรงพยาบาลสะเมิง";
//                break;
//            case "13":
//                changeUser = "โรงพยาบาลจอมทอง";
//                break;
//            case "14":
//                changeUser = "โรงพยาบาลเชียงดาว";
//                break;
//            case "15":
//                changeUser = "โรงพยาบาลฮอด";
//                break;
//            case "16":
//                changeUser = "โรงพยาบาลพร้าว";
//                break;
//            case "17":
//                changeUser = "โรงพยาบาลดอยเต่า";
//                break;
//            case "18":
//                changeUser = "โรงพยาบาลไชยปราการ";
//                break;
//            case "19":
//                changeUser = "โรงพยาบาลเวียงแหง";
//                break;
//            case "20":
//                changeUser = "โรงพยาบาลฝาง";
//                break;
//            case "21":
//                changeUser = "โรงพยาบาลเทพรัตนเวชชานุกูล เฉลิมพระเกียรติ ๖๐ พรรษา";
//                break;
//            case "22":
//                changeUser = "โรงพยาบาลแม่อาย";
//                break;
//            case "23":
//                changeUser = "โรงพยาบาลอมก๋อย";
//                break;
//            case "24":
//                changeUser = "โรงพยาบาลวัดจันทร์ เฉลิมพระเกียรติ ๘๐ พรรษา";
//                break;
//            case "25":
//                changeUser = "โรงพยาบาลราชเวช";
//                break;
//            case "26":
//                changeUser = "โรงพยาบาลใกล้หมอ";
//                break;
//            case "27":
//                changeUser = "โรงพยาบาลแมคคอร์มิค";
//                break;
//            case "28":
//                changeUser = "โรงพยาบาลเซนทรัลเมมโมเรียล";
//                break;
//            case "29":
//                changeUser = "โรงพยาบาลทุ่งหัวช้าง";
//                break;
//            case "30":
//                changeUser = "โรงพยาบาลบ้านธิ";
//                break;
//            case "31":
//                changeUser = "โรงพยาบาลบ้านโฮ่ง";
//                break;
//            case "32":
//                changeUser = "โรงพยาบาลแม่ทา";
//                break;
//            case "33":
//                changeUser = "โรงพยาบาลลี้";
//                break;
//            case "34":
//                changeUser = "โรงพยาบาลลำพูน";
//                break;
//            case "35":
//                changeUser = "โรงพยาบาลเวียงหนองล่อง";
//                break;
//            case "36":
//                changeUser = "โรงพยาบาลป่าซาง";
//                break;
//            case "37":
//                changeUser = "โรงพยาบาลศรีสังวาลย์";
//                break;
//            case "38":
//                changeUser = "โรงพยาบาลขุนยวม";
//                break;
//            case "39":
//                changeUser = "โรงพยาบาลปาย";
//                break;
//            case "40":
//                changeUser = "โรงพยาบาลแม่สะเรียง";
//                break;
//            case "41":
//                changeUser = "โรงพยาบาลแม่ลาน้อย";
//                break;
//            case "42":
//                changeUser = "โรงพยาบาลสบเมย";
//                break;
//            case "43":
//                changeUser = "โรงพยาบาลปางมะผ้า";
//                break;
//        }
//    }



}
