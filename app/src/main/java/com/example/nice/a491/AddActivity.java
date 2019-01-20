package com.example.nice.a491;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class AddActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private TextView desidtv, diseasetv, agetv ,checkedtv;
    private String status, sex, hospital, disease ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_page);


        Spinner hospital = findViewById(R.id.des);
        Spinner disease = findViewById(R.id.spinner);

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
        if (v.getId() == R.id.back){
            nextPage(MainActivity.class);
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
            case R.id.radioButton3:
                if (checked)
                    sex = "male";
                    break;
            case R.id.radioButton4:
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
            case R.id.radioButton:
                if (checked)
                    status = "f";
                break;
            case R.id.radioButton2:
                if (checked)
                    status = "p";
                break;
        }
    }

    private void addData(){
        DatabaseReference myRef = database.getReference("0001");

        agetv = findViewById(R.id.editText2);
//        desidtv = findViewById(R.id.spinner2);
//        diseasetv = findViewById(R.id.spinner1);


//        desidtv.getText();
//        diseasetv.getText();



        myRef.child("patient").child("age").setValue(agetv.getText().toString());
        myRef.child("patient").child("sex").setValue(sex);
        myRef.child("status").child("status").setValue(status);
        myRef.child("status").child("destination").setValue(hospital);
        myRef.child("patient").child("disease").setValue(disease);
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(parent.getId()) {
            case R.id.des:
                hospital = parent.getItemAtPosition(position).toString();
                break;
            case R.id.spinner:
                disease = parent.getItemAtPosition(position).toString();
                Log.d("gg ","zzzzzzzzzzzz" + disease);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(this, "You selected nothing", Toast.LENGTH_LONG).show();
    }

}
