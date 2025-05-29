package com.example.mounthub;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mounthub.Location;
import com.example.mounthub.R;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {
    private Location[] locations;

    public LocationAdapter(Location[] locations) {
        this.locations = locations;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_location, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        holder.nameTextView.setText(locations[position].getName());
        holder.typeTextView.setText(locations[position].getLocationType());
    }

    @Override
    public int getItemCount() {
        return locations.length;
    }

    static class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, typeTextView;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.location_name);
            typeTextView = itemView.findViewById(R.id.location_type);
        }
    }
}
