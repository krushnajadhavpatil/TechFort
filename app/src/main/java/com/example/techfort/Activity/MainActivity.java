package com.example.techfort.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;


import com.example.techfort.Adapter.CategoryAdapter;
import com.example.techfort.Model.CategoryModel;
import com.example.techfort.R;
import com.example.techfort.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class  MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    ActivityMainBinding binding;
    FirebaseFirestore database;
    Toolbar toolbar;
    NavigationView navigationView;
    BottomNavigationView mainbottomNav;
    FirebaseAuth mAuth;
    private String current_user_id;
    CardView c1,c2,c3,c4,c5,c6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        mainbottomNav = findViewById(R.id.mainbottomNav);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();


         c1=findViewById(R.id.e1);
       c1.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View view) {
               Intent intent=new Intent(MainActivity.this, eithicalhack_course.class);
                startActivity(intent);

            }
      });
        c2=findViewById(R.id.k1);
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, kalilinux_course.class);
                startActivity(intent);

            }
        });
        c3=findViewById(R.id.p1);
        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, pythonhack_course.class);
                startActivity(intent);

            }
        });
        c4=findViewById(R.id.cy1);
        c4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, cyber_course.class);
                startActivity(intent);

            }
        });
        c5=findViewById(R.id.i1);
        c5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, Informationsecurity_course.class);
                startActivity(intent);

            }
        });
        c6=findViewById(R.id.n1);
        c6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, network_course.class);
                startActivity(intent);

            }
        });
        //Bottom Click Listener

        mainbottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.bottom_learn:
                        Intent learn = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(learn);
                        finish();
                        return true;

                    case R.id.bottom_code:
                        Intent code = new Intent(MainActivity.this, SetupActivity.class);
                        startActivity(code);
                        return true;

                    case R.id.bottom_discuss:
                        Intent discuss = new Intent(MainActivity.this, DiscussActivity.class);
                        startActivity(discuss);
                        return true;

                    case R.id.bottom_wq:
                        Intent wquiz = new Intent(MainActivity.this, WeeklyQuizActivity.class);
                        startActivity(wquiz);
                        return true;
                    default:
                        return false;
                }
            }
        });


        ArrayList<CategoryModel> categories = new ArrayList<>();
        CategoryAdapter adapter = new CategoryAdapter(this, categories);

        database.collection("Categories").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                categories.clear();
                for (DocumentSnapshot snapshot : value.getDocuments()) {
                    CategoryModel model = snapshot.toObject((CategoryModel.class));
                    model.setCategoryId((snapshot.getId()));
                    categories.add(model);
                }
                adapter.notifyDataSetChanged();
            }
        });

//        binding.categoryList.setLayoutManager(new GridLayoutManager(this, 2));
//        binding.categoryList.setAdapter(adapter);

        //Navigation
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();
        //by default home will be checked
        navigationView.setCheckedItem(R.id.nav_home);


    }

    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    //Navigation intents

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                Intent p = new Intent(this, MainActivity.class);
                startActivity(p);
                break;
            case R.id.nav_bookmark:
                Intent b = new Intent(this, BookmarkActivity.class);
                startActivity(b);
                break;

            case R.id.nav_logout:
                logout();
                finish();
                break;
            case R.id.share:
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                String body = "Download this App";
                share.putExtra(Intent.EXTRA_TEXT,body);
                startActivity(Intent.createChooser(share,"Share Using"));
                break;
            case R.id.feedback:
                Toast.makeText(this, "Give Feedback", Toast.LENGTH_SHORT).show();
                break;
            case R.id.help:
                Toast.makeText(this, "Help", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_profile:
                Intent setupIntent = new Intent(this, SetupActivity.class);
                startActivity(setupIntent);
                break;


        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {

        mAuth.signOut();
        sendToLogin();

    }

    private void sendToLogin() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            sendToLogin();
        } else {
            current_user_id = mAuth.getCurrentUser().getUid();
            database.collection("Users").document(current_user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if (task.isSuccessful()) {
                        if (!task.getResult().exists()) {
                            Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);
                            startActivity(setupIntent);
                            finish();
                        }
                    } else {
                        String e = task.getException().getMessage();
                        Toast.makeText(MainActivity.this, "Error" + e, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}