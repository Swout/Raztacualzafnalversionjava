package com.swout.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ViewPager myViewPager;
    private TabLayout myTableLayout;
    private TabsAccessorAdapter myTabsAccessorAdapter;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference Rootref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        Rootref = FirebaseDatabase.getInstance().getReference();

        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Rastacualza");

        myViewPager = (ViewPager) findViewById(R.id.main_tabs_pager);
        myTabsAccessorAdapter = new TabsAccessorAdapter(getSupportFragmentManager());
        myViewPager.setAdapter(myTabsAccessorAdapter);

        myTableLayout = (TabLayout) findViewById(R.id.main_tabs);
        myTableLayout.setupWithViewPager(myViewPager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (currentUser == null)
        {
            SendUserToLoginActivity();
        }
        else{
            VerifyUserExistance();
        }
    }

    private void VerifyUserExistance() {
        String CurrentUserId = mAuth.getCurrentUser().getUid();
        Rootref.child("Users").child(CurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("name").exists()){
                    Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                }else{
                       SendUserToSettingsActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SendUserToLoginActivity() {
        Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.option_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         super.onOptionsItemSelected(item);

         if(item.getItemId() == R.id.main_Logout_option){
             mAuth.signOut();
             SendUserToLoginActivity();
         }
        if(item.getItemId() == R.id.main_settings_option){
            SendUserToSettingsActivity();

        }
        if(item.getItemId() == R.id.main_find_friends_option){

        }
        return true;
    }


    private void SendUserToSettingsActivity() {
        Intent settingsIntent = new Intent(MainActivity.this,SettingsActivity.class);
        settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(settingsIntent);
        finish();
    }
}