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
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RecordActivity extends AppCompatActivity {
    private EditText comment;
    private ListView listview;
    private ArrayList<String> list=new ArrayList<>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private String user, id;
    private Toolbar toolbar;
    private double lat =0;
    private double lng  = 0;
    private String formattedDate;
//    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_record);

        Intent passID = getIntent();
        user = passID.getStringExtra("user");
        id = passID.getStringExtra("id");

        toolbar = findViewById(R.id.include);
        toolbar.setTitle(id);
        setSupportActionBar(toolbar);


        comment = findViewById(R.id.edittext_chatbox);
        listview =  findViewById(R.id.list_comment);

        final ArrayAdapter<String> adapter = new ArrayAdapter(RecordActivity.this,android.R.layout.simple_dropdown_item_1line,list);
        listview.setAdapter(adapter);




//        TextView textView = findViewById(R.id.requestDataTextView);
//        textView.setText(message);

        DatabaseReference ref = database.getReference("List/" + id);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Log.d("zzzzzzzzzzzzz" , "1 " + lat + " " +lng);
                    lat = dataSnapshot.child("lat").getValue(Double.class);
                    lng = dataSnapshot.child("lng").getValue(Double.class);
                    Log.d("zzzzzzzzzzzzz" , "2 " + lat + " " +lng);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("ggg", "failread realtimedb");
            }
        });

        DatabaseReference Ref = database.getReference("List").child(id).child("comment");
        Ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot post : dataSnapshot.getChildren()){
                    String comment = post.getValue(String.class);
                   list.add(comment);

                   adapter.notifyDataSetChanged();
                }





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    // This "process" method MUST be bound in the layout XML file, "android:onClick="process""
    public void process(View v) {
        if (v.getId() == R.id.info){
            nextPage(InformationActivity.class, user, id);
        }else if (v.getId() == R.id.map) {
            nextPage(SendGps.class, user, id);
        }else if (v.getId() == R.id.map2) {
            nextPagemap2(MapsActivity.class, user, id);
        }else if (v.getId() == R.id.image) {
            nextPage(ImageActivity.class, user, id);
        }else if (v.getId() == R.id.button_chatbox_send) {
            addData(comment.getText().toString());
        }

        hideKeyboardInput(v);
    }

    private void nextPage(Class page, String user, String id){
        Intent next = new Intent(this,page);
        next.putExtra("user", user);
        next.putExtra("id", id);
        next.putExtra("state", "1");
        startActivity(next);
    }
    private void nextPagemap(Class page, String user, String id){
        Intent next = new Intent(this,page);
        next.putExtra("type","1");
        next.putExtra("user", user);
        next.putExtra("id", id);
        startActivity(next);
    }
    private void nextPagemap2(Class page, String user, String id){
        Intent next = new Intent(this,page);
        next.putExtra("type","2");
        next.putExtra("user", user);
        next.putExtra("id", id);
        next.putExtra("lat", lat);
        next.putExtra("lng", lng);
        Log.d("zzzzzzzzzzzzz" , "3 " + lat + " " +lng);
        next.putExtra("state", "1");
        startActivity(next);
    }
    // To hide Android soft keyboard
    private void hideKeyboardInput(View v){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
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
            case R.id.action_logout:
                nextPage(LoginActivity.class, " "," ");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void addData(String commet) {
        if(TextUtils.isEmpty(commet)){
            Toast.makeText(RecordActivity.this, "กรุณาใส่ข้อความ",Toast.LENGTH_SHORT).show();
            return;
        }
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("ddHHmmss");
        formattedDate = df.format(c);

        DatabaseReference myRef = database.getReference("List/" + id).child("comment");
        Log.d("zzzzzz12","id" + id + "con " + convertID(id) );
        myRef.child(formattedDate).setValue(convertID(user) + ": " +commet); //1

        comment.setText("");

    }

    public String convertID(String user){
        String newUser = " ";
        switch (user){
            case "01":
                newUser = "maharat";
                break;
            case "02":
                newUser = "nakornpink";
                break;
            case "03":
                newUser = "sarapee";
                break;
            case "04":
                newUser = "sansai";
                break;
            case "05":
                newUser = "sankampang";
                break;
            case "06":
                newUser = "hangdong";
                break;
            case "07":
                newUser = "doisaket";
                break;
            case "08":
                newUser = "sanpatong";
                break;
            case "09":
                newUser = "maeon";
                break;
            case "10":
                newUser = "maewang";
                break;
            case "11":
                newUser = "maetang";
                break;
            case "12":
                newUser = "samoeng";
                break;
            case "13":
                newUser = "jomthong";
                break;
            case "14":
                newUser = "chiangdow";
                break;
            case "15":
                newUser = "hod";
                break;
            case "16":
                newUser = "phrao";
                break;
            case "17":
                newUser = "doitao";
                break;
            case "18":
                newUser = "chaiprakarn";
                break;
            case "19":
                newUser = "wianghang";
                break;
            case "20":
                newUser = "fang";
                break;
            case "21":
                newUser = "dvnhos";
                break;
            case "22":
                newUser = "maeai";
                break;
            case "23":
                newUser = "omkoi";
                break;
            case "24":
                newUser = "watchan";
                break;
            case "25":
                newUser = "rajavej";
                break;
            case "26":
                newUser = "klaimor";
                break;
            case "27":
                newUser = "mccormick";
                break;
            case "28":
                newUser = "centralmemorial";
                break;
            case "29":
                newUser = "thchos";
                break;
            case "30":
                newUser = "banthi";
                break;
            case "31":
                newUser = "banhong";
                break;
            case "32":
                newUser = "maetha";
                break;
            case "33":
                newUser = "lamphun";
                break;
            case "34":
                newUser = "lee";
                break;
            case "35":
                newUser = "wnlhos";
                break;
            case "36":
                newUser = "pasang";
                break;
            case "37":
                newUser = "srisangwan";
                break;
            case "38":
                newUser = "khunyuam";
                break;
            case "39":
                newUser = "pai";
                break;
            case "40":
                newUser = "maesariang";
                break;
            case "41":
                newUser = "maelanoi";
                break;
            case "42":
                newUser = "sobmoei";
                break;
            case "43":
                newUser = "pangmapha";
                break;
        }
        return newUser;
    }

}
