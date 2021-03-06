package com.example.plantapp.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plantapp.R;
import com.example.plantapp.objects.Plant;

import java.util.ArrayList;
import java.util.List;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.PlantViewHolder> implements Filterable {
    private List<Plant> mPlantList;
    private List<Plant> mPlantListFull;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClickListener(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class PlantViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView1;
        public TextView mTextView2;

        public PlantViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            mTextView1 = itemView.findViewById(R.id.textView);
            mTextView2 = itemView.findViewById(R.id.textView2);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onItemClickListener(position);
                        }
                    }
                }
            });
        }
    }

    public PlantAdapter(List<Plant> plantList) {
        mPlantList = plantList;
        mPlantListFull = new ArrayList<>(plantList);
    }

    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.plant_item,
                parent, false);
        PlantViewHolder pvh = new PlantViewHolder(v, mListener);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {
        Plant currentPlant = mPlantList.get(position);

        holder.mTextView1.setText(currentPlant.getName());
        holder.mTextView2.setText(currentPlant.getScientific_Name());
    }

    @Override
    public int getItemCount() {
        return mPlantList.size();
    }

    @Override
    public Filter getFilter() {
        return plantFilter;
    }

    private Filter plantFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Plant> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0) {
                filteredList.addAll(mPlantListFull);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(Plant item : mPlantListFull) {
                    if(item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mPlantList.clear();
            mPlantList.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };
}