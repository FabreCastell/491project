package com.example.nice.a491;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
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
    private TextView desidtv, diseasetv, agetv ,checkedtv;
    private String status, sex, hospital, disease ;
    float x1,x2,y1,y2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_page);


        Spinner hospital = findViewById(R.id.desSpinner);
        Spinner disease = findViewById(R.id.disSpinner);

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
        }else if (v.getId() == R.id.submit){
            addData();
            nextPage(MainActivity.class);
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

    public void onRadioSexClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.bmale:
                if (checked)
                    sex = "male";
                    break;
            case R.id.bfemale:
                if (checked)
                    sex = "female";
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
                    status = "f";
                break;
            case R.id.bp:
                if (checked)
                    status = "p";
                break;
        }
    }

        private void addData(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String formattedDate = df.format(c);
        DatabaseReference myRef = database.getReference("List/" + formattedDate);

        agetv = findViewById(R.id.ageEdit);
        myRef.child("id").setValue(formattedDate);
        myRef.child("age").setValue(agetv.getText().toString());
        myRef.child("sex").setValue(sex);
        myRef.child("status").setValue(status);
        myRef.child("destination").setValue(hospital);
        myRef.child("disease").setValue(disease);
        if(status.equals("p")){
            myRef.child("transfer").setValue("transfering");
        }else{
            myRef.child("transfer").setValue("done");
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
                Log.d("gg ","zzzzzzzzzzzz" + disease);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(this, "You selected nothing", Toast.LENGTH_LONG).show();
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
                if(x1 <= x2){
                    Intent i = new Intent(this, ListActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
                break;
        }
        return false;
    }

}
