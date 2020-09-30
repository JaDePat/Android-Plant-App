package com.example.androidplantapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/* BaseActivity is a abstract Super class for all the other activities accessed using the
* Bottom navigation view (so SearchActivity, Shelf Activity, & Wishlist Activity
 */
public abstract class MainActivity extends AppCompatActivity
implements BottomNavigationView.OnNavigationItemSelectedListener {

    protected BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
}