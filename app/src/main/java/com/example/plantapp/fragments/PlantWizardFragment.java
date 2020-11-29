package com.example.plantapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.transition.TransitionInflater;
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
import com.example.plantapp.activities.PlantWizard;
import com.example.plantapp.objects.Plant;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlantWizardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlantWizardFragment extends Fragment {

    // initial declarations
    private DataBaseHelper dataBaseHelper;
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

    private int visibilityOfPlantWizard, visibilityOfCollapsedPlantWizard, visibilityOfPlantList;

    private Button showPlantWizard;

    private CardView cv_plantWizardContainer, plantWizardCollapsed;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PlantWizardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlantWizardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlantWizardFragment newInstance(String param1, String param2) {
        PlantWizardFragment fragment = new PlantWizardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_plant_wizard, container, false);

        getActivity().setTitle("Plant Wizard");

        cv_plantWizardContainer = view.findViewById(R.id.cv_plantWizardContainer);
        plantWizardCollapsed = view.findViewById(R.id.plantWizardCollapsed);
        showPlantWizard = view.findViewById(R.id.showPlantWizard);

        // Light filter chips
        chipBright = view.findViewById(R.id.brightFilterChip);
        chipMedium = view.findViewById(R.id.mediumFilterChip);
        chipLow = view.findViewById(R.id.lowFilterChip);
        chipDirect = view.findViewById(R.id.directFilterChip);
        chipIndirect = view.findViewById(R.id.indirectFilterChip);

        // Humidity filter chips
        chipHumidityVeryHigh = view.findViewById(R.id.veryHighHumidityFilterChip);
        chipHumidityHigh = view.findViewById(R.id.highHumidityFilterChip);
        chipHumidityModerate = view.findViewById(R.id.moderateHumidityFilterChip);
        chipHumidityLow = view.findViewById(R.id.lowHumidityFilterChip);

        // Poison filter chips
        chipPoisonous = view.findViewById(R.id.poisonousFilterChip);
        chipNonPoisonous = view.findViewById(R.id.nonPoisonousFilterChip);

        // TextView for the number of results
        numberOfResults = view.findViewById(R.id.numberOfResults);

        // Spinner for the minimum temperature value
        minTemp = view.findViewById(R.id.spinner_minimumTemp);
        ArrayAdapter<CharSequence> tempArrayAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.temperatureRangeArray, android.R.layout.simple_spinner_item);
        tempArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        minTemp.setAdapter(tempArrayAdapter);
        minTemp.setSelection(0);

        // Spinner for the maximum temperature value
        maxTemp = view.findViewById(R.id.spinner_maximumTemp);
        maxTemp.setAdapter(tempArrayAdapter);
        maxTemp.setSelection(40);

        // The button that sets the filter
        showMeThePlants = view.findViewById(R.id.showMeThePlants);

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
        mRecyclerView = view.findViewById(R.id.rc_plantsList);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        mAdapter = new PlantAdapter(new ArrayList<Plant>());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        // Get the button and set an on click listener to call the plant wizard function
        showMeThePlants = view.findViewById(R.id.showMeThePlants);
        showMeThePlants.setOnClickListener(new View.OnClickListener() {
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
                    mAdapter = new PlantAdapter(results);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mAdapter);
                    mRecyclerView.setBackgroundColor(getResources().getColor(R.color.plantWizardBackground));

                    String numResults = results.size() + " results";
                    numberOfResults.setText(numResults);
                    dataBaseHelper.close();
                    cv_plantWizardContainer.setVisibility(View.GONE);
                    plantWizardCollapsed.setVisibility(View.VISIBLE);

                    mAdapter.setOnItemClickListener(new PlantAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClickListener(int position) {
                            Plant plant = results.get(position);

                            PlantFragment plantFragment = new PlantFragment();
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("Selected", plant);
                            plantFragment.setArguments(bundle);

                            FragmentTransaction fragTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                            fragTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.fade_out_for_sliding_right);
                            fragTransaction.add(R.id.fl_plantWizardContainer, plantFragment);
                            fragTransaction.hide(PlantWizardFragment.this).show(plantFragment).commit();
                            //getActivity().getSupportFragmentManager().beginTransaction().show(plantFragment).commit();

                        }
                    });
                }
            }
        });

        showPlantWizard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plantWizardCollapsed.setVisibility(View.INVISIBLE);
                cv_plantWizardContainer.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    

}