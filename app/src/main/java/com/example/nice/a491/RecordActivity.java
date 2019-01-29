package com.example.nice.a491;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

public class RecordActivity extends AppCompatActivity {
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_record);

        Intent intent = getIntent();
        message = intent.getStringExtra("data");
//        TextView textView = findViewById(R.id.requestDataTextView);
//        textView.setText(message);

    }

    // This "process" method MUST be bound in the layout XML file, "android:onClick="process""
    public void process(View v) {
        if (v.getId() == R.id.back){
            nextPage(ListActivity.class);
        }else if (v.getId() == R.id.info){
            Intent next = new Intent(this,InformationActivity.class);
            next.putExtra("data",message);
//            next.putExtra("allData" ,allData);
            startActivity(next);
        }else if (v.getId() == R.id.map) {
            nextPagemap(MapsActivity.class);
        }else if (v.getId() == R.id.map2) {
            nextPagemap2(MapsActivity.class);
        }else if (v.getId() == R.id.image) {
            nextPage(ImageActivity.class);
        }
        hideKeyboardInput(v);
    }

    private void nextPage(Class page){
        Intent next = new Intent(this,page);
        startActivity(next);
    }
    private void nextPagemap(Class page){
        Intent next = new Intent(this,page);
        next.putExtra("type","1");
        startActivity(next);
    }
    private void nextPagemap2(Class page){
        Intent next = new Intent(this,page);
        next.putExtra("type","2");
        startActivity(next);
    }
    // To hide Android soft keyboard
    private void hideKeyboardInput(View v){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
