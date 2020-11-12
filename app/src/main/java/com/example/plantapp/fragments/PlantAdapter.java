package com.example.plantapp.fragments;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plantapp.R;
import com.example.plantapp.objects.Plant;

import java.util.ArrayList;
import java.util.List;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.PlantViewHolder> implements Filterable {
    private List<Plant> mPlantList;
    private List<Plant> mPlantListFull;
    private int limit = 10;
    private ShelfFragment shelfFragment = new ShelfFragment();

    public class PlantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTextView1;
        public TextView mTextView2;

        public PlantViewHolder(@NonNull View itemView) {
            super(itemView);

            mTextView1 = itemView.findViewById(R.id.textView);
            mTextView2 = itemView.findViewById(R.id.textView2);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int mPosition = getLayoutPosition();
            Plant plant_selected = mPlantList.get(mPosition);

            PlantFragment plantFragment = new PlantFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("Plant Selected", plant_selected);
            plantFragment.setArguments(bundle);

            FragmentManager fragmentManager = plantFragment.getActivity().getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.plant_name_fragment, null);
            fragmentTransaction.commit();
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
        PlantViewHolder pvh = new PlantViewHolder(v);
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
        if(mPlantList.size() > limit)
            return limit;
        else {
            return mPlantList.size();
        }

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