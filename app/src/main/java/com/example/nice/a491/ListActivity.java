package com.example.nice.a491;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class ListActivity extends AppCompatActivity {
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_page);
        Intent intent = getIntent();
        message = intent.getStringExtra("data");


    }

    // This "process" method MUST be bound in the layout XML file, "android:onClick="process""
    public void process(View v) {
        if (v.getId() == R.id.back){
            nextPage(MainActivity.class);
        }else if (v.getId() == R.id.list1){
            nextPage(RecordActivity.class);
        }else if (v.getId() == R.id.list2) {
            nextPage(RecordActivity.class);
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
}
