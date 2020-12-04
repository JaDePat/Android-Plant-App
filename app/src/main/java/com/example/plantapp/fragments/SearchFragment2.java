package com.example.plantapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.plantapp.DataBaseHelper;
import com.example.plantapp.R;
import com.example.plantapp.objects.Plant;

// SearchFragment2 is accessed by clicking on a row in the ShelfFragment. It displays a photo of
// the plant that was clicked along with information about it.

// It uses the ShelfAdapter class to bind the adapter to the data and populate the RecyclerView
// with expandable cardviews.
// Clicking on a cardview (e.g. Light) displays the text for that characteristic for the given plant
// (e.g. Areca palms require bright indirect light.)

// There are also “Remove from Shelf” and “Plant Doctor” buttons.

public class SearchFragment2 extends Fragment {
    private Plant plant;

    public SearchFragment2() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search2, container, false);

        // Connecting the TextView
        TextView textView = v.findViewById(R.id.tv_plant_scientific_name);
        TextView textView1 = v.findViewById(R.id.tv_plant_light_text);
        TextView textView2 = v.findViewById(R.id.tv_plant_water_text);
        TextView textView3 = v.findViewById(R.id.tv_plant_fertilizer_text);
        TextView textView4 = v.findViewById(R.id.tv_plant_temperature_text);
        TextView textView5 = v.findViewById(R.id.tv_plant_humidity_text);
        TextView textView6 = v.findViewById(R.id.tv_plant_flowering_text);
        TextView textView7 = v.findViewById(R.id.tv_plant_pests_text);
        TextView textView8 = v.findViewById(R.id.tv_plant_diseases_text);
        TextView textView9 = v.findViewById(R.id.tv_plant_soil_text);
        TextView textView10 = v.findViewById(R.id.tv_plant_pot_size_text);
        TextView textView11 = v.findViewById(R.id.tv_plant_pruning_text);
        TextView textView12 = v.findViewById(R.id.tv_plant_propagation_text);
        TextView textView13 = v.findViewById(R.id.tv_plant_poisonous_plant_info_text);

        // Grab the information that was passed
        Bundle bundle = getArguments();
        plant = bundle.getParcelable("Selected");

        getActivity().setTitle(plant.getName());
        textView.setText(plant.getScientific_Name());
        textView1.setText(plant.getLight());
        textView2.setText(plant.getWater());
        textView3.setText(plant.getFertilizer());
        textView4.setText(plant.getTemperature());
        textView5.setText(plant.getHumidity());
        textView6.setText(plant.getFlowering());
        textView7.setText(plant.getPests());
        textView8.setText(plant.getDiseases());
        textView9.setText(plant.getSoil());
        textView10.setText(plant.getPot_size());
        textView11.setText(plant.getPruning());
        textView12.setText(plant.getPropagation());
        textView13.setText(plant.getPoisonous_plant_info());

        final Button shelfButton = (Button) v.findViewById(R.id.bt_add_shelf);
        shelfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext());
                long newRowID = dataBaseHelper.addToShelf(plant);
                if(newRowID == -2){
                    Toast.makeText(getContext(), plant.getName() + " is already in your Shelf",
                            Toast.LENGTH_SHORT).show();
                }
                else if(newRowID == -1) {
                    Toast.makeText(getContext(), "Error, cannot add to Shelf",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(), plant.getName() + " added to Shelf",
                            Toast.LENGTH_SHORT).show();
                }
                dataBaseHelper.close();
            }
        });

        Button wishlistButton = (Button) v.findViewById(R.id.bt_add_wishlist);
        wishlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext());
                long newRowID = dataBaseHelper.addToWishList(plant);
                if(newRowID == -2){
                    Toast.makeText(getContext(), plant.getName() + " is already in your Wishlist",
                            Toast.LENGTH_SHORT).show();
                }
                else if(newRowID == -1) {
                    Toast.makeText(getContext(), "Error, cannot add to Wishlist",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(), plant.getName() + " added to Wishlist",
                            Toast.LENGTH_SHORT).show();
                }

                dataBaseHelper.close();
            }
        });

        return v;
    }

}
