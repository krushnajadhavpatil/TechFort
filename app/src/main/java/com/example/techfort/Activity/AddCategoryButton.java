package com.example.techfort.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.techfort.Model.CategoryModel;
import com.example.techfort.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class AddCategoryButton extends AppCompatActivity {

    Toolbar bar;
    DatabaseReference database;
    StorageReference storageReference;
    private Uri mainImageURI = null;
    ImageView img;
    long count=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category_button);

        bar = (Toolbar) findViewById(R.id.new_category_toolbar);
        setSupportActionBar(bar);
        getSupportActionBar().setTitle("Add New Category");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        storageReference = FirebaseStorage.getInstance().getReference();

        database = FirebaseDatabase.getInstance().getReference();

        EditText text = findViewById(R.id.add_category);

        Button b =findViewById(R.id.btn);

        img = findViewById(R.id.img);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(AddCategoryButton.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        Toast.makeText(AddCategoryButton.this, "Permission Denied", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(AddCategoryButton.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                    } else {
                        ImagePicker();
                    }
                } else {
                    ImagePicker();
                }
            }
        });

        database.child("category").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                count=snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (text.getText().toString().length() > 0 && mainImageURI != null) {
                    StorageReference image_path = storageReference.child("category_img").child(Math.random() + ".jpg");

                    UploadTask uploadTask = image_path.putFile(mainImageURI);

                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return image_path.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
//                                Log.i("The URL : ", downloadUrl.toString());
//                                Toast.makeText(SetupActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                                mainImageURI= task.getResult();

                                CategoryModel category = new CategoryModel(String.valueOf(count+1),text.getText().toString(),mainImageURI.toString());

                                database.child("category").push().setValue(category).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Intent intent = new Intent(AddCategoryButton.this,MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });

                            } else {
                                String e = task.getException().getMessage();
                                Toast.makeText(AddCategoryButton.this, "Error" + e, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }
            }
        });

    }

    private void ImagePicker() {
        // start picker to get image for cropping and then use the image in cropping activity
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(this);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainImageURI = result.getUri();
                img.setImageURI((mainImageURI));

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}