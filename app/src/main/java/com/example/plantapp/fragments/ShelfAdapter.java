package com.example.plantapp.fragments;

import com.example.plantapp.R;
import com.example.plantapp.objects.Plant;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ShelfAdapter extends RecyclerView.Adapter<ShelfAdapter.ShelfViewHolder> {

    private Context context;
    private List<Plant> plants;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClickListener(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    class ShelfViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPlantImage;
        private TextView tvPlantName;
        private TextView tvScientificName;

        public ShelfViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            ivPlantImage = itemView.findViewById(R.id.ivPlantImage);
            tvPlantName = itemView.findViewById(R.id.tvPlantName);
            tvScientificName = itemView.findViewById(R.id.tvScientificName);

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

    public ShelfAdapter(Context context, List<Plant> plants) {
        this.context = context;
        this.plants = plants;
        //android.util.Log.d("INT", String.format("size = %d", plants.size()));
        for (int i = 0 ; i < plants.size() ; i++)
            Log.d("item" , plants.get(i).getName());
    }

    @NonNull
    @Override
    public ShelfViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.plant_row, parent, false);
        return new ShelfViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ShelfViewHolder holder, int position) {
        Plant currentPlant = plants.get(position);

        holder.tvPlantName.setText(currentPlant.getName());
        holder.tvScientificName.setText(currentPlant.getScientific_Name());
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

