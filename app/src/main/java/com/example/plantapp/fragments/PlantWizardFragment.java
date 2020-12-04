package com.example.plantapp.fragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plantapp.DataBaseHelper;
import com.example.plantapp.R;
import com.example.plantapp.objects.Plant;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;

public class PlantWizardFragment extends Fragment {

    // initial declarations
    private DataBaseHelper dataBaseHelper;
    private SearchAdapter adSearch;
    private RecyclerView rvSearch;
    private RecyclerView.LayoutManager lmSearch;
    // chips for the light filter
    private Chip chipBright, chipMedium, chipLow, chipDirect, chipIndirect;
    // chips for the humidity filter
    private Chip chipHumidityVeryHigh, chipHumidityHigh, chipHumidityModerate, chipHumidityLow;
    // chips for the poison filter
    private Chip chipPoisonous, chipNonPoisonous;
    // button to display the plants
    private Button btShowMeThePlants;
    // array to hold 'light' filter values
    private ArrayList<String> lightSelectedChipData;
    // array to hold 'humidity' filter values
    private ArrayList<String> humiditySelectedChipData;
    // array to hold 'poison' filter values
    private ArrayList<String> poisonSelectedChipData;
    // text view to display how many plants were returned
    private TextView tvNumberOfResults;
    // spinners for the temperature range
    private Spinner minTemp, maxTemp;
    // array for the spinners values
    private ArrayList<String> selectedTemperatureValues;

    private int visibilityOfPlantWizard, visibilityOfCollapsedPlantWizard, visibilityOfPlantList;

    private Button btShowPlantWizard;

    private CardView cvPlantWizardContainer, cvPlantWizardCollapsed;

    public PlantWizardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_plant_wizard, container, false);

        getActivity().setTitle("Plant Wizard");

        cvPlantWizardContainer = view.findViewById(R.id.cv_plant_wizard_container);
        cvPlantWizardCollapsed = view.findViewById(R.id.cv_plant_wizard_collapsed);
        btShowPlantWizard = view.findViewById(R.id.bt_show_plant_wizard);

        // Light filter chips
        chipBright = view.findViewById(R.id.bright_filter_chip);
        chipMedium = view.findViewById(R.id.medium_filter_chip);
        chipLow = view.findViewById(R.id.low_filter_chip);
        chipDirect = view.findViewById(R.id.direct_filter_chip);
        chipIndirect = view.findViewById(R.id.indirect_filter_chip);

        // Humidity filter chips
        chipHumidityVeryHigh = view.findViewById(R.id.very_high_humidity_filter_chip);
        chipHumidityHigh = view.findViewById(R.id.high_humidity_filter_chip);
        chipHumidityModerate = view.findViewById(R.id.moderate_humidity_filter_chip);
        chipHumidityLow = view.findViewById(R.id.low_humidity_filter_chip);

        // Poison filter chips
        chipPoisonous = view.findViewById(R.id.poisonous_filter_chip);
        chipNonPoisonous = view.findViewById(R.id.non_poisonous_filter_chip);

        // TextView for the number of results
        tvNumberOfResults = view.findViewById(R.id.tv_number_of_results);

        // Spinner for the minimum temperature value
        minTemp = view.findViewById(R.id.min_temp_spinner);
        ArrayAdapter<CharSequence> tempArrayAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.temperatureRangeArray, android.R.layout.simple_spinner_item);
        tempArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        minTemp.setAdapter(tempArrayAdapter);
        minTemp.setSelection(0);

        // Spinner for the maximum temperature value
        maxTemp = view.findViewById(R.id.max_temp_spinner);
        maxTemp.setAdapter(tempArrayAdapter);
        maxTemp.setSelection(40);

        // The button that sets the filter
        btShowMeThePlants = view.findViewById(R.id.bt_show_me_the_plants);

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
        rvSearch = view.findViewById(R.id.rv_plants_list);
        rvSearch.setHasFixedSize(true);
        lmSearch = new LinearLayoutManager(view.getContext());
        adSearch = new SearchAdapter(new ArrayList<Plant>());
        rvSearch.setLayoutManager(lmSearch);
        rvSearch.setAdapter(adSearch);

        // Get the button and set an on click listener to call the plant wizard function
        btShowMeThePlants = view.findViewById(R.id.bt_show_me_the_plants);
        btShowMeThePlants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get database helper, perform light filter function, and display results
                int minimumTemperature = Integer.parseInt(minTemp.getSelectedItem().toString());
                int maximumTemperature = Integer.parseInt(maxTemp.getSelectedItem().toString());
                if(minimumTemperature > maximumTemperature) {
                    Toast.makeText(getContext(), "Please fix temperature values",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    dataBaseHelper = new DataBaseHelper(getContext());
                    dataBaseHelper.initializeDataBase();
                    final ArrayList<Plant> results = dataBaseHelper.plantWizard(lightSelectedChipData,
                            humiditySelectedChipData, poisonSelectedChipData, minimumTemperature,
                            maximumTemperature);
                    adSearch = new SearchAdapter(results);
                    rvSearch.setLayoutManager(lmSearch);
                    rvSearch.setAdapter(adSearch);
                    rvSearch.setBackgroundColor(getResources().getColor(R.color.plantWizardBackground));

                    String numResults = results.size() + " results";
                    tvNumberOfResults.setText(numResults);
                    dataBaseHelper.close();
                    cvPlantWizardContainer.setVisibility(View.GONE);
                    cvPlantWizardCollapsed.setVisibility(View.VISIBLE);

                    adSearch.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClickListener(int position) {
                            Plant plant = results.get(position);

                            SearchFragment2 searchFragment2 = new SearchFragment2();
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("Selected", plant);
                            searchFragment2.setArguments(bundle);

                            FragmentTransaction fragTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                            fragTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.fade_out_for_sliding_right);
                            fragTransaction.add(R.id.fl_plant_wizard_container, searchFragment2);
                            fragTransaction.hide(PlantWizardFragment.this).show(searchFragment2).commit();

                        }
                    });
                }
            }
        });

        btShowPlantWizard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cvPlantWizardCollapsed.setVisibility(View.INVISIBLE);
                cvPlantWizardContainer.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }



}