import com.example.plantapp.R;
import com.example.plantapp.objects.Plant;

import android.content.Context;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class ShelfAdapter extends RecyclerView.Adapter<ShelfAdapter.ViewHolder> {

    private Context context;
    private List<Plant> plants;

    public ShelfAdapter(Context context, List<Plant> posts) {
        this.context = context;
        this.plants = plants;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.plant_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Plant plant = plants.get(position);
        holder.bind(plant);

    }

    @Override
    public int getItemCount() {
        return plants.size();
    }

    public void clear() {
        plants.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Plant> list) {
        plants.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPlantImage;
        private TextView tvPlantName;
        private TextView tvScientificName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPlantImage = itemView.findViewById(R.id.ivPlantImage);
            tvPlantName = itemView.findViewById(R.id.tvPlantName);
            tvScientificName = itemView.findViewById(R.id.tvScientificName);

        }

        public void bind(Plant post) {
            // TODO: bind data from database
            // Bind the post data to the view element
            // TODO: Since we aren't using Parse we need a different way to grab this image from the database
            //ParseFile image = plant.getImage();
            //if (image != null) {
            //Glide.with(context).load(image.getUrl()).into(ivImage);
            //}
            //tvPlantName.setText(post.getUser().getUsername());
            //tvScientificName.setText(post.getDescription());
        }
    }

}

