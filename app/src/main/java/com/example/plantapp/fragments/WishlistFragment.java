package com.example.plantapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.plantapp.DataBaseHelper;
import com.example.plantapp.R;
import com.example.plantapp.fragments.ShelfAdapter;
import com.example.plantapp.fragments.ShelfFragment2;
import com.example.plantapp.objects.Plant;

import java.util.ArrayList;
import java.util.List;
// Displays a list of the plants that the user owns
public class WishlistFragment extends Fragment {
    private RecyclerView rvWishlist;
    private WishlistAdapter adWishlist;
    private RecyclerView.LayoutManager lmWishlist;
    private List<Plant> plants;
    private TextView wishlistWelcome, wishlistWelcome2, wishlistWelcome3;

    public WishlistFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBaseHelper dpHelper = new DataBaseHelper(getActivity());
        dpHelper.initializeDataBase();
        plants = dpHelper.getWishlistPlants();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);
        getActivity().setTitle("Wishlist");

        wishlistWelcome = view.findViewById(R.id.wishlistWelcome);
        wishlistWelcome2 = view.findViewById(R.id.wishlistWelcome2);
        wishlistWelcome3 = view.findViewById(R.id.wishlistWelcome3);

        rvWishlist = view.findViewById(R.id.rvWishlist);
        rvWishlist.setHasFixedSize(true);
        lmWishlist = new GridLayoutManager(view.getContext(), 2);
        rvWishlist.setLayoutManager(lmWishlist);

        if(!plants.isEmpty()) {

            if(wishlistWelcome.getVisibility() == View.VISIBLE) {
                wishlistWelcome.setVisibility(View.GONE);
                wishlistWelcome2.setVisibility(View.GONE);
                wishlistWelcome3.setVisibility(View.GONE);
            }

            adWishlist = new WishlistAdapter(getContext(), plants);
            rvWishlist.setAdapter(adWishlist);

            adWishlist.setOnItemClickListener(new WishlistAdapter.OnItemClickListener() {
                @Override
                public void onItemClickListener(int position) {
                    Plant plant = plants.get(position);

                    WishlistFragment2 wishlistFragment2 = new WishlistFragment2();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("Selected", plant);
                    wishlistFragment2.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction().
                            setCustomAnimations(R.anim.slide_in_right, R.anim.fade_out_for_sliding_right, R.anim.fade_in, R.anim.slide_out).
                            replace(R.id.flContainer, wishlistFragment2)
                            .addToBackStack(null).commit();
                }
            });
        }
        else {
            adWishlist = new WishlistAdapter(getContext(), new ArrayList<Plant>());
            rvWishlist.setAdapter(adWishlist);
            wishlistWelcome.setVisibility(View.VISIBLE);
            wishlistWelcome2.setVisibility(View.VISIBLE);
            wishlistWelcome3.setVisibility(View.VISIBLE);
        }

        return view;
    }

}