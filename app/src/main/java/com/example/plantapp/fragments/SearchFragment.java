package com.example.plantapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
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
import com.example.plantapp.activities.PlantWizard;
import com.example.plantapp.objects.Plant;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

// the SearchFragment is accessible from the bottom navigation bar
// Contains a search bar, a list of plants filtered based on the search, and a button
// that takes the user to the plant wizard

public class SearchFragment extends Fragment {
    private RecyclerView rvSearch;
    private SearchAdapter adSearch;
    private RecyclerView.LayoutManager lmSearch;
    private List<Plant> plantList;
    private FloatingActionButton fabPlantWizardOpener;


    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        DataBaseHelper dbHelper = new DataBaseHelper(getActivity());
        dbHelper.initializeDataBase();
        plantList = dbHelper.getPlants();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        getActivity().setTitle("Search");

        fabPlantWizardOpener = view.findViewById(R.id.btnPlantWizard);

        rvSearch = view.findViewById(R.id.rvSearch);
        rvSearch.setHasFixedSize(true);
        lmSearch = new LinearLayoutManager(view.getContext());
        adSearch = new SearchAdapter(plantList);

        rvSearch.setLayoutManager(lmSearch);
        rvSearch.setAdapter(adSearch);

        adSearch.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                Plant plant = plantList.get(position);

                SearchFragment2 searchFragment2 = new SearchFragment2();
                Bundle bundle = new Bundle();
                bundle.putParcelable("Selected", plant);
                searchFragment2.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction().
                        setCustomAnimations(R.anim.slide_in_right, R.anim.fade_out_for_sliding_right, R.anim.fade_in, R.anim.slide_out).
                        replace(R.id.fl_fragment_container, searchFragment2)
                        .addToBackStack("fragment_plant").commit();
            }
        });

        fabPlantWizardOpener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPlantWizardActivity();
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.plant_search, menu);

        MenuItem searchItem = menu.findItem(R.id.plant_menu_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adSearch.getFilter().filter(newText);
                return false;
            }
        });
    }

    public void openPlantWizardActivity() {
        Intent intent = new Intent(getContext(), PlantWizard.class);
        startActivity(intent);
    }

}