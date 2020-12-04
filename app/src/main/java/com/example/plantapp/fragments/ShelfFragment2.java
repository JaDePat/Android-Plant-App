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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plantapp.DataBaseHelper;
import com.example.plantapp.R;
import com.example.plantapp.objects.Plant;
import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

import static com.example.plantapp.R.id.et_personal_plant_care;
import static com.example.plantapp.R.id.tv_personal_plant_care_notes;
import static com.example.plantapp.R.id.tv_personal_plant_care_notes;

//  ShelfFragment2 is accessed by clicking on a row in the ShelfFragment. It displays a photo of the
//  plant that was clicked along with information about it.

//  It uses the ShelfAdapter class to bind the adapter to the data and populate the RecyclerView
//  with expandable cardviews. Clicking on a cardview (e.g. Light) displays the text for that
//  characteristic for the given plant (e.g. Areca palms require bright indirect light.).

//  There are also “Remove from Shelf” and “Plant Doctor”
//  buttons.

public class ShelfFragment2 extends Fragment {
    private Plant plant;
    private RecyclerView rvShelf2;
    private ShelfAdapter2 adShelf2;
    private RecyclerView.LayoutManager lmShelf2;
    DataBaseHelper dbHelper;
    private TextView etPlantCare;
    private HashMap<Integer, String> plantCareNotes;

    public ShelfFragment2() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DataBaseHelper(getActivity());
        dbHelper.initializeDataBase();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_shelf2, container, false);
        ImageView ivPlant= v.findViewById(R.id.iv_plant_image);
        TextView tvName = v.findViewById(R.id.tv_plant_name);
        TextView tvScientificName = v.findViewById(R.id.tv_plant_scientific_name);
        SharedPreferences preferences = getContext().getSharedPreferences("Your_Shared_Prefs",
                Context.MODE_PRIVATE);
        etPlantCare= v.findViewById(et_personal_plant_care);

        Bundle bundle = getArguments();
        plant = bundle.getParcelable("Selected");
        tvName.setText(plant.getName());
        tvScientificName.setText(plant.getScientific_Name());
        if(preferences.contains(Integer.toString(plant.getID()))) {
            etPlantCare.setText(preferences.getString(Integer.toString(plant.getID()), ""));
        }

        rvShelf2 = v.findViewById(R.id.rv_shelf2);
        rvShelf2.setHasFixedSize(true);
        lmShelf2 = new LinearLayoutManager(v.getContext());
        adShelf2 = new ShelfAdapter2(getContext(), plant);

        rvShelf2.setLayoutManager(lmShelf2);
        rvShelf2.setAdapter(adShelf2);

        Button btnRemove = (Button) v.findViewById(R.id.bt_remove_plant);
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShelfFragment shelfFragment = new ShelfFragment();
                dbHelper.deleteFromShelf(String.valueOf(plant.getID()));
                Toast.makeText(getContext(), "Removed from shelf!", Toast.LENGTH_SHORT).show();
                SharedPreferences preferences = getContext().getSharedPreferences("Your_Shared_Prefs",
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove(Integer.toString(plant.getID()));
                editor.apply();
                getActivity().getSupportFragmentManager().popBackStack();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.slide_out)
                        .replace(R.id.fl_fragment_container, shelfFragment)
                        .commit();
            }
        });

        Button saveButton = v.findViewById(R.id.bt_save_plant_care_notes);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plantCareNotes = new HashMap<>();
                plantCareNotes.put(plant.getID(), etPlantCare.getText().toString());
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