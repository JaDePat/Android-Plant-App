package com.example.plantapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plantapp.DataBaseHelper;
import com.example.plantapp.R;
import com.example.plantapp.objects.Plant;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.List;

public class ShelfFragment2 extends Fragment {
    private Plant plant;
    private RecyclerView rvShelf2;
    private ShelfAdapter2 adShelf2;
    private RecyclerView.LayoutManager lmShelf2;
    DataBaseHelper dpHelper;
    private TextView plantCareNotesTextBox;
    private HashMap<Integer, String> plantCareNotes;

    public ShelfFragment2() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dpHelper = new DataBaseHelper(getActivity());
        dpHelper.initializeDataBase();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_shelf2, container, false);
        ImageView ivPlant= v.findViewById(R.id.ivPlantW);
        TextView tvName = v.findViewById(R.id.tvNameW);
        TextView tvSName = v.findViewById(R.id.tvSNameW);
        SharedPreferences preferences = getContext().getSharedPreferences("Your_Shared_Prefs",
                Context.MODE_PRIVATE);
        plantCareNotesTextBox = v.findViewById(R.id.personalPlantCare);

        Bundle bundle = getArguments();
        plant = bundle.getParcelable("Selected");
        Log.i("here", plant.getName());
        tvName.setText(plant.getName());
        tvSName.setText(plant.getScientific_Name());
        if(preferences.contains(Integer.toString(plant.getID()))) {
            plantCareNotesTextBox.setText(preferences.getString(Integer.toString(plant.getID()), ""));
        }

        rvShelf2 = v.findViewById(R.id.rvShelf2);
        rvShelf2.setHasFixedSize(true);
        lmShelf2 = new LinearLayoutManager(v.getContext());
        adShelf2 = new ShelfAdapter2(getContext(), plant);

        rvShelf2.setLayoutManager(lmShelf2);
        rvShelf2.setAdapter(adShelf2);

        Button removeButton = (Button) v.findViewById(R.id.btRemovePlant);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShelfFragment shelfFragment = new ShelfFragment();
                dpHelper.deleteFromShelf(String.valueOf(plant.getID()));
                Toast.makeText(getContext(), "Removed from shelf!", Toast.LENGTH_SHORT).show();
                SharedPreferences preferences = getContext().getSharedPreferences("Your_Shared_Prefs",
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove(Integer.toString(plant.getID()));
                editor.apply();
                getActivity().getSupportFragmentManager().popBackStack();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.slide_out)
                        .replace(R.id.flContainer, shelfFragment)
                        .commit();
            }
        });

        Button saveButton = v.findViewById(R.id.savePlantCareNotes);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plantCareNotes = new HashMap<>();
                plantCareNotes.put(plant.getID(), plantCareNotesTextBox.getText().toString());
                SharedPreferences preferences = getContext().getSharedPreferences("Your_Shared_Prefs",
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                for (Integer key : plantCareNotes.keySet()) {
                    editor.putString(key.toString(), plantCareNotes.get(key));
                }
                editor.apply();
                Toast.makeText(getContext(), "Plant care notes saved", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    };

    public void setArguments(Context context, Bundle bundle) {
    }
}