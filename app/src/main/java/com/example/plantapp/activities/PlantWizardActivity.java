package com.example.plantapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.plantapp.R;
import com.example.plantapp.fragments.PlantWizardFragment;

import java.util.List;

public class PlantWizard extends AppCompatActivity {
    private Fragment plantWizard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_wizard);

        FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();

        plantWizard = new PlantWizardFragment();

        fragmentManager.add(R.id.fl_plant_wizard_container, plantWizard);
        fragmentManager.show(plantWizard).commit();

    }

    @Override
    public void onBackPressed() {
        List<Fragment> holdMe = getSupportFragmentManager().getFragments();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(holdMe.size() > 1) {
            Fragment plantInfo = holdMe.get(1);
            Fragment plantList = holdMe.get(0);
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.slide_out, R.anim.slide_out, R.anim.fade_in);
            fragmentTransaction.hide(plantInfo).show(plantList).commit();
            getSupportFragmentManager().beginTransaction().remove(plantInfo).commit();
            setTitle("Plant Wizard");
        }
        else{
            getSupportFragmentManager().beginTransaction().remove(holdMe.get(0)).commit();
            this.finish();
        }
    }
}