package com.example.plantapp.fragments;

import android.content.Context;
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

public class WishlistFragment2 extends Fragment {
    private Plant plant;
    private RecyclerView rvWishlist2;
    private WishlistAdapter2 adWishlist2;
    private RecyclerView.LayoutManager lmWishlist2;
    DataBaseHelper dbHelper;
    private Button btRemoveFromWishlist;

    public WishlistFragment2() {

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
        View v = inflater.inflate(R.layout.fragment_wishlist2, container, false);
        ImageView ivPlant= v.findViewById(R.id.iv_plant_image);
        TextView tvName = v.findViewById(R.id.tv_plant_name);
        TextView tvSName = v.findViewById(R.id.tv_plant_scientific_name);

        Bundle bundle = getArguments();
        plant = bundle.getParcelable("Selected");
        Log.i("here", plant.getName());
        tvName.setText(plant.getName());
        tvSName.setText(plant.getScientific_Name());

        rvWishlist2 = v.findViewById(R.id.rv_wishlist2);
        rvWishlist2.setHasFixedSize(true);
        lmWishlist2 = new LinearLayoutManager(v.getContext());
        adWishlist2 = new WishlistAdapter2(getContext(), plant);

        rvWishlist2.setLayoutManager(lmWishlist2);
        rvWishlist2.setAdapter(adWishlist2);

        btRemoveFromWishlist = v.findViewById(R.id.bt_remove_from_wishlist);
        btRemoveFromWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WishlistFragment wishlistFragment = new WishlistFragment();
                dbHelper.deleteFromWishlist(String.valueOf(plant.getID()));
                Toast.makeText(getContext(), "Removed from wishlist!", Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().popBackStack();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.slide_out)
                        .replace(R.id.fl_fragment_container, wishlistFragment)
                        .commit();
            }
        });

        return v;
    }

    public void setArguments(Context context, Bundle bundle) {
    }
}