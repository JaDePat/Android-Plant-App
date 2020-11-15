package com.example.plantapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.plantapp.DataBaseHelper;
import com.example.plantapp.R;
import com.example.plantapp.objects.Plant;

import java.util.List;
// Displays a list of the plants that the user owns
public class ShelfFragment extends Fragment {
    private RecyclerView rvShelf;
    private ShelfAdapter adShelf;
    private RecyclerView.LayoutManager lmShelf;
    private List<Plant> plants;

    public ShelfFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       DataBaseHelper dpHelper = new DataBaseHelper(getActivity());
       dpHelper.initializeDataBase();
       plants = dpHelper.getOwnedPlants();
       // android.util.Log.d("INT", String.format("size = %d", plants.size()));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shelf, container, false);
        rvShelf = view.findViewById(R.id.rvShelf);
        rvShelf.setHasFixedSize(true);
        lmShelf = new LinearLayoutManager(view.getContext());
        adShelf = new ShelfAdapter(getContext(), plants);

        rvShelf.setLayoutManager(lmShelf);
        rvShelf.setAdapter(adShelf);

        return view;
    }

}