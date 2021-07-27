package com.example.covislot;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

public class AvailableSlotActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Button redirect;
    private SharedPreferences sp;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slot_available_activity);

        redirect = findViewById(R.id.redirect);

        sp =  getSharedPreferences("login",MODE_PRIVATE);

        toolbar = findViewById(R.id.main_toolbar2);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.sidebarDrawer2);
        navigationView = findViewById(R.id.nav_view2);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        redirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri baseUri = Uri.parse("https://www.cowin.gov.in/home");
                // Create a new intent to view the cowin URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, baseUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        if(item.getItemId() == R.id.nav_item_logout) {
            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            editor.commit();
            startActivity(new Intent(AvailableSlotActivity.this,RegistrationActivity.class));
        }
        else if(item.getItemId() == R.id.nav_item_change_pin) {
            startActivity(new Intent(AvailableSlotActivity.this,ChangePinPopupActivity.class));
        }
        return false;
    }
}
