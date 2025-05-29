package com.example.mounthub;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    private final List<Location> locations;
    private final Context context;

    public LocationAdapter(List<Location> locations, Context context) {
        this.locations = locations;
        this.context = context;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_location, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        Location loc = locations.get(position);
        if (loc == null) return;

        holder.name.setText(loc.getName());
        holder.distance.setText(String.format("Distance: %.0f m", loc.getDistance()));

        int iconRes;
        if (loc.getLocationType() == null) {
            iconRes = R.drawable.ic_default_loc;
        } else {
            switch (loc.getLocationType().toLowerCase()) {
                case "water source":
                    iconRes = R.drawable.ic_water;
                    break;
                case "mountain":
                    iconRes = R.drawable.ic_mountain;
                    break;
                case "village":
                    iconRes = R.drawable.ic_village;
                    break;
                case "refuge":
                    iconRes = R.drawable.ic_refuge;
                    break;
                default:
                    iconRes = R.drawable.ic_default_loc;
            }
        }
        holder.locType.setImageResource(iconRes);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, LocationManager.class);
            intent.putExtra("name", loc.getName());
            intent.putExtra("type", loc.getLocationType());
            intent.putExtra("lat", loc.getCoordinates().getLatitude());
            intent.putExtra("lon", loc.getCoordinates().getLongitude());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return locations != null ? locations.size() : 0;
    }

    public static class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView name, distance;
        ImageView locType;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.location_name);
            distance = itemView.findViewById(R.id.location_distance);
            locType = itemView.findViewById(R.id.location_type_icon);
        }
    }
}
