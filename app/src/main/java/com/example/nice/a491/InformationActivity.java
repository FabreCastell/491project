package com.example.nice.a491;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class InformationActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private TextView idtv, hospitalidtv, desidtv, diseasetv, sextv, statustv, agetv, transfertv;
    private String transfer, status;
    private String user, id;
    private Toolbar toolbar;
    private double lat =0;
    private double lng  = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information_page);

        Intent intent = getIntent();
        user = intent.getStringExtra("user");
        id = intent.getStringExtra("id");

        toolbar = findViewById(R.id.include);
        toolbar.setTitle("ข้อมูล " + id);
        setSupportActionBar(toolbar);

        showData();




    }

    // This "process" method MUST be bound in the layout XML file, "android:onClick="process""
    public void process(View v) {
        if (v.getId() == R.id.add){
            nextPage(StatusActivity.class, user, id);
        }else if(v.getId() == R.id.change){
            changeState();
        }
        hideKeyboardInput(v);
    }

    private void nextPage(Class page, String user, String id){
        Intent next = new Intent(this,page);
        next.putExtra("user", user);
        next.putExtra("id", id);


        startActivity(next);
    }

    // To hide Android soft keyboard
    private void hideKeyboardInput(View v){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private void showData() {
        DatabaseReference myref = database.getReference("List/" + id);
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String age = dataSnapshot.child("age").getValue(String.class);
                String desid = dataSnapshot.child("destination").getValue(String.class);
                String disease = dataSnapshot.child("disease").getValue(String.class);
                String hospitalid = dataSnapshot.child("hospital").getValue(String.class);
                String sex = dataSnapshot.child("sex").getValue(String.class);
                status = dataSnapshot.child("status").getValue(String.class);
                transfer = dataSnapshot.child("transfer").getValue(String.class);

                idtv = findViewById(R.id.textView7);
                hospitalidtv = findViewById(R.id.textView8);
                agetv = findViewById(R.id.textView9);
                sextv = findViewById(R.id.textView10);
                diseasetv = findViewById(R.id.textView11);
                statustv = findViewById(R.id.textView12);
                desidtv = findViewById(R.id.textView13);
                transfertv = findViewById(R.id.textView14);

                idtv.setText("เวลา: " + id);
                hospitalidtv.setText("โรงพยาบาล: " + hospitalid);
                agetv.setText("อายุ: " + age);
                sextv.setText("เพศ: " + sex);
                statustv.setText("สถาณะ: " + status);
                desidtv.setText("โรงพยาบาลปลายทาง: " + desid);
                diseasetv.setText("กลุ่มโรคหัวใจ: " + disease);
                transfertv.setText("สถาณะการส่ง: " + transfer);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
               Log.d("ggg", "failread realtimedb");
            }
        });
    }

    public void changeState(){
        DatabaseReference myRef = database.getReference("List/" + id);
        if(status.equals("ส่งต่อไปโรงบาลอื่น")) {
            if(transfer.equals("กำลังเคลื่อนย้าย")) {
                myRef.child("transfer").setValue("ถึงโรงพยาบาลปลายทางแล้ว");
            }else{
                Toast.makeText(InformationActivity.this, "รายการนี้ถึงโรงพยาบาลปลายทางแล้ว",Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(InformationActivity.this, "รายการนี้ไม่ได้ส่งต่อผู้ป่วย",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.app_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.actin_logout:
                nextPage(LoginActivity.class, " "," ");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
