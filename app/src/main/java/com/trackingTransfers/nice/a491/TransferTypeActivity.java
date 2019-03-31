package com.trackingTransfers.nice.a491;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class TransferTypeActivity extends AppCompatActivity {

    private String user, id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transfer_type);

        Intent passID = getIntent();
        user = passID.getStringExtra("user");
//        id = passID.getStringExtra("id");
    }

    public void process(View v) {
        if (v.getId() == R.id.addNew){
            nextPage(AddActivity.class, user);
        }else if(v.getId() == R.id.update){
            nextPage(MainActivity.class, user);
        }
        hideKeyboardInput(v);
    }

    private void nextPage(Class page, String user){
        Intent next = new Intent(this,page);
        next.putExtra("user", user);
        next.putExtra("state", "2");
        startActivity(next);
    }

    private void hideKeyboardInput(View v){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

}