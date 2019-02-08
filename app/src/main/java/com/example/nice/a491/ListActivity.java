package com.example.nice.a491;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

public class ListActivity extends AppCompatActivity {
    String message;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("List");
    ListView listview;
    ArrayList<String> list=new ArrayList<>();
    ArrayList<Map<String,Object>> allData=new ArrayList<>();
    float x1,x2,y1,y2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_page);
        Intent intent = getIntent();
        message = intent.getStringExtra("data");

        listview = findViewById(R.id.show);
        final ArrayAdapter<String> adapter = new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,list);
        listview.setAdapter(adapter);
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                list.add(dataSnapshot.child("id").getValue(String.class) + " " + dataSnapshot.child("age").getValue(String.class) + " " + dataSnapshot.child("sex").getValue(String.class) + " " + dataSnapshot.child("disease").getValue(String.class));
                allData.add((Map<String, Object>)dataSnapshot.getValue());
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                list.remove(dataSnapshot.getValue(String.class));
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
                        if(x1 <= x2){
                            Intent i = new Intent(ListActivity.this, MainActivity.class);
                            startActivity(i);
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        }else if(x1 >= x2){
                            Intent i = new Intent(ListActivity.this, AddActivity.class);
                            startActivity(i);
                        }
                        break;
                }
                return false;
            }

        });

        showData();

    }

    // This "process" method MUST be bound in the layout XML file, "android:onClick="process""
    public void process(View v) {
        if (v.getId() == R.id.logout){
            nextPage(LoginActivity.class);
        }else if (v.getId() == R.id.currentlist){
            nextPage(MainActivity.class);
        }else if (v.getId() == R.id.alllist){
            nextPage(ListActivity.class);
        }else if (v.getId() == R.id.add){
            nextPage(AddActivity.class);
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

    public void showData(){
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data = parent.getItemAtPosition(position).toString();

                Intent intent = new Intent(ListActivity.this, RecordActivity.class);
                //based on item add info to intent
                intent.putExtra("data" ,data);
//                intent.putExtra("allData" ,allData);
                startActivity(intent);
            }

        });
    }



}
