package com.example.plantapp.fragments;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plantapp.R;
import com.example.plantapp.objects.Plant;

import java.util.ArrayList;
import java.util.List;

// The SearchAdapter  converts a plant object into a list row item to be inserted into the
// RecyclerView contained in the SearchFragment class.

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> implements Filterable {
    private List<Plant> PlantList;
    private List<Plant> PlantListFull;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClickListener(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class SearchViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public TextView tvScientificName;

        public SearchViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_plant_name);
            tvScientificName = itemView.findViewById(R.id.tv_plant_scientific_name);

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

    public SearchAdapter(List<Plant> plantList) {
        PlantList = plantList;
        PlantListFull = new ArrayList<>(plantList);
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_plant_search_adapter,
                parent, false);
        SearchViewHolder svh = new SearchViewHolder(v, mListener);
        return svh;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        Plant currentPlant = PlantList.get(position);

        holder.tvName.setText(currentPlant.getName());
        holder.tvScientificName.setText(currentPlant.getScientific_Name());
    }

    @Override
    public int getItemCount() {
        return PlantList.size();
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
                filteredList.addAll(PlantListFull);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(Plant item : PlantListFull) {
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
            PlantList.clear();
            PlantList.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };
}