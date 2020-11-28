package com.example.plantapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.plantapp.R;
import com.example.plantapp.fragments.SearchFragment;
import com.example.plantapp.fragments.ShelfFragment;
import com.example.plantapp.fragments.WishlistFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final FragmentManager fragmentManager = getSupportFragmentManager();
        /*final Fragment shelf = new ShelfFragment();
        final Fragment search = new SearchFragment();
        final Fragment wishlist = new WishlistFragment();*/

        final BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                int currentlySelected = bottomNavigationView.getSelectedItemId();
                int newlySelected = item.getItemId();
                switch (newlySelected) {
                    case R.id.action_search:
                         fragment = new SearchFragment();
                         break;
                    case R.id.action_wishlist:
                         fragment = new WishlistFragment();
                         break;
                    case R.id.action_shelf:
                    default:
                        fragment = new ShelfFragment();
                        break;
                }

                if(currentlySelected != newlySelected) {
                    if (currentlySelected == R.id.action_shelf && (newlySelected == R.id.action_search ||
                            newlySelected == R.id.action_wishlist)) {
                        fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.fade_out_for_sliding_right).
                                replace(R.id.flContainer, fragment).commit();
                    } else if (currentlySelected == R.id.action_search && newlySelected == R.id.action_shelf) {
                        fragmentManager.beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.slide_out).
                                replace(R.id.flContainer, fragment).commit();
                    } else if (currentlySelected == R.id.action_search && newlySelected == R.id.action_wishlist) {
                        fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.fade_out_for_sliding_right).
                                replace(R.id.flContainer, fragment).commit();
                    }
                    else{
                        fragmentManager.beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.slide_out).
                                replace(R.id.flContainer, fragment).commit();
                    }
                }

                return true;
            }
        });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_shelf);
        fragmentManager.beginTransaction().replace(R.id.flContainer, new ShelfFragment()).commit();
    }
}