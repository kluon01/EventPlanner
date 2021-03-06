package com.example.eventplanner;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;


import com.example.eventplanner.presenter.firebase.LoginPresenter;

import com.example.eventplanner.presenter.PermissionsPresenter;
import com.example.eventplanner.ui.AddEventActivity;

import com.example.eventplanner.ui.TabsPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @BindView(R.id.fab)
    FloatingActionButton fab;
    PermissionsPresenter permissionsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        permissionsPresenter = new PermissionsPresenter(this);

        if (!permissionsPresenter.hasAllPermissions()) {
            permissionsPresenter.requestPermissions();
        }

        int[] tabIcons = {
                R.drawable.ic_cake_black_24dp,
                R.drawable.ic_event_black_24dp,
                R.drawable.ic_person_black_24dp
        };

        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddEventActivity.class);
            startActivity(intent);
        });

        TabsPagerAdapter tabsPagerAdapter = new TabsPagerAdapter(this, getSupportFragmentManager());

        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(tabsPagerAdapter);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        tabs.getTabAt(0).setIcon(tabIcons[0]);
        tabs.getTabAt(1).setIcon(tabIcons[1]);
        tabs.getTabAt(2).setIcon(tabIcons[2]);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onBackPressed() {
        // When user returns to login screen
        super.onBackPressed();
        mAuth.signOut();
    }
}