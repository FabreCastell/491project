package com.trackingTransfers.nice.a491;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private String user, id;
    private Toolbar toolbar;
    private String status = "รักษาเอง", hospital;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_status);

        Intent intent = getIntent();
        user = intent.getStringExtra("user");
        id = intent.getStringExtra("id");

        toolbar = findViewById(R.id.include);
        toolbar.setTitle("แก้ไขสถาณะ " + id);
        setSupportActionBar(toolbar);

        Spinner hospital = findViewById(R.id.desSpinner);

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.hospital, android.R.layout.simple_spinner_dropdown_item);

// Apply the adapter to the spinner
        hospital.setAdapter(adapter);
        hospital.setOnItemSelectedListener(this);

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
                    status = "ส่งต่อไปโรงบาลอื่น";
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(parent.getId()) {
            case R.id.desSpinner:
                hospital = parent.getItemAtPosition(position).toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(this, "You selected nothing", Toast.LENGTH_LONG).show();
    }

    // This "process" method MUST be bound in the layout XML file, "android:onClick="process""
    public void process(View v) {
        if (v.getId() == R.id.submit){
            changeStatus();
        }
        hideKeyboardInput(v);
    }

    private void nextPage(Class page, String user, String id){
        Intent next = new Intent(this,page);
        next.putExtra("user", user);
        next.putExtra("id", id);
        startActivity(next);
    }

    private void changeStatus(){
        DatabaseReference myRef = database.getReference("List/" + id);
        myRef.child("status").setValue(status);
        if(status.equals("ส่งต่อไปโรงบาลอื่น")){
            myRef.child("transfer").setValue("กำลังเคลื่อนย้าย");
            myRef.child("destination").setValue(hospital);
        }else{
            myRef.child("transfer").setValue(" ");
            myRef.child("destination").setValue(" ");
        }
        nextPage(InformationActivity.class, user, id);

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
                nextPage(LoginActivity.class, "","");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
