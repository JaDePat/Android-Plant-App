package com.example.plantapp.fragments;

import com.alespero.expandablecardview.ExpandableCardView;
import com.example.plantapp.R;
import com.example.plantapp.objects.Plant;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

// ShelfAdapter2 converts a plant characteristic (eg. Light) into a list row item to be inserted
// into the RecyclerView contained in the ShelfFragment2 class.

public class ShelfAdapter2 extends RecyclerView.Adapter<ShelfAdapter2.ShelfViewHolder2> {

    private Context context;
    private Plant plant;
    private List<String> plantAttributes = new ArrayList<>();
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClickListener(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    class ShelfViewHolder2 extends RecyclerView.ViewHolder {
        private ExpandableCardView card;
        private TextView expandedCard;

        public ShelfViewHolder2(@NonNull View itemView, @NonNull View itemView2, final OnItemClickListener listener) {
            super(itemView);

            card = itemView.findViewById(R.id.cv_attribute);
            expandedCard = itemView.findViewById(R.id.tv_expanded_cardview);

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

    public ShelfAdapter2(Context context, Plant plant) {
        this.context = context;
        this.plant = plant;
        this.plantAttributes.add(0,plant.getLight());
        this.plantAttributes.add(1,plant.getWater());
        this.plantAttributes.add(2,plant.getFertilizer());
        this.plantAttributes.add(3,plant.getTemperature());
        this.plantAttributes.add(4,plant.getHumidity());
        this.plantAttributes.add(5,plant.getFlowering());
        this.plantAttributes.add(6,plant.getPests());
        this.plantAttributes.add(7,plant.getDiseases());
        this.plantAttributes.add(8,plant.getSoil());
        this.plantAttributes.add(9,plant.getPot_size());
        this.plantAttributes.add(10,plant.getPruning());
        this.plantAttributes.add(11,plant.getPropagation());
        this.plantAttributes.add(12,plant.getPoisonous_plant_info());
    }

    @NonNull
    @Override
    public ShelfViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview, parent, false);
        View expanded_view = LayoutInflater.from(context).inflate(R.layout.expanded_cardview, parent, false);
        return new ShelfViewHolder2(view, expanded_view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ShelfViewHolder2 holder, int position) {
        String currentAttribute = plantAttributes.get(position);
        switch(position) {
            case 0:
                holder.card.setIcon(R.drawable.ic_light);
                holder.card.setTitle("Light");
                break;
            case 1:
                holder.card.setIcon(R.drawable.ic_water);
                holder.card.setTitle("Water");
                break;
            case 2:
                holder.card.setIcon(R.drawable.ic_fertilizer);
                holder.card.setTitle("Fertilizer");
                break;
            case 3:
                holder.card.setIcon(R.drawable.ic_temperature);
                holder.card.setTitle("Temperature");
                break;
            case 4:
                holder.card.setIcon(R.drawable.ic_humidity);
                holder.card.setTitle("Humidity");
                break;
            case 5:
                holder.card.setIcon(R.drawable.ic_flowering);
                holder.card.setTitle("Flowering");
                break;
            case 6:
                holder.card.setIcon(R.drawable.ic_pests);
                holder.card.setTitle("Pests");
                break;
            case 7:
                holder.card.setIcon(R.drawable.ic_diseases);
                holder.card.setTitle("Diseases");
                break;
            case 8:
                holder.card.setIcon(R.drawable.ic_soil);
                holder.card.setTitle("Soil");
                break;
            case 9:
                holder.card.setIcon(R.drawable.ic_pot_size);
                holder.card.setTitle("Pot size");
                break;
            case 10:
                holder.card.setIcon(R.drawable.ic_pruning);
                holder.card.setTitle("Pruning");
                break;
            case 11:
                holder.card.setIcon(R.drawable.ic_propagation);
                holder.card.setTitle("Propagation");
                break;
            case 12:
                holder.card.setIcon(R.drawable.ic_poisonous);
                holder.card.setTitle("Poisonous");
                break;
        }
        holder.expandedCard.setText(currentAttribute);
    }

    @Override
    public int getItemCount() {
        return plantAttributes.size();
    }

    public void clear() {
        plantAttributes.clear();
        notifyDataSetChanged();
    }
}