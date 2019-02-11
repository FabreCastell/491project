package com.example.nice.a491;

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
import android.widget.TextView;

public class RecordActivity extends AppCompatActivity {
    private String user, id;
    private Toolbar toolbar;

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

//        TextView textView = findViewById(R.id.requestDataTextView);
//        textView.setText(message);

    }

    // This "process" method MUST be bound in the layout XML file, "android:onClick="process""
    public void process(View v) {
        if (v.getId() == R.id.back){
            nextPage(ListActivity.class, user, " ");
        }else if (v.getId() == R.id.info){
            nextPage(InformationActivity.class, user, id);
        }else if (v.getId() == R.id.map) {
            nextPagemap(MapsActivity.class, user, id);
        }else if (v.getId() == R.id.map2) {
            nextPagemap2(MapsActivity.class, user, id);
        }else if (v.getId() == R.id.image) {
            nextPage(ImageActivity.class, user, id);
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
            case R.id.actin_logout:
                nextPage(LoginActivity.class, " "," ");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
