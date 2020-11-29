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
// Displays a list of the plants that the user owns
public class ShelfFragment extends Fragment {
    private RecyclerView rvShelf;
    private ShelfAdapter adShelf;
    private RecyclerView.LayoutManager lmShelf;
    private List<Plant> plants;
    private TextView welcomeMessage, welcomeMessagePartTwo, welcomeMessagePartThree;

    public ShelfFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       DataBaseHelper dpHelper = new DataBaseHelper(getActivity());
       dpHelper.initializeDataBase();
       plants = dpHelper.getOwnedPlants();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shelf, container, false);
        getActivity().setTitle("Shelf");
        welcomeMessage = view.findViewById(R.id.shelfWelcome);
        welcomeMessagePartTwo = view.findViewById(R.id.shelfWelcomePart2);
        welcomeMessagePartThree = view.findViewById(R.id.shelfWelcomePart3);

        rvShelf = view.findViewById(R.id.rvShelf);
        rvShelf.setHasFixedSize(true);
        lmShelf = new LinearLayoutManager(view.getContext());
        rvShelf.setLayoutManager(lmShelf);

        if(!plants.isEmpty()) {

            if(welcomeMessage.getVisibility() != View.GONE) {
                welcomeMessage.setVisibility(View.GONE);
                welcomeMessagePartTwo.setVisibility(View.GONE);
                welcomeMessagePartThree.setVisibility(View.GONE);
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
                            replace(R.id.flContainer, shelfFragment2)
                            .addToBackStack(null).commit();
                }
            });
        }
        else {
            adShelf = new ShelfAdapter(getContext(), new ArrayList<Plant>());
            rvShelf.setAdapter(adShelf);
            welcomeMessage.setVisibility(View.VISIBLE);
            welcomeMessagePartTwo.setVisibility(View.VISIBLE);
            welcomeMessagePartThree.setVisibility(View.VISIBLE);
        }

        return view;
    }

}