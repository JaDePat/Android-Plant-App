package com.example.plantapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plantapp.DataBaseHelper;
import com.example.plantapp.R;
import com.example.plantapp.fragments.PlantAdapter;
import com.example.plantapp.fragments.PlantFragment;
import com.example.plantapp.fragments.PlantWizardFragment;
import com.example.plantapp.objects.Plant;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class PlantWizard extends AppCompatActivity {

    // initial declarations
    /*private DataBaseHelper dataBaseHelper;
    private PlantAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    // chips for the light filter
    private Chip chipBright, chipMedium, chipLow, chipDirect, chipIndirect;
    // chips for the humidity filter
    private Chip chipHumidityVeryHigh, chipHumidityHigh, chipHumidityModerate, chipHumidityLow;
    // chips for the poison filter
    private Chip chipPoisonous, chipNonPoisonous;
    // button to display the plants
    private Button showMeThePlants;
    // array to hold 'light' filter values
    private ArrayList<String> lightSelectedChipData;
    // array to hold 'humidity' filter values
    private ArrayList<String> humiditySelectedChipData;
    // array to hold 'poison' filter values
    private ArrayList<String> poisonSelectedChipData;
    // text view to display how many plants were returned
    private TextView numberOfResults;
    // spinners for the temperature range
    private Spinner minTemp, maxTemp;
    // array for the spinners values
    private ArrayList<String> selectedTemperatureValues;

    private Button showPlantWizard;

    private CardView cv_plantWizardContainer, plantWizardCollapsed;*/

    private Fragment plantWizard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_wizard);

        FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();

        plantWizard = new PlantWizardFragment();

        fragmentManager.add(R.id.fl_plantWizardContainer, plantWizard);
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
            //fragmentTransaction.remove(plantInfo).commit();
            //getSupportFragmentManager().beginTransaction().show(holdMe.get(0)).commit();
            getSupportFragmentManager().beginTransaction().remove(plantInfo).commit();
            setTitle("Plant Wizard");
        }
        else{
            getSupportFragmentManager().beginTransaction().remove(holdMe.get(0)).commit();
            this.finish();
        }
    }
}