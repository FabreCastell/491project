package com.trackingTransfers.nice.a491;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;


public class ImageActivity extends AppCompatActivity {
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDataImageRef;
    private String user, id;
    private Toolbar toolbar;
    private ProgressDialog mProgressDialog;
    private ProgressBar mLoadImage ;

    private static final int GALLERY_INTENT = 2;

    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private List<upload> mUploads;

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_page);

        Intent intent = getIntent();
        user = intent.getStringExtra("user");
        id = intent.getStringExtra("id");

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        mDataImageRef = FirebaseDatabase.getInstance().getReference("uploads").child(id).child("image");



        toolbar = findViewById(R.id.include);
        toolbar.setTitle("รูปภาพ " + id);
        setSupportActionBar(toolbar);


        imageView = findViewById(R.id.imageShow);

        mProgressDialog = new ProgressDialog(ImageActivity.this);
        mLoadImage = findViewById(R.id.loadimage);

        mRecyclerView = findViewById(R.id.recycleView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mUploads = new ArrayList<>();
//        Log.d("zzzzzzzzzz", "id  "+ mDataImageRef.getPath());
        mDataImageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot post : dataSnapshot.getChildren()){
                    upload upload = post.getValue(upload.class);
                    mUploads.add(upload);
                }
                mAdapter = new ImageAdapter(ImageActivity.this, mUploads);

                mRecyclerView.setAdapter(mAdapter);

                mLoadImage.setVisibility(View.INVISIBLE);

//                Picasso.get()
//                        .load(dataSnapshot.child(id).child("imageUrl").getValue(String.class))
//                        .fit()
//                        .centerCrop()
//                        .into(imageView);
//                Log.d("zzzzzzzzzz", "id  "+ dataSnapshot.child(id).child("imageUrl").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                toastMessage(databaseError.getMessage());
                mLoadImage.setVisibility(View.INVISIBLE);

            }
        });




//        upload upload = dataSnapshot.getValue(upload.class);
//        mUploads.add(upload);
//        Log.d("zzzzzzzzzz", "mUploads  "+ mUploads);

//        mAdapter = new ImageAdapter(ImageActivity.this,mUploads);
//
//        mRecyclerView.setAdapter(mAdapter);
    }

    // This "process" method MUST be bound in the layout XML file, "android:onClick="process""
    public void process(View v) {
        if (v.getId() == R.id.upload){
            upload();
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

    public void upload(){

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_INTENT);

    }



    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {

            mProgressDialog.setMessage("Uploading Image...");
            mProgressDialog.show();

            final Uri uri = data.getData();

            final StorageReference storageReference = mStorageRef.child(user).child(System.currentTimeMillis() +  "." + getTypeImage(uri));
            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Get a URL to the uploaded content

                    toastMessage("Upload Success");
                    mProgressDialog.dismiss();

                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String  downloadUrl = uri.toString();
                            Log.d("zzzzzzzzzz", " "+ downloadUrl);

                            upload upload = new upload(" ", downloadUrl);

                            String uploadID = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(id).child("image").push().setValue(upload);

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    toastMessage("Upload Failed");
                    mProgressDialog.dismiss();
                }
            });
        }
    }

    private String getTypeImage(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }



}
