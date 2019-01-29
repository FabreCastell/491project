package com.example.nice.a491;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class InformationActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private TextView idtv, hospitalidtv, desidtv, diseasetv, sextv, statustv, agetv;
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information_page);
//        initinstance();
        Intent intent = getIntent();
        data = intent.getStringExtra("data");
        idtv = findViewById(R.id.textView7);
        idtv.setText(data);
    }

    // This "process" method MUST be bound in the layout XML file, "android:onClick="process""
    public void process(View v) {
        if (v.getId() == R.id.back){
            nextPage(RecordActivity.class);
        }else if (v.getId() == R.id.add){
            nextPage(StatusActivity.class);
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

//    private void initinstance() {
//        DatabaseReference myref = database.getReference("0001");
//        myref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
////                Log.d("ggg","kuy" + dataSnapshot.getValue(String.class));
//
//                String age = dataSnapshot.child("patient/age").getValue(String.class);
//                String desid = dataSnapshot.child("status/destination").getValue(String.class);
//                String disease = dataSnapshot.child("patient/disease").getValue(String.class);
//                String hospitalid = dataSnapshot.child("patient/hospital").getValue(String.class);
//                String sex = dataSnapshot.child("patient/sex").getValue(String.class);
//                String status = dataSnapshot.child("status/status").getValue(String.class);
//
//                idtv = findViewById(R.id.textView7);
//                hospitalidtv = findViewById(R.id.textView8);
//                agetv = findViewById(R.id.textView9);
//                sextv = findViewById(R.id.textView10);
//                statustv = findViewById(R.id.textView11);
//                desidtv = findViewById(R.id.textView12);
//                diseasetv = findViewById(R.id.textView13);
//
//                idtv.setText("0001");
//                hospitalidtv.setText(hospitalid);
//                agetv.setText(age);
//                sextv.setText(sex);
//                statustv.setText(status);
//                desidtv.setText(desid);
//                diseasetv.setText(disease);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//               Log.d("ggg", "failread realtimedb");
//            }
//
//
//        });
//    }

}
