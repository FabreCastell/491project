package com.example.nice.a491;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity  {
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("List");
    private ListView listview;
    private ArrayList<String> list=new ArrayList<>();
    private ArrayList<String> listID=new ArrayList<>();
//    private ArrayList<Map<String,ArrayList<String>>> data =new ArrayList<>();
//    private ArrayList<Map<String,Object>> allData=new ArrayList<>();
    float x1,x2,y1,y2;
    private String user;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        Intent passUser = getIntent();
        user = passUser.getStringExtra("user");

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("กำลังเคลื่อย้าย");
        setSupportActionBar(toolbar);

        listview = findViewById(R.id.show);

        final ArrayAdapter<String> adapter=new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,listID);
        listview.setAdapter(adapter);

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String checkTranfer = dataSnapshot.child("transfer").getValue(String.class);
                String t = "กำลังเคลื่อนย้าย";
                if(checkTranfer != null && checkTranfer.equals(t)){

//                    list.add(dataSnapshot.child("id").getValue(String.class) + " " + dataSnapshot.child("age").getValue(String.class) + " " + dataSnapshot.child("sex").getValue(String.class) + " " + dataSnapshot.child("disease").getValue(String.class));
//                    allData.add((Map<String, Object>)dataSnapshot.getValue());
                    listID.add(dataSnapshot.child("id").getValue(String.class));
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                listID.remove(dataSnapshot.child("id").getValue(String.class));
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        listview.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        y1 = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = event.getX();
                        y2 = event.getY();
                        if(x1 > x2 + 300){
                            nextPage(ListActivity.class, user, " ");
                        }
                        break;
                }
                return false;
            }
        });


//
        showData();
//        this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    // This "process" method MUST be bound in the layout XML file, "android:onClick="process""
    public void process(View v) {
        if (v.getId() == R.id.alllist){
            nextPage(ListActivity.class, user," ");

        }else if (v.getId() == R.id.add){
            nextPage(AddActivity.class, user," ");
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

    public void showData(){
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data = parent.getItemAtPosition(position).toString();
//                Intent intent = new Intent(MainActivity.this, RecordActivity.class);
//                intent.putExtra("data" ,data);
//                startActivity(intent);


//                Log.d("ggggggggg","id" + data);
                nextPage(RecordActivity.class, user, data);
//                Intent passID = new Intent(MainActivity.this, RecordActivity.class);
//                passID.putExtra("id" ,data);
//                startActivity(passID);
            }

        });
    }

//    private void signOut() {
//        mAuth.signOut();
//        updateUI(null);
//    }


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
