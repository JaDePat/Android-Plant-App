package com.example.plantapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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

        final FragmentManager fragmentManager = getSupportFragmentManager();

        plantWizard = new PlantWizardFragment();

        fragmentManager.beginTransaction().add(R.id.fl_plantWizardContainer, plantWizard).commit();

        fragmentManager.beginTransaction().show(plantWizard).commit();

        /*cv_plantWizardContainer = findViewById(R.id.cv_plantWizardContainer);
        plantWizardCollapsed = findViewById(R.id.plantWizardCollapsed);
        showPlantWizard = findViewById(R.id.showPlantWizard);

        // Light filter chips
        chipBright = findViewById(R.id.brightFilterChip);
        chipMedium = findViewById(R.id.mediumFilterChip);
        chipLow = findViewById(R.id.lowFilterChip);
        chipDirect = findViewById(R.id.directFilterChip);
        chipIndirect = findViewById(R.id.indirectFilterChip);

        // Humidity filter chips
        chipHumidityVeryHigh = findViewById(R.id.veryHighHumidityFilterChip);
        chipHumidityHigh = findViewById(R.id.highHumidityFilterChip);
        chipHumidityModerate = findViewById(R.id.moderateHumidityFilterChip);
        chipHumidityLow = findViewById(R.id.lowHumidityFilterChip);

        // Poison filter chips
        chipPoisonous = findViewById(R.id.poisonousFilterChip);
        chipNonPoisonous = findViewById(R.id.nonPoisonousFilterChip);

        // TextView for the number of results
        numberOfResults = findViewById(R.id.numberOfResults);

        // Spinner for the minimum temperature value
        minTemp = findViewById(R.id.spinner_minimumTemp);
        ArrayAdapter<CharSequence> tempArrayAdapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.temperatureRangeArray, android.R.layout.simple_spinner_item);
        tempArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        minTemp.setAdapter(tempArrayAdapter);
        minTemp.setSelection(0);

        // Spinner for the maximum temperature value
        maxTemp = findViewById(R.id.spinner_maximumTemp);
        maxTemp.setAdapter(tempArrayAdapter);
        maxTemp.setSelection(40);

        // The button that sets the filter
        showMeThePlants = findViewById(R.id.showMeThePlants);

        // Initialize light chip array list
        lightSelectedChipData = new ArrayList<>();

        // This is a listener that keeps track of whether or not a light chip is selected
        CompoundButton.OnCheckedChangeListener lightCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    lightSelectedChipData.add(buttonView.getText().toString());
                }
                else {
                    lightSelectedChipData.remove(buttonView.getText().toString());
                }
            }
        };

        // Initialize humidity chip array list
        humiditySelectedChipData = new ArrayList<>();

        // This is a listener that keeps track of whether or not a humidity chip is selected
        CompoundButton.OnCheckedChangeListener humidityCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    humiditySelectedChipData.add(buttonView.getText().toString());
                }
                else {
                    humiditySelectedChipData.remove(buttonView.getText().toString());
                }
            }
        };

        // Initialize poison chip array list
        poisonSelectedChipData = new ArrayList<>();

        // This is a listener that keeps track of whether or not a poison chip is selected
        CompoundButton.OnCheckedChangeListener poisonCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    poisonSelectedChipData.add(buttonView.getText().toString());
                }
                else {
                    poisonSelectedChipData.remove(buttonView.getText().toString());
                }
            }
        };

        // Listener for the Spinners


        // Assign the 'light' listener to the 'light' chips
        chipBright.setOnCheckedChangeListener(lightCheckedChangeListener);
        chipMedium.setOnCheckedChangeListener(lightCheckedChangeListener);
        chipLow.setOnCheckedChangeListener(lightCheckedChangeListener);
        chipDirect.setOnCheckedChangeListener(lightCheckedChangeListener);
        chipIndirect.setOnCheckedChangeListener(lightCheckedChangeListener);

        // Assign the 'humidity' listener to the 'humidity' chips
        chipHumidityVeryHigh.setOnCheckedChangeListener(humidityCheckedChangeListener);
        chipHumidityHigh.setOnCheckedChangeListener(humidityCheckedChangeListener);
        chipHumidityModerate.setOnCheckedChangeListener(humidityCheckedChangeListener);
        chipHumidityLow.setOnCheckedChangeListener(humidityCheckedChangeListener);

        // Assign the 'poison' listener to the 'poison' chips
        chipPoisonous.setOnCheckedChangeListener(poisonCheckedChangeListener);
        chipNonPoisonous.setOnCheckedChangeListener(poisonCheckedChangeListener);

        // Get the recycler view and set an empty adapter
        mRecyclerView = findViewById(R.id.rc_plantsList);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mAdapter = new PlantAdapter(new ArrayList<Plant>());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        // Get the button and set an on click listener to call the plant wizard function
        showMeThePlants = findViewById(R.id.showMeThePlants);
        showMeThePlants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get database helper, perform light filter function, and display results
                int minimumTemperature = Integer.parseInt(minTemp.getSelectedItem().toString());
                int maximumTemperature = Integer.parseInt(maxTemp.getSelectedItem().toString());
                if(minimumTemperature > maximumTemperature) {
                    Toast.makeText(getApplicationContext(), "Please fix temperature values",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    dataBaseHelper = new DataBaseHelper(getApplicationContext());
                    dataBaseHelper.initializeDataBase();
                    final ArrayList<Plant> results = dataBaseHelper.plantWizard(lightSelectedChipData,
                            humiditySelectedChipData, poisonSelectedChipData, minimumTemperature,
                            maximumTemperature);
                    mAdapter = new PlantAdapter(results);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mAdapter);

                    mAdapter.setOnItemClickListener(new PlantAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClickListener(int position) {
                            Plant plant = results.get(position);

                            PlantFragment plantFragment = new PlantFragment();
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("Selected", plant);
                            plantFragment.setArguments(bundle);

                            PlantWizard.this.getSupportFragmentManager().beginTransaction().replace(R.id.fl_plantWizardContainer, plantFragment)
                                    .addToBackStack("fragment_plant").commit();
                        }
                    });

                    String numResults = results.size() + " results";
                    numberOfResults.setText(numResults);
                    dataBaseHelper.close();
                    cv_plantWizardContainer.setVisibility(View.GONE);
                    plantWizardCollapsed.setVisibility(View.VISIBLE);
                }
            }
        });

        showPlantWizard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plantWizardCollapsed.setVisibility(View.INVISIBLE);
                cv_plantWizardContainer.setVisibility(View.VISIBLE);
            }
        }); */

    }

    @Override
    public void onBackPressed() {
        List<Fragment> holdMe = getSupportFragmentManager().getFragments();
        if(holdMe.size() > 1) {
            getSupportFragmentManager().beginTransaction().hide(holdMe.get(1)).commit();
            getSupportFragmentManager().beginTransaction().show(holdMe.get(0)).commit();
            getSupportFragmentManager().beginTransaction().remove(holdMe.get(1)).commit();
            setTitle("Plant Wizard");
        }
        else{
            getSupportFragmentManager().beginTransaction().remove(holdMe.get(0)).commit();
            this.finish();
        }
    }
}