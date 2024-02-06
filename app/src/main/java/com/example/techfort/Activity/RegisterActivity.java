package com.example.techfort.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.techfort.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText reg_email;
    private EditText reg_password;
    private EditText reg_confirm_pass;
    private Button reg_btn;
    private Button reg_login_btn;
    private ProgressBar reg_progress;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerr);

        mAuth = FirebaseAuth.getInstance();

        reg_email = (EditText) findViewById(R.id.reg_email);
        reg_password = (EditText) findViewById(R.id.reg_password);
        reg_confirm_pass = (EditText) findViewById(R.id.reg_confirm_pass);
        reg_btn = (Button) findViewById(R.id.reg_btn);
        reg_login_btn = (Button) findViewById(R.id.reg_login_btn);
        reg_progress = (ProgressBar) findViewById(R.id.reg_progress);


        //Status Bar Color
        getWindow().setStatusBarColor(ContextCompat.getColor(RegisterActivity.this,R.color.color_white));

        reg_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
//                startActivity(registerIntent);
                finish();
            }
        });



        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Email = reg_email.getText().toString();
                String Password = reg_password.getText().toString();
                String Confirm_Pass = reg_confirm_pass.getText().toString();

                if (!TextUtils.isEmpty(Email) && !TextUtils.isEmpty(Password) && !TextUtils.isEmpty(Confirm_Pass)) {
                    if (Password.equals(Confirm_Pass)) {

                        reg_progress.setVisibility(View.VISIBLE);
                        mAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    Intent setupIntent = new Intent(RegisterActivity.this, SetupActivity.class);
                                    startActivity(setupIntent);
                                    finish();
                                } else {
                                    String e = task.getException().getMessage();
                                    Toast.makeText(RegisterActivity.this, "Error" + e, Toast.LENGTH_SHORT).show();

                                }
                                reg_progress.setVisibility(View.INVISIBLE);
                            }
                        });
                    } else {
                        Toast.makeText(RegisterActivity.this, "Conform Password Not Matched", Toast.LENGTH_LONG).show();
                    }
                }


            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            sendToMain();
        }
    }

    private void sendToMain() {
        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();

    }
}