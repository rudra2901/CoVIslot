package com.example.covislot;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import java.lang.invoke.ConstantCallSite;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.work.Worker;

public class SignedInActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Button checkSlots;
    SharedPreferences sp;
    ArrayList<session> avlSession;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    //TODO: Add Change Pin Mechanism

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signed_in_activity);

        sp =  getSharedPreferences("login",MODE_PRIVATE);

        checkSlots = findViewById(R.id.check_slots);

        toolbar = findViewById(R.id.main_toolbar1);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.sidebarDrawer1);
        navigationView = findViewById(R.id.nav_view1);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        String pin= sp.getString("pinCode", null);
        Calendar cDate = Calendar.getInstance();
        cDate.getTime();
        String dt = cDate.get(Calendar.DAY_OF_MONTH) + "-" + (cDate.get(Calendar.MONTH)+1) + "-" + cDate.get(Calendar.YEAR);

        avlSession = QueryUtils.checkSlotAvailability(pin, dt);

        checkSlots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                avlSession = QueryUtils.checkSlotAvailability(pin, dt);
                if(avlSession!= null && !avlSession.isEmpty()) {
                    startActivity(new Intent(SignedInActivity.this,AvailableSlotActivity.class));
                }
                else {
                    Toast.makeText(SignedInActivity.this, "No Slots Available", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();

        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(CheckSessionWorker.class, 15, TimeUnit.MINUTES).setConstraints(constraints).build();

        WorkManager workManager = WorkManager.getInstance(this);
        workManager.enqueue(workRequest);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        if(item.getItemId() == R.id.nav_item_two) {
            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            editor.commit();
            startActivity(new Intent(SignedInActivity.this,RegistrationActivity.class));
        }
        return false;
    }
}
