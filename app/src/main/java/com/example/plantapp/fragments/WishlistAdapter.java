package com.example.plantapp.fragments;


import com.example.plantapp.R;
import com.example.plantapp.objects.Plant;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder> {

    private Context context;
    private List<Plant> plants;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClickListener(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    class WishlistViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPlantW;
        private TextView tvNameW;
        private TextView tvSNameW;

        public WishlistViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            ivPlantW= itemView.findViewById(R.id.ivPlantW);
            tvNameW = itemView.findViewById(R.id.tvNameW);
            tvSNameW = itemView.findViewById(R.id.tvSNameW);

            itemView.setOnClickListener(new View.OnClickListener () {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onItemClickListener(position);
                        }
                    }
                }
            });
        }
    }

    public WishlistAdapter(Context context, List<Plant> plants) {
        this.context = context;
        this.plants = plants;
        //android.util.Log.d("INT", String.format("size = %d", plants.size()));
        for (int i = 0 ; i < plants.size() ; i++)
            Log.d("item" , plants.get(i).getName());
    }

    @NonNull
    @Override
    public WishlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.wishlist_plant_row, parent, false);
        return new WishlistViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistViewHolder holder, int position) {
        Plant currentPlant = plants.get(position);

        holder.tvNameW.setText(currentPlant.getName());
        holder.tvSNameW.setText(currentPlant.getScientific_Name());
    }

    @Override
    public int getItemCount() {

        return plants.size();
    }

    public void clear() {
        plants.clear();
        notifyDataSetChanged();
    }




}