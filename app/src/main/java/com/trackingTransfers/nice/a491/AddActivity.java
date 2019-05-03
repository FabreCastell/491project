package com.trackingTransfers.nice.a491;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private EditText agetv, numbertv;
    private String status = "รักษาเอง", sex = "ชาย", hospital, disease ;
    private float x1,x2,y1,y2;
    private String user, id;
    private Toolbar toolbar;
    private double lat =0;
    private double lng =0;
    private String changeUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_page);

        Intent passUser = getIntent();
        user = passUser.getStringExtra("user");

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("เพิ่มผู้ป่วย");
        setSupportActionBar(toolbar);

        Spinner hospital = findViewById(R.id.desSpinner);
        Spinner disease = findViewById(R.id.disSpinner);
        agetv = findViewById(R.id.ageEdit);
//        numbertv = findViewById(R.id.numberEdit);

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.hospital, android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> adapterdisease = ArrayAdapter.createFromResource(this,
                R.array.group, android.R.layout.simple_spinner_dropdown_item);

// Apply the adapter to the spinner
        hospital.setAdapter(adapter);
        disease.setAdapter(adapterdisease);
        hospital.setOnItemSelectedListener(this);
        disease.setOnItemSelectedListener(this);

        DatabaseReference mapRef = database.getReference("Hospital/" + user);
        mapRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("zzzzzzzzzz", "user ::: "+ user);
                Log.d("zzzzzzzzzzzzz" , "" + lat + " " +lng);
                lat = dataSnapshot.child("lat").getValue(Double.class);
                lng = dataSnapshot.child("long").getValue(Double.class);
                Log.d("zzzzzzzzzzzzz" , "2 " + lat + " " +lng);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("ggg", "failread realtimedb");
            }



        });

    }

    // This "process" method MUST be bound in the layout XML file, "android:onClick="process""
    public void process(View v) {
        if (v.getId() == R.id.currentlist){
            nextPage(MainActivity.class,user," ");
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }else if (v.getId() == R.id.alllist){
            nextPage(ListActivity.class,user," ");
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }else if (v.getId() == R.id.add){
            nextPage(AddActivity.class,user," ");
        }else if (v.getId() == R.id.submit){
            addData(agetv.getText().toString());

        }
        hideKeyboardInput(v);
    }

    private void nextPage(Class page, String user, String id){
        Intent next = new Intent(this,page);
        next.putExtra("user", user);
        next.putExtra("state","1");
        next.putExtra("id", id);

        startActivity(next);
    }
    // To hide Android soft keyboard
    private void hideKeyboardInput(View v){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public void onRadioSexClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.bmale:
                if (checked)
                    sex = "ชาย";
                    break;
            case R.id.bfemale:
                if (checked)
                    sex = "หญิง";
                    break;
        }
    }

    public void onRadioStatusClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.bf:
                if (checked)
                    status = "รักษาเอง";
                break;
            case R.id.bp:
                if (checked)
                    status = "ส่งต่อไปโรงพยาบาลอื่น";
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(parent.getId()) {
            case R.id.desSpinner:
                hospital = parent.getItemAtPosition(position).toString();
                break;
            case R.id.disSpinner:
                disease = parent.getItemAtPosition(position).toString();
                //Log.d("gg ","zzzzzzzzzzzz" + disease);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(this, "กรุณาเลือก", Toast.LENGTH_LONG).show();
    }

    private void addData(String agetv){
        if(TextUtils.isEmpty(agetv)){
            Toast.makeText(AddActivity.this, "กรุณาใส่อายุ",Toast.LENGTH_SHORT).show();
            return;
        }
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String formattedDate = df.format(c);
        checkHospital(user);

        DatabaseReference myRef = database.getReference("List/" + formattedDate);
        myRef.child("id").setValue(formattedDate); //1
        myRef.child("hospital").setValue(changeUser);//6
        myRef.child("age").setValue(agetv); //3
        myRef.child("sex").setValue(sex); //2
        myRef.child("status").setValue(status); //7
        myRef.child("disease").setValue(disease); //4
        myRef.child("lat").setValue(lat);
        myRef.child("lng").setValue(lng);


        Log.d("zzzzzzzzzzzzz" , "3 " + lat + " " +lng);

//        if(disease.equals("nsteacs")){
//            if(TextUtils.isEmpty(numbertv)){
//                Toast.makeText(AddActivity.this, "กรุณาใส่เลขกำกับโรค",Toast.LENGTH_SHORT).show();
//                myRef.removeValue();
//                return;
//            }
//            myRef.child("numberDisease").setValue(numbertv); //4
//        }else{
//            myRef.child("numberDisease").setValue(" "); //4
//        }
        id = formattedDate;

        if(status.equals("ส่งต่อไปโรงพยาบาลอื่น")){
            myRef.child("transfer").setValue("กำลังเคลื่อนย้าย");
            myRef.child("destination").setValue(hospital);//5
            nextPage(SendGps.class, user, id);
        }else{
            myRef.child("transfer").setValue(" ");
            myRef.child("destination").setValue(" ");//5
            nextPage(ListActivity.class, user, id);
        }
        nextPage(ListActivity.class, user, id);
    }

    public boolean onTouchEvent(MotionEvent touchEvent){
        switch(touchEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                y1 = touchEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                y2 = touchEvent.getY();
                if(x1 < x2 - 300){
                    nextPage(ListActivity.class,user," ");
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
                break;
        }
        return false;
    }



    public void checkHospital(String user){
        switch (user){
            case "01":
                changeUser = "โรงพยาบาลมหาราชนครเชียงใหม่";
                break;
            case "02":
                changeUser = "โรงพยาบาลนครพิงค์";
                break;
            case "03":
                changeUser = "โรงพยาบาลสารภี";
                break;
            case "04":
                changeUser = "โรงพยาบาลสันทราย";
                break;
            case "05":
                changeUser = "โรงพยาบาลสันกำแพง";
                break;
            case "06":
                changeUser = "โรงพยาบาลหางดง";
                break;
            case "07":
                changeUser = "โรงพยาบาลดอยสะเก็ด";
                break;
            case "08":
                changeUser = "โรงพยาบาลสันป่าตอง";
                break;
            case "09":
                changeUser = "โรงพยาบาลแม่ออน";
                break;
            case "10":
                changeUser = "โรงพยาบาลแม่วาง";
                break;
            case "11":
                changeUser = "โรงพยาบาลแม่แตง";
                break;
            case "12":
                changeUser = "โรงพยาบาลสะเมิง";
                break;
            case "13":
                changeUser = "โรงพยาบาลจอมทอง";
                break;
            case "14":
                changeUser = "โรงพยาบาลเชียงดาว";
                break;
            case "15":
                changeUser = "โรงพยาบาลฮอด";
                break;
            case "16":
                changeUser = "โรงพยาบาลพร้าว";
                break;
            case "17":
                changeUser = "โรงพยาบาลดอยเต่า";
                break;
            case "18":
                changeUser = "โรงพยาบาลไชยปราการ";
                break;
            case "19":
                changeUser = "โรงพยาบาลเวียงแหง";
                break;
            case "20":
                changeUser = "โรงพยาบาลฝาง";
                break;
            case "21":
                changeUser = "โรงพยาบาลเทพรัตนเวชชานุกูล เฉลิมพระเกียรติ ๖๐ พรรษา";
                break;
            case "22":
                changeUser = "โรงพยาบาลแม่อาย";
                break;
            case "23":
                changeUser = "โรงพยาบาลอมก๋อย";
                break;
            case "24":
                changeUser = "โรงพยาบาลวัดจันทร์ เฉลิมพระเกียรติ ๘๐ พรรษา";
                break;
            case "25":
                changeUser = "โรงพยาบาลราชเวช";
                break;
            case "26":
                changeUser = "โรงพยาบาลใกล้หมอ";
                break;
            case "27":
                changeUser = "โรงพยาบาลแมคคอร์มิค";
                break;
            case "28":
                changeUser = "โรงพยาบาลเซนทรัลเมมโมเรียล";
                break;
            case "29":
                changeUser = "โรงพยาบาลทุ่งหัวช้าง";
                break;
            case "30":
                changeUser = "โรงพยาบาลบ้านธิ";
                break;
            case "31":
                changeUser = "โรงพยาบาลบ้านโฮ่ง";
                break;
            case "32":
                changeUser = "โรงพยาบาลแม่ทา";
                break;
            case "33":
                changeUser = "โรงพยาบาลลี้";
                break;
            case "34":
                changeUser = "โรงพยาบาลลำพูน";
                break;
            case "35":
                changeUser = "โรงพยาบาลเวียงหนองล่อง";
                break;
            case "36":
                changeUser = "โรงพยาบาลป่าซาง";
                break;
            case "37":
                changeUser = "โรงพยาบาลศรีสังวาลย์";
                break;
            case "38":
                changeUser = "โรงพยาบาลขุนยวม";
                break;
            case "39":
                changeUser = "โรงพยาบาลปาย";
                break;
            case "40":
                changeUser = "โรงพยาบาลแม่สะเรียง";
                break;
            case "41":
                changeUser = "โรงพยาบาลแม่ลาน้อย";
                break;
            case "42":
                changeUser = "โรงพยาบาลสบเมย";
                break;
            case "43":
                changeUser = "โรงพยาบาลปางมะผ้า";
                break;
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
        switch (item.getItemId()) {
            case R.id.action_logout:
                nextPage(LoginActivity.class, " ", " ");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
