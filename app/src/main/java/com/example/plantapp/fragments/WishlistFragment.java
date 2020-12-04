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
// WishlistFragment uses the WishlistAdapter class to bind the adapter to the data and populate the
// RecyclerView with plants from the WISHLIST_TABLE in the database.
public class WishlistFragment extends Fragment {
    private RecyclerView rvWishlist;
    private WishlistAdapter adWishlist;
    private RecyclerView.LayoutManager lmWishlist;
    private List<Plant> plants;
    private TextView tvWishlistWelcome, tvWishlistWelcome2, tvWishlistWelcome3;

    public WishlistFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBaseHelper dbHelper = new DataBaseHelper(getActivity());
        dbHelper.initializeDataBase();
        plants = dbHelper.getWishlistPlants();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);
        getActivity().setTitle("Wishlist");

        tvWishlistWelcome = view.findViewById(R.id.tv_wishlist_welcome);
        tvWishlistWelcome2 = view.findViewById(R.id.tv_wishlist_welcome2);
        tvWishlistWelcome3 = view.findViewById(R.id.tv_wishlist_welcome3);

        rvWishlist = view.findViewById(R.id.rv_wishlist);
        rvWishlist.setHasFixedSize(true);
        lmWishlist = new GridLayoutManager(view.getContext(), 2);
        rvWishlist.setLayoutManager(lmWishlist);

        if(!plants.isEmpty()) {

            if(tvWishlistWelcome.getVisibility() == View.VISIBLE) {
                tvWishlistWelcome.setVisibility(View.GONE);
                tvWishlistWelcome2.setVisibility(View.GONE);
                tvWishlistWelcome3.setVisibility(View.GONE);
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
                            replace(R.id.fl_fragment_container, wishlistFragment2)
                            .addToBackStack(null).commit();
                }
            });
        }
        else {
            adWishlist = new WishlistAdapter(getContext(), new ArrayList<Plant>());
            rvWishlist.setAdapter(adWishlist);
            tvWishlistWelcome.setVisibility(View.VISIBLE);
            tvWishlistWelcome2.setVisibility(View.VISIBLE);
            tvWishlistWelcome3.setVisibility(View.VISIBLE);
        }

        return view;
    }

}