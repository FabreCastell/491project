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
        adapter.clear();
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
        if (v.getId() == R.id.back){
            nextPage(ListActivity.class, user, " ");
        }else if (v.getId() == R.id.info){
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
        myRef.child(formattedDate).setValue(commet); //1

        finish();
        startActivity(getIntent());

    }

}
