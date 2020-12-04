package com.example.plantapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.plantapp.DataBaseHelper;
import com.example.plantapp.R;
import com.example.plantapp.objects.Plant;

import java.util.ArrayList;
import java.util.List;

// ShelfFragment  uses the ShelfAdapter class to bind the adapter to the data and populate the
// RecyclerView with plants from the PLANTS_OWNED_TABLE in the database.

public class ShelfFragment extends Fragment {
    private RecyclerView rvShelf;
    private ShelfAdapter adShelf;
    private RecyclerView.LayoutManager lmShelf;
    private List<Plant> plants;
    private TextView tvWelcomeMessage, tvWelcomeMessagePartTwo, tvWelcomeMessagePartThree;

    public ShelfFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       DataBaseHelper dbHelper = new DataBaseHelper(getActivity());
       dbHelper.initializeDataBase();
       plants = dbHelper.getOwnedPlants();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shelf, container, false);
        getActivity().setTitle("Shelf");
        tvWelcomeMessage = view.findViewById(R.id.tv_shelf_welcome);
        tvWelcomeMessagePartTwo = view.findViewById(R.id.tv_shelf_welcome_part2);
        tvWelcomeMessagePartThree = view.findViewById(R.id.tv_shelf_welcome_part3);

        rvShelf = view.findViewById(R.id.rvShelf);
        rvShelf.setHasFixedSize(true);
        lmShelf = new LinearLayoutManager(view.getContext());
        rvShelf.setLayoutManager(lmShelf);

        if(!plants.isEmpty()) {

            if(tvWelcomeMessage.getVisibility() != View.GONE) {
                tvWelcomeMessage.setVisibility(View.GONE);
                tvWelcomeMessagePartTwo.setVisibility(View.GONE);
                tvWelcomeMessagePartThree.setVisibility(View.GONE);
            }

            adShelf = new ShelfAdapter(getContext(), plants);
            rvShelf.setAdapter(adShelf);

            adShelf.setOnItemClickListener(new ShelfAdapter.OnItemClickListener() {
                @Override
                public void onItemClickListener(int position) {
                    Plant plant = plants.get(position);

                    ShelfFragment2 shelfFragment2 = new ShelfFragment2();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("Selected", plant);
                    shelfFragment2.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction().
                            setCustomAnimations(R.anim.slide_in_right, R.anim.fade_out_for_sliding_right, R.anim.fade_in, R.anim.slide_out).
                            replace(R.id.fl_fragment_container, shelfFragment2)
                            .addToBackStack(null).commit();
                }
            });
        }
        else {
            adShelf = new ShelfAdapter(getContext(), new ArrayList<Plant>());
            rvShelf.setAdapter(adShelf);
            tvWelcomeMessage.setVisibility(View.VISIBLE);
            tvWelcomeMessagePartTwo.setVisibility(View.VISIBLE);
            tvWelcomeMessagePartThree.setVisibility(View.VISIBLE);
        }

        return view;
    }

}