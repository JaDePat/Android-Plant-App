package com.example.plantapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.plantapp.R;
import com.example.plantapp.objects.Plant;

public class PlantFragment extends Fragment {
    private Plant plant;

    public PlantFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_plant, container, false);
        TextView textView = v.findViewById(R.id.plant_name_fragment);
        TextView textView1 = v.findViewById(R.id.plant_scientific_name_fragment);

        Bundle bundle = getArguments();
        plant = bundle.getParcelable("Selected");
        textView.setText(plant.getName());
        textView1.setText(plant.getScientific_Name());

        return v;
    };
}
